package com.github.dumpram.sbm.util.bluetooth;

/**
 * Ovo sučelje mora implementirati svatko tko želi biti obavješten o promjenama
 * na ulaznim tokovima podataka.
 * 
 * @author Ivan Pavić
 *
 */
public interface BluetoothConnectionListener {
	
	/**
	 * Metoda se pokreće nakon što {@link BluetoothConnection} obavijesti sve zainteresirane za podatke.
	 * @param data podaci kao polje okteta
	 */
	void dataReceived(byte[] data);

}
