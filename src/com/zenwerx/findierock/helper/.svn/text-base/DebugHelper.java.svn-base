package com.zenwerx.findierock.helper;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class DebugHelper {
	// Define the debug signature hash (Android default debug cert). Code from sigs[i].hashCode()
	protected final static int DEBUG_SIGNATURE_HASH = 2073860387;
	private static Boolean _isDebugBuild = null;

	// Checks if this apk was built using the debug certificate
	// Used e.g. for Google Maps API key determination (from: http://whereblogger.klaki.net/2009/10/choosing-android-maps-api-key-at-run.html)
	public static Boolean isDebugBuild(Context context) {
	    if (_isDebugBuild == null) {
	        try {
	            _isDebugBuild = false;
	            Signature [] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
	            for (int i = 0; i < sigs.length; i++) {
	                if (sigs[i].hashCode() == DEBUG_SIGNATURE_HASH) {
	                    Log.d("DebugHelper", "This is a debug build!");
	                    _isDebugBuild = true;
	                    break;
	                }
	            }
	        } catch (NameNotFoundException e) {
	            e.printStackTrace();
	        }      
	    }
	    return _isDebugBuild;
	}
}
