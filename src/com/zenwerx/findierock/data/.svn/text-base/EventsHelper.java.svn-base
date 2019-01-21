package com.zenwerx.findierock.data;

import java.util.ArrayList;
import java.util.Date;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zenwerx.findierock.model.Artist;
import com.zenwerx.findierock.model.Event;
import com.zenwerx.findierock.model.Venue;

public class EventsHelper {
	private static final String TAG = "findierock.EventsHelper";
	
	public final String COL_EVENTID = "_id";
	public final String COL_NAME = "name";
	public final String COL_LATITUDE = "latitude";
	public final String COL_LONGITUDE = "longitude";
	public final String COL_DESCRIPTION = "description";
	public final String COL_STARTTIME = "startTime";
	public final String COL_AGEOFMAJORITY = "ageOfMajority";
	public final String COL_VENUEID = "venueId";
	
	public final String DATABASE_TABLE_EVENTS = "events";
	public final String DATABASE_TABLE_EVENTARTISTS = "eventartists";
	    	
	private final SQLiteDatabase mDb;
	
	public EventsHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private final ContentValues getContentValues(Event e)
	{
		final ContentValues cv = new ContentValues();
				
		cv.put(COL_EVENTID, e.getEventId());
		cv.put(COL_LATITUDE, e.getLatitude());
		cv.put(COL_LONGITUDE, e.getLongitude());
		cv.put(COL_NAME, e.getName());
		cv.put(COL_DESCRIPTION, e.getDescription());
		cv.put(COL_STARTTIME , (e.getStartTime().getTime()/1000));
		cv.put(COL_AGEOFMAJORITY, e.getAgeOfMajority());
		cv.put(COL_VENUEID, e.getVenueId());
		
		return cv;
	}
    	
	public ArrayList<Event> getEventsForToday()
	{
		ArrayList<Event> events = new ArrayList<Event>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_EVENTS, null, "date('now') = date(startTime, 'unixepoch', 'localtime')", null, null, null, null, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
    			Event e = readEvent(c);
    			
    			events.add(e);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return events;
	}
	
	public int countEventsForToday()
	{
		int result = 0;
		
		final Cursor c = mDb.rawQuery("SELECT COUNT(1) FROM " + DATABASE_TABLE_EVENTS + " WHERE date('now') = date(startTime, 'unixepoch', 'localtime')", null);
		
		if (c != null && c.moveToFirst())
		{
			result = c.getInt(0);
		}
		
		c.close();
		
		return result;
	}
	
	public ArrayList<Event> getEventsForTodayOrLaterAtVenue(Venue v)
	{
		ArrayList<Event> events = new ArrayList<Event>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_EVENTS, null, "date('now') <= date(startTime, 'unixepoch', 'localtime') and venueId=?", 
								new String[] { Integer.toString(v.getVenueId()) }, null, null, COL_STARTTIME, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
    			Event e = readEvent(c, false);
    			
    			events.add(e);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return events;
	}
	
	public ArrayList<Event> getEventsForTodayOrLaterWithArtist(Artist a)
	{
		ArrayList<Event> events = new ArrayList<Event>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_EVENTS + " e, " + DATABASE_TABLE_EVENTARTISTS + " ea",
				new String[] { "e." + COL_EVENTID, COL_AGEOFMAJORITY, COL_DESCRIPTION, COL_LATITUDE, COL_LONGITUDE, COL_NAME, COL_STARTTIME, COL_VENUEID },
				"ea.EventId=e._id AND ea.ArtistId=? AND date('now') <= date(startTime, 'unixepoch', 'localtime')", new String[] { Integer.toString(a.getArtistId())}, 
				null, null, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
    			Event e = readEvent(c, false);
    			
    			events.add(e);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return events;
	}
	
	public Event getEvent(int eventId)
	{
		final Cursor c = mDb.query(DATABASE_TABLE_EVENTS, null, "_id=?", new String[] { (new Integer(eventId)).toString() }, null, null, null, null);
		
		Event e = null;
		
		if (c != null && c.moveToFirst())
		{
			e = readEvent(c);
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return e;
	}
	
	private Event readEvent(final Cursor c)
	{
		return readEvent(c, true);
	}
	
	private Event readEvent(final Cursor c, final boolean getVenue)
	{
		final int iId = c.getColumnIndex(COL_EVENTID);
		final int iName = c.getColumnIndex(COL_NAME);
		final int iLat  = c.getColumnIndex(COL_LATITUDE);
		final int iLong = c.getColumnIndex(COL_LONGITUDE);
		final int iDesc = c.getColumnIndex(COL_DESCRIPTION);
		final int iTime = c.getColumnIndex(COL_STARTTIME);
		final int iAoM = c.getColumnIndex(COL_AGEOFMAJORITY);
		final int iVID = c.getColumnIndex(COL_VENUEID);
		
		Event e = new Event();
		e.setAgeOfMajority(c.getInt(iAoM) != 0);
		e.setDescription(c.getString(iDesc));
		e.setEventId(c.getInt(iId));
		e.setLatitude(c.getDouble(iLat));
		e.setLongitude(c.getDouble(iLong));
		e.setName(c.getString(iName));
		Date d = new Date();
		d.setTime(c.getLong(iTime)*1000);
		e.setStartTime(d);
		e.setVenueId(c.getInt(iVID));
		
		if (getVenue)
		{
			Venue v = FindieDbHelper.getInstance().getVenueHelper().getVenueForEvent(e);
			e.setVenue(v);
		}
		
		ArrayList<Artist> artists = FindieDbHelper.getInstance().getArtistHelper().getArtistsForEvent(e);
		if (artists != null)
		{
			Artist[] aa = new Artist[artists.size()];
			e.setArtists(artists.toArray(aa));
		}
		
		return e;
	}
	
	public void saveEvents(Event[] events)
	{
		saveEvents(events, true);
	}
	
	public void saveEvents(Event[] events, boolean saveVenue)
	{
		if (events != null)
		{
			try
			{
    			mDb.beginTransaction();
	    		for (Event e : events)
	    		{
	    			final ContentValues cv = getContentValues(e);
    				try
    				{
	    				long id = mDb.insertOrThrow(DATABASE_TABLE_EVENTS, null, cv);
	    				Log.d(TAG, "Inserted id " + Long.toString(id));
    				} catch (SQLiteConstraintException ex)
    				{
    					if (!(mDb.update(DATABASE_TABLE_EVENTS, cv, "_id=?", new String[] { Integer.toString(e.getEventId()) }) == 1))
    						throw ex;
    				}
    				
    				// Save artists
    				ArtistHelper h = FindieDbHelper.getInstance().getArtistHelper();
    				h.saveArtists(e.getArtists());
    				h.mapArtistsToEvent(e.getArtists(), e);
    				if (saveVenue)
    				{
	    				// Save venue
	    				VenueHelper v = FindieDbHelper.getInstance().getVenueHelper();
	    				v.saveVenues(new Venue[] { e.getVenue() });
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

	public ArrayList<Event> findEvents(String search) {
		ArrayList<Event> events = new ArrayList<Event>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_EVENTS, null, "name LIKE ?", new String[] { "%" + search + "%" }, null, null, null, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
    			Event e = readEvent(c);
    			
    			events.add(e);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return events;
	}
}
