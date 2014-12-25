package com.wenfeng.runtracker;

import android.content.Context;
import android.location.Location;

public class TrackingLocationReceiver extends Locationreceiver {

	@Override
	protected void onLocationReceived(Context context, Location loc) {
		super.onLocationReceived(context, loc);
		RunManager.get(context).insertLocation(loc);
	}

}
