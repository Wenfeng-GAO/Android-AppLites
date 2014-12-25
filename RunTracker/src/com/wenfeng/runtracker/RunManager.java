package com.wenfeng.runtracker;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import com.wenfeng.runtracker.RunDatabaseHelper.RunCursor;

public class RunManager {
	private static final String TAG = RunManager.class.getSimpleName();
	
	public static final String ACTION_LOCATION = "com.wenfeng.runtracker.ACTION_LOCATION";
	private static final String PREFS_FILE = "runs";
	private static final String PREF_CURRENT_RUN_ID = "RunManager.currentRunId";
	private static RunManager sRunManager;
	private Context mAppContext;
	private LocationManager mLocationManager;
	private RunDatabaseHelper mHelper;
	private SharedPreferences mPrefs;
	private long mCurrentRunId;
	
	// The private constructor forces users to use RunManager.get(Context)
	private RunManager(Context appContext) {
		mAppContext = appContext;
		mLocationManager = (LocationManager)mAppContext.getSystemService(Context.LOCATION_SERVICE);
		mHelper = new RunDatabaseHelper(mAppContext);
		mPrefs = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
		mCurrentRunId = mPrefs.getLong(PREF_CURRENT_RUN_ID, -1);
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
	
	public Run startNewRun() {
		// Insert a run into the db
		Run run = insertRun();
		// Start tracking the run
		startTrackingRun(run);
		return run;
	}
	
	public void insertLocation(Location loc) {
		if (mCurrentRunId != -1) {
			mHelper.insertLocation(mCurrentRunId, loc);
		} else {
			Log.e(TAG, "Location received with no tracking run; igoring.");
		}
	}

	public RunCursor queryRuns() {
		return mHelper.queryRun();
	}
	
	public void stopRun() {
		stopLocationUpdates();
		mCurrentRunId = -1;
		mPrefs.edit().remove(PREF_CURRENT_RUN_ID).commit();
	}

	private Run insertRun() {
		Run run = new Run();
		run.setId(mHelper.insertrun(run));
		return run;
	}

	private void startTrackingRun(Run run) {
		// Keep the ID
		mCurrentRunId = run.getId();
		// Store it in shared preferences
		mPrefs.edit().putLong(PREF_CURRENT_RUN_ID, mCurrentRunId).commit();
		// Start location updates
		startLocationUpdates();
	}
}
