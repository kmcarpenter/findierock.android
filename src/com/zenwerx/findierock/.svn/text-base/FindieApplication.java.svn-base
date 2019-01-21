package com.zenwerx.findierock;

import java.io.File;

import com.zenwerx.findierock.data.FindieDbHelper;
import com.zenwerx.findierock.helper.ImageDownloader;

import android.app.Application;
import android.os.AsyncTask;

public class FindieApplication extends Application {

	// 10MB
	private int MAX_CACHE = 1024 * 1024 * 10;
	
	public void onCreate() {
		super.onCreate();
		
		ImageDownloader.getInstance().setMode(ImageDownloader.Mode.CORRECT);
		FindieDbHelper.getInstance().setContext(getApplicationContext());
		
		CacheCleanup task = new CacheCleanup();
		task.execute((Void)null);
	}
	
	
	@Override
	public void onTerminate() {
		FindieDbHelper.getInstance().setContext(null);
		
		super.onTerminate();
	}
	
	private class CacheCleanup extends AsyncTask<Void, Void, Void>
	{

		@Override
		protected Void doInBackground(Void... params) {

			File cacheDir = getCacheDir();
			File[] files = cacheDir.listFiles();
			
			long sum = 0;
			for(File file : files)
			{
				sum += file.length();
			}
			
			if (sum >= MAX_CACHE)
			{
				for(File file : files)
				{
					file.delete();
				}	
			}
			
			return null;
		}
		
	}
}
