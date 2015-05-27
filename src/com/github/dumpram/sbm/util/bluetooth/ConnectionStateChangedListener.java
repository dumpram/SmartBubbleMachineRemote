package com.github.dumpram.sbm.util.bluetooth;
/**
 * Ovo sučelje moraju implementirati oni razredi koje žele biti obaviješteni
 * o promjeni stanje veze.
 * 
 * @author Ivan Pavić
 *
 */
public interface ConnectionStateChangedListener {
	/**
	 * Metoda se izvršava pri promjeni stanja veze.
	 */
	void stateChanged();

}
