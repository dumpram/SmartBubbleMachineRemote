package com.github.dumpram.sbm.util.listeners;


import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;
import android.view.View;
import android.view.View.OnClickListener;


/**
 * Ovaj razred implementira sučelje {@link OnClickListener} i ovisno 
 * o stanju veze može izvoditi dvije radnje.
 * 
 * @author Ivan Pavić
 *
 */
public abstract class AlternateFunctionListener implements OnClickListener {

	@Override
	public void onClick(View v) {
		if (ConnectionHandler.getInstance().isConnected()) {
			OnClickSecondary();
		} else {
			OnClickPrimary();
		}
	}
	/**
	 * Metoda se poziva kad je uređaj odspojen.
	 */
	public abstract void OnClickPrimary();
	
	/**
	 * Metoda se poziva kad je uređaj spojen.
	 */
	public abstract void OnClickSecondary();

}
