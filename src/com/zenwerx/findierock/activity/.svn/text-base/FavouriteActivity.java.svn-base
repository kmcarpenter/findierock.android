package com.zenwerx.findierock.activity;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.helper.ArtistListHelper;
import com.zenwerx.findierock.activity.helper.HeaderHelper;
import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.AnalyticsHelper;
import com.zenwerx.findierock.helper.ArtistListener;
import com.zenwerx.findierock.helper.HeaderListener;
import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.model.Artist;

public class FavouriteActivity extends ListActivity implements HeaderListener, ArtistListener {
	
	private static final String TAG = "findierock.FavouriteActivity";
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourites);
     
        TextView title = (TextView) findViewById(R.id.viewTitle);
		title.setText(R.string.title_favourites);
		
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

    @Override
    public void onRefresh(View v)
    {
    	refreshEvents();
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
	
	public void onFavourite(View v) {
		// this view
		return;
	}
	
	private void refreshEvents()
	{
		FillDataTask task = new FillDataTask();
		task.execute((Void)null);
	}
	
	private class FillDataTask extends AsyncTask<Void, Void, ArrayList<Artist>>
	{
		
		@Override
		protected ArrayList<Artist> doInBackground(Void... params) {
			return FindieDbHelper.getInstance().getArtistHelper().getFavourites();
		}
		
		@Override
		public void onPostExecute(ArrayList<Artist> favourites) {
						
			Artist[] favouritesArray = new Artist[favourites.size()];
			favourites.toArray(favouritesArray);		
	        
	        setListAdapter(ArtistListHelper.getInstance().getArrayListAdapter(FavouriteActivity.this, favouritesArray));
		}
	};
	
	public void onArtistDetailRowClick(View v)
    {
    	ArtistListHelper.getInstance().onDetailRowClick(this, v);
    }
    
    public void onShowArtistDetail(View v)
    {
    	ArtistListHelper.getInstance().onShowDetail(this, v);
    }
    
    public void onMarkFav(View v)
    {
    	final View vv = v;
    	
    	new AlertDialog.Builder(this)
    		.setPositiveButton(getResources().getString(R.string.ok), new OnClickListener() {    			
				@Override
				public void onClick(DialogInterface dialog, int which) {

					ArtistListHelper.getInstance().onMarkFav(FavouriteActivity.this, vv);
					
					FillDataTask task = new FillDataTask();
					task.execute((Void)null);
				}
    			
    		})
    		.setNegativeButton(getResources().getString(R.string.cancel), null)
    		.setMessage(getResources().getString(R.string.favourite_delete))
    		.show();
    }
    
    public void onArtistShare(View v)
    {
    	ArtistListHelper.getInstance().onShare(this, v);
    }
}