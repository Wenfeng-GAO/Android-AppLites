package fr.wenfeng.webview.webview;

import android.graphics.Bitmap;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;


public class MainActivity extends ActionBarActivity {
    private final String URL1 = "http://www.gymglish.com";
    private final String URL2 = "http://www.frantastique.com";
    private final String URL3 = "http://vatefaireconjuguer.com";

    private WebView mWebView;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.webView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        mWebView.getSettings().setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new MyWebClient());

        // Preventing WebView reload on rotation
        if (savedInstanceState == null) {
            mWebView.loadUrl(URL1);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mWebView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mWebView.restoreState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The menu with 3 items.
        // When selecting the menu item the WebView loads the selected URL.
        int id = item.getItemId();

        switch (id) {
            case R.id.gymglish:
                mWebView.loadUrl(URL1);
                return true;
            case R.id.frantastique:
                mWebView.loadUrl(URL2);
                return true;
            case R.id.vatefaireconjuguer:
                mWebView.loadUrl(URL3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // A inner class to control the progress bar and the redirection
    class MyWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);
            if (mProgressBar.getVisibility() == View.VISIBLE) {
                mProgressBar.setProgress(view.getProgress());
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            super.onScaleChanged(view, oldScale, newScale);
        }
    }
}
