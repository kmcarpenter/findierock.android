package com.zenwerx.findierock.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;

public abstract class BaseIntentService extends IntentService {
	
	public BaseIntentService(String name) {
		super(name);
			
	}
	
	/**
	 * Returns battery status. True if less than 10% remaining.
	 * @param battery Battery Intent
	 * @return Battery is low
	 */
	protected boolean getIsLowBattery() {
		IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
	    Intent battery = registerReceiver(null, batIntentFilter);
		    
	    float pctLevel = (float)battery.getIntExtra(BatteryManager.EXTRA_LEVEL, 1) / 
	    battery.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
	    return pctLevel < 0.15;
	}
	
	protected boolean getIsConnected() {
		// Check if we're connected to a data network, and if so - if it's a mobile network.
		ConnectivityManager cm = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
	    NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
	    
	    return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
	
	protected boolean getIsMobileData() {
		// Check if we're connected to a data network, and if so - if it's a mobile network.
		ConnectivityManager cm = ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE));
		NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;
	    
	    return activeNetwork != null && activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE;
	}

	
}
