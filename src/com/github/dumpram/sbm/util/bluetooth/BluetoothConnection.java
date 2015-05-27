package com.github.dumpram.sbm.util.bluetooth;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import android.R.integer;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Razred je podrazred razreda {@link Thread} i predstavlja vezu između 2 bluetooth
 * uređaja. Instancira se iz razreda {@link ConnectionHandler} i pomoću njega upravlja.
 * 
 * @author Ivan Pavic
 *
 */
public class BluetoothConnection extends Thread {
	/**
	 * Ulazni tok podataka.
	 */
	private InputStream input;
	/**
	 * Izlazni tok podataka.
	 */
	private OutputStream output;
	/**
	 * Pomoćna varijabla.
	 */
	private int lastValue;
	/**
	 * Zastavica indicira stanje veze.
	 */
	private boolean isStopped;
	/**
	 * Bluetooth socket.
	 */
	private BluetoothSocket socket;
	
	/**
	 * Lista {@link BluetoothConnectionListener} koji čekaju nove podatke iz ulaznog toka.
	 */
	private List<BluetoothConnectionListener> listeners;	
	/**
	 * Konstruktor incijalizira sve privatne varijable i započinje vezu.
	 * @param device uređaj za spajanje
	 * @param listeners svi oni koji čekaju podatke s veze
	 * @throws IOException u slučaju pogreške pri otvaranju veze
	 */
	public BluetoothConnection(BluetoothDevice device, List<BluetoothConnectionListener> listeners)  throws IOException {
		socket = device.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));		
		input = socket.getInputStream();
		output = socket.getOutputStream();
		this.listeners = listeners;
		socket.connect();
	}
	
	@Override
	public void run() {
		int bytes;
		byte[] buffer = new byte[256];
		while(!isStopped) {
			try {
				bytes = input.read(buffer);
				if (bytes != -1) {
					fire(buffer, bytes);
				}
			} catch (IOException e) {
				continue;
			}
		}
	}
	/**
	 * Metoda obavještava sve {@link BluetoothConnectionListener}-e o promjeni na ulaznom
	 * toku podataka i šalje im te podatke
	 * @param buffer {@link Byte} polje podataka
	 * @param bytes količina podataka
	 */
	private void fire(byte[] buffer, int bytes) {
		byte[] forExport = new byte[bytes];
		for (int i = 0; i < bytes; i++) {
			forExport[i] = buffer[i];
		}
		for (BluetoothConnectionListener i : listeners) {
			i.dataReceived(forExport);
		}
	}
	/**
	 * Metoda piše u izlazni tok.
	 * @param bytes dano polje okteta
	 * @throws IOException u slučaju pogreške pri pisanju na izlazni tok
	 */
	public void write(byte[] bytes) throws IOException {
		output.write(bytes);
	} 
	/**
	 * Metoda piše jedan oktet na izlazni tok.
	 * @param b oktet
	 * @throws IOException u slučaju pogreške pri pisanju na izlazni tok
	 */
	public void write(int b) throws IOException {
		output.write(b);
	}
	/**
	 * Dohvaća pomoćnu varijablu.
	 * @return {@link integer} pomoćna varijabla
	 */
	public int getLastValue() {
		return lastValue;
	}
	/**
	 * Metoda zaustavlja konekciju.
	 * @param isStopped ako je <code>true</code> konekcija će biti zaustavljena, inače neće
	 */
	public void setStopped(boolean isStopped) {
		this.isStopped = isStopped;
	}
	/**
	 * Metoda dohvaća {@link BluetoothSocket} na koji je spojen uređaj.
	 * @return {@link BluetoothSocket} preko kojeg ostvarena konekcija
	 */
	public BluetoothSocket getSocket() {
		return socket;
	}			
}

