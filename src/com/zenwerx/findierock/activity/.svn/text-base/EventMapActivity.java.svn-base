package com.zenwerx.findierock.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.DebugHelper;
import com.zenwerx.findierock.helper.EventItemizedOverlay;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Event;
import com.zenwerx.findierock.service.restful.RestfulServiceHelper;

public class EventMapActivity extends com.google.android.maps.MapActivity implements HeaderListener {

	private static final String TAG = "findierock.EventMapActivity";
	
	private List<Overlay> mMapOverlays;
	private EventItemizedOverlay mItemizedOverlay;
	private LocationManager mLocationManager;
	private MapView mMapView;
	private ProgressDialog mProgressDialog = null;
	int mRequest = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		//overridePendingTransition(R.anim.flip_in, R.anim.flip_out);
		
		if (DebugHelper.isDebugBuild(this))
		{
			setContentView(R.layout.eventmap_debug);
		} else
		{
			setContentView(R.layout.eventmap_release);
		}
		mMapView = (MapView)findViewById(R.id.mapview);
				
		TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_map);
	     
        mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = mLocationManager.getBestProvider(crit, true);
        Location loc = mLocationManager.getLastKnownLocation(provider);
        if (loc != null)
        {
        	GeoPoint point = new GeoPoint((int)(loc.getLatitude() * 1000000.0f), (int)(loc.getLongitude()*1000000.0f));
        	mMapView.getController().setCenter(point);
        }
        
        mMapView.getController().setZoom(9);
        mMapView.setBuiltInZoomControls(true);
        
        mMapOverlays = mMapView.getOverlays();
        
        SettingsHelper.SetIsDataNew(this, false);
        
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
		
		if(SettingsHelper.IsDataNew(this))
		{
			SettingsHelper.SetIsDataNew(this, false);
			FillDataTask task = new FillDataTask();
	        task.execute((Void)null);
		}
		
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
		
	@Override
	public void onRefresh(View v) {
		refreshEvents(mMapView.getMapCenter());
	}

	@Override
	public void onSearch(View v) {
		HeaderHelper.getInstance().onSearch(this, v);
	}
	
	@Override
	public void onEventList(View v) {
		HeaderHelper.getInstance().onEventList(this, v);
	}
	
	@Override
	public void onMaps(View v) {
		// This screen
		return;
	}
	
    @Override
    public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	private class FillDataTask extends AsyncTask<Void, Void, ArrayList<Event>>
	{
		@Override
		public void onPostExecute(ArrayList<Event> events)
		{
			if (mItemizedOverlay != null)
			{
				mMapOverlays.remove(mMapOverlays.indexOf(mItemizedOverlay));
			}
			
			if (events.size() > 0)
			{
				Drawable drawable = EventMapActivity.this.getResources().getDrawable(R.drawable.icon);
		        mItemizedOverlay = new EventItemizedOverlay(drawable, EventMapActivity.this);
			        
		        for (Event e : events)
		        {
				
					GeoPoint point = new GeoPoint((int)(e.getLatitude() * 1000000.0f), (int)(e.getLongitude() * 1000000.0f));
					OverlayItem item = new OverlayItem(point, e.getName(),
											getResources().getString(R.string.map_venue) + e.getVenue().getName() + "\r\n" +
											getResources().getString(R.string.map_where) + e.getVenue().getAddress() + "\r\n" +
											getResources().getString(R.string.map_when) + e.getStartTime().toLocaleString() );
					
					mItemizedOverlay.addOverlay(item);
				}
					
		        mMapOverlays.clear();
				mMapOverlays.add(mItemizedOverlay);
				mMapView.invalidate();
			}
		}

		@Override
		protected ArrayList<Event> doInBackground(Void... params) {
			FindieDbHelper helper = FindieDbHelper.getInstance();
			ArrayList<Event> events = helper.getEventsHelper().getEventsForToday();
			
			return events;
		}
	};
	
	private void refreshEvents(GeoPoint point)
	{
		Location l = new Location(LocationManager.GPS_PROVIDER);
		
		float latitude = point.getLatitudeE6()/1000000.0f;
		float longitude = point.getLongitudeE6()/1000000.f;

		l.setLatitude(latitude);
		l.setLongitude(longitude);
		
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    	Date now = c.getTime();
    	mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading), true);
    	mRequest = RestfulServiceHelper.getInstance().getLatestEvents(this, EventListHelper.getInstance().getResultReceiver(eventCallback, l), 
    																			l, SettingsHelper.MaxSearchRadius(this), now, true);
	}
	
	private Handler eventCallback = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			SettingsHelper.SetIsDataNew(EventMapActivity.this, true);
			
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
