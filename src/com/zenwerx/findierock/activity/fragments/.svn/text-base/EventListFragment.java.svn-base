package com.zenwerx.findierock.activity.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Event;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EventListFragment extends Fragment {

		public static final String KEY_VIEWS = "numViews";
		public static final String KEY_POSITION = "position";
		private static final String TAG = "findierock.EventListFragment";
		
		private LocationManager mLocationManager = null;
		private Event[] mEvents;
		private int mPosition = 0;
		private int mTotal = 0;
		
		private TextView mPrev = null;
		private TextView mCur = null;
		private TextView mNext = null;
		private TextView mNoEvents = null;
		private LinearLayout mProgress = null;
		
		ListView mList = null;
				
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
		{
			super.onCreateView(inflater, container, savedInstanceState);
			mLocationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
						
			Bundle args = getArguments();
			mPosition = args.getInt(KEY_POSITION);
			mTotal = args.getInt(KEY_VIEWS);
			
			View v = inflater.inflate(R.layout.eventlist_fragment, container, false);
			
			mList = (ListView)v.findViewById(R.id.eventlist_list);
			mProgress = (LinearLayout)v.findViewById(R.id.event_progress);
			mNoEvents = (TextView)v.findViewById(R.id.eventlist_noevents);
			mPrev = (TextView)v.findViewById(R.id.prevView);
			mCur = (TextView)v.findViewById(R.id.thisView);
			mNext = (TextView)v.findViewById(R.id.nextView);
			
			updateTitles(false);
			
			return v;
		}
		
		public void updateTitles(boolean refresh)
		{		
			final boolean imperial = SettingsHelper.UsingImperial(getActivity());
			final TextView[] views = new TextView[] { mPrev, mCur, mNext };
			final double steps = (SettingsHelper.MaxSearchRadius(getActivity())/(float)(mTotal-1));
			int offset = -1;
			
			for (TextView pos : views)
			{
				final boolean isNext = (pos.getId() == mNext.getId());
				final boolean isPrev = (pos.getId() == mPrev.getId());
				final boolean isCur = (pos.getId() == mCur.getId());
				if ((mPosition == 0 && isPrev) 
						|| (mPosition == (mTotal-1) && isNext ) )
				{
					pos.setVisibility(View.GONE);
				}
				else if ( (mPosition == (mTotal - 1) && isCur)
						|| (mPosition == (mTotal - 2) && isNext ))
				{
					String text = "All";
					if (isNext)
						text += " >";
					pos.setText(text);
				}
				else 
				{
					int dist = (int) Math.round(steps * (mPosition + 1 + offset));
					String text = String.format("%1$d %2$s", dist, imperial ? "mi" : "km" );
					if (isNext)
						text += " >";
					else if (isPrev)
						text = "< " + text;
					
					pos.setText( text );
				}
				offset++;
			}
			
			if (refresh)
			{
				mProgress.setVisibility(View.VISIBLE);
				mNoEvents.setVisibility(View.GONE);
				mList.setVisibility(View.GONE);
				FillDataTask task = new FillDataTask();
				task.execute((Void)null);
			}
		}
		
		@Override
		public void onActivityCreated(Bundle savedInstanceState)
		{
			super.onActivityCreated(savedInstanceState);
			
			mProgress.setVisibility(View.VISIBLE);
			mNoEvents.setVisibility(View.GONE);
			mList.setVisibility(View.GONE);
			FillDataTask task = new FillDataTask();
			task.execute((Void)null);
		}
				
		private class FillDataTask extends AsyncTask<Void, Void, Void>
		{	

			@Override
			protected Void doInBackground(Void... params) {
				
				try
				{
					Criteria crit = new Criteria();
					crit.setAccuracy(Criteria.ACCURACY_COARSE);
					
					String provider = mLocationManager.getBestProvider(crit, true);
					final Location loc = mLocationManager.getLastKnownLocation(provider);
					
					ArrayList<Event> events = FindieDbHelper.getInstance().getEventsHelper().getEventsForToday();
								
					if (mPosition != (mTotal - 1))
					{
						double steps = (SettingsHelper.MaxSearchRadius(getActivity())/(float)(mTotal-1));
						double maxDist = steps * (mPosition + 1);
										
						for( int i = events.size() - 1; i >= 0; i--)
						{
							double dist = events.get(i).getDistanceFrom(loc);
							Log.d(TAG, String.format("Checking to see if %1$f > %2$f", dist, maxDist));
							if ( dist > maxDist)
							{
								events.remove(i);
							}
						}
					}
					
					Comparator<Event> comp = new Comparator<Event>() {
	
						@Override
						public int compare(Event e1, Event e2) {
							double d1 = e1.getDistanceFrom(loc);
							double d2 = e2.getDistanceFrom(loc);
					
							if (d1 == d2)
								return 0;
							if (d1 > d2)
								return 1;
							else
								return -1;
						}
					};
					
					Collections.sort(events, comp);
					
					mEvents = new Event[events.size()];
					events.toArray(mEvents);
				} catch (NullPointerException e)
				{
					Log.d(TAG, "Whoa there... I think my activity probably got destroyed!");
				}
		
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result)
			{
				try
				{
					mProgress.setVisibility(View.GONE);
					if (mEvents.length > 0)
					{
						mList.setVisibility(View.VISIBLE);
						mNoEvents.setVisibility(View.GONE);
						mList.setAdapter(EventListHelper.getInstance().getArrayListAdapter(getActivity(), mEvents));
					}
					else
					{
						mList.setVisibility(View.GONE);
						mNoEvents.setVisibility(View.VISIBLE);
					}
				} catch (NullPointerException e)
				{
					Log.d(TAG, "At this piont, we're pretty sure the activity got demolished...");
				}
			}
			
			
		}
		
		
}
