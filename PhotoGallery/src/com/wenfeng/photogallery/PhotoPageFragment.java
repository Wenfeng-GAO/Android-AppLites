package com.wenfeng.photogallery;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class PhotoPageFragment extends VisibleFragment {
	private String mUrl;
	private WebView mWebView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		
		mUrl = getActivity().getIntent().getData().toString();
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_photo_page, container, false);
		
		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		progressBar.setMax(100); // WebChromeClient reports in range 0-100
		
		mWebView = (WebView) view.findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return false;
			}
		});
		
		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.INVISIBLE);
				} else {
					progressBar.setVisibility(View.VISIBLE);
					progressBar.setProgress(newProgress);
				}
			}
		});
		
		mWebView.loadUrl(mUrl);
		return view;
	}
	
}
