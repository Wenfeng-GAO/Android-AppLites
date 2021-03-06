package com.wenfeng.photogallery;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class ThumbnailDownloader<Token> extends HandlerThread {
	private static final String TAG = ThumbnailDownloader.class.getSimpleName();
	private static final int MESSAGE_DOWNLOAD = 0;
	
	private Handler handler, responseHandler;
	private Listener<Token> listener;
	private Map<Token, String> requestMap = Collections.synchronizedMap(new HashMap<Token, String>());

	public interface Listener<Token> {
		void onThumbnailDownloader(Token token, Bitmap thumbnail);
	}
	public void setListener(Listener<Token> listener) {
		this.listener = listener; 
	}
	
	@SuppressLint("HandlerLeak")
	@Override
	protected void onLooperPrepared() {
		super.onLooperPrepared();
		handler = new Handler() {

			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				Token token = (Token)msg.obj;
				Log.i(TAG, "Got a request for url: " + requestMap.get(token));
				handleRequest(token);
			}
			
		};
	}
	
	public void clearQueue() {
		handler.removeMessages(MESSAGE_DOWNLOAD);
		requestMap.clear();
	}
	
	private void handleRequest(final Token token) {
		final String url = requestMap.get(token);
		if (url == null) {
			return;
		}
		try {
			byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
			final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
			Log.i(TAG, "Bitmap created");
			responseHandler.post(new Runnable() {
				
				@Override
				public void run() {
					if (requestMap.get(token) == url) {
						requestMap.remove(token);
						listener.onThumbnailDownloader(token, bitmap);
					}
				}
			});
		} catch (IOException e) {
			Log.e(TAG, "Error downloading image", e);
		}
	}
	
	public ThumbnailDownloader(Handler responseHandler) {
		super(TAG);
		this.responseHandler = responseHandler;
	}
	
	public void queueThumbnail(Token token, String url) {
		Log.i(TAG, "Got an URL: " + url);
		requestMap.put(token, url);
		
		handler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
	}
}
