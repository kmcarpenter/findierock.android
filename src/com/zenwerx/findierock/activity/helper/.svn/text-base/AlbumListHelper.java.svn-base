package com.zenwerx.findierock.activity.helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.AlbumActivity;
import com.zenwerx.findierock.activity.adapter.AdvertisingAdapter;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.ImageDownloader;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.AlbumTrack;
import com.zenwerx.findierock.model.Artist;

public class AlbumListHelper {
	
	protected static final String TAG = "findierock.AlbumListHelper";

	private AlbumListHelper() {}
	
    private static Object mLock = new Object();
    private static AlbumListHelper mInstance = null;
    
    public static AlbumListHelper getInstance()
    {
    	synchronized(mLock)
    	{
    		if (mInstance == null)
    		{
    			mInstance = new AlbumListHelper();
    		}
    		return mInstance;
    	}
    }
    
	public AdvertisingAdapter<Album> getArrayListAdapter(final Activity act, final Album[] albums)
    {
    	ArrayAdapter<Album> adapter =  new ArrayAdapter<Album>(act, R.id.eventinfo_artists, albums)
        {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		Album v = getItem(position);
        		
        		return createAlbumListItem(act, v);
        	}
        };
        
        return new AdvertisingAdapter<Album>(act, adapter);
    }
    
    public View createAlbumListItem(final Activity act, final Album a)
    {
    	View v = act.getLayoutInflater().inflate(R.layout.bandinfo_albumrow, null);
		
		if (a != null && v != null)
		{
			TextView name = (TextView)v.findViewById(R.id.bandinfo_albumname);
			name.setTag(a.getAlbumId());
			name.setText(a.getName());		// 
			
			TextView release = (TextView)v.findViewById(R.id.bandinfo_albumreleased);
			if (a.getReleaseDate() != null)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(a.getReleaseDate());
				release.setText(Integer.toString(c.get(Calendar.YEAR)));
			}
						
			if (a.getLargeImage() != null && !a.getLargeImage().equals(""))
			{
				ImageView pic = (ImageView) v.findViewById(R.id.albumlist_pic);
				ImageDownloader.getInstance().download(SettingsHelper.UrlBase() + a.getLargeImage(), pic);
			}
			
			TextView art = (TextView)v.findViewById(R.id.albuminfo_btn_album_detail);
			art.setTag(a.getAlbumId());
			
			TextView fav = (TextView)v.findViewById(R.id.albuminfo_btn_album_fav);
			fav.setTag(a.getAlbumId());
			int top = a.isFavourite() ? R.drawable.btn_star_big_on : R.drawable.btn_star_big_off;
			fav.setCompoundDrawablesWithIntrinsicBounds(0, top, 0, 0);
			
			TextView share = (TextView)v.findViewById(R.id.albuminfo_btn_album_share);
			share.setTag(a.getAlbumId());
		}

		return v;	
    }

	public void sortAlbumsByReleaseDate(ArrayList<Album> albums) {

		Comparator<Album> comp = new Comparator<Album>() {

			@Override
			public int compare(Album a1, Album a2) {
				if (a1.getReleaseDate() != null && a2.getReleaseDate() == null)
				{
					return 1;
				}
				else if (a2.getReleaseDate() != null && a1.getReleaseDate() == null)
				{
					return -1;
				}
				else if (a1.getReleaseDate().equals(a2.getReleaseDate()))
				{
					return 0;
				}
				else
				{
					if (a1.getReleaseDate().after(a2.getReleaseDate()))
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
		
		Collections.sort(albums, comp);
	}
	
	public void onDetailRowClick(Context ctx, View v )
	{
		RelativeLayout parent = (RelativeLayout)v.getParent();
		View op = parent.findViewById(R.id.albumlist_album_operation_row);
		boolean expanded = op.getVisibility() != View.GONE;
		
		op.setVisibility(!expanded ? View.VISIBLE : View.GONE );
		
		ImageView iv = (ImageView)v.findViewById(R.id.img_expandable);
		if (iv != null)
		{
			iv.setImageResource(!expanded ? R.drawable.arrow_up_float : R.drawable.arrow_down_float);
		}
	}
	
	public void onShowDetail(Context ctx, View v)
	{
		Intent intent = new Intent(ctx, AlbumActivity.class);
		Integer albumId = (Integer) v.getTag();
		intent.putExtra("albumid", albumId.intValue());
		
		try
		{
			ctx.startActivity(intent);
		}
		catch (Exception e)
		{
			Log.d("findierock", e.toString());
		}
	}
	
	public void onFavourite(Context c, View v)
	{
		TextView fav = (TextView)v;
    	Integer albumId = (Integer)v.getTag();
    	Album album = FindieDbHelper.getInstance().getAlbumHelper().getAlbum(albumId);
    	album.setFavourite(!album.isFavourite());
    	
    	FindieDbHelper.getInstance().getAlbumHelper().saveAlbums(new Album[] { album });
    	
    	int top = (album.isFavourite() ? R.drawable.btn_star_big_on : R.drawable.btn_star_big_off);
    	fav.setCompoundDrawablesWithIntrinsicBounds(0, top, 0, 0);
	}
	
	public void onShare(final Activity act, View v)
	{
		Album a = FindieDbHelper.getInstance().getAlbumHelper().getAlbum((Integer)v.getTag());
		Artist art = FindieDbHelper.getInstance().getArtistHelper().getArtist(a.getArtistId());
    	
    	String detail = act.getResources().getString(R.string.album_share_detail);
    	detail = String.format(detail, a.getName(), art.getName());
    	
    	Intent intent = new Intent(android.content.Intent.ACTION_SEND);
    	intent.setType("text/plain");
    	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, a.getName());
    	intent.putExtra(android.content.Intent.EXTRA_TEXT, detail);
    	
    	act.startActivity(Intent.createChooser(intent, act.getResources().getString(R.string.album_share_title) 
    												+ a.getName() ) );
	}

	public AdvertisingAdapter<AlbumTrack> getArrayListAdapter(final Activity act, final AlbumTrack[] tracks)
    {
    	ArrayAdapter<AlbumTrack> adapter = new ArrayAdapter<AlbumTrack>(act, R.id.eventinfo_artists, tracks)
        {
        	@Override
        	public View getView(int position, View convertView, ViewGroup parent) {
        		AlbumTrack a = getItem(position);
        		
        		return createAlbumTrackListItem(act, a);
        	}
        };
        return new AdvertisingAdapter<AlbumTrack>(act, adapter);
    }
	
	public View createAlbumTrackListItem(final Activity act, final AlbumTrack a)
    {
    	View v = act.getLayoutInflater().inflate(R.layout.albuminfo_trackrow, null);
		
		if (a != null && v != null)
		{
			TextView name = (TextView)v.findViewById(R.id.trackrow_track_name);
			name.setText(a.getName());
			
			TextView length = (TextView)v.findViewById(R.id.trackrow_track_length);			
			length.setText(String.format("%1$d:%2$02d", a.getLength()/60,a.getLength()%60));			
		}

		return v;	
    }
}
