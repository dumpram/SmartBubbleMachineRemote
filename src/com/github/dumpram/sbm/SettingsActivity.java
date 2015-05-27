package com.github.dumpram.sbm;


import java.util.HashMap;
import java.util.Map;

import com.github.dumpram.sbm.adapter.SettingListAdapter;
import com.github.dumpram.sbm.settings.BluetoothSettings;
import com.github.dumpram.sbm.settings.RTCSettings;
import com.github.dumpram.sbm.settings.WiFlySettings;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * Aktivnost prikazuje listu postavki koje se mogu mijenjati. Pritiskom
 * na određenu stavku prelazi se u neku od sljedećih aktivnosti: {@link BluetoothSettingsActivity},
 * {@link RTCSettingsActivity} ili {@link WiFlySettingsActivity}.
 * 
 * @author Ivan Pavić
 *
 */
public class SettingsActivity extends Activity {
	
	
	private final Map<String, Intent> map = new HashMap<String, Intent>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setTitle("Configure Smart Bubble Machine");
		createMap();
		ListView settingsList = (ListView) findViewById(R.id.settings_list);
		final SettingListAdapter settingsAdapter = new SettingListAdapter(getApplicationContext(), 0);
		settingsAdapter.add(BluetoothSettings.getInstance());
		settingsAdapter.add(WiFlySettings.getInstance());
		settingsAdapter.add(RTCSettings.getInstance());
		settingsList.setAdapter(settingsAdapter);
		settingsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(map.get((settingsAdapter.getItem(position).getName())));
			}
		});
		
	}

	private void createMap() {
		map.put("Bluetooth Settings", new Intent(SettingsActivity.this, BluetoothSettingsActivity.class));
		map.put("WiFly Settings", new Intent(SettingsActivity.this, WiFlySettingsActivity.class));
		map.put("RTC Settings", new Intent(SettingsActivity.this, RTCSettingsActivity.class));
	}
}
