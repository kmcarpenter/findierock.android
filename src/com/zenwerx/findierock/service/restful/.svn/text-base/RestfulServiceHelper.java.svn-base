package com.zenwerx.findierock.service.restful;

import java.util.Date;

import com.zenwerx.findierock.helper.ConversionHelper;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Artist;


import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.ResultReceiver;
import android.util.Log;

public class RestfulServiceHelper {
		
	private final static String TAG = "findierock.ResultfulServiceHelper";
	
	private static RestfulServiceHelper mInstance = null;
	private static Object mLock = new Object();
	
	private int requestId;
	
	private final String LATEST_EVENTS = new String("Events");
	private final String ARTIST_ALBUMS = new String("Albums");
	private final String REFRESH_ARTIST = new String("Artist");
	private final String REFRESH_VENUE = new String("Venue");
	
	private RestfulServiceHelper()
	{

	}
	
	public static RestfulServiceHelper getInstance()
	{
		synchronized(mLock)
		{
			if (mInstance == null)
			{
				mInstance = new RestfulServiceHelper(); 
			}
			return mInstance;
		}
	}
	
	/**
	 * This is the real service dispatcher used by all public facing methods. 
	 * 
	 * @param context
	 * @param receiver
	 * @param params
	 * @param values
	 * @return
	 */
	private int dispatchServiceRequest(Context context, ResultReceiver receiver, String operation, String[] params, String[] values, boolean forcedUpdate)
	{
		int dispatchedRequest = 0;
		synchronized(this)
		{
			final Intent intent = new Intent(Intent.ACTION_SYNC, null, context, RestfulService.class);
			intent.putExtra("receiver", receiver);
			intent.putExtra("requestId", requestId);
			intent.putExtra("operation", operation);
			intent.putExtra("params", params);
			intent.putExtra("values", values);
			intent.putExtra("forcedUpdate", forcedUpdate);
			
			Log.d(TAG, "Starting thread for updates...");
			context.startService(intent);
			
			dispatchedRequest = requestId;
			
			requestId++;
		}
		
		return dispatchedRequest;
	}
	
	public int getLatestEvents(Context context, ResultReceiver receiver, Location location, int maxDistance, Date lastUpdate, boolean forcedUpdate)
	{
		Log.d(TAG, "Looking for latest events.");
		
		String[] params = new String[] {
				"latitude",
				"longitude",
				"maxdistance",
				"lastupdate"
		};
		
		if (SettingsHelper.UsingImperial(context))
			maxDistance = (int)Math.ceil(ConversionHelper.Distance.KiloMetresToMiles(maxDistance));
		
		String[] values = new String[] {
				new Double(location.getLatitude()).toString(),
				new Double(location.getLongitude()).toString(),
				new Integer(maxDistance).toString(),
				lastUpdate.toString()
		};

		return dispatchServiceRequest(context, receiver, LATEST_EVENTS, params, values, forcedUpdate);
	}
	
	public int getAlbums(Context context, ResultReceiver receiver, Artist artist, boolean forcedUpdate)
	{
		String[] params = new String[] { "artist" };
		String[] values = new String[] { Integer.toString(artist.getArtistId()) };
		
		return dispatchServiceRequest(context, receiver, ARTIST_ALBUMS, params, values, forcedUpdate );
	}
	
	public int getArtist(Context context, ResultReceiver receiver, int artistId, boolean forcedUpdate)
	{
		String[] params = new String[] { "artist" };
		String[] values = new String[] { Integer.toString(artistId) };
		
		return dispatchServiceRequest(context, receiver, REFRESH_ARTIST, params, values, forcedUpdate );
	}
	
	public int getVenue(Context context, ResultReceiver receiver, int venueId, boolean forcedUpdate)
	{
		String[] params = new String[] { "venue" };
		String[] values = new String[] { Integer.toString(venueId) };
		
		return dispatchServiceRequest(context, receiver, REFRESH_VENUE, params, values, forcedUpdate );
	}
	
}
