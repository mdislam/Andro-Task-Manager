package com.mesba.taskschedular;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import com.mesba.dynamicui.R;

public class SettingsPrefs extends PreferenceActivity implements OnSharedPreferenceChangeListener{
	
	 private String TAG= "prefs";
	 private DatabaseAdapter dbAdapter;

	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.settings_prefs);
		
		ActionBar actionbar = getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		
		 PreferenceManager.setDefaultValues(SettingsPrefs.this, R.xml.settings_prefs, false);
	     dbAdapter= new DatabaseAdapter(getApplicationContext());
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		switch (item.getItemId()) {
		case android.R.id.home:
			// app icon in action bar clicked; go home
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			break;
		}

		return super.onOptionsItemSelected(item);
	}
	
	@SuppressWarnings("deprecation")
	protected void onResume(){
        super.onResume();
        // Set up a listener whenever a key changes             
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPref, String key) {
		// TODO Auto-generated method stub
		
		@SuppressWarnings("deprecation")
		ListPreference listPreference = (ListPreference) findPreference(key);
		String currValue = listPreference.getValue();
		//Toast.makeText(getApplicationContext(),currValue.substring(0, 2),Toast.LENGTH_SHORT).show();
		Log.v(TAG, "settings change key = "+key);
		dbAdapter.Open();
		if(key.equals("reminder")){
			dbAdapter.setNotificationB4(Integer.parseInt(currValue.substring(0, 2)));
		}else if(key.equals("frequency")){
			dbAdapter.setNotificationFreq(Integer.parseInt(currValue.substring(0, 2)));
		}else if(key.equals("type")){
			dbAdapter.setNotificationType(currValue);
		}else if(key.equals("repetition")){
			if(currValue.equals("Once")){
				currValue="";
			}
			dbAdapter.setRecurrenceFlag(currValue);
		}
		dbAdapter.Close();
		
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);
	}
	
  
	
}
