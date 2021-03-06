package com.wenfeng.fivecmps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class CherryBlossomFragment extends Fragment {
	private AudioPlayer mPlayer; 
	private Button mButtonPlay, mButtonStop, mButtonPause;
	private SurfaceView mSurfaceView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_cherry_blossom, container);
		
		mPlayer = new AudioPlayer();
		
		// surface view
		mSurfaceView = (SurfaceView) view.findViewById(R.id.surfaceview);
		// play button
		mButtonPlay = (Button) view.findViewById(R.id.button_play);
		mButtonPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mPlayer.play(getActivity(), mSurfaceView.getHolder());
			}
		});
		
		// stop button
		mButtonStop = (Button) view.findViewById(R.id.button_stop);
		mButtonStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPlayer.stop();
			}
		});
		
		// pause button
		mButtonPause = (Button) view.findViewById(R.id.button_pause);
		mButtonPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				if (mButtonPause.getText().equals(getResources().getString(R.string.button_pause))) {
					mPlayer.pause();
					mButtonPause.setText("Continue");
				} else {
					mPlayer.continu();
					mButtonPause.setText(R.string.button_pause);
				}
			}
		});
		
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mPlayer.stop();
	}

}
