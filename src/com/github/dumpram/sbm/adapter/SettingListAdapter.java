package com.github.dumpram.sbm.adapter;

import com.github.dumpram.sbm.R;
import com.github.dumpram.sbm.SettingsActivity;
import com.github.dumpram.sbm.settings.Settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Klasa predstavlja adapter za postavke u listi. Koristi se za 
 * izlistavanje postavki u klasi {@link SettingsActivity}.
 * 
 * @author Ivan Pavić
 *
 */
public class SettingListAdapter extends ArrayAdapter<Settings> {

	public SettingListAdapter(Context context, int resource) {
		super(context, resource);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(getContext()); 
			convertView = inflater.inflate(R.layout.list_item, parent, false);
		}
		
		TextView deviceName = (TextView) convertView.findViewById(R.id.device_name);
		TextView deviceAddress = (TextView) convertView.findViewById(R.id.device_address);
		deviceName.setText(" " + getItem(position).getName());
		deviceAddress.setText(" " + getItem(position).getDescription());
		
		return convertView;
	}
	
	

}
