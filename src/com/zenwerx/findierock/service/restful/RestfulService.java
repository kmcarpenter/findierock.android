package com.zenwerx.findierock.service.restful;


import java.util.HashMap;

import com.zenwerx.findierock.helper.SettingsHelper;
import com.zenwerx.findierock.service.BaseIntentService;

import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class RestfulService extends BaseIntentService implements IRestfulCallback {
	
	private final static String TAG = "findierock.RestfulService";
	
	private final int MAX_THREADS = 4;
	
	private int mThreadsRunning = 0;
	private HashMap<Integer, ResultReceiver> results = new HashMap<Integer, ResultReceiver>();
	
	private static final String URI_BASE = SettingsHelper.UrlBase() + "/Rest/"; 
	
	
	public RestfulService()
	{
		super("RestfulService");
		
		Log.d(TAG, "Konstruct!!");
	}
	
	protected void onHandleIntent(Intent intent)
	{
		 Log.d(TAG, "Service started");
		 final ResultReceiver receiver = intent.getParcelableExtra("receiver");
		 final int requestId = intent.getIntExtra("requestId", 0);
		 final boolean forcedUpdate = intent.getBooleanExtra("forcedUpdate", false);
		 
		 // Don't bother if there's no network
		 // If this was a forced update, just do it
		 // If we're on mobile, and the update on mobile settings is false, don't do it
		 // Or if we're low on battery, don't do it
		 // TODO: Check is background
		 if (!getIsConnected()
				 || (!forcedUpdate &&
						 ((getIsMobileData() && !SettingsHelper.UpdateOnMobile(this))
						 	|| (getIsLowBattery()))))
		 {
			 Log.d(TAG, "Not connected... or some other stuff. So I'm not updating.");
			 receiver.send(-1, null);
			 return;
		 }
		 
		 while(mThreadsRunning >= MAX_THREADS)
		 {
			 try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// Ignore
			}
		 }
		 	 	 
		 String operation = intent.getStringExtra("operation");
		 RestClient client = new RestClient(URI_BASE + operation + "/", RequestMethod.GET);
		 		 
		 String[] params = intent.getStringArrayExtra("params");
		 String[] values = intent.getStringArrayExtra("values");
		
		 // This means I busted shit
		 if (params.length == values.length)
		 { 
			 for( int i = 0; i < params.length; i++)
			 {
				 client.AddParam(params[i], values[i]);			 
			 }
			 
			 synchronized(this)
			 {
				 client.setCallback(this, requestId);
				 results.put(requestId, receiver);
				 mThreadsRunning++;
			 }
	
			 Thread worker = new Thread(client);
			 worker.start();
		 }
		 else
		 {
			 Log.e("SERVICE", "Params and values do not match");
		 }
	}

	@Override
	public void send(int requestId, int responseCode, String response) {
		ResultReceiver r = results.get(requestId);
		if (r != null)
		{
			Bundle b = new Bundle();
			b.putInt("reponseCode", responseCode);
			b.putString("response", response);
			r.send(1, b);
			
			results.remove(requestId);
		}
		synchronized(this)
		{
			mThreadsRunning--;
			if (mThreadsRunning <= 0)
			{
				stopSelf();
			}
		}
	}
}
