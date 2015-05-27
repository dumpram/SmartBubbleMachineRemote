package com.github.dumpram.sbm.util.bluetooth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.dumpram.sbm.util.bluetooth.exceptions.DeviceNotConnectedException;

import android.bluetooth.BluetoothDevice;
/**
 * Klasa je singleton razred što znači da postoji samo jedna njezina instanca u čitavom
 * programu. Ona jedina smije spajati odspajati uređaje nakon što joj drugi razredi
 * daju uređaj na koji se žele spojiti. Također preko nje se dodaju {@link ConnectionStateChangedListener}
 * koje ona obaviještava o promjeni stanja veze. Isto tako se dodaju {@link BluetoothConnectionListener}
 * čije obavještavanje delegira instanci {@link BluetoothConnection} koja obaviještava {@link BluetoothConnectionListener}
 * o novim podacima.
 * 
 * @author Ivan Pavić
 *
 */
public class ConnectionHandler {
	/**
	 * Bluetooth veza.
	 */
	private BluetoothConnection connection;
	/**
	 * {@link ConnectionStateChangedListener} listeneri.
	 */
	private List<ConnectionStateChangedListener> listeners = new ArrayList<ConnectionStateChangedListener>();
	/**
	 * {@link BluetoothConnectionListener} listeneri.
	 */
	private List<BluetoothConnectionListener> bListeners = new ArrayList<BluetoothConnectionListener>();
	/**
	 * Instanca klase.
	 */
	private static ConnectionHandler instance = new ConnectionHandler();
	
	/**
	 * Privatni konstruktor.
	 */
	private ConnectionHandler() {
	
	}
	/**
	 * Dohvaća instancu klase.
	 * @return {@link ConnectionHandler} instanca
	 */
	public static ConnectionHandler getInstance() {
		return instance;
	}
	
	/**
	 * Odspaja trenutrno spojeni uređaj.
	 * @throws IOException u slučaju neuspješnog odspajanja
	 */
	public void disconnect() throws IOException {
		if (connection != null) {
			instance.connection.setStopped(true);
			instance.connection.getSocket().close();
			instance.connection = null;
			fireConnectionStateChanged();
		}
	}
	/**
	 * Metoda obaviještava {@link ConnectionStateChangedListener}-e o promjenama
	 * stanja veze.
	 */
	private void fireConnectionStateChanged() {
		for (ConnectionStateChangedListener i : listeners) {
			i.stateChanged();
		}
	}
	/**
	 * Metoda spaja dani uređaj.
	 * @param remoteDevice dani uređaj
	 * @throws IOException u slučaju neuspješnog spajanja zbog greške u otvaranju veze
	 * @throws DeviceNotConnectedException u slučaja neuspješnog spajanja zbog udaljenosti uređaja
	 */
	public void connect(BluetoothDevice remoteDevice) throws IOException, DeviceNotConnectedException {
		connection = new BluetoothConnection(remoteDevice, bListeners);
		if (!connection.getSocket().isConnected()) {
			connection = null;
			throw new DeviceNotConnectedException();
		}
		connection.start();
		fireConnectionStateChanged();
	}
	
	/**
	 * Šalje podatke spojenom uređaju.
	 * @param bytes dani podaci
	 * @throws IOException u slučaju greške prilikom slanja
	 */
	public void sendBytes(byte[] bytes) throws IOException {
		if(connection != null) {
			connection.write(bytes);
		}
	}
	
	/**
	 * 
	 * @return vraća <code>true</code> u slučaju da je uređaj spojen, <code>false</code> inače
	 */
	public boolean isConnected() {
		return (connection != null)? true : false;
	}
	
	/**
	 * Metoda dodaje {@link ConnectionStateChangedListener} u listu.
	 * @param connectionStateChangedListener listener za dodati u listu
	 */
	public void addConnectionStateChangedListener(
			ConnectionStateChangedListener connectionStateChangedListener) {
		listeners.add(connectionStateChangedListener);		
	}
	
	/**
	 * Metoda dodaje {@link BluetoothConnectionListener} u listu u {@link BluetoothConnection}-u.
	 * @param bluetoothConnectionListener listener za dodati u listu
	 */
	public void addBluetoothConnectionListener(
			BluetoothConnectionListener bluetoothConnectionListener) {
		bListeners.add(bluetoothConnectionListener);
	}
	

}
