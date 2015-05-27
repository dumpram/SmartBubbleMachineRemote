package com.github.dumpram.sbm;

import java.io.IOException;

import com.github.dumpram.sbm.settings.BluetoothSettings;
import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Klasa predstavlja administratorsko sučelje za mijenjanje postavki bluetooth veze. 
 * Bluetooth veza se mijenja slanjem niza znakova bluetoothom. Niz znakova koji se šalje 
 * enkapsulira postavke koje se mijenjaju upisom u predviđena polja. Promjena postavki se 
 * potvrđuje pritiskom na tipku pri dnu ekrana. Nakon pritiska na tipku podaci se šalju bluetooth
 * vezom i aktivnost se završava, a korisnik se preusmjerava u aktivnost s postavkama.
 * 
 * @author Ivan Pavić
 *
 */
public class BluetoothSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bluetooth_settings);
		setTitle("HC05 Module - Configuration");
		final EditText moduleName = (EditText) findViewById(R.id.bluetooth_name);
		final EditText modulePassword = (EditText) findViewById(R.id.bluetooth_pswd);
		Button okButton = (Button) findViewById(R.id.OK_button);
		moduleName.setText(BluetoothSettings.getInstance().getProperty("name"));
		modulePassword.setText(BluetoothSettings.getInstance().getProperty("pswd"));
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BluetoothSettings.getInstance().setProperty("name", moduleName.getText().toString());
				BluetoothSettings.getInstance().setProperty("pswd", modulePassword.getText().toString());
				String name = moduleName.getText().toString();
				String pswd = modulePassword.getText().toString();
				name = "!05$bluetooth.config$" + name + "\r\n" + pswd + "#";
				try {
					ConnectionHandler.getInstance().sendBytes(name.getBytes());
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't apply changes!", Toast.LENGTH_SHORT).show();
				}
				finish();
			}
			
		});
	}
}
