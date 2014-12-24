package com.wenfeng.runtracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

public class RunManager {
	private static final String TAG = RunManager.class.getSimpleName();
	
	public static final String ACTION_LOCATION = "com.wenfeng.runtracker.ACTION_LOCATION";
	private static RunManager sRunManager;
	private Context mAppContext;
	private LocationManager mLocationManager;
	
	// The private constructor forces users to use RunManager.get(Context)
	private RunManager(Context appContext) {
		mAppContext = appContext;
		mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);
	}
	
	public static RunManager get(Context context) {
		if (sRunManager == null) {
			// Use the application context to avoid leaking activities
			sRunManager = new RunManager(context.getApplicationContext());
		}
		return sRunManager;
	}
	
	private PendingIntent getLocationPendingIntent(boolean shouldCreate) {
		Intent broadcast = new Intent(ACTION_LOCATION);
		int flags = shouldCreate ? 0 : PendingIntent.FLAG_NO_CREATE;
		return PendingIntent.getBroadcast(mAppContext, 0, broadcast, flags);
	}
	
	public void startLocationUpdates() {
		String provider = LocationManager.GPS_PROVIDER;
		
		// Get the last know location and broadcast it if you have one
		Location lastKnownloc = mLocationManager.getLastKnownLocation(provider);
		if (lastKnownloc != null) {
			// rest the time to now
			lastKnownloc.setTime(System.currentTimeMillis());
			broadcastLocation(lastKnownloc);
		}
		
		// Start updates from the location manager
		PendingIntent pIntent = getLocationPendingIntent(true);
		mLocationManager.requestLocationUpdates(provider, 0, 0, pIntent);
	}
	
	private void broadcastLocation(Location location) {
		Intent broadcast = new Intent(ACTION_LOCATION);
		broadcast.putExtra(LocationManager.KEY_LOCATION_CHANGED, location);
		mAppContext.sendBroadcast(broadcast);
	}

	public void stopLocationUpdates() {
		PendingIntent pIntent = getLocationPendingIntent(false);
		if (pIntent != null) {
			mLocationManager.removeUpdates(pIntent);
			pIntent.cancel();
		}
	}
	
	public boolean isTrackingRun() {
		return getLocationPendingIntent(false) != null;
	}
}
