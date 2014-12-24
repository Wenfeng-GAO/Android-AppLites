package com.wenfeng.runtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class RunFragment extends Fragment {
	
	private RunManager mRunManager;
	private Button mButtonStart, mButtonStop;
	private TextView mTextViewStarted, mTextViewLatitude, mTextViewLongitude, mTextViewAltitude, mTextViewDuration;
	private Run mRun;
	private Location mLastLocation;
	
	private BroadcastReceiver mLocationreceiver = new Locationreceiver() {

		@Override
		protected void onProviderEnabledChanged(boolean enabled) {
			super.onProviderEnabledChanged(enabled);
			int toastText = enabled ? R.string.gps_enabled : R.string.gps_disabled;
			Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onLocationReceived(Context context, Location loc) {
			super.onLocationReceived(context, loc);
			mLastLocation = loc;
			if (isVisible()) {
				updateUI();
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mRunManager = RunManager.get(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_run, container, false);
		mTextViewStarted = (TextView) view.findViewById(R.id.textView_started);
		mTextViewLatitude = (TextView) view.findViewById(R.id.textView_latitude);
		mTextViewLongitude = (TextView) view.findViewById(R.id.textView_longitude);
		mTextViewAltitude = (TextView) view.findViewById(R.id.textView_altitude);
		mTextViewDuration = (TextView) view.findViewById(R.id.textView_elapsed_time);
		
		mButtonStart = (Button) view.findViewById(R.id.button_start);
		mButtonStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mRunManager.startLocationUpdates();
				mRun = new Run();
				updateUI();
			}
		});
		mButtonStop = (Button) view.findViewById(R.id.button_stop);
		mButtonStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mRunManager.stopLocationUpdates();
				updateUI();
			}
		});
		
		updateUI();
		
		return view;
	}
	private void updateUI() {
		boolean started = mRunManager.isTrackingRun();
		
		if (mRun != null) {
			mTextViewStarted.setText(mRun.getStartDate().toString());
		}
		
		int durationSeconds = 0;
		if (mRun != null && mLastLocation != null) {
			durationSeconds = mRun.getDurationSeconds(mLastLocation.getTime());
			mTextViewLatitude.setText(Double.toString(mLastLocation.getLatitude()));
			mTextViewLongitude.setText(Double.toString(mLastLocation.getLongitude()));
			mTextViewAltitude.setText(Double.toString(mLastLocation.getAltitude()));
		}
		mTextViewDuration.setText(Run.formatDuration(durationSeconds));
		mButtonStart.setEnabled(!started);
		mButtonStop.setEnabled(started);
	}
	@Override
	public void onStart() {
		super.onStart();
		getActivity().registerReceiver(mLocationreceiver, new IntentFilter(RunManager.ACTION_LOCATION));
	}
	@Override
	public void onStop() {
		getActivity().unregisterReceiver(mLocationreceiver);
		super.onStop();
	}
	
}
