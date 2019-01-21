package com.zenwerx.findierock.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.zenwerx.findierock.data.FavouriteHelper.FavouriteType;
import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.Artist;
import com.zenwerx.findierock.model.Event;

public class ArtistHelper {
	private static final String TAG = "findierock.ArtistHelper";
	
	public final String COL_ARTISTID = "_id";
	public final String COL_NAME = "name";
	public final String COL_BIO = "bio";
	public final String COL_WEBSITE = "website";
	public final String COL_SMALLIMAGE = "smallimage";
	public final String COL_LARGEIMAGE = "largeimage";
	public final String COL_FETCHEDALBUMS = "fetchedalbums";
	
	private final String COL_EV_ARTISTID = "artistid";
	private final String COL_EV_EVENTID = "eventid";
		
	public final String DATABASE_TABLE_ARTISTS = "artists";
	public final String DATABASE_TABLE_EVENTARTISTS = "eventartists";
	    	
	private final SQLiteDatabase mDb;
	
	public ArtistHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private final ContentValues getContentValues(Artist a)
	{
		final ContentValues cv = new ContentValues();
		
		cv.put(COL_ARTISTID, a.getArtistId());
		cv.put(COL_NAME, a.getName());
		cv.put(COL_BIO, a.getBio());
		cv.put(COL_WEBSITE , a.getWebsite());
		cv.put(COL_LARGEIMAGE, a.getLargeImage());
		cv.put(COL_SMALLIMAGE, a.getSmallImage());
		cv.put(COL_FETCHEDALBUMS, a.didFetchAlbums() ? 1 : 0);
		
		return cv;
	}
	
	private final ContentValues getContentValues(Artist a, Event e)
	{
		final ContentValues cv = new ContentValues();
		
		cv.put(COL_EV_ARTISTID, a.getArtistId());
		cv.put(COL_EV_EVENTID, e.getEventId());
		
		return cv;
	}

	private Artist readArtist(final Cursor c)
	{
		return readArtist(c, true);
	}
	
	private Artist readArtist(final Cursor c, final boolean readEvents)
	{
		final int iId = c.getColumnIndex(COL_ARTISTID);
		final int iName = c.getColumnIndex(COL_NAME);
		final int iBio  = c.getColumnIndex(COL_BIO);
		final int iSite = c.getColumnIndex(COL_WEBSITE);
		final int iSmImg = c.getColumnIndex(COL_SMALLIMAGE);
		final int iLgImg = c.getColumnIndex(COL_LARGEIMAGE);
		final int iAlbs = c.getColumnIndex(COL_FETCHEDALBUMS);
		
		Artist a = new Artist();
		a.setArtistId(c.getInt(iId));
		a.setBio(c.getString(iBio));
		a.setWebsite(c.getString(iSite));
		a.setName(c.getString(iName));
		a.setSmallImage(c.getString(iSmImg));
		a.setLargeImage(c.getString(iLgImg));
		a.setFetchedAlbums(c.getInt(iAlbs) != 0);
		
		if (readEvents)
		{
			ArrayList<Event> events = FindieDbHelper.getInstance().getEventsHelper().getEventsForTodayOrLaterWithArtist(a);
			if (events != null)
			{
				Event[] aa = new Event[events.size()];
				a.setUpcomingEvents(events.toArray(aa));
			}
		}
		
		ArrayList<Album> albums = FindieDbHelper.getInstance().getAlbumHelper().getAlbumsForArtist(a);
		if (albums != null)
		{
			Album[] albumArray = new Album[albums.size()];		
			a.setAlbums(albums.toArray(albumArray));
		}
		
		return a;
	}
	
	public Artist getArtist(int artistId)
	{
		
		Artist a = null;
	
		final Cursor c = mDb.query(DATABASE_TABLE_ARTISTS,
							null,
							"_id=?", new String[] { new Integer(artistId).toString() }, 
							null, null, null);
	
		if (c != null && c.moveToFirst())
		{
			a = readArtist(c);
		}
		
		if (c != null)
		{
			c.close();
		}
	
		return a;
	}

	public ArrayList<Artist> getArtistsForEvent(Event e)
	{
		ArrayList<Artist> artists = new ArrayList<Artist>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ARTISTS + " a, " + DATABASE_TABLE_EVENTARTISTS + " ea",
								new String[] { "a." + COL_ARTISTID, COL_NAME, COL_BIO, COL_WEBSITE, COL_SMALLIMAGE, COL_LARGEIMAGE, COL_FETCHEDALBUMS},
								"ea.ArtistId=a._id AND ea.EventId=?", new String[] { new Integer(e.getEventId()).toString() }, 
								null, null, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
    			artists.add(readArtist(c, false));
    			
    			c.moveToNext();
			}
			
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return artists;
	}
	
	public void mapArtistsToEvent(Artist[] artists, Event event)
	{
		if (artists != null && event != null)
		{
			try
			{
    			mDb.beginTransaction();
	    		for (Artist a : artists)
	    		{
    				ContentValues cv = getContentValues(a, event);
    				try
    				{
	    				long id = mDb.insertOrThrow(DATABASE_TABLE_EVENTARTISTS, null, cv);
	    				Log.d(TAG, "Inserted id " + Long.toString(id));
    				} catch (SQLiteConstraintException ex)
    				{
    					// yum
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
	
	public void saveArtists(Artist[] artists)
	{
		saveArtists(artists, false);
	}
	
	public void saveArtists(Artist[] artists, boolean saveEventsAndAlbums)
	{
		if (artists != null)
		{
			try
			{
    			mDb.beginTransaction();
	    		for (Artist a : artists)
	    		{
	    			final ContentValues cv = getContentValues(a);
    				try
    				{
	    				long id = mDb.insertOrThrow(DATABASE_TABLE_ARTISTS, null, cv);
	    				Log.d(TAG, "Inserted id " + Long.toString(id));
    				} catch (SQLiteConstraintException ex)
    				{    					
    					if (!(mDb.update(DATABASE_TABLE_ARTISTS, cv, "_id=?", new String[] { Integer.toString(a.getArtistId()) }) == 1))
    						throw ex;
    				}
    				
    				if (saveEventsAndAlbums)
    				{
	    				FindieDbHelper.getInstance().getEventsHelper().saveEvents(a.getUpcomingEvents());
	    				FindieDbHelper.getInstance().getAlbumHelper().saveAlbums(a.getAlbums());
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

	public ArrayList<Artist> getFavourites() {
		ArrayList<Artist> artists = new ArrayList<Artist>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ARTISTS,
								null, COL_ARTISTID + " IN (SELECT favid FROM favourites WHERE favtype=?)", 
								new String[] { Integer.toString(FavouriteType.FAV_ARTIST.ordinal()) }, 
								null, null, COL_NAME);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
    			artists.add(readArtist(c, false));
    			
    			c.moveToNext();
			}
			
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return artists;
	}

	public ArrayList<Artist> findArtists(String search) {
		ArrayList<Artist> artists = new ArrayList<Artist>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ARTISTS, null, "name LIKE ?", new String[] { "%" + search + "%" }, null, null, null, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				Artist a = readArtist(c);
    			
				artists.add(a);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return artists;
	}
}
