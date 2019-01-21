package com.zenwerx.findierock.activity.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.zenwerx.findierock.activity.BandActivity;
import com.zenwerx.findierock.activity.adapter.AdvertisingAdapter;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.ImageDownloader;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Artist;

public class ArtistListHelper {
	private String TAG = "findierock.ArtistListHelper";
	
	private ArtistListHelper() {}
	
    private static Object mLock = new Object();
    private static ArtistListHelper mInstance = null;
    
    public static ArtistListHelper getInstance()
    {
    	synchronized(mLock)
    	{
    		if (mInstance == null)
    		{
    			mInstance = new ArtistListHelper();
    		}
    		return mInstance;
    	}
    }
    
    public void onDetailRowClick(final Context ctx, final View v)
    {
    	RelativeLayout parent = (RelativeLayout)v.getParent();
		View op = parent.findViewById(R.id.eventlist_artist_operation_row);
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
    	Intent intent = new Intent(ctx, BandActivity.class);
		Integer artistId = (Integer) v.getTag();
		intent.putExtra("artistid", artistId.intValue());
		
		try
		{
			ctx.startActivity(intent);
		}
		catch (Exception e)
		{
			Log.d("findierock", e.toString());
		}
    }
    
    public void onMarkFav(final Context ctx, final View v)
    {    	
    	TextView fav = (TextView)v;
    	Integer artistId = (Integer)v.getTag();
    	Artist artist = FindieDbHelper.getInstance().getArtistHelper().getArtist(artistId);
    	artist.setFavourite(!artist.isFavourite());
    	
    	FindieDbHelper.getInstance().getArtistHelper().saveArtists(new Artist[] { artist });
    	
		int top = (artist.isFavourite() ? R.drawable.btn_star_big_on : R.drawable.btn_star_big_off);
		fav.setCompoundDrawablesWithIntrinsicBounds(0, top, 0, 0);
    	
    }
    
    public void onShare(final Context ctx, final View v)
    {
    	Integer artistId = (Integer)v.getTag();
    	
    	Artist a = FindieDbHelper.getInstance().getArtistHelper().getArtist(artistId);
    	
    	String detail = ctx.getResources().getString(R.string.artist_share_detail);
    	detail = String.format(detail, a.getName());
    	
    	Intent i = new Intent(android.content.Intent.ACTION_SEND);
    	i.setType("text/plain");
    	i.putExtra(android.content.Intent.EXTRA_SUBJECT, a.getName());
    	i.putExtra(android.content.Intent.EXTRA_TEXT, detail);
    	
    	ctx.startActivity(Intent.createChooser(i, ctx.getResources().getString(R.string.artist_share_title) + a.getName() ) );
    }
    
    public AdvertisingAdapter<Artist> getArrayListAdapter(final Activity act, final Artist[] artists)
    {
    	ArrayAdapter<Artist> adapter = new ArrayAdapter<Artist>(act, R.id.eventinfo_artists, artists)
        {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		Artist a = getItem(position);
        		
        		return createArtistListItem(act, a);
        	}
        };
        return new AdvertisingAdapter<Artist>(act, adapter);
    }
    
    public View createArtistListItem(final Activity act, final Artist a)
    {
    	View v = act.getLayoutInflater().inflate(R.layout.eventinfo_artistrow, null);
		
		if (a != null && v != null)
		{
			TextView name = (TextView)v.findViewById(R.id.eventlist_artistname);
			name.setTag(a.getArtistId());
			name.setText(a.getName());
			
			TextView art = (TextView)v.findViewById(R.id.eventinfo_btn_artist_detail);
			art.setTag(a.getArtistId());
			
			TextView fav = (TextView)v.findViewById(R.id.eventinfo_btn_artist_fav);
			fav.setTag(a.getArtistId());
			int top = (a.isFavourite() ? R.drawable.btn_star_big_on : R.drawable.btn_star_big_off);
			fav.setCompoundDrawablesWithIntrinsicBounds(0, top, 0, 0);
			
			TextView share = (TextView)v.findViewById(R.id.eventinfo_btn_artist_share);
			share.setTag(a.getArtistId());
			
			if (a.getLargeImage() != null && !a.getLargeImage().equals(""))
			{
				ImageView pic = (ImageView) v.findViewById(R.id.eventlist_pic);
				ImageDownloader.getInstance().download(SettingsHelper.UrlBase() + a.getLargeImage(), pic);
			}
		}

		return v;	
    }
    
    public ResultReceiver getResultReceiver(final Handler callback)
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
						Artist artist = g.fromJson(resultData.getString("response"), Artist.class );
						artist.setFetchedAlbums(true);
												
						FindieDbHelper.getInstance().getArtistHelper().saveArtists(new Artist[] { artist }, true);
																		
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

	public void sortArtistsByName(ArrayList<Artist> artists) {
		Comparator<Artist> comp = new Comparator<Artist>()
		{
			@Override
			public int compare(Artist a1, Artist a2) {
				return a1.getName().compareTo(a2.getName());
			}
		};
		
		Collections.sort(artists, comp);		
	}
}
