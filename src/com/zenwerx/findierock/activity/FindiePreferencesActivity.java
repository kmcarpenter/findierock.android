package com.zenwerx.findierock.activity;

import java.util.Date;

import android.app.backup.BackupManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.SettingsHelper;

public class FindiePreferencesActivity extends PreferenceActivity {
		private BackupManager mBackupManager = null;
		
		private static final String TAG = "findierock.PreferencesActivity";
		
	 	@Override
	    public void onCreate(Bundle savedInstanceState) {
	 		super.onCreate(savedInstanceState);
	 			 			
	 		mBackupManager = new BackupManager(this);
	 		addPreferencesFromResource(R.xml.prefs);
		}
	 	
	 	@Override
		public void onPause()
		{
			super.onPause();
					
			// Set the changed flag
			SettingsHelper.SetLastSettingsUpdate(this, new Date());
			mBackupManager.dataChanged();
			
			// Go in background
			SettingsHelper.SetInBackground(this, true);
		}
		
		@Override
		public void onResume()
		{
			AnalyticsHelper.getInstance().trackPageView("/" + TAG);
			SettingsHelper.SetInBackground(this, false);
			
			super.onResume();
		}
}
