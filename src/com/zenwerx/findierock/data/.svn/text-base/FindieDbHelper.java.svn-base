package com.zenwerx.findierock.data;

import android.content.Context;

public class FindieDbHelper {

	private static FindieDbHelper mInstance = null;
	private static Object mLock = new Object();
	
	private FindieDbAdapter mDbAdapter = null;
			
	private FindieDbHelper()
	{
		
	}
	
	public static FindieDbHelper getInstance()
	{
		synchronized(mLock)
		{
			if (mInstance == null)
			{
				mInstance = new FindieDbHelper(); 
			}
			return mInstance;
		}
	}
	
	public void setContext(Context context)
	{
		/**
		 * I am going to assume, since I'm the only one writing this... nobody will
		 * hold a reference to the db adapter other than this class. Meaning when 
		 * I do this, the old instance will be gc'd.
		 */
		if (context == null && mDbAdapter != null)
		{
			mDbAdapter.close();
		}
		else
		{
			mDbAdapter = new FindieDbAdapter(context);
			mDbAdapter.open();
		}
	}
	
	public EventsHelper getEventsHelper()
	{
		return mDbAdapter.getEventsHelper();
	}
	
	public ArtistHelper getArtistHelper()
	{
		return mDbAdapter.getArtistHelper();
	}
	
	public VenueHelper getVenueHelper()
	{
		return mDbAdapter.getVenueHelper();
	}
	
	public StatusHelper getStatusHelper()
	{
		return mDbAdapter.getStatusHelper();
	}
	
	public AlbumHelper getAlbumHelper()
	{
		return mDbAdapter.getAlbumHelper();
	}
	
	public AlbumTrackHelper getAlbumTrackHelper()
	{
		return mDbAdapter.getAlbumTrackHelper();
	}
	
	public FavouriteHelper getFavouriteHelper()
	{
		return mDbAdapter.getFavouriteHelper();
	}
}
