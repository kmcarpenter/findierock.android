package com.zenwerx.findierock.activity.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;
 
/**
 * List adapter decorator that inserts adverts into the list.
 * @author Daniel Dyer
 * @param <T>
 */
public class AdvertisingAdapter<T> extends ArrayAdapter<T>
{
    private static final String ADMOB_PUBLISHER_ID = "a14f484d8466452";
    private static final String MC_SAMSUNG_GALAXYS = "7C253639289152F6D4AAF76E6A075FCB";
 
    private final Activity activity;
    private final ArrayAdapter<T> delegate;
 
    public AdvertisingAdapter(Activity activity, ArrayAdapter<T> delegate)
    {
    	super(activity, 0);
    	    	
        this.activity = activity;
        this.delegate = delegate;
        delegate.registerDataSetObserver(new DataSetObserver()
        {
            @Override
            public void onChanged()
            {
                notifyDataSetChanged();
            }
 
            @Override
            public void onInvalidated()
            {
                notifyDataSetInvalidated();
            }
        });
    }
 
    public int getCount()
    {
        return delegate.getCount() + 1;
    }
 
    public T getItem(int i)
    {
        return delegate.getItem(i - 1);
    }
 
    public long getItemId(int i)
    {
        return delegate.getItemId(i - 1);
    }
 
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (position == 0)
        {
            if (convertView instanceof AdView)
            {
                return convertView;
            }
            else
            {
                AdView adView = new AdView(activity, AdSize.BANNER, ADMOB_PUBLISHER_ID);
                
                // Disable focus for sub-views of the AdView to avoid problems with
                // trackpad navigation of the list.
                for (int i = 0; i < adView.getChildCount(); i++)
                {
                    adView.getChildAt(i).setFocusable(false);
                }
                adView.setFocusable(false);
                // Default layout params have to be converted to ListView compatible
                // params otherwise there will be a ClassCastException.
                float density = activity.getResources().getDisplayMetrics().density;
                int height = Math.round(AdSize.BANNER.getHeight() * density);
                AbsListView.LayoutParams params
                    = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
                                                   height);
                adView.setLayoutParams(params);
                
                AdRequest request = new AdRequest();
            	request.addTestDevice(AdRequest.TEST_EMULATOR);
            	request.addTestDevice(AdvertisingAdapter.MC_SAMSUNG_GALAXYS);
            	adView.loadAd(request);
                return adView;
            }
        }
        else
        {
            return delegate.getView(position - 1, convertView, parent);
        }
    }
 
    @Override
    public int getViewTypeCount()
    {
        return delegate.getViewTypeCount() + 1;
    }
 
    @Override
    public int getItemViewType(int position)
    {
        return position == 0 ? delegate.getViewTypeCount()
                             : delegate.getItemViewType(position - 1);
    }
 
    @Override
    public boolean areAllItemsEnabled()
    {
        return false;
    }
 
    @Override
    public boolean isEnabled(int position)
    {
        return position != 0 && delegate.isEnabled(position - 1);
    }
}