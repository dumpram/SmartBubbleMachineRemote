package com.github.dumpram.sbm.util.listeners;


import com.github.dumpram.sbm.util.bluetooth.ConnectionHandler;

import android.view.View;
import android.view.View.OnClickListener;

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
	 * Action when device is disconnected.
	 */
	public abstract void OnClickPrimary();
	
	/**
	 * Action when device is connected.
	 */
	public abstract void OnClickSecondary();

}
