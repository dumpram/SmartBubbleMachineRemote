package com.github.dumpram.sbm;
import java.io.IOException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.dumpram.sbm.util.bluetooth.BluetoothConnectionListener;
import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;
import com.github.dumpram.sbm.util.bluetooth.ConnectionStateChangedListener;
import com.github.dumpram.sbm.util.listeners.AlternateFunctionListener;


/**
 * Main Activity in application. Contains terminal, action buttons
 * which change Activity.
 *
 * @author Ivan Pavi�
 */
public class TerminalActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_terminal);       
        
        setTitle("Bluetooth Terminal");
        initGUI();
    }
    

    /**
     * Method initializes GUI.
     */
	private void initGUI() {
		final Button leftButton = (Button) findViewById(R.id.left_button);
		final Button centerButton = (Button) findViewById(R.id.center_button);
		final Button helpButton = (Button) findViewById(R.id.help_button);
		final TextView terminalRx = (TextView) findViewById(R.id.terminal_rx);
		final EditText terminalTx = (EditText) findViewById(R.id.terminal_tx);
		
		
		terminalRx.setMovementMethod(new ScrollingMovementMethod());
		ConnectionHandler.getInstance().addBluetoothConnectionListener(new BluetoothConnectionListener() {
			
			@Override
			public void dataReceived(final byte[] data) {
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						terminalRx.append(new String(data));
						
					}
				});
			}
		});
		
		ConnectionHandler.getInstance().addConnectionStateChangedListener(new ConnectionStateChangedListener() {
			
			@Override 
			public void stateChanged() {
				boolean isConnected = ConnectionHandler.getInstance().isConnected();
				leftButton.setText((isConnected)? "Send" : "Pair device");
				centerButton.setText((isConnected)? "Disconnect" : "Connect device");
				helpButton.setText((isConnected)? "Clear" : "Help");
				terminalRx.setText("");
			}
		});
		
		leftButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				try {
					ConnectionHandler.getInstance().sendBytes(terminalTx.getText().toString().getBytes());
				} catch (IOException e) {
					e.printStackTrace();
				}
				terminalTx.setText("");
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(TerminalActivity.this, PairActivity.class));
			}
		});
		
		centerButton.setOnClickListener(new AlternateFunctionListener() {
			
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
				startActivity(new Intent(TerminalActivity.this, ConnectActivity.class));
			}
		});
		
		helpButton.setOnClickListener(new AlternateFunctionListener() {
			
			@Override
			public void OnClickSecondary() {
				terminalRx.setText("");
			}
			
			@Override
			public void OnClickPrimary() {
				startActivity(new Intent(TerminalActivity.this, HelpActivity.class));
			}
		});
		
	}
}
