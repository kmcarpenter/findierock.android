package com.zenwerx.findierock.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class FindieDbAdapter {
	
	private static final String TAG = "findierock.FindieDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    
    private static final String DATABASE_NAME = "data";
    
    private static final String CREATE_TABLE_ARTISTS = "create table artists( _id integer primary key, name text, bio text, website text, smallimage text, largeimage text, fetchedalbums integer )";
    private static final String CREATE_TABLE_EVENTARTISTS = "create table eventartists( eventid integer, artistid integer, primary key( eventid, artistid ) )";
    private static final String CREATE_TABLE_EVENTS = "create table events ( _id integer primary key, latitude real, longitude real, name text, description text, startTime integer, ageOfMajority integer, venueId integer );";
    private static final String CREATE_TABLE_OPERATIONS = "create table operations ( _id integer primary key autoincrement, status integer );";
    private static final String CREATE_TABLE_STATUS = "create table status ( _id integer primary key autoincrement, lastUpdate text, lastLatitude real, lastLongitude real );";
    private static final String CREATE_TABLE_VENUES = "create table venues ( _id integer primary key, name text, address text, address2 text, city text, province text, country text, website text, latitude real, longitude real, smallimage text, largeimage text)";    
    private static final String CREATE_TABLE_ALBUMS = "create table albums ( _id integer primary key, artistid integer, name text, moreurl text, smallimage text, largeimage text, releasedate integer, lastfmid text )";
    private static final String CREATE_TABLE_FAVOURITES = "create table favourites ( favtype integer, favid integer, primary key( favtype, favid ) )";
    private static final String CREATE_TABLE_ALBUMTRACKS = "create table albumtracks ( _id integer primary key, albumid integer, name text, length integer, lastfmid text)";
    
    private static final String UPDATE_TABLE_EVENTS_0 = "alter table events add venueId integer";
    
    private static final String UPDATE_TABLE_VENUES_0 = "alter table venues add latitude real";
    private static final String UPDATE_TABLE_VENUES_1 = "alter table venues add longitude real";
    private static final String UPDATE_TABLE_VENUES_2 = "alter table venues add fav integer";
    private static final String UPDATE_TABLE_VENUES_3 = "alter table venues add smallimage text";
    private static final String UPDATE_TABLE_VENUES_4 = "alter table venues add largeimage text";
        
    private static final String UPDATE_TABLE_ARTISTS_0 = "alter table artists add fav integer";
    private static final String UPDATE_TABLE_ARTISTS_1 = "alter table artists add smallimage text";
    private static final String UPDATE_TABLE_ARTISTS_2 = "alter table artists add largeimage text";
    private static final String UPDATE_TABLE_ARTISTS_3 = "alter table artists add fetchedalbums integer";
        
    private static final String UPDATE_TABLE_STATUS_0 = "alter table status add lastLatitude real";
    private static final String UPDATE_TABLE_STATUS_1 = "alter table status add lastLongitude real";
    
    private static final String UPDATE_TABLE_ALBUMS_0 = "alter table albums add releasedate integer";
    private static final String UPDATE_TABLE_ALBUMS_1 = "alter table albums add lastfmid text";
    
    
    private static final int DATABASE_VERSION = 18;

    private final Context mContext;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE_EVENTS);
            db.execSQL(CREATE_TABLE_OPERATIONS);
            db.execSQL(CREATE_TABLE_STATUS);
            db.execSQL(CREATE_TABLE_ARTISTS);
            db.execSQL(CREATE_TABLE_EVENTARTISTS);
            db.execSQL(CREATE_TABLE_VENUES);
            db.execSQL(CREATE_TABLE_ALBUMS);
            db.execSQL(CREATE_TABLE_FAVOURITES);
            db.execSQL(CREATE_TABLE_ALBUMTRACKS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion);
    
            if (oldVersion < 7)
            {
            	db.execSQL(CREATE_TABLE_ARTISTS);
            	db.execSQL(CREATE_TABLE_EVENTARTISTS);
            }
            if (oldVersion < 9)
            {
            	db.execSQL(CREATE_TABLE_VENUES);
            	db.execSQL(UPDATE_TABLE_EVENTS_0);
            }
            if (oldVersion < 10)
            {
            	db.execSQL(UPDATE_TABLE_VENUES_0);
            	db.execSQL(UPDATE_TABLE_VENUES_1);
            }
            if (oldVersion < 11)
            {
            	db.execSQL(UPDATE_TABLE_VENUES_2);
            	db.execSQL(UPDATE_TABLE_ARTISTS_0);
            }
            if (oldVersion < 14)
            {
            	db.execSQL(UPDATE_TABLE_STATUS_0);
            	db.execSQL(UPDATE_TABLE_STATUS_1);
            }
            if (oldVersion < 15)
            {
            	db.execSQL(UPDATE_TABLE_VENUES_3);
            	db.execSQL(UPDATE_TABLE_VENUES_4);
            	db.execSQL(UPDATE_TABLE_ARTISTS_1);
            	db.execSQL(UPDATE_TABLE_ARTISTS_2);
            	db.execSQL(UPDATE_TABLE_ARTISTS_3);
            	db.execSQL(CREATE_TABLE_ALBUMS);
            }
            if (oldVersion < 16)
            {
            	db.execSQL(UPDATE_TABLE_ALBUMS_0);
            }
            if (oldVersion < 17)
            {
            	db.execSQL(CREATE_TABLE_FAVOURITES);
            }
            if (oldVersion < 18)
            {
            	db.execSQL(CREATE_TABLE_ALBUMTRACKS);
            	db.execSQL(UPDATE_TABLE_ALBUMS_1);
            }
        }
    }
    
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     * 
     * @param mContext the Context within which to work
     */
    public FindieDbAdapter(Context context) {
        this.mContext = context;
    }

    /**
     * Open the findie database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     * 
     * @return this (self reference, allowing this to be chained in an
     *         initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public FindieDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }
    
    public void close() {
        mDbHelper.close();
    }

    public EventsHelper getEventsHelper()
    {
    	return new EventsHelper(mDb);
    }
    
    public ArtistHelper getArtistHelper()
    {
    	return new ArtistHelper(mDb);
    }
    
   public VenueHelper getVenueHelper()
   {
	   return new VenueHelper(mDb);
   }
   
   public StatusHelper getStatusHelper()
   {
	   return new StatusHelper(mDb);
   }
   
   public AlbumHelper getAlbumHelper()
   {
	   return new AlbumHelper(mDb);
   }

	public FavouriteHelper getFavouriteHelper() {
		return new FavouriteHelper(mDb);
	}
	
	public AlbumTrackHelper getAlbumTrackHelper() {
		return new AlbumTrackHelper(mDb);
	}
}

