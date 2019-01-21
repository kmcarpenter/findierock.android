package com.zenwerx.findierock.helper;
import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class SettingsHelper {
	
	public final static String SP_UNITS = "units";
	public final static String SP_MINDIST = "minUpdateDistance";
	public final static String SP_MAXRAD = "maxSearchRadius";
	public final static String SP_MINHRS = "minUpdateHours";
	public final static String SP_UPDMOB = "updateOnMobile";
	
	private final static String SP_RUNONCE = "appRunOnce";
	private final static String SP_BACKGROUND = "inBackground";
	private final static String SP_DATANEW = "dataNew";
	private final static String SP_LASTUPDATE = "lastUpdate";
	private final static String FILE_OTHER_PREFS = "nonEssentialPrefs";
	
	public static final boolean UsingImperial(Context ctx)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		return Boolean.parseBoolean(prefs.getString(SP_UNITS, "false"));
	}
	
	public static final int MinUpdateDistance(Context ctx)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		return Integer.parseInt(prefs.getString(SP_MINDIST, "25"));
	}
	
	public static final int MinUpdateHours(Context ctx)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		return Integer.parseInt(prefs.getString(SP_MINHRS, "4"));
	}
	
	public static final int MaxSearchRadius(Context ctx)
	{
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		return Integer.parseInt(prefs.getString(SP_MAXRAD, "25"));
	}
	
	public static final boolean AppRunOnce(Context ctx)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		SharedPreferences.Editor ed = prefs.edit();
		
		boolean val = prefs.getBoolean(SP_RUNONCE, false);
		 
		ed.putBoolean(SP_RUNONCE, true);
		ed.commit();
		
		return val;
	}
	
	public static final boolean InBackground(Context ctx)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		
		return prefs.getBoolean(SP_BACKGROUND, true);
	}
	
	public static void SetInBackground(Context ctx, boolean inBackground)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		SharedPreferences.Editor ed = prefs.edit(); 
		
		ed.putBoolean(SP_BACKGROUND, inBackground);
		ed.commit();
	}
	
	public static final boolean IsDataNew(Context ctx)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		
		return prefs.getBoolean(SP_DATANEW, false);
	}
	
	public static void SetIsDataNew(Context ctx, boolean dataNew)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		SharedPreferences.Editor ed = prefs.edit();
		
		ed.putBoolean(SP_DATANEW, dataNew);
		ed.commit();
	}

	public static final boolean UpdateOnMobile(Context ctx) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
		
		return prefs.getBoolean(SP_UPDMOB, false);
	}
	
	public static final String UrlBase() {
		return "http://www.findierock.com";
	}

	public static void SetLastSettingsUpdate(Context ctx, Date lastUpdate)
	{
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		SharedPreferences.Editor ed = prefs.edit();
		
		ed.putLong(SP_LASTUPDATE, lastUpdate.getTime());
		ed.commit();
	}
	
	public static Date LastSettingsUpdate(Context ctx) {
		SharedPreferences prefs = ctx.getSharedPreferences(FILE_OTHER_PREFS, Context.MODE_PRIVATE );
		
		long millis = prefs.getLong(SP_LASTUPDATE, System.currentTimeMillis());
		
		return new Date(millis);
	}

	public static String AnalyticsAccount() {
		return "UA-25475171-1";
	}
}
