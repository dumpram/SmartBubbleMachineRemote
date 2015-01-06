package com.github.dumpram.sbm;
import java.io.IOException;

import com.github.dumpram.sbm.ConnectActivity;
import com.github.dumpram.sbm.HelpActivity;
import com.github.dumpram.sbm.PairActivity;
import com.github.dumpram.sbm.R;
import com.github.dumpram.sbm.util.bluetooth.BluetoothConnectionListener;
import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;
import com.github.dumpram.sbm.util.bluetooth.ConnectionStateChangedListener;
import com.github.dumpram.sbm.util.listeners.AlternateFunctionListener;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;



/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class StartupActivity extends Activity {
	
	private static int CLICK_NUMBER = 7;  

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
     * Method initializes GUI.
     */
	private void initGUI() {
		final Button leftButton = (Button) findViewById(R.id.left_button);
		final Button centerButton = (Button) findViewById(R.id.center_button);
		final Button helpButton = (Button) findViewById(R.id.help_button);
		final TextView logo = (TextView) findViewById(R.id.logo);
		
		leftButton.setVisibility(View.GONE);
		centerButton.setVisibility(View.GONE);
		
		logo.setOnClickListener(new OnClickListener() {
			
			private int clickCounter;
			
			@Override
			public void onClick(View v) {
				if(clickCounter < 7) {
					clickCounter++;
					if(clickCounter > 4) {
						Toast.makeText(getApplicationContext(), "You are " + (CLICK_NUMBER - clickCounter) + " steps from entering admin mode", Toast.LENGTH_SHORT).show();
					}
				}
				if(clickCounter == 7) {
					leftButton.setVisibility(View.VISIBLE);
					centerButton.setVisibility(View.VISIBLE);
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
			}
		});
		
		leftButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(StartupActivity.this, SettingsActivity.class));
			}
		});
		
		centerButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(StartupActivity.this, TerminalActivity.class));				
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
     * Method prompts user to enable bluetooth adapter if available.
     */
    private void initBluetooth() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
    	if (adapter == null) {
    		Toast.makeText(getApplicationContext(), "No bluetooth adapter available!", Toast.LENGTH_LONG).show();
    		return;
    	}
        while (!adapter.isEnabled()) {
			Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBluetooth, 0);
		}
	}
}
