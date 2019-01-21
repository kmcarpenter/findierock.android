package com.zenwerx.findierock.data;

import java.util.Date;

import com.zenwerx.findierock.model.Status;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class StatusHelper {
	private final String TAG = "findierock.StatusHelper";
	
	public final String DATABASE_TABLE_STATUS = "status";
	
	public final String COL_STATUSID = "_id";
	public final String COL_LASTLATITUDE = "lastLatitude";
	public final String COL_LASTLONGITUDE = "lastLongitude";
	public final String COL_LASTUPDATE = "lastUpdate";
	
	private final SQLiteDatabase mDb;
	
	public StatusHelper(SQLiteDatabase db)
	{
		this.mDb = db;
	}
	
	private final ContentValues getContentValues(Status s)
	{
		final ContentValues cv = new ContentValues();
		
		cv.put(COL_STATUSID, s.getStatusId());
		cv.put(COL_LASTLATITUDE, s.getLastLatitude());
		cv.put(COL_LASTLONGITUDE, s.getLastLongitude());
		cv.put(COL_LASTUPDATE, (s.getLastUpdate().getTime()/1000));
		
		return cv;
	}
	
	private Status readStatus(Cursor c)
	{
		final int iId = c.getColumnIndex(COL_STATUSID);
		final int iUp = c.getColumnIndex(COL_LASTUPDATE);
		final int iLat = c.getColumnIndex(COL_LASTLATITUDE);
		final int iLong = c.getColumnIndex(COL_LASTLONGITUDE);
		
		Status s = new Status();
		
		s.setStatusId(c.getInt(iId));
		s.setLastUpdate(new Date(c.getLong(iUp) * 1000));
		s.setLastLatitude(c.getDouble(iLat));
		s.setLastLongitude(c.getDouble(iLong));
		
		return s;
	}
	
	public Status getLatestStatus()
	{
		Status result = null;
		
		final Cursor c = mDb.query(DATABASE_TABLE_STATUS, null, null, null, null, null, COL_LASTUPDATE + " DESC", "1");
		
		if (c != null && c.moveToFirst())
		{
			result = readStatus(c);
		}
		
		if (c != null)
		{
			c.close();
		}
		
		return result;
	}
	
	public void saveStatus(Status status)
	{
		if (status != null)
		{
			try
			{
    			mDb.beginTransaction();
	    		
    			final ContentValues cv = getContentValues(status);
    			
    			try
    			{
					long id = mDb.insertOrThrow(DATABASE_TABLE_STATUS, null, cv);
					Log.d(TAG, "Inserted id " + Long.toString(id));
    			} catch (SQLiteConstraintException ex)
    			{
    				if (!(mDb.update(DATABASE_TABLE_STATUS, cv, "_id=?", new String[] { Integer.toString(status.getStatusId()) }) == 1))
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
}
