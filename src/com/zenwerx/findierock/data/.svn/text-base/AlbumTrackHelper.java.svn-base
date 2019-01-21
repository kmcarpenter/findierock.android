package com.zenwerx.findierock.data;

import java.util.ArrayList;
import com.zenwerx.findierock.model.Album;
import com.zenwerx.findierock.model.AlbumTrack;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AlbumTrackHelper {
	private final String TAG = "findierock.AlbumTrackHelper";
	
	public final String DATABASE_TABLE_ALBUMTRACKS = "albumtracks";
	
	public final String COL_TRACKID = "_id";
	public final String COL_ALBUMID = "albumid";
	public final String COL_NAME = "name";
	public final String COL_LENGTH = "length";
	public final String COL_LASTFMID = "lastfmid";
	
	private final SQLiteDatabase mDb;
	
	public AlbumTrackHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private final ContentValues getContentValues(AlbumTrack a)
	{
		final ContentValues cv = new ContentValues();
		
		cv.put(COL_TRACKID, a.getTrackId());
		cv.put(COL_ALBUMID, a.getAlbumId());
		cv.put(COL_NAME, a.getName());
		cv.put(COL_LENGTH, a.getLength());
		cv.put(COL_LASTFMID, a.getLastFmId());
		
		return cv;
	}
	
	private AlbumTrack readAlbumTrack(final Cursor c)
	{
		final int iId = c.getColumnIndex(COL_TRACKID);
		final int iAlbumId = c.getColumnIndex(COL_ALBUMID);
		final int iName = c.getColumnIndex(COL_NAME);
		final int iLength = c.getColumnIndex(COL_LENGTH);
		final int iLastFmId = c.getColumnIndex(COL_LASTFMID);
		
		
		AlbumTrack a = new AlbumTrack();
		
		
		a.setTrackId(c.getInt(iId));
		a.setAlbumId(c.getInt(iAlbumId));
		a.setName(c.getString(iName));
		a.setLength(c.getInt(iLength));
		a.setLastFmId(c.getString(iLastFmId));
		
		return a;
	}
	
	public ArrayList<AlbumTrack> getAlbumTracksForAlbum(Album a)
	{
		ArrayList<AlbumTrack> result = new ArrayList<AlbumTrack>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ALBUMTRACKS, null, COL_ALBUMID + " = ?", new String[] { Integer.toString(a.getAlbumId()) }
								, null, null, null, null);
		
		if (c != null && c.moveToFirst()) {
			while (!c.isAfterLast()) {
				result.add(readAlbumTrack(c));
    			
    			c.moveToNext();
			}
		}
		
		if (c != null) {
			c.close();
		}
		
		return result;
	}
	
	public void saveAlbumTrack(AlbumTrack track)
	{
		if (track != null) {
			try {
    			mDb.beginTransaction();
	    		
    			final ContentValues cv = getContentValues(track);
    			
    			try
    			{
					long id = mDb.insertOrThrow(DATABASE_TABLE_ALBUMTRACKS, null, cv);
					Log.d(TAG, "Inserted id " + Long.toString(id));
    			} catch (SQLiteConstraintException ex)
    			{
    				if (!(mDb.update(DATABASE_TABLE_ALBUMTRACKS, cv, "_id=?", new String[] { Integer.toString(track.getTrackId()) }) == 1))
    					throw ex;
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

	public void saveAlbumTracks(AlbumTrack[] tracks) 
	{
		if (tracks != null) {
			for( AlbumTrack track : tracks ) {
				saveAlbumTrack(track);
			}
		}
	}

	public ArrayList<AlbumTrack> findAlbumTracks(String search) {
		ArrayList<AlbumTrack> tracks = new ArrayList<AlbumTrack>();
		
		final Cursor c = mDb.query(DATABASE_TABLE_ALBUMTRACKS, null, "name LIKE ?", new String[] { "%" + search + "%" }, null, null, null, null);
		
		if (c != null && c.moveToFirst()) {
			while (!c.isAfterLast())
			{
				AlbumTrack a = readAlbumTrack(c);
    			
    			tracks.add(a);
    			
    			c.moveToNext();
			}
		}
		
		if (c != null) {
			c.close();
		}
		
		return tracks;
	}
}