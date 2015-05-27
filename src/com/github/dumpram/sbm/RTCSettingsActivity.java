package com.github.dumpram.sbm;


import java.io.IOException;

import com.github.dumpram.sbm.settings.RTCSettings;
import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Klasa predstavlja administratorsko sučelje za mijenjanje postavki vremena na mikrokontroleru. 
 * Vrijeme se mijenja slanjem niza znakova bluetoothom. Niz znakova koji se šalje 
 * enkapsulira postavke koje se mijenjaju upisom u predviđena polja. Promjena postavki se 
 * potvrđuje pritiskom na tipku pri dnu ekrana. Nakon pritiska na tipku podaci se šalju bluetooth
 * vezom i aktivnost se završava, a korisnik se preusmjerava u aktivnost s postavkama.
 * 
 * @author Ivan Pavić
 *
 */
public class RTCSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rtcsettings);
		setTitle("Real Time Clock - Configuration");
		final EditText time = (EditText) findViewById(R.id.time);
		final EditText date = (EditText) findViewById(R.id.date);
		Button okButton = (Button) findViewById(R.id.OK_button);
		time.setText(RTCSettings.getInstance().getProperty("time"));
		date.setText(RTCSettings.getInstance().getProperty("date"));
		okButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RTCSettings.getInstance().setProperty("time", time.getText().toString());
				RTCSettings.getInstance().setProperty("date", date.getText().toString());
				String timeString = time.getText().toString();
				timeString = "!03$" + timeString + "#";
				try {
					ConnectionHandler.getInstance().sendBytes(timeString.getBytes());
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't apply changes!", Toast.LENGTH_SHORT).show();
				}
				String dateString = date.getText().toString();
				dateString = "!04$" + dateString + "#";
				try {
					ConnectionHandler.getInstance().sendBytes(dateString.getBytes());
				} catch (IOException e) {
					Toast.makeText(getApplicationContext(), "Couldn't apply changes!", Toast.LENGTH_SHORT).show();
				}
				finish();
			}
		});
	}
}
