package com.github.dumpram.sbm;
import java.io.IOException;

import com.github.dumpram.sbm.R;
import com.github.dumpram.sbm.util.bluetooth.BluetoothConnectionListener;
import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;
import com.github.dumpram.sbm.util.bluetooth.ConnectionStateChangedListener;
import com.github.dumpram.sbm.util.listeners.AlternateFunctionListener;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


/**
 * Aktivnost predstavlja početno sučelje aplikacije. Sastoji se od kontrolnih 
 * sklopki, pwm slidera za kontroliranje motora i monitora za senzore. Također
 * je dostupan i prikaz vremena koje daje uređaj te status konekcije uređaja. Grafičko
 * korisničko sučelje ima i 3 tipke sa po 2 funkcije koje služe za spajanje, uparivanje
 * uređaja odnosno za postavke, pomoć i odspajanje su uređaja. Klase odnosno aktivnosti
 * koje se pozivaju iz ove klase uz pomoć sučelja su {@link ConnectActivity}, {@link HelpActivity},
 * {@link PairActivity}, {@link SettingsActivity}, {@link TerminalActivity}.
 * 
 * @author Ivan Pavić
 *
 */
public class StartupActivity extends Activity {
	
	private static int CLICK_NUMBER = 10;  
	
	private enum ParseState {
		SYNC, FILL
	}
	
	private ParseState curState = ParseState.SYNC;
	
	private int fuel = 0;
	
	private String time = "00 : 00 : 00";
	
	private String parseBuffer = new String();

	
	private boolean isUserAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        initGUI();
    }
    
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initBluetooth();
    }
    
    /**
     * Metoda inicijalizira grafičko korisničko sučelje.
     */
	private void initGUI() {
		final Button leftButton = (Button) findViewById(R.id.left_button);
		final Button centerButton = (Button) findViewById(R.id.center_button);
		final Button helpButton = (Button) findViewById(R.id.help_button);
		final TextView logo = (TextView) findViewById(R.id.logo);
		
		boolean isConnected = ConnectionHandler.getInstance().isConnected();
		leftButton.setText((isConnected)? "Settings" : "Pair device");
		centerButton.setText((isConnected)? "Terminal" : "Connect device");
		helpButton.setText((isConnected)? "Disconnect" : "Help");
		
		initControls();
		
		
		logo.setOnClickListener(new OnClickListener() {
			
			private int clickCounter;
			
			@Override
			public void onClick(View v) {
				
				clickCounter++;
				
				if (clickCounter <= 4) {	
					return;
				}
				
				if(clickCounter <= 9) {
					Toast.makeText(getApplicationContext(), "You are " + (CLICK_NUMBER - clickCounter) + " steps from entering admin mode", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(clickCounter == 10) {
					Toast.makeText(getApplicationContext(), "You are now admin!", Toast.LENGTH_SHORT).show();
					isUserAdmin = true;
					leftButton.setVisibility(View.VISIBLE);
					centerButton.setVisibility(View.VISIBLE);
					return;
				}
			}
		});

		
		ConnectionHandler.getInstance().addConnectionStateChangedListener(new ConnectionStateChangedListener() {
			
			@Override 
			public void stateChanged() {
				boolean isConnected = ConnectionHandler.getInstance().isConnected();
				leftButton.setText((isConnected)? "Settings" : "Pair device");
				centerButton.setText((isConnected)? "Terminal" : "Connect device");
				helpButton.setText((isConnected)? "Disconnect" : "Help");
				
				if(isUserAdmin == isConnected) {
					leftButton.setVisibility(View.VISIBLE);
					centerButton.setVisibility(View.VISIBLE);
				}
				if(!isUserAdmin && isConnected) {
					leftButton.setVisibility(View.GONE);
					centerButton.setVisibility(View.GONE);
				}
			} 
		});
		
		leftButton.setOnClickListener(new AlternateFunctionListener() {

			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(StartupActivity.this, PairActivity.class));
			}

			@Override
			public void OnClickSecondary() {
				startActivity(new Intent(StartupActivity.this, SettingsActivity.class));
			}
			
		});
		
		centerButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				startActivity(new Intent(StartupActivity.this, TerminalActivity.class));
				
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(StartupActivity.this, ConnectActivity.class));
				
			}
		});
			
	
		
		helpButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				try {
					ConnectionHandler.getInstance().disconnect();
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "This shouldn't happen!", Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(StartupActivity.this, HelpActivity.class));
			}
		});
		
	}
	
	/**
	 * Metoda incijalizira kontrole u grafičkom korisničkom sučelju.
	 */
	void initControls() {
        final ToggleButton acSwitch = (ToggleButton) findViewById(R.id.ac_control);
		final SeekBar dcControl = (SeekBar) findViewById(R.id.dc_control);
		final SeekBar fuelLevel = (SeekBar) findViewById(R.id.fuel_level);
		final TextView currentTime = (TextView) findViewById(R.id.time);
		final TextView connectionStatus = (TextView) findViewById(R.id.status);
		
		boolean isConnected = ConnectionHandler.getInstance().isConnected();
		connectionStatus.setText("Connection status: " + ((isConnected)? "Connected" : "Disconnected"));

		
		fuelLevel.setEnabled(false);
		
		ConnectionHandler.getInstance().addConnectionStateChangedListener(new ConnectionStateChangedListener() {
			
			@Override
			public void stateChanged() {
				boolean isConnected = ConnectionHandler.getInstance().isConnected();
				connectionStatus.setText("Connection status: " + ((isConnected)? "Connected" : "Disconnected"));
			}
		});
		
		acSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				String data = (isChecked) ? "1" : "0";
				try {
					ConnectionHandler.getInstance().sendBytes(new String("!01$" + data + "#").getBytes());
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't send data! Try reconnecting device!", Toast.LENGTH_SHORT).show();
				}				
			}
		});
		
		dcControl.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
								
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
					
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				try {
					ConnectionHandler.getInstance().sendBytes(new String("!02$" + progress + "#").getBytes());
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't send data! Try reconnecting device!", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		ConnectionHandler.getInstance().addBluetoothConnectionListener(new BluetoothConnectionListener() {
			
			@Override
			public void dataReceived(byte[] data) {
				for(byte i : data) {
					parse((char) i);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							currentTime.setText("RTC Time: " + time.substring(0, time.length() - 1));
							fuelLevel.setProgress(fuel);	
						}
					});
					
				}
			}
		});		
	}
	
	
	
	private void parse(char curByte) {
		if (curState == ParseState.FILL) {
			parseBuffer += curByte;
		}
		switch(curByte) {
			case '!': 
				if (curState == ParseState.SYNC) {
					curState = ParseState.FILL;
				}
				break;
			case '#':
				if (curState == ParseState.FILL) {
					curState = ParseState.SYNC;
					tryToParseBuffer();
					parseBuffer = "";
				}
				break;
			}
	}
	
	
	
	 private void tryToParseBuffer() {
		 String[] parts = parseBuffer.split("i");
		 if (parts == null || parts.length != 2) {
			 return;
		 }
		 if (!parts[1].contains("t")) {
			 return;
		 }
		 if (!(parts[1].charAt(2) != 't' && parts[1].charAt(5) != 't')) {
			 return;
		 }
		 try {
			fuel = Integer.parseInt(parts[0]);
		} catch (NumberFormatException e) {
			
		}
		time = parts[1].replaceAll("t", ":");
	 }


	/**
     * Method prompts user to enable bluetooth adapter if available.
     */
    private void initBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    	if (adapter == null) {
    		Toast.makeText(getApplicationContext(), "No bluetooth adapter available!", Toast.LENGTH_LONG).show();
    		return;
    	}
        if (!adapter.isEnabled()) {
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}
	}
}
