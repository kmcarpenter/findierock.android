package com.zenwerx.findierock.activity;

import java.util.Calendar;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.AlbumListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.ImageDownloader;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.Artist;

public class AlbumActivity extends Activity implements HeaderListener {

	private int mAlbumId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.albuminfo);

		TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_album_info);
		
		mAlbumId = getIntent().getExtras().getInt("albumid");
        
		FillDataTask task = new FillDataTask();
		task.execute((Void)null);
	}
	
	private class FillDataTask extends AsyncTask<Void, Void, Album>
	{
		public void onPostExecute(Album album)
		{		    	
			TextView name 		= (TextView)findViewById(R.id.albuminfo_name);
			TextView band 		= (TextView)findViewById(R.id.albuminfo_band);
			TextView trackCount	= (TextView)findViewById(R.id.albuminfo_track_count);
			TextView year 		= (TextView)findViewById(R.id.albuminfo_year);
						
			Artist a = FindieDbHelper.getInstance().getArtistHelper().getArtist(album.getArtistId());
			band.setText(a.getName());
			if (album.getReleaseDate() != null)
			{
				Calendar c = Calendar.getInstance();
				c.setTime(album.getReleaseDate());
				year.setText(Integer.toString(c.get(Calendar.YEAR)));
			}
			
			ImageView photo 	= (ImageView)findViewById(R.id.album_photo);
			if (album.getLargeImage() != null && !album.getLargeImage().equals(""))
			{
				ImageDownloader.getInstance().download(SettingsHelper.UrlBase() + album.getLargeImage(), photo);
			}
			
			name.setText(album.getName());
			trackCount.setText((new StringBuilder()).append(album.getTracks().length).append(" tracks").toString());
			
			
			ListView tracks = (ListView)findViewById(R.id.albuminfo_tracks);
	        
			tracks.setAdapter(AlbumListHelper.getInstance().getArrayListAdapter(AlbumActivity.this, album.getTracks()));
	    }

		@Override
		protected Album doInBackground(Void... params) {
			return FindieDbHelper.getInstance().getAlbumHelper().getAlbum(mAlbumId);
		}
	};

	@Override
	public void onRefresh(View v) {
		// Do Nothing		
	}

	@Override
	public void onSearch(View v) {
		HeaderHelper.getInstance().onSearch(this, v);
	}
	
	@Override
	public void onEventList(View v) {
		HeaderHelper.getInstance().onEventList(this, v);
	}
	
	@Override
	public void onMaps(View v) {
		HeaderHelper.getInstance().onMaps(this, v);
	}

	@Override
	public void onFavourite(View v) {
		HeaderHelper.getInstance().onFavourite(this, v);
	}

}
