package com.zenwerx.findierock.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.activity.helper.VenueListHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.EventListener;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.ImageDownloader;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Venue;
import com.zenwerx.findierock.service.restful.RestfulServiceHelper;

public class VenueActivity  extends Activity implements HeaderListener, EventListener {
	
	private int mRequest = -1;
	private ProgressDialog mProgressDialog = null;
	private int mVenueId = -1; 
	
	private final static String TAG = "findierock.VenueActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.venueinfo);
        
        TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_venue);
		
        mVenueId = getIntent().getExtras().getInt("venueid");
		        
		FillDataTask task = new FillDataTask();
		task.execute((Void)null);
	}
    
    @Override
	public void onPause()
	{
		super.onPause();
		
		if (mProgressDialog != null) {
			mRequest = -1;
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		
		SettingsHelper.SetInBackground(this, true);
	}
	
	@Override
	public void onResume()
	{
		AnalyticsHelper.getInstance().trackPageView("/" + TAG);
		
		SettingsHelper.SetInBackground(this, false);
		
		super.onResume();
	}
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.basicmenu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		return HeaderHelper.getInstance().onOptionsItemSelected(this, item);
	}
    
	private class FillDataTask extends AsyncTask<Void, Void, Venue>
	{
		public void onPostExecute(Venue venue)
		{		    	
			TextView name 		= (TextView)findViewById(R.id.venueinfo_name);
			TextView address 	= (TextView)findViewById(R.id.venueinfo_address);
			TextView address2 	= (TextView)findViewById(R.id.venueinfo_address2);
			TextView citystate 	= (TextView)findViewById(R.id.venueinfo_citystate);
			//TextView website 	= (TextView)findViewById(R.id.venueinfo_website);
			
			ImageView photo 	= (ImageView)findViewById(R.id.venue_photo);
			if (venue.getLargeImage() != null && !venue.getLargeImage().equals(""))
			{
				ImageDownloader.getInstance().download(SettingsHelper.UrlBase() + venue.getLargeImage(), photo);
			}
			
			name.setText(venue.getName());
			address.setText(venue.getAddress());
			if (venue.getAddress2() == null || venue.getAddress2().trim().equals(""))
			{
				address2.setVisibility(View.GONE);
			} else {
				address2.setText(venue.getAddress2());
			}
			String city = venue.getCity();
			if (!city.equals("") && !venue.getProvince().equals(""))
			{
				city += ", ";
			}
			city += venue.getProvince();
			
			citystate.setText(city);
			
			/*
			if (venue.getWebsite() == null || venue.getWebsite().trim().equals("")) {
				website.setVisibility(View.GONE);
			} else {
				website.setText(venue.getWebsite());
				Linkify.addLinks(website, Linkify.WEB_URLS);
			}
			*/
			
			ListView events = (ListView)findViewById(R.id.venueinfo_events);
	        
	        events.setAdapter(EventListHelper.getInstance().getArrayListAdapter(VenueActivity.this, venue.getEvents(), true, true));
	    }

		@Override
		protected Venue doInBackground(Void... params) {
			return FindieDbHelper.getInstance().getVenueHelper().getVenue(mVenueId);
		}
	};
	
    @Override
    public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}
    
	@Override
    public void onRefresh(View v)
    {
		refreshVenue(true);
    }
    
	@Override
	public void onSearch(View v) {
		HeaderHelper.getInstance().onSearch(this, v);
	}
	
	@Override
	public void onEventList(View v) {
		HeaderHelper.getInstance().onEventList(this, v);
		return;
	}
	
	@Override
	public void onMaps(View v) {
		HeaderHelper.getInstance().onMaps(this, v);
	}
	
	public void onEventDetailRowClick(View v)
	{
		EventListHelper.getInstance().onDetailRowClick(this, v);
	}
	
	public void onShowEventDetail(View v)
	{
		EventListHelper.getInstance().onShowDetail(this, v);
	}
	
	public void onShowEventVenue(View v)
	{
		EventListHelper.getInstance().onShowVenue(this, v);
	}
	
	public void onEventAddCalendar(View v)
	{
		EventListHelper.getInstance().onEventAddCalendar(this, v);
	}
	
	public void onEventShare(View v)
	{
    	EventListHelper.getInstance().onEventShare(this, v);
	}
	
	private void refreshVenue(boolean forced)
	{
		boolean update = forced;
		//if (!update)
		//{
		//	if (mVenue != null && !mVenue.didFetchAlbums())
		//		update = true;
		//}
		
		if (update && mRequest <= 0)
		{
			mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading_venue), true);
	    	mRequest = RestfulServiceHelper.getInstance().getVenue(this, VenueListHelper.getInstance().getResultReceiver(venueCallback), 
	    																	mVenueId, forced);
		}
	}
	
	private Handler venueCallback = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			
			// reset
			if (mRequest == -1)
				return;
			mRequest = -1;
			
			FillDataTask task = new FillDataTask();
			task.execute((Void)null);
		}
	};
}