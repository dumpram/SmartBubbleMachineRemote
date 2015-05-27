package com.github.dumpram.sbm.settings;

import java.util.HashMap;
import java.util.Map;
import com.github.dumpram.sbm.WiFlySettingsActivity;
/**
 * Postavke WiFly modula. Podrazred razreda {@link Settings}. Omogućuje
 * mijenjanje postavki WiFlyModula u {@link WiFlySettingsActivity}.
 * Klasa je oblikovana singleton obrascem te na taj način mogu postojati
 * samo jedne bluetooth postavke za uređaj.
 * 
 * @author Ivan Pavić
 *
 */
public class WiFlySettings extends Settings {
	
	private final String name = "WiFly Settings";
	
	private final String description = "Change Wifly configuration settings";
	
	private static WiFlySettings instance = new WiFlySettings();
	
	private Map<String, String> settings = new HashMap<String, String>();
	
	public WiFlySettings() {
		settings.put("ssid", "WiflySSID");
		settings.put("wpa", "12345678");
		settings.put("wep", "1A648C9FE2");
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static WiFlySettings getInstance() {
		return instance;
	}

	public String getProperty(String key) {
		return settings.get(key);
	}
	
	public void setProperty(String key, String value) {
		settings.put(key, value);
	}
}
