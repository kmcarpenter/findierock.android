package com.zenwerx.findierock.data;

import java.util.ArrayList;
import java.util.Date;

import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.AlbumTrack;
import com.zenwerx.findierock.model.Artist;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlbumHelper {
	private final String TAG = "findierock.AlbumHelper";
	
	public final String DATABASE_TABLE_ALBUMS = "albums";
	
	public final String COL_ALBUMID = "_id";
	public final String COL_ARTISTID = "artistid";
	public final String COL_NAME = "name";
	public final String COL_MOREURL = "moreurl";
	public final String COL_SMALLIMAGE = "smallimage";
	public final String COL_LARGEIMAGE = "largeimage";
	public final String COL_RELEASE = "releasedate";
	public final String COL_LASTFMID = "lastfmid";
	
	private final SQLiteDatabase mDb;
	
	public AlbumHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private final ContentValues getContentValues(Album a)
	{
		final ContentValues cv = new ContentValues();
		
		cv.put(COL_ALBUMID, a.getAlbumId());
		cv.put(COL_ARTISTID, a.getArtistId());
		cv.put(COL_NAME, a.getName());
		cv.put(COL_MOREURL, a.getMoreUrl());
		cv.put(COL_SMALLIMAGE, a.getSmallImage());
		cv.put(COL_LARGEIMAGE, a.getLargeImage());
		cv.put(COL_RELEASE, a.getReleaseDate().getTime()/1000);
		cv.put(COL_LASTFMID, a.getLastFmId());
		
		return cv;
	}
	
	private Album readAlbum(final Cursor c)
	{
		final int iId = c.getColumnIndex(COL_ALBUMID);
		final int iArtId = c.getColumnIndex(COL_ARTISTID);
		final int iName = c.getColumnIndex(COL_NAME);
		final int iMore = c.getColumnIndex(COL_MOREURL);
		final int iSmall = c.getColumnIndex(COL_SMALLIMAGE);
		final int iLarge = c.getColumnIndex(COL_LARGEIMAGE);
		final int iRelease = c.getColumnIndex(COL_RELEASE);
		final int iLastFm = c.getColumnIndex(COL_LASTFMID);
		
		Album a = new Album();
		
		a.setAlbumId(c.getInt(iId));
		a.setArtistId(c.getInt(iArtId));
		a.setName(c.getString(iName));
		a.setMoreUrl(c.getString(iMore));
		a.setSmallImage(c.getString(iSmall));
		a.setLargeImage(c.getString(iLarge));
		a.setReleaseDate(new Date(c.getLong(iRelease)*1000));
		a.setLastFmId(c.getString(iLastFm));
		
		ArrayList<AlbumTrack> at = FindieDbHelper.getInstance().getAlbumTrackHelper().getAlbumTracksForAlbum(a);
		AlbumTrack[] ata = new AlbumTrack[at.size()];
		a.setTracks(at.toArray(ata));
		
		return a;
	}
	
	public Album getAlbum(int albumId)
	{
		Album result = null;
		
		final Cursor c = mDb.query(DATABASE_TABLE_ALBUMS, null, COL_ALBUMID + " = ?", new String[] { Integer.toString(albumId) }
								, null, null, null, null);
		
		if (c != null && c.moveToFirst()) {
			result = readAlbum(c);
		}
		
		if (c != null) {
			c.close();
		}
		
		return result;
	}
	
	public ArrayList<Album> getAlbumsForArtist(Artist a)
	{
		ArrayList<Album> result = new ArrayList<Album>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ALBUMS, null, COL_ARTISTID + " = ?", new String[] { Integer.toString(a.getArtistId()) }
								, null, null, null, null);
		
		if (c != null && c.moveToFirst()) {
			while (!c.isAfterLast()) {
				result.add(readAlbum(c));
    			
    			c.moveToNext();
			}
		}
		
		if (c != null) {
			c.close();
		}
		
		return result;
	}
	
	public void saveAlbum(Album album)
	{
		if (album != null)
		{
			try
			{
    			mDb.beginTransaction();
	    		
    			final ContentValues cv = getContentValues(album);
    			
    			try
    			{
					long id = mDb.insertOrThrow(DATABASE_TABLE_ALBUMS, null, cv);
					Log.d(TAG, "Inserted id " + Long.toString(id));
    			} catch (SQLiteConstraintException ex)
    			{
    				if (!(mDb.update(DATABASE_TABLE_ALBUMS, cv, "_id=?", new String[] { Integer.toString(album.getAlbumId()) }) == 1))
    					throw ex;
				}
    			
    			FindieDbHelper.getInstance().getAlbumTrackHelper().saveAlbumTracks(album.getTracks());
	    		
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

	public void saveAlbums(Album[] albums) 
	{
		if (albums != null)
		{
			for( Album album : albums )
			{
				saveAlbum(album);
			}
		}
	}

	public ArrayList<Album> findAlbums(String search) {
		ArrayList<Album> albums = new ArrayList<Album>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ALBUMS, null, "name LIKE ?", new String[] { "%" + search + "%" }, null, null, null, null);
		
		if (c != null && c.moveToFirst())
		{
			while (!c.isAfterLast())
			{
				Album a = readAlbum(c);
    			
    			albums.add(a);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return albums;
	}
}
