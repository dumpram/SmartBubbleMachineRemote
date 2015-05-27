package com.github.dumpram.sbm;

import android.app.Activity;
import android.os.Bundle;

/**
 * Aktivnost prikazuje upute za korištenje aplikacije. Korisnik klikom na item
 * u listi prelazi u sljedeću aktivnost.
 * 
 * @author Ivan Pavic
 *
 */
public class HelpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		setTitle("Help");
	}
}
