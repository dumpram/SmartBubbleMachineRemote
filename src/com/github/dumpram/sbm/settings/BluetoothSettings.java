package com.github.dumpram.sbm.settings;

import java.util.HashMap;
import java.util.Map;

import com.github.dumpram.sbm.BluetoothSettingsActivity;
/**
 * Postavke bluetootha. Podrazred razreda {@link Settings}. Omogućuje
 * mijenjanje postavki bluetootha u {@link BluetoothSettingsActivity}.
 * Klasa je oblikovana singleton obrascem te na taj način mogu postojati
 * samo jedne bluetooth postavke za uređaj.
 * 
 * @author Ivan Pavić
 *
 */
public class BluetoothSettings extends Settings {
	
	private final String name = "Bluetooth Settings";
	
	private final String description = "Change bluetooth configuration settings";
	
	private static BluetoothSettings instance = new BluetoothSettings();
	
	private Map<String, String> settings = new HashMap<String, String>();
	
	private BluetoothSettings() {
		settings.put("name", "SBM_Bluetooth");
		settings.put("pswd", "1234");
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public static BluetoothSettings getInstance() {
		return instance;
	}
	
	public String getProperty(String key) {
		return settings.get(key);
	}
	
	public void setProperty(String key, String value) {
		settings.put(key, value);
	}

}
