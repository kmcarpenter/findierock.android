package com.zenwerx.findierock.activity.fragments;

import java.util.ArrayList;
import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.AlbumListHelper;
import com.zenwerx.findierock.activity.helper.ArtistListHelper;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.activity.helper.VenueListHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.Artist;
import com.zenwerx.findierock.model.Event;
import com.zenwerx.findierock.model.Venue;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment 
{

	public static final String KEY_PREV = "prevText";
	public static final String KEY_CUR = "curText";
	public static final String KEY_NEXT = "nextText";
	public static final String KEY_TYPE = "fragType";
	public static final String KEY_QUERY = "query";
	
	public static enum FRAG_TYPE 
	{
		FRAG_TYPE_EVENTS,
		FRAG_TYPE_VENUES,
		FRAG_TYPE_ARTISTS,
		FRAG_TYPE_ALBUMS
	}
	
	@SuppressWarnings("unused")
	private static final String TAG = "findierock.SearchFragment";
	
	private FRAG_TYPE mFragType = null;	
	private String mQuery = "";
	
	private TextView mPrev = null;
	private TextView mCur = null;
	private TextView mNext = null;
	
	private TextView mNoInfo = null;
	private LinearLayout mProgress = null;
		
	ListView mList = null;
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		super.onCreateView(inflater, container, savedInstanceState);
		View v = inflater.inflate(R.layout.search_fragment, container, false);
		
		Bundle args = getArguments();
					
		int fragType = args.getInt(KEY_TYPE);
		for(FRAG_TYPE frag : FRAG_TYPE.values())
		{
			if (frag.ordinal() == fragType)
			{
				mFragType = frag;
				break;
			}
		}
		mQuery = args.getString(KEY_QUERY);
		
		String nextText = args.getString(KEY_NEXT);
		String curText = args.getString(KEY_CUR);
		String prevText = args.getString(KEY_PREV);
		
		if (!prevText.equals(""))
			prevText = "< " + prevText;
		if (!nextText.equals(""))
			nextText += " >";
		
		mList = (ListView)v.findViewById(R.id.search_list);
		
		mPrev = (TextView)v.findViewById(R.id.prevView);
		mPrev.setText(prevText);
		mCur = (TextView)v.findViewById(R.id.thisView);
		mCur.setText(curText);
		mNext = (TextView)v.findViewById(R.id.nextView);
		mNext.setText(nextText);
		
		mProgress = (LinearLayout)v.findViewById(R.id.search_progress);
		mNoInfo = (TextView)v.findViewById(R.id.search_noresults);
					
		return v;
	}
			
	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
		refreshData();
	}
	
			
	public void refreshData()
	{	
		mProgress.setVisibility(View.VISIBLE);
		mNoInfo.setVisibility(View.GONE);
		mList.setVisibility(View.GONE);
		if (mFragType == FRAG_TYPE.FRAG_TYPE_ALBUMS)
		{
			FillAlbumsTask task = new FillAlbumsTask();
			task.execute((Void)null);
		}
		else if (mFragType == FRAG_TYPE.FRAG_TYPE_EVENTS)
		{
			FillEventsTask task = new FillEventsTask();
			task.execute((Void)null);
		}
		else if (mFragType == FRAG_TYPE.FRAG_TYPE_ARTISTS)
		{
			FillArtistsTask task = new FillArtistsTask();
			task.execute((Void)null);
		}
		else if (mFragType == FRAG_TYPE.FRAG_TYPE_VENUES)
		{
			FillVenuesTask task = new FillVenuesTask();
			task.execute((Void)null);
		}
	}
	
	private class FillAlbumsTask extends AsyncTask<Void, Void, Album[]>
	{

		@Override
		protected Album[] doInBackground(Void... params) {
			ArrayList<Album> albums = FindieDbHelper.getInstance().getAlbumHelper().findAlbums(mQuery);
			
			AlbumListHelper.getInstance().sortAlbumsByReleaseDate(albums);
			
			Album[] albumArray = new Album[albums.size()];
			albums.toArray(albumArray);
			
			return albumArray;
		}
		
		@Override
		public void onPostExecute(Album[] albumArray)
		{
			mProgress.setVisibility(View.GONE);
			if (albumArray.length > 0)
			{
				mList.setVisibility(View.VISIBLE);
				mList.setAdapter(AlbumListHelper.getInstance().getArrayListAdapter(getActivity(), albumArray));
			} else {
				mNoInfo.setVisibility(View.VISIBLE);
				mList.setVisibility(View.GONE);
			}
		}
	};
	
	private class FillEventsTask extends AsyncTask<Void, Void, Event[]>
	{

		@Override
		protected Event[] doInBackground(Void... params) {
			ArrayList<Event> events = FindieDbHelper.getInstance().getEventsHelper().findEvents(mQuery);
			
			EventListHelper.getInstance().sortEventsByDate(events);
			
			Event[] eventArray = new Event[events.size()];
			events.toArray(eventArray);
			
			return eventArray;
		}
		
		@Override
		public void onPostExecute(Event[] eventArray)
		{
			mProgress.setVisibility(View.GONE);
			if (eventArray.length > 0)
			{
				mList.setVisibility(View.VISIBLE);
				mList.setAdapter(EventListHelper.getInstance().getArrayListAdapter(getActivity(), eventArray, false, true));
			} else {
				mNoInfo.setVisibility(View.VISIBLE);
				mList.setVisibility(View.GONE);
			}
		}
		
	};
	
	private class FillArtistsTask extends AsyncTask<Void, Void, Artist[]>
	{

		@Override
		protected Artist[] doInBackground(Void... params) {
			ArrayList<Artist> artists = FindieDbHelper.getInstance().getArtistHelper().findArtists(mQuery);
			
			ArtistListHelper.getInstance().sortArtistsByName(artists);
			
			Artist[] artistArray = new Artist[artists.size()];
			artists.toArray(artistArray);
			
			return artistArray;
		}
		
		@Override
		public void onPostExecute(Artist[] artistArray)
		{
			mProgress.setVisibility(View.GONE);
			if (artistArray.length > 0)
			{
				mList.setVisibility(View.VISIBLE);
				mList.setAdapter(ArtistListHelper.getInstance().getArrayListAdapter(getActivity(), artistArray));
			} else {
				mNoInfo.setVisibility(View.VISIBLE);
				mList.setVisibility(View.GONE);
			}
		}
		
	}

	private class FillVenuesTask extends AsyncTask<Void, Void, Venue[]>
	{

		@Override
		protected Venue[] doInBackground(Void... params) {
			ArrayList<Venue> venues = FindieDbHelper.getInstance().getVenueHelper().findVenues(mQuery);
			
			VenueListHelper.getInstance().sortVenuesByName(venues);
			
			Venue[] venueArray = new Venue[venues.size()];
			venues.toArray(venueArray);
			
			return venueArray;
		}
		
		@Override
		public void onPostExecute(Venue[] venueArray)
		{
			mProgress.setVisibility(View.GONE);
			if (venueArray.length > 0)
			{
				mList.setVisibility(View.VISIBLE);
				mList.setAdapter(VenueListHelper.getInstance().getArrayListAdapter(getActivity(), venueArray));
			} else {
				mNoInfo.setVisibility(View.VISIBLE);
				mList.setVisibility(View.GONE);
			}
		}
	}
}	
