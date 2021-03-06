package com.wenfeng.fivecmps;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.SurfaceHolder;

public class AudioPlayer {
	private MediaPlayer mPlayer;
	
	public void stop() {
		if(mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}
	
	public void play(Context context, SurfaceHolder sh) {
		stop();
		
		mPlayer = MediaPlayer.create(context, R.raw.five_centimeters_per_second);
		mPlayer.setDisplay(sh);
		mPlayer.setScreenOnWhilePlaying(true);
		mPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				stop();
			}
		});
		
		
		mPlayer.start();
	}
	
	public void pause() {
		mPlayer.pause();
	}
	
	public void continu() {
		mPlayer.start();
	}
	
}
