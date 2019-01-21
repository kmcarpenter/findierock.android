package com.zenwerx.findierock.activity;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.ArtistListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.ArtistListener;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Event;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class EventActivity extends Activity implements HeaderListener, ArtistListener {
	
	private static final String TAG = "findierock.EventDetailActivity";
	
	private int mEventId = -1;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventinfo);
        
        TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_event_detail);
		
		mEventId = getIntent().getExtras().getInt("eventid");
	
		FillDataTask task = new FillDataTask();
		task.execute((Void)null);
    }
    
    @Override
	public void onPause()
	{
		super.onPause();
		
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
	
	public class FillDataTask extends AsyncTask<Void, Void, Event>
	{	
		@Override
	    public void onPostExecute(Event e)
	    { 
			TextView name = (TextView)findViewById(R.id.eventinfo_name);
			TextView aom = (TextView)findViewById(R.id.eventinfo_aom);
			TextView date = (TextView)findViewById(R.id.eventinfo_date);
			TextView venue = (TextView)findViewById(R.id.eventinfo_venue);
			ListView artists = (ListView)findViewById(R.id.eventinfo_artists);
	       
	        // Now create an array adapter and set it to display using our row
	        artists.setAdapter(ArtistListHelper.getInstance().getArrayListAdapter(EventActivity.this, e.getArtists()));
			
	        venue.setTag(e.getVenueId());
	        
	        SpannableString content = new SpannableString(e.getVenue().getName());
	        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
	        venue.setText(content);
	        
			name.setText(e.getName());
			aom.setText(e.getAgeOfMajority() ? R.string.eventinfo_aom_true : R.string.eventinfo_aom_false);
			date.setText(e.getStartTime().toLocaleString());
	    }

		@Override
		protected Event doInBackground(Void... params) {
			return FindieDbHelper.getInstance().getEventsHelper().getEvent(mEventId);
		}
	}
    
    public void onArtistDetailRowClick(View v)
    {
    	ArtistListHelper.getInstance().onDetailRowClick(this, v);
    }
    
    public void onVenueClick(View v)
    {
    	Intent intent = new Intent(this, VenueActivity.class);
		Integer venueId = (Integer) v.getTag();
		intent.putExtra("venueid", venueId.intValue());
		
		try
		{
			this.startActivity(intent);
		}
		catch (Exception e)
		{
			Log.d("findierock", e.toString());
		}  	
    }
    
    public void onShowArtistDetail(View v)
    {
    	ArtistListHelper.getInstance().onShowDetail(this, v);
    }
    
    public void onMarkFav(View v)
    {
    	ArtistListHelper.getInstance().onMarkFav(this, v);
    }
    
    public void onArtistShare(View v)
    {
    	ArtistListHelper.getInstance().onShare(this, v);
    }
    
    @Override
    public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}
    
	@Override
    public void onRefresh(View v)
    {
		FillDataTask task = new FillDataTask();
		task.execute((Void)null);
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
}
