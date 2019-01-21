package com.zenwerx.findierock.helper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.zenwerx.findierock.service.AnalyticsUpdateService;

public class AnalyticsHelper {
	private static AnalyticsHelper mInstance = null;
	private static Object mLock = new Object();
	
	private static final String TAG = "findierock.AnalyticsHelper";
		
	public static AnalyticsHelper getInstance()
	{
		synchronized(mLock)
		{
			if (mInstance == null)
				mInstance = new AnalyticsHelper();
		}
		return mInstance;
	}
	
	private GoogleAnalyticsTracker mTracker;
	private PendingIntent mAlarm;
	private boolean mTracking = false;
	
	private AnalyticsHelper() { }
	
	public void init()
	{
		if (mTracker == null)
			mTracker = GoogleAnalyticsTracker.getInstance();
	}
	
	public void startNewSession(Context ctx)
	{		
		if (!mTracking)
		{
			if (mTracker == null)
			{
				init();
			}
			
			mTracker.start(SettingsHelper.AnalyticsAccount(), 30000, ctx );
			
			Log.d(TAG, "Registering new alarm...");
			Intent alarm = new Intent(ctx, AnalyticsUpdateService.class);
			mAlarm = PendingIntent.getService(ctx, 0, alarm, PendingIntent.FLAG_UPDATE_CURRENT);
			
			AlarmManager am = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
			am.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + (30*1000), 
								AlarmManager.INTERVAL_FIFTEEN_MINUTES, mAlarm );
			
			mTracking = true;
		}
	}
	
	public boolean dispatch()
	{
		boolean result = false;
		if (mTracking)
		{
			// avoid race condition
			if (mTracker == null)
			{
				init();
			}
			result = mTracker.dispatch();
		}
		return result;
	}
	
	public void stopSession(Context ctx)
	{
		if (mTracking)
		{
			AlarmManager am = (AlarmManager)ctx.getSystemService(Context.ALARM_SERVICE);
			am.cancel(mAlarm);
			
			if (mTracker != null)
			{
				mTracker.stop();
				dispatch();
			}
			
			mTracking = false;
		}
	}
	
	public void trackPageView(String page)
	{
		if (mTracking)
		{
			if (mTracker == null)
			{
				init();
			}
			mTracker.trackPageView(page);
		}
	}
}
