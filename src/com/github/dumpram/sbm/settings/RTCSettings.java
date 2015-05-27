package com.github.dumpram.sbm.settings;

import java.util.HashMap;
import java.util.Map;
/**
 * Postavke Real Time Clock-a. Podrazred razreda {@link Settings}. Omogućuje
 * mijenjanje postavki RTC-a u {@link RTCSettingsActivity}.
 * Klasa je oblikovana singleton obrascem te na taj način mogu postojati
 * samo jedne RTC postavke za uređaj.
 * 
 * @author Ivan Pavić
 *
 */
public class RTCSettings extends Settings {
	
	private final String name = "RTC Settings";
	
	private final String description = "Change Real Time Clock Configuration";
	
	private static RTCSettings instance = new RTCSettings();
	
	private Map<String, String> settings = new HashMap<String, String>();
	
	private RTCSettings() {
		settings.put("time", "00:00:00");
		settings.put("date", "17/1/2015");
	}


	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}
	
	public static RTCSettings getInstance() {
		return instance;
	}
	
	public String getProperty(String key) {
		return settings.get(key);
	}
	
	public void setProperty(String key, String value) {
		settings.put(key, value);
	}

}
