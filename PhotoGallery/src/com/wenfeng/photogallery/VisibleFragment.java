package com.wenfeng.photogallery;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.util.Log;

public class VisibleFragment extends Fragment {
	private static final String TAG = VisibleFragment.class.getSimpleName();
	
	private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Log.i(TAG, "Canceling notification");
			setResultCode(Activity.RESULT_CANCELED);
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter(PollService.ACTION_SHOW_NOTIFICATION);
		getActivity().registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null);
	}

	@Override
	public void onPause() {
		super.onPause();
		getActivity().unregisterReceiver(mOnShowNotification);
	}
	
	
}
