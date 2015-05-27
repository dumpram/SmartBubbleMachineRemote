package com.github.dumpram.sbm;

import java.io.IOException;

import com.github.dumpram.sbm.settings.WiFlySettings;
import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
/**
 * Klasa predstavlja administratorsko sučelje za mijenjanje postavki bežične veze na mikrokontroleru. 
 * Vrijeme se mijenja slanjem niza znakova bluetoothom. Niz znakova koji se šalje 
 * enkapsulira postavke koje se mijenjaju upisom u predviđena polja. Promjena postavki se 
 * potvrđuje pritiskom na tipku pri dnu ekrana. Nakon pritiska na tipku podaci se šalju bluetooth
 * vezom i aktivnost se završava, a korisnik se preusmjerava u aktivnost s postavkama.
 * 
 * @author Ivan Pavić
 *
 */
public class WiFlySettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wi_fly_settings);
		setTitle("WiFly Module - Configuration");
		final EditText ssid = (EditText) findViewById(R.id.ssid);
		final EditText wpa = (EditText) findViewById(R.id.wpa);
		final EditText wep = (EditText) findViewById(R.id.wep);
		Button okButton = (Button) findViewById(R.id.OK_button);
		ssid.setText(WiFlySettings.getInstance().getProperty("ssid"));
		wpa.setText(WiFlySettings.getInstance().getProperty("wpa"));
		wep.setText(WiFlySettings.getInstance().getProperty("wep"));
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				WiFlySettings.getInstance().setProperty("ssid", ssid.getText().toString());
				WiFlySettings.getInstance().setProperty("wpa", wpa.getText().toString());
				WiFlySettings.getInstance().setProperty("wep", wpa.getText().toString());
				String ssidString = ssid.getText().toString();
				String wpaString = wpa.getText().toString();
				String wepString = wep.getText().toString();
				String file = "!05$wifly.txt$" + ssidString + "\r\n" + wpaString + "\r\n" + wepString + "#";
				try {
					ConnectionHandler.getInstance().sendBytes(file.getBytes());
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't apply changes!", Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});
	}


}
