package com.zenwerx.findierock.activity.helper;

import com.zenwerx.findierock.R;
import com.zenwerx.findierock.activity.FavouriteActivity;
import com.zenwerx.findierock.activity.FindiePreferencesActivity;
import com.zenwerx.findierock.activity.ListingActivity;
import com.zenwerx.findierock.activity.EventMapActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public final class HeaderHelper
{
    private HeaderHelper() {}
	
    private static Object mLock = new Object();
    private static HeaderHelper mInstance = null;
    
    public static HeaderHelper getInstance()
    {
    	synchronized(mLock)
    	{
    		if (mInstance == null)
    		{
    			mInstance = new HeaderHelper();
    		}
    		return mInstance;
    	}
    }
    
    public void onSearch(final Activity act, final View v)
    {
    	 act.onSearchRequested();
    }
    
    public void onMaps(final Activity act, final View v)
    {
    	Intent i = new Intent(act, EventMapActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	act.startActivity(i);
    }
    
    public void onEventList(final Activity act, final View v)
    {
    	Intent i = new Intent(act, ListingActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	act.startActivity(i);
    }
    
    public void onFavourite(final Activity act, final View v)
    {
    	Intent i = new Intent(act, FavouriteActivity.class);
    	i.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    	act.startActivity(i);
    }
    
    public boolean onOptionsItemSelected(final Activity act, final MenuItem item)
    {
    	switch(item.getItemId())
		{
		case R.id.menu_settings:
			Intent i = new Intent(act, FindiePreferencesActivity.class);
			act.startActivity(i);
			return true;
		case R.id.menu_about:
			AboutDialog.create(act).show();
			return true;
		default:
			return true;
		}
    }
    
    private static final class AboutDialog {

		 public static AlertDialog create(final Context context) {
		  final TextView message = new TextView(context);
		  final Spanned s = Html.fromHtml(context.getResources().getString(R.string.about_text));

		  message.setText(s);
		  message.setMovementMethod(LinkMovementMethod.getInstance());

		  return new AlertDialog.Builder(context)
		   .setTitle(R.string.about)
		   .setCancelable(true)
		   .setIcon(android.R.drawable.ic_dialog_info)
		   .setPositiveButton(R.string.ok, null)
		   .setView(message)
		   .create();
		 }
		}
}
