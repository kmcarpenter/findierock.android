package com.zenwerx.findierock.service;

import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.SettingsHelper;

import android.content.Intent;
import android.util.Log;

public class AnalyticsUpdateService extends BaseIntentService {

	private final static String TAG = "findierock.AnalyticsUpdateService";
	
	public AnalyticsUpdateService()
	{
		super("AnalyticsUpdateService");
		Log.d(TAG, "Constructing update service");
	}
	
	public AnalyticsUpdateService(String name) {
		super(name);
		Log.d(TAG, "Constructing update service with name " + name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Log.d(TAG, "Handling intent... but first need to check my connection stuff");
		
		// Don't bother if there's no network
		// If we're on mobile, and the update on mobile settings is false, don't do it
		// Or if we're low on battery, don't do it
		// TODO: Check is background
		if (!getIsConnected()
			 || (getIsMobileData() && !SettingsHelper.UpdateOnMobile(this))
			 || (getIsLowBattery()))
	 		 
		{
			Log.d(TAG, "Not a valid connection available.");
			return;
		}
		
		Log.d(TAG, "Dispatching to GA.");
		
		AnalyticsHelper.getInstance().dispatch();
	}

}
