package com.zenwerx.findierock.activity;

import java.util.ArrayList;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.fragments.BandInfoFragment;
import com.zenwerx.findierock.activity.fragments.BandInfoFragment.FRAG_TYPE;
import com.zenwerx.findierock.activity.helper.AlbumListHelper;
import com.zenwerx.findierock.activity.helper.ArtistListHelper;
import com.zenwerx.findierock.activity.helper.EventListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.AlbumListener;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.EventListener;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.ImageDownloader;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Artist;
import com.zenwerx.findierock.service.restful.RestfulServiceHelper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.text.Html;
import android.text.SpannableString;
import android.view.Menu;

public class BandActivity extends FragmentActivity implements HeaderListener, EventListener, AlbumListener{

	private static final String TAG = "findierock.BandActivity";
	
	private int mRequest = -1;
	private ProgressDialog mProgressDialog = null;
	private Artist mArtist = null;
	
	private BandPagerAdapter mAdapter = null;
	private ViewPager mPager = null;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bandinfo);

		TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_band_info);
		
		int artistId = getIntent().getExtras().getInt("artistid");
		mArtist = FindieDbHelper.getInstance().getArtistHelper().getArtist(artistId);
		
		mAdapter = new BandPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.bandinfo_pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int index) {
				if (index == 1)
					refreshAlbums(false);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) { /* yum */ }
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { /* yum */ }			
		});
		
		fillData(false);
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
		if (mProgressDialog != null)
		{
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
	
	private void fillData(boolean doAlbums)
	{
		if (doAlbums)
			refreshAlbums(false);
		
		TextView name = (TextView) findViewById(R.id.bandinfo_name);
		TextView bio = (TextView) findViewById(R.id.bandinfo_biomore);
		TextView website = (TextView) findViewById(R.id.bandinfo_website);

		if (mArtist.getLargeImage() != null && !mArtist.getLargeImage().equals(""))
		{
			ImageView pic = (ImageView) findViewById(R.id.band_photo);
			ImageDownloader.getInstance().download(SettingsHelper.UrlBase() + mArtist.getLargeImage(), pic);
		}
		
		name.setText(mArtist.getName());
		
		SpannableString content = new SpannableString(getResources().getString(R.string.band_bio));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		bio.setText(content);
		
		if (mArtist.getWebsite() == null || mArtist.getWebsite().trim().equals("")) {
			website.setVisibility(View.GONE);
		} else {
			website.setText(mArtist.getWebsite());
			Linkify.addLinks(website, Linkify.WEB_URLS);
		}
		
		findViewById(R.id.bandinfo_biomore).setTag(mArtist.getArtistId());
		
		for (BandInfoFragment frag : mFrags )
		{
			frag.refreshData();
		}
	}

	public void onBioMore(View v) {
		View bioView = LayoutInflater.from(this).inflate(R.layout.bandinfo_bio,
				null);
		TextView bio = (TextView) bioView.findViewById(R.id.bandinfo_bio_text);
		bio.setText(Html.fromHtml(mArtist.getBio().replace("\r\n", "<br /><br />")));
		Linkify.addLinks(bio, Linkify.WEB_URLS);

		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle(R.string.band_bio_detail);
		ad.setView(bioView);
		ad.setPositiveButton(R.string.ok, null);
		ad.show();
	}

	@Override
    public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}
    
	@Override
    public void onRefresh(View v)
    {
		refreshAlbums(true);
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
	};
	
	@Override
	public void onMaps(View v) {
		HeaderHelper.getInstance().onMaps(this, v);
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

	public void onAlbumMore(View v)
	{
		String url = (String) v.getTag();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(SettingsHelper.UrlBase() + url));
		startActivity(i);
	}
	
	private void showProgress()
	{
		mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading_artist), true);
	}
	
	private void refreshAlbums(boolean forced)
	{
		boolean update = forced;
		if (!update)
		{
			if (mArtist != null && !mArtist.didFetchAlbums())
				update = true;
		}
		
		if (update && mRequest <= 0)
		{
			showProgress();
	    	mRequest = RestfulServiceHelper.getInstance().getArtist(this, ArtistListHelper.getInstance().getResultReceiver(albumCallback), 
	    																	mArtist.getArtistId(), forced);
		}
	}
	
	private Handler albumCallback = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			if (mProgressDialog != null)
			{
				mProgressDialog.dismiss();
				mProgressDialog = null;
			}
			
			// reset
			if (mRequest == -1)
				return;
			
			mRequest = -1;
			
			// Get a new copy of the artist
			mArtist = FindieDbHelper.getInstance().getArtistHelper().getArtist(mArtist.getArtistId());

			// FIXME: this gets overwritten on a refresh of other data
			mArtist.setFetchedAlbums(true);
			FindieDbHelper.getInstance().getArtistHelper().saveArtists(new Artist[] { mArtist } );
			
			// load data
			fillData(true);
		}
	};
	
	private final static int NUM_VIEWS = 2;
	private ArrayList<BandInfoFragment> mFrags = new ArrayList<BandInfoFragment>();
	
	private class BandPagerAdapter extends FragmentPagerAdapter
	{
		public BandPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return NUM_VIEWS;
		}

		public Fragment getItem(int position)
		{
			BandInfoFragment frag = null;
			try
			{
				frag = mFrags.get(position);
			}
			catch(IndexOutOfBoundsException e)
			{
				frag = new BandInfoFragment();
				Bundle b = new Bundle();
				
				String next = position == 0 ? "Albums" : "";
				String cur = position == 0 ? "Events" : "Albums";
				String prev = position == 0 ? "" : "Events";
				FRAG_TYPE fragType  = position == 0 ? FRAG_TYPE.FRAG_TYPE_EVENTS : FRAG_TYPE.FRAG_TYPE_ALBUMS;
				
				b.putString(BandInfoFragment.KEY_NEXT, next);
				b.putString(BandInfoFragment.KEY_PREV, prev);
				b.putString(BandInfoFragment.KEY_CUR, cur);
				b.putInt(BandInfoFragment.KEY_TYPE, fragType.ordinal());
				b.putInt(BandInfoFragment.KEY_ARTIST, mArtist.getArtistId());
				
				frag.setArguments(b);
				
				mFrags.add( frag );	
			}
			
			return frag;
		}
	}
}
