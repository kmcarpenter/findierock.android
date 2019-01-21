package com.zenwerx.findierock.data;

import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.Artist;
import com.zenwerx.findierock.model.Favourite;
import com.zenwerx.findierock.model.Venue;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class FavouriteHelper {
	public static enum FavouriteType
	{
		FAV_NONE,
		FAV_ARTIST,
		FAV_VENUE,
		FAV_ALBUM
	}
	
	private static final String TAG = "findierock.ArtistHelper";
	
	public final String COL_FAVTYPE = "favtype";
	public final String COL_FAVID = "favid";
	
	public final String DATABASE_TABLE_FAVOURITES = "favourites";
		    	
	private SQLiteDatabase mDb;
	
	public FavouriteHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private ContentValues getContentValues(Favourite f)
	{
		ContentValues cv = new ContentValues();
		
		cv.put(COL_FAVTYPE, Integer.toString(f.getFavouriteType().ordinal()));
		cv.put(COL_FAVID, Integer.toString(f.getFavouriteId()));
		
		return cv;
	}
	
	@SuppressWarnings("unused")
	private Favourite readFavourite(Cursor c)
	{
		int iType = c.getColumnIndex(COL_FAVTYPE);
		int iId = c.getColumnIndex(COL_FAVID);
		
		Favourite f = new Favourite();
		f.setFavouriteId(c.getInt(iId));
		int type = c.getInt(iType);
		for(FavouriteType t : FavouriteType.values())
		{
			if (t.ordinal() == type )
			{
				f.setFavouriteType(t);
				break;
			}
		}
		
		return f;
	}
	
	public boolean IsFavourite(Artist artist)
	{
		return IsFavourite(FavouriteType.FAV_ARTIST, artist.getArtistId());
	}
	
	public boolean IsFavourite(Venue venue)
	{
		return IsFavourite(FavouriteType.FAV_VENUE, venue.getVenueId());
	}
	
	public boolean IsFavourite(Album album)
	{
		return IsFavourite(FavouriteType.FAV_ALBUM, album.getAlbumId());
	}
	
	private boolean IsFavourite(FavouriteType type, int favId)
	{
		boolean result = false;
		
		Cursor c = mDb.query(DATABASE_TABLE_FAVOURITES,
							null,
							COL_FAVID + "=? AND " + COL_FAVTYPE + " =?", 
							new String[] { Integer.toString(favId), Integer.toString(type.ordinal()) }, 
							null, null, null);
	
		if (c != null && c.moveToFirst())
		{
			result = true;
		}
		
		if (c != null)
		{
			c.close();
		}
	
		return result;
	}
	
	public void setFavourite(Artist artist)
	{
		setFavourite(FavouriteType.FAV_ARTIST, artist.getArtistId());
	}
	
	public void setFavourite(Venue venue)
	{
		setFavourite(FavouriteType.FAV_VENUE, venue.getVenueId());
	}
	
	public void setFavourite(Album album)
	{
		setFavourite(FavouriteType.FAV_ALBUM, album.getAlbumId());
	}
	
	private void setFavourite(FavouriteType type, int favId)
	{
		try
		{
			mDb.beginTransaction();

			Favourite f = new Favourite();
			f.setFavouriteId(favId);
			f.setFavouriteType(type);
			ContentValues cv = getContentValues(f);
			try
			{
				long id = mDb.insertOrThrow(DATABASE_TABLE_FAVOURITES, null, cv);
				Log.d(TAG, "Inserted id " + Long.toString(id));
			} catch (SQLiteConstraintException ex)
			{    					
				// don't need to bother
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
	
	public void deleteFavourite(Artist artist)
	{
		deleteFavourite(FavouriteType.FAV_ARTIST, artist.getArtistId());
	}
	
	public void deleteFavourite(Venue venue)
	{
		deleteFavourite(FavouriteType.FAV_VENUE, venue.getVenueId());
	}
	
	public void deleteFavourite(Album album)
	{
		deleteFavourite(FavouriteType.FAV_ALBUM, album.getAlbumId());
	}
	
	private void deleteFavourite(FavouriteType type, int favId)
	{
		try
		{
			mDb.beginTransaction();

			mDb.delete(DATABASE_TABLE_FAVOURITES, COL_FAVID + "=? AND " + COL_FAVTYPE + " =?", 
						new String[] { Integer.toString(favId), Integer.toString(type.ordinal()) } );
				
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