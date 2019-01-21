package com.zenwerx.findierock.receiver;

import com.zenwerx.findierock.helper.PassiveLocationHelper;
import com.zenwerx.findierock.helper.SettingsHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
  
	private static final String TAG = "findierock.BootReceiver";
	
	public BootReceiver()
	{
		super();
		
		Log.d(TAG, "KONSTRUCT!");
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Got boot intent!");
		  
		if (SettingsHelper.AppRunOnce(context)) {
			PassiveLocationHelper.StartListening(context.getApplicationContext());
		}
	}
}