package com.zenwerx.findierock.activity;

import java.util.ArrayList;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.fragments.SearchFragment;
import com.zenwerx.findierock.activity.fragments.SearchFragment.FRAG_TYPE;
import com.zenwerx.findierock.activity.helper.AlbumListHelper;
import com.zenwerx.findierock.activity.helper.ArtistListHelper;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.helper.AlbumListener;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.ArtistListener;
import com.zenwerx.findierock.helper.EventListener;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.helper.VenueListener;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SearchActivity extends FragmentActivity implements HeaderListener, EventListener, ArtistListener, VenueListener, AlbumListener {
	
	private SearchPagerAdapter mAdapter = null;
	private ViewPager mPager = null;
	private String mQuery = "";
	
	private static final String TAG = "findierock.SearchActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.search);
        
        TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_search);
		
		mAdapter = new SearchPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.search_pager);
		mPager.setAdapter(mAdapter);
		mPager.setCurrentItem(1);
		
        // Get the intent, verify the action and get the query
		Intent intent = getIntent();
		if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
		  mQuery = intent.getStringExtra(SearchManager.QUERY);
		  
		  fillData();
		}
        
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
    
    private void fillData()
    {
		for( SearchFragment frag : mFrags )
		{
			frag.refreshData();
		}
    }
        
    @Override
    public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}
    
	@Override
    public void onRefresh(View v)
    {
		fillData();
		return;
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

	@Override
	public void onEventAddCalendar(View v) {
		EventListHelper.getInstance().onEventAddCalendar(this, v);		
	}

	@Override
	public void onEventDetailRowClick(View v) {
		EventListHelper.getInstance().onDetailRowClick(this, v);		
	}

	@Override
	public void onEventShare(View v) {
		EventListHelper.getInstance().onEventShare(this, v);
	}

	@Override
	public void onShowEventDetail(View v) {
		EventListHelper.getInstance().onShowDetail(this, v);
	}

	@Override
	public void onShowEventVenue(View v) {
		EventListHelper.getInstance().onShowVenue(this, v);
	}

	@Override
	public void onArtistDetailRowClick(View v) {
		ArtistListHelper.getInstance().onDetailRowClick(this, v);
	}

	@Override
	public void onArtistShare(View v) {
		ArtistListHelper.getInstance().onShare(this, v);
	}

	@Override
	public void onMarkFav(View v) {
		ArtistListHelper.getInstance().onMarkFav(this, v);
	}

	@Override
	public void onShowArtistDetail(View v) {
		ArtistListHelper.getInstance().onShowDetail(this, v);
	}

	@Override
	public void onShareVenue(View v) {
		//VenueListHelper.getInstance()
	}

	@Override
	public void onShowVenue(View v) {
	}

	@Override
	public void onVenueDetailRowClick(View v) {
	}
	
	@Override
	public void onAlbumDetailRowClick(View v) {
		AlbumListHelper.getInstance().onDetailRowClick(this, v);
	}

	@Override
	public void onShowAlbumDetail(View v) {
		AlbumListHelper.getInstance().onShowDetail(this, v);
	}

	@Override
	public void onAlbumFavourite(View v) {
		AlbumListHelper.getInstance().onFavourite(this, v);
	}

	@Override
	public void onAlbumShare(View v) {
		AlbumListHelper.getInstance().onShare(this, v);
	}

	private final static int NUM_VIEWS = 4;
	private ArrayList<SearchFragment> mFrags = new ArrayList<SearchFragment>();
	
	private class SearchPagerAdapter extends FragmentPagerAdapter
	{
		public SearchPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return NUM_VIEWS;
		}

		public Fragment getItem(int position)
		{
			SearchFragment frag = null;
			try
			{
				frag = mFrags.get(position);
			}
			catch(IndexOutOfBoundsException e)
			{
				frag = new SearchFragment();
				Bundle b = new Bundle();
				
				String next = "";
				String cur = "";
				String prev = "";
				FRAG_TYPE fragType  = FRAG_TYPE.FRAG_TYPE_ALBUMS;
				
				switch (position)
				{
				default:
				case 0:
					prev = "";
					cur = "Venues";
					next = "Events";
					fragType = FRAG_TYPE.FRAG_TYPE_VENUES;
					break;
				case 1:
					prev = "Venues";
					cur = "Events";
					next = "Artists";
					fragType = FRAG_TYPE.FRAG_TYPE_EVENTS;
					break;
				case 2:
					prev = "Events";
					cur = "Artists";
					next = "Albums";
					fragType = FRAG_TYPE.FRAG_TYPE_ARTISTS;
					break;
				case 3:
					prev = "Artists";
					cur = "Albums";
					next = "";
					fragType = FRAG_TYPE.FRAG_TYPE_ALBUMS;
					break;
				}
				
				b.putString(SearchFragment.KEY_NEXT, next);
				b.putString(SearchFragment.KEY_PREV, prev);
				b.putString(SearchFragment.KEY_CUR, cur);
				b.putInt(SearchFragment.KEY_TYPE, fragType.ordinal());
				b.putString(SearchFragment.KEY_QUERY, mQuery);
				
				frag.setArguments(b);
				
				mFrags.add( frag );	
			}
			
			return frag;
		}
	}
}
