package com.zenwerx.findierock;

import java.io.IOException;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class FindieBackupAgent extends BackupAgentHelper {
	private final static String TAG = "findierock.BackupAgent";
	
	@Override
	public void onCreate() {
		Log.d(TAG, "Adding shared preference helper...");
		
		SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, "com.zenwerx.findierock_preferences");

		addHelper("findieBackupHelper", helper);
	}
	
	@Override
	public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data,ParcelFileDescriptor newState) throws IOException
	{
		Log.d(TAG, "I should be backing up right now...");
		super.onBackup(oldState, data, newState);
		Log.d(TAG, "I should be done backing up right now...");
	}
	
	@Override
	public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException
	{
		
	}
}
