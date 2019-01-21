package com.zenwerx.findierock.receiver;

import java.util.Calendar;
import java.util.Date;

import com.zenwerx.findierock.activity.ListingActivity;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.ConversionHelper;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Status;
import com.zenwerx.findierock.service.restful.RestfulServiceHelper;
import com.zenwerx.findierock.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class LocationChangedReceiver extends BroadcastReceiver {
	private final String TAG = "findierock.LocationChangedReceiver";
	private int mResultsCount = 0;
	private Context mContext = null; 
	
	private static final int EVENTS_NOTIFICATION_ID = 1;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.d(TAG, "Received location intent.");
		
		// Check if we're running in the foreground, if not, check if
	  	// we have permission to do background updates.
		ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	  	boolean backgroundAllowed = cm.getBackgroundDataSetting();
	  	boolean inBackground = SettingsHelper.InBackground(context);
	  	
	  	if (!backgroundAllowed && inBackground)
	  	{
	  		Log.d(TAG, "In background, no background data allowed.");
	  		return;
	  	}
	  	
	  	mContext = context.getApplicationContext();
		
		String locationKey = LocationManager.KEY_LOCATION_CHANGED;
		
		Log.d(TAG, "Received passive location change");
		
		if (intent.hasExtra(locationKey)) {
			Location location = (Location)intent.getExtras().get(locationKey);
			
			Status status = FindieDbHelper.getInstance().getStatusHelper().getLatestStatus();
			if (status == null)
			{
				status = new Status();
			}
			
			double dist = ConversionHelper.GeoLocation.GetAbsoluteDistance(status.getLastLatitude(), status.getLastLongitude(), 
																				location.getLatitude(), location.getLongitude());			
			Calendar c = Calendar.getInstance();
			c.setTime(status.getLastUpdate());
			c.add(Calendar.HOUR, SettingsHelper.MinUpdateHours(mContext));
			Date minUpdateTime = c.getTime();
			
			if (dist > SettingsHelper.MinUpdateDistance(mContext) || minUpdateTime.before(new Date()))
			{
				Log.d(TAG, "Actually looking for new events...");
				mResultsCount = FindieDbHelper.getInstance().getEventsHelper().countEventsForToday();
				
				RestfulServiceHelper.getInstance().getLatestEvents(mContext, EventListHelper.getInstance().getResultReceiver(notificationHandler, location), 
						location, SettingsHelper.MaxSearchRadius(mContext), new Date(), false);
			}
			else
			{
				Log.d(TAG, "Distance or time not up to snuff... skipping updates.");
			}
		}
	}
	
	private Handler notificationHandler = new Handler()
	{
		@Override
		public void handleMessage(Message m)
		{			
			int newEvents = FindieDbHelper.getInstance().getEventsHelper().countEventsForToday();
			int diff = newEvents - mResultsCount;
			
			if (diff > 0)
			{
				SettingsHelper.SetIsDataNew(mContext, true);
				
				if (SettingsHelper.InBackground(mContext))
				{
					String title = String.format("%s New Events", Integer.toString(diff)); 
					NotificationManager nm = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
					
					Notification not = new Notification(R.drawable.newicon, title, System.currentTimeMillis());
					not.flags |= Notification.FLAG_AUTO_CANCEL;
					
					Intent i = new Intent(mContext, ListingActivity.class);
					PendingIntent pending = PendingIntent.getActivity(mContext, 0, i, 0);
					
					not.setLatestEventInfo(mContext, title, "There are new indie events close by!", pending);
					
					nm.notify(EVENTS_NOTIFICATION_ID, not);
				}
			}
		}
	};

}
