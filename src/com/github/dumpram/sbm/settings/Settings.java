package com.github.dumpram.sbm.settings;
/**
 * Klasa predstavlja abstraktne postavke uređaja.
 * 
 * @author Ivan Pavić
 *
 */
public abstract class Settings {
	
	/**
	 * Metoda dohvaća ime postavki.
	 * @return {@link String} ime postavki
	 */
	public abstract String getName();
	/**
	 * Metoda dohvaća opis postavki.
	 * @return {@link String} opis postavki
	 */
	public abstract String getDescription();

}
