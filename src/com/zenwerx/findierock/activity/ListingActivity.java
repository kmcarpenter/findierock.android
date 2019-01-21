package com.zenwerx.findierock.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.app.ProgressDialog;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.fragments.EventListFragment;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.ConversionHelper;
import com.zenwerx.findierock.helper.EventListener;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.MyLocation;
import com.zenwerx.findierock.helper.PassiveLocationHelper;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.helper.MyLocation.LocationResult;
import com.zenwerx.findierock.model.Status;
import com.zenwerx.findierock.service.restful.RestfulServiceHelper;

public class ListingActivity extends FragmentActivity implements
		HeaderListener, EventListener {
	private final String TAG = "findierock.ListActivity";

	private LocationManager mLocationManager;
	private ProgressDialog mProgressDialog = null;
	private EventPagerAdapter mAdapter = null;
	private ViewPager mPager = null;
		
	private Date mLastUpdate = null;	

	int mRequest = -1;
	int mPageIndex = -1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "FindieRock is rocking!");

		super.onCreate(savedInstanceState);
		setContentView(R.layout.eventlist);

		TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_events);

		PassiveLocationHelper.StartListening(getApplicationContext());
		
		AnalyticsHelper.getInstance().init();
		AnalyticsHelper.getInstance().startNewSession(getApplicationContext());

		mLocationManager = (LocationManager) this
				.getSystemService(LOCATION_SERVICE);

		mLastUpdate = new Date();
		
		SettingsHelper.SetIsDataNew(ListingActivity.this, false);
		refreshEvents(false);

		mAdapter = new EventPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.eventlist_pager);
		mPager.setAdapter(mAdapter);
		
		mPager.setCurrentItem((int)Math.ceil(NUM_VIEWS/2.0f)-1);
	}

	@Override
	public void onPause() {
		super.onPause();

		if (mProgressDialog != null) {
			mRequest = -1;
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
		
		SettingsHelper.SetInBackground(this, true);
	}
	
	@Override
	public void onResume() {
		AnalyticsHelper.getInstance().trackPageView("/" + TAG);
		
		SettingsHelper.SetInBackground(this, false);
		
		if (SettingsHelper.IsDataNew(this) || mLastUpdate.before(SettingsHelper.LastSettingsUpdate(this))) {
			
			fillData();
			SettingsHelper.SetIsDataNew(this, false);
			
		}

		super.onResume();
	}
	
	@Override
	public void onDestroy()
	{
		if (isFinishing())
			AnalyticsHelper.getInstance().stopSession(this);
		
		super.onDestroy();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.basicmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return HeaderHelper.getInstance().onOptionsItemSelected(this, item);
	}

	@Override
	public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}

	@Override
	public void onRefresh(View v) {
		refreshEvents(true);
	}

	@Override
	public void onSearch(View v) {
		HeaderHelper.getInstance().onSearch(this, v);
	}

	@Override
	public void onEventList(View v) {
		// This screen
		return;
	}

	@Override
	public void onMaps(View v) {
		HeaderHelper.getInstance().onMaps(this, v);
	}

	private void refreshEvents(boolean forced) {
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_COARSE);
		String provider = mLocationManager.getBestProvider(crit, true);

		refreshEvents(mLocationManager.getLastKnownLocation(provider), forced);
	}

	private void refreshEvents(Location location, final boolean forced) {
		boolean update = forced;
		if (!forced && location != null) {
			Status status = FindieDbHelper.getInstance().getStatusHelper()
					.getLatestStatus();
			if (status == null)
				status = new Status();

			double dist = ConversionHelper.GeoLocation.GetAbsoluteDistance(
					location.getLatitude(), location.getLongitude(), status
							.getLastLatitude(), status.getLastLongitude());
			Calendar c = Calendar.getInstance();
			c.setTime(status.getLastUpdate());
			c.add(Calendar.HOUR, SettingsHelper.MinUpdateHours(this));
			Date minUpdateTime = c.getTime();

			if (dist > SettingsHelper.MinUpdateDistance(this)
					|| minUpdateTime.before(new Date())) {
				update = true;
			}
		}
		if (update && mRequest <= 0) {
			if (location != null) {
				Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
				Date now = c.getTime();
				mProgressDialog = ProgressDialog.show(this, "",
						getString(R.string.loading), true);
				mRequest = RestfulServiceHelper.getInstance().getLatestEvents(
						this,
						EventListHelper.getInstance().getResultReceiver(
								eventCallback, location), location,
						SettingsHelper.MaxSearchRadius(this), now, true);
			} else {
				// request location
				MyLocation ml = new MyLocation();
				ml.getLocation(this, new LocationResult() {

					@Override
					public void gotLocation(Location location) {
						// recurse if not null
						// ie: don't bother if we got null again... don't want a
						// recursive loop
						if (location != null) {
							refreshEvents(location, forced);
						}
					}

				});

			}
		} else {
			// Passive, use what we have already
			fillData();
		}
	}

	private void fillData() {
		
		mLastUpdate = new Date();
		
		if (mPager != null)
		{
			for(int i = 0; i < NUM_VIEWS; i++)
			{
				try
				{
					mFrags.get(i).updateTitles(true);
				}
				catch (IndexOutOfBoundsException e)
				{
					// yum!
					Log.d(TAG, "Fragment as position " + i + " wasn't created yet!" );
				}
			}
		}
	}

	public void onEventDetailRowClick(View v) {
		EventListHelper.getInstance().onDetailRowClick(this, v);
	}

	public void onShowEventDetail(View v) {
		EventListHelper.getInstance().onShowDetail(this, v);
	}

	public void onShowEventVenue(View v) {
		EventListHelper.getInstance().onShowVenue(this, v);
	}

	public void onEventAddCalendar(View v) {
		EventListHelper.getInstance().onEventAddCalendar(this, v);
	}

	public void onEventShare(View v) {
		EventListHelper.getInstance().onEventShare(this, v);
	}

	private Handler eventCallback = new Handler() {
		public void handleMessage(Message msg) {
			SettingsHelper.SetIsDataNew(ListingActivity.this, true);

			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			
			// reset
			if (mRequest == -1)
				return;
			mRequest = -1;

			// load data
			fillData();
		}
	};

	private final static int NUM_VIEWS = 4;
	private ArrayList<EventListFragment> mFrags = new ArrayList<EventListFragment>();

	private class EventPagerAdapter extends FragmentPagerAdapter {

		public EventPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return NUM_VIEWS;
		}

		public Fragment getItem(int position)
		{
			//return new EventListFragment(mEvents, (int)(SettingsHelper.MaxSearchRadius(ListingActivity.this) * (position/(float)NUM_VIEWS) ) );
			EventListFragment frag = null;
			try
			{
				frag = mFrags.get(position);
			}
			catch(IndexOutOfBoundsException e)
			{
				frag = new EventListFragment();
				Bundle b = new Bundle();
				b.putInt(EventListFragment.KEY_POSITION, position);
				b.putInt(EventListFragment.KEY_VIEWS, NUM_VIEWS);
				frag.setArguments(b);
				
				mFrags.add( frag );	
			}
			
			return frag;
		}
	}
}
