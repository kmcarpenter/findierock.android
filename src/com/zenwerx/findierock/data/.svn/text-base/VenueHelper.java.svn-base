package com.zenwerx.findierock.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zenwerx.findierock.model.Event;
import com.zenwerx.findierock.model.Venue;

public class VenueHelper {
	private static final String TAG = "findierock.VenueHelper";
	
	public final String COL_VENUEID = "_id";
	public final String COL_NAME = "name";
	public final String COL_ADDRESS = "address";
	public final String COL_ADDRESS2 = "address2";
	public final String COL_CITY = "city";
	public final String COL_PROVINCE = "province";
	public final String COL_COUNTRY = "country";
	public final String COL_WEBSITE = "website";
	public final String COL_LATITUDE = "latitude";
	public final String COL_LONGITUDE = "longitude";
	public final String COL_SMALLIMAGE = "smallimage";
	public final String COL_LARGEIMAGE = "largeimage";
			
	public final String DATABASE_TABLE_VENUES = "venues";
		    	
	private final SQLiteDatabase mDb;
	
	public VenueHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private final ContentValues getContentValues(Venue v)
	{
		final ContentValues cv = new ContentValues();
		
		cv.put(COL_VENUEID, v.getVenueId());
		cv.put(COL_NAME, v.getName());
		cv.put(COL_ADDRESS, v.getAddress());
		cv.put(COL_ADDRESS2, v.getAddress2());
		cv.put(COL_CITY, v.getCity());
		cv.put(COL_PROVINCE, v.getProvince());
		cv.put(COL_COUNTRY, v.getCountry());
		cv.put(COL_WEBSITE, v.getWebsite());
		cv.put(COL_LATITUDE, v.getLatitude());
		cv.put(COL_LONGITUDE, v.getLongitude());
		cv.put(COL_SMALLIMAGE, v.getSmallImage());
		cv.put(COL_LARGEIMAGE, v.getLargeImage());
		
		return cv;
	}
	
	private Venue readVenue(Cursor c)
	{
		final int iId = c.getColumnIndex(COL_VENUEID);
		final int iName = c.getColumnIndex(COL_NAME);
		final int iAddr = c.getColumnIndex(COL_ADDRESS);
		final int iAddr2 = c.getColumnIndex(COL_ADDRESS2);
		final int iCity = c.getColumnIndex(COL_CITY);
		final int iProv = c.getColumnIndex(COL_PROVINCE);
		final int iCountry = c.getColumnIndex(COL_COUNTRY);
		final int iWeb = c.getColumnIndex(COL_WEBSITE);
		final int iLat = c.getColumnIndex(COL_LATITUDE);
		final int iLong = c.getColumnIndex(COL_LONGITUDE);
		final int iSmImg = c.getColumnIndex(COL_SMALLIMAGE);
		final int iLgImg = c.getColumnIndex(COL_LARGEIMAGE);
		
		Venue v = new Venue();
		v.setVenueId(c.getInt(iId));
		v.setName(c.getString(iName));
		v.setAddress(c.getString(iAddr));
		v.setAddress2(c.getString(iAddr2));
		v.setCity(c.getString(iCity));
		v.setProvince(c.getString(iProv));
		v.setCountry(c.getString(iCountry));
		v.setWebsite(c.getString(iWeb));
		v.setLatitude(c.getDouble(iLat));
		v.setLongitude(c.getLong(iLong));
		v.setLargeImage(c.getString(iLgImg));
		v.setSmallImage(c.getString(iSmImg));
		
		ArrayList<Event> events = FindieDbHelper.getInstance().getEventsHelper().getEventsForTodayOrLaterAtVenue(v);
		if (events != null)
		{
			Event[] aa = new Event[events.size()];
			v.setEvents(events.toArray(aa));
		}
		
		return v;
	}
	
	public Venue getVenue(int venueId)
	{
		
		Venue v = null;
	
		Cursor c = mDb.query(DATABASE_TABLE_VENUES,
							null,
							"_id=?", new String[] { new Integer(venueId).toString() }, 
							null, null, null);
	
		if (c != null && c.moveToFirst())
		{
			v = readVenue(c);
		}
		
		if (c != null)
		{
			c.close();
		}
	
		return v;
	}

	public Venue getVenueForEvent(Event e)
	{
		return getVenue(e.getVenueId());
	}
	
	public void saveVenues(Venue[] venues)
	{
		saveVenues(venues, false);
	}
	
	public void saveVenues(Venue[] venues, boolean saveEvents)
	{
		if (venues != null)
		{
			try
			{
    			mDb.beginTransaction();
	    		for (Venue v : venues)
	    		{
    				final ContentValues cv = getContentValues(v);
    				try
    				{
	    				long id = mDb.insertOrThrow(DATABASE_TABLE_VENUES, null, cv);
	    				Log.d(TAG, "Inserted id " + Long.toString(id));
    				} catch (SQLiteConstraintException ex)
    				{
    					if (!(mDb.update(DATABASE_TABLE_VENUES, cv, "_id=?", new String[] { Integer.toString(v.getVenueId()) }) == 1))
    						throw ex;
    				}
    				
    				if (saveEvents)
    				{
    					FindieDbHelper.getInstance().getEventsHelper().saveEvents(v.getEvents(), false);
    				}
	    		}
	    		mDb.setTransactionSuccessful();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
				Log.e(TAG, "Can't insert: " + ex.toString());
			}
			finally
			{
				mDb.endTransaction();
			}	    		
		}
	}

	public ArrayList<Venue> findVenues(String search) {
		ArrayList<Venue> venues = new ArrayList<Venue>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_VENUES, null, "name LIKE ?", new String[] { "%" + search + "%" }, null, null, null, null);
		
		if (c != null && c.moveToFirst()) {
			while (!c.isAfterLast()) {
				Venue v = readVenue(c);
    			
				venues.add(v);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null) {
			c.close();
		}
		
		return venues;
	}
}
