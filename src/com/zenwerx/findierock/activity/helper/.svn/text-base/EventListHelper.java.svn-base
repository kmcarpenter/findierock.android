package com.zenwerx.findierock.activity.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.EventActivity;
import com.zenwerx.findierock.activity.VenueActivity;
import com.zenwerx.findierock.activity.adapter.AdvertisingAdapter;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.ConversionHelper;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Event;
import com.zenwerx.findierock.model.Status;

public class EventListHelper {
	private final String TAG = "findierock.EvenListHelper";
	
    private EventListHelper() {}
	
    private static Object mLock = new Object();
    private static EventListHelper mInstance = null;
    
    public static EventListHelper getInstance()
    {
    	synchronized(mLock)
    	{
    		if (mInstance == null)
    		{
    			mInstance = new EventListHelper();
    		}
    		return mInstance;
    	}
    }
	
    public void onDetailRowClick(final Activity act, final View v)
	{
		RelativeLayout parent = (RelativeLayout)v.getParent();
		View op = parent.findViewById(R.id.operation_row);
		boolean expanded = op.getVisibility() != View.GONE;
		
		op.setVisibility(!expanded ? View.VISIBLE : View.GONE );
		
		ImageView iv = (ImageView)v.findViewById(R.id.img_expandable);
		if (iv != null)
		{
			iv.setImageResource(!expanded ? R.drawable.arrow_up_float : R.drawable.arrow_down_float);
		}
	}
	
	public void onShowDetail(final Context ctx, final View v)
	{
		Intent intent = new Intent(ctx, EventActivity.class);
		Integer eventId = (Integer) v.getTag();
		intent.putExtra("eventid", eventId.intValue());
		
		try
		{
			ctx.startActivity(intent);
		}
		catch (Exception e)
		{
			Log.d("findierock", e.toString());
		}
	}
	
	public void onShowVenue(final Activity act, final View v)
	{
		Intent intent = new Intent(act, VenueActivity.class);
		Integer venueId = (Integer) v.getTag();
		intent.putExtra("venueid", venueId.intValue());
		
		try
		{
			act.startActivity(intent);
		}
		catch (Exception e)
		{
			Log.d("findierock", e.toString());
		}
	}
	
	public void onEventAddCalendar(final Activity act, final View v)
	{
		Event e = FindieDbHelper.getInstance().getEventsHelper().getEvent((Integer)v.getTag());
		
		Intent intent = new Intent(Intent.ACTION_EDIT);
		intent.setType("vnd.android.cursor.item/event");
		intent.putExtra("beginTime", e.getStartTime().getTime());
		intent.putExtra("endTime", e.getStartTime().getTime()+(60*60*1000));
		intent.putExtra("title", e.getName());
		intent.putExtra("eventLocation", e.getVenue().getName());
		act.startActivity(Intent.createChooser(intent, act.getResources().getString(R.string.event_add_calendar)));
	}
	
	public void onEventShare(final Activity act, final View v)
	{
    	Event e = FindieDbHelper.getInstance().getEventsHelper().getEvent((Integer)v.getTag());
    	
    	String detail = act.getResources().getString(R.string.event_share_detail);
    	detail = String.format(detail, e.getName(), e.getStartTime().toLocaleString());
    	
    	Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, e.getName());
    	intent.putExtra(android.content.Intent.EXTRA_TEXT, detail);
    	
    	act.startActivity(Intent.createChooser(intent, act.getResources().getString(R.string.event_share_title) 
    												+ e.getName() ) );
	}
	
	public AdvertisingAdapter<Event> getArrayListAdapter(final Activity act, final Event[] events)
	{
		AdvertisingAdapter<Event> adapter = null;
		if (events != null && act != null)
			adapter = getArrayListAdapter(act, events, false, false);
		return adapter;
	}
	
	public AdvertisingAdapter<Event> getArrayListAdapter(final Activity act, final Event[] events, final boolean hideVenue, final boolean hideDist)
	{
		ArrayAdapter<Event> adapter = new ArrayAdapter<Event>(act, R.id.venueinfo_events, events)
        {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		Event e = getItem(position);
				
        		return createEventListItem(act, e, hideVenue, hideDist);
        	}
        };
        return new AdvertisingAdapter<Event>(act, adapter);
	}
	
	public View createEventListItem(final Activity act, final Event e)
	{
		return createEventListItem(act, e, false, false);
	}
	
	public View createEventListItem(final Activity act, final Event e, final boolean hideVenue, final boolean hideDist)
	{
		View v = act.getLayoutInflater().inflate(R.layout.eventlist_row, null);

		if (e != null && v != null) {
			TextView dist = (TextView)v.findViewById(R.id.event_dist);
			
			if(hideDist)
			{
				dist.setVisibility(View.GONE);
			}
			else
			{
				Criteria crit = new Criteria();
				crit.setAccuracy(Criteria.ACCURACY_COARSE);
				
				LocationManager lm = (LocationManager) act.getSystemService(Context.LOCATION_SERVICE);
				String provider = lm.getBestProvider(crit, true);
				Location loc = lm.getLastKnownLocation(provider);
				
				dist.setVisibility(View.VISIBLE);
				double eventDist = e.getDistanceFrom(loc);
				if (SettingsHelper.UsingImperial(act))
					eventDist = Math.ceil(ConversionHelper.Distance.KiloMetresToMiles(eventDist));
				dist.setText(String.format("%1$d %2$s", (int)eventDist, SettingsHelper.UsingImperial(act) ? "mi" : "km"));
			}
			
			TextView time = (TextView) v.findViewById(R.id.event_time);
			TextView name = (TextView) v.findViewById(R.id.event_desc);

			TextView eventButton = (TextView) v.findViewById(R.id.btn_event_detail);
			eventButton.setTag(e.getEventId());
			
			TextView calButton = (TextView) v.findViewById(R.id.btn_event_addcalendar);
			calButton.setTag(e.getEventId());
			
			TextView shareButton = (TextView) v.findViewById(R.id.btn_event_share);
			shareButton.setTag(e.getEventId());
			
			TextView venueButton = (TextView) v.findViewById(R.id.btn_venue_detail);
			if (hideVenue)
			{
				((View)venueButton.getParent()).setVisibility(View.GONE);
			} else {
				venueButton.setTag(e.getVenueId());
			}

			time.setText(e.getStartTime().toLocaleString());
			name.setText(e.getName());
		}

		return v;
	}
	
	public ResultReceiver getResultReceiver(final Handler callback, final Location location)
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
						
						Log.d(TAG, String.format("Started with %d events", FindieDbHelper.getInstance().getEventsHelper().countEventsForToday()));
						
						Event[] events = g.fromJson(resultData.getString("response"), Event[].class );
						FindieDbHelper.getInstance().getEventsHelper().saveEvents(events);
						
						Status status = new Status(location, new Date());
						FindieDbHelper.getInstance().getStatusHelper().saveStatus(status);
						
						Log.d(TAG, String.format("Ended with %d events", FindieDbHelper.getInstance().getEventsHelper().countEventsForToday()));
												
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
	
	public void sortEventsByDate(ArrayList<Event> events)
	{
		Comparator<Event> comp = new Comparator<Event>() {

			@Override
			public int compare(Event e1, Event e2) {
				if (e1.getStartTime() != null && e2.getStartTime() == null)
				{
					return 1;
				}
				else if (e2.getStartTime() != null && e1.getStartTime() == null)
				{
					return -1;
				}
				else if (e1.getStartTime().equals(e2.getStartTime()))
				{
					return 0;
				}
				else
				{
					if (e1.getStartTime().after(e2.getStartTime()))
					{
						return 1;
					}
					else 
					{
						return -1;
					}
				}
			}
		};
		
		Collections.sort(events, comp);
	}
}
