package com.zenwerx.findierock.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;

import com.zenwerx.findierock.receiver.LocationChangedReceiver;

public class PassiveLocationHelper {
	private final static String TAG = "findierock.PassiveLocationHelper";
	
	private static PendingIntent mPending = null;

	public static void StopListening(Context ctx)
	{
		if (mPending != null)
		{
			LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
			locationManager.removeUpdates(mPending);
		}
	}
	
	public static void StartListening(Context ctx)
	{
		if (mPending == null)
		{
			Log.d(TAG, "Registering for passive updates");
			Intent passiveIntent = new Intent(ctx, LocationChangedReceiver.class);
			mPending = PendingIntent.getBroadcast(ctx, 0, passiveIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	
			// Location updates every 15 minutes
			// Or if you've moved MinUpdateDistance km
			LocationManager locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(
					LocationManager.PASSIVE_PROVIDER,
					AlarmManager.INTERVAL_FIFTEEN_MINUTES, (SettingsHelper
							.MinUpdateDistance(ctx) * 1000), mPending);
		} else {
			Log.d(TAG, "Already listening for updates... skipping request.");
		}
	}
	
}
