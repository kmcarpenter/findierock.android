package com.zenwerx.findierock.activity.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.adapter.AdvertisingAdapter;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.ImageDownloader;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Venue;

public class VenueListHelper {
	
	private static final String TAG = "findierock.VenueListHelper";
	
	private VenueListHelper() {}
	
    private static Object mLock = new Object();
    private static VenueListHelper mInstance = null;
    
    public static VenueListHelper getInstance()
    {
    	synchronized(mLock)
    	{
    		if (mInstance == null)
    		{
    			mInstance = new VenueListHelper();
    		}
    		return mInstance;
    	}
    }
    
	public AdvertisingAdapter<Venue> getArrayListAdapter(final Activity act, final Venue[] venues)
    {
    	ArrayAdapter<Venue> adapter = new ArrayAdapter<Venue>(act, R.id.eventinfo_artists, venues)
        {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		Venue v = getItem(position);
        		
        		return createVenueListItem(act, v);
        	}
        };
        return new AdvertisingAdapter<Venue>(act, adapter);
    }
    
    public View createVenueListItem(final Activity act, final Venue v)
    {
    	View vv = act.getLayoutInflater().inflate(R.layout.venueinfo_venuerow, null);
		
		if (v != null && vv != null)
		{
			TextView name = (TextView)vv.findViewById(R.id.venuelist_venuename);
			name.setTag(v.getVenueId());
			name.setText(v.getName());
			
			ImageView ven = (ImageView)vv.findViewById(R.id.venuelist_btn_venue_view);
			ven.setTag(v.getVenueId());
			
			ImageView share = (ImageView)vv.findViewById(R.id.venuelist_btn_venue_share);
			share.setTag(v.getVenueId());
			
			if (v.getLargeImage() != null && !v.getLargeImage().equals(""))
			{
				ImageView pic = (ImageView) vv.findViewById(R.id.venuelist_pic);
				ImageDownloader.getInstance().download(SettingsHelper.UrlBase() + v.getLargeImage(), pic);
			}
		}

		return vv;	
    }
    
    public ResultReceiver getResultReceiver(final Handler callback)
	{
		return new ResultReceiver(null)
		{
			@Override
			protected void onReceiveResult(int resultCode, Bundle resultData) {
				super.onReceiveResult(resultCode, resultData);
				
				if (resultCode == 1)
				{
					Gson g =  new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'z").create();
					
					try
					{
						Venue venue = g.fromJson(resultData.getString("response"), Venue.class );
																		
						FindieDbHelper.getInstance().getVenueHelper().saveVenues( new Venue[] { venue }, true );
																		
						if (callback != null)
							callback.sendMessage(new Message());
					}
					catch (Exception e)
					{
						Log.d(TAG, e.toString());
					}
				}
				
			}
		};
	}

	public void sortVenuesByName(ArrayList<Venue> venues) {
		
		Comparator<Venue> comp = new Comparator<Venue>()
		{
			@Override
			public int compare(Venue v1, Venue v2) {
				return v1.getName().compareTo(v2.getName());
			}
		};
		
		Collections.sort(venues, comp);
	}
}
