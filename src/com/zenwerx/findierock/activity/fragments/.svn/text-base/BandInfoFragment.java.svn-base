package com.zenwerx.findierock.activity.fragments;

import java.util.ArrayList;
import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.adapter.AdvertisingAdapter;
import com.zenwerx.findierock.activity.helper.AlbumListHelper;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.Artist;
import com.zenwerx.findierock.model.Event;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class BandInfoFragment extends Fragment {

		public static final String KEY_PREV = "prevText";
		public static final String KEY_CUR = "curText";
		public static final String KEY_NEXT = "nextText";
		public static final String KEY_TYPE = "fragType";
		public static final String KEY_ARTIST = "artistId";
		
		public static enum FRAG_TYPE 
		{
			FRAG_TYPE_EVENTS,
			FRAG_TYPE_ALBUMS
		}
		
		@SuppressWarnings("unused")
		private static final String TAG = "findierock.BandInfoFragment";
		
		private FRAG_TYPE mFragType = null;	
		private Artist mArtist = null;

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
			View v = inflater.inflate(R.layout.bandinfo_fragment, container, false);
			
			Bundle args = getArguments();
						
			mArtist = FindieDbHelper.getInstance().getArtistHelper().getArtist(args.getInt(KEY_ARTIST));
			
			int fragType = args.getInt(KEY_TYPE);
			for(FRAG_TYPE frag : FRAG_TYPE.values())
			{
				if (frag.ordinal() == fragType)
				{
					mFragType = frag;
					break;
				}
			}
			
			String nextText = args.getString(KEY_NEXT);
			String curText = args.getString(KEY_CUR);
			String prevText = args.getString(KEY_PREV);
			
			if (!prevText.equals(""))
				prevText = "< " + prevText;
			if (!nextText.equals(""))
				nextText += " >";
			
			mList = (ListView)v.findViewById(R.id.bandinfo_list);
			
			mProgress = (LinearLayout)v.findViewById(R.id.bandinfo_progress);
			mNoInfo = (TextView)v.findViewById(R.id.bandinfo_noinfo);
			
			mPrev = (TextView)v.findViewById(R.id.prevView);
			mPrev.setText(prevText);
			mCur = (TextView)v.findViewById(R.id.thisView);
			mCur.setText(curText);
			mNext = (TextView)v.findViewById(R.id.nextView);
			mNext.setText(nextText);
						
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
			if (mFragType == FRAG_TYPE.FRAG_TYPE_ALBUMS)
			{
				_refreshAlbums();
			}
			else
			{
				_refreshEvents();
			}
		}
		
		private class FillAlbumsTask extends AsyncTask<Void, Void, Album[]>
		{

			@Override
			protected Album[] doInBackground(Void... params) {
				ArrayList<Album> albums = FindieDbHelper.getInstance().getAlbumHelper().getAlbumsForArtist(mArtist);
				
				AlbumListHelper.getInstance().sortAlbumsByReleaseDate(albums);
				
				Album[] albumArray = new Album[albums.size()];
				albums.toArray(albumArray);
				
				return albumArray;
			}
			
			@Override
			public void onPostExecute(Album[] albums)
			{
				mProgress.setVisibility(View.GONE);
				if (albums.length > 0)
				{
					mNoInfo.setVisibility(View.GONE);
					mList.setVisibility(View.VISIBLE);
					AdvertisingAdapter<Album> adapter = AlbumListHelper.getInstance().getArrayListAdapter(getActivity(), albums);
					mList.setAdapter(adapter);
				} else {
					mNoInfo.setVisibility(View.VISIBLE);
					mList.setVisibility(View.GONE);
				}
			}
		}
		
		private void _refreshAlbums()
		{
			mProgress.setVisibility(View.VISIBLE);
			mNoInfo.setVisibility(View.GONE);
			mList.setVisibility(View.GONE);
			FillAlbumsTask task = new FillAlbumsTask();
			task.execute((Void)null);
		}
		
		private class FillEventsTask extends AsyncTask<Void, Void, Event[]>
		{

			@Override
			protected Event[] doInBackground(Void... params) {
				ArrayList<Event> events = FindieDbHelper.getInstance().getEventsHelper().getEventsForTodayOrLaterWithArtist(mArtist);
				
				EventListHelper.getInstance().sortEventsByDate(events);
				
				Event[] eventArray = new Event[events.size()];
				events.toArray(eventArray);
				
				return eventArray;
			}
			
			@Override
			public void onPostExecute(Event[] events)
			{
				mProgress.setVisibility(View.GONE);
				if (events.length > 0)
				{
					mNoInfo.setVisibility(View.GONE);
					mList.setVisibility(View.VISIBLE);
					ArrayAdapter<Event> adapter = EventListHelper.getInstance().getArrayListAdapter(getActivity(), events);
					mList.setAdapter(adapter);
				} else {
					mNoInfo.setVisibility(View.VISIBLE);
					mList.setVisibility(View.GONE);
				}
			}
			
		}
		
		private void _refreshEvents()
		{
			mProgress.setVisibility(View.VISIBLE);
			mNoInfo.setVisibility(View.GONE);
			mList.setVisibility(View.GONE);
			FillEventsTask task = new FillEventsTask();
			task.execute((Void)null);
		}
}
