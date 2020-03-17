package com.appcoins.sdk.billing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.appcoins.sdk.billing.utils.LayoutUtils;

public class WebViewActivity extends Activity implements WebViewView {

  public static final int SUCCESS = 1;
  public static final int FAIL = 0;
  private static final String CURRENT_URL = "current_url";
  private static final String URL = "url";
  private String currentUrl;

  public static Intent newIntent(Activity activity, String url) {
    Intent intent = new Intent(activity, WebViewActivity.class);
    intent.putExtra(URL, url);
    return intent;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);

    RelativeLayout mainLayout = new RelativeLayout(this);
    mainLayout.setLayoutParams(layoutParams);
    WebView webView = new WebView(this);
    LayoutUtils.setMargins(layoutParams, 8, 8, 8, 8);
    webView.setLayoutParams(layoutParams);
    mainLayout.addView(webView);

    setContentView(mainLayout);
    lockCurrentPosition();
    if (savedInstanceState == null) {
      currentUrl = getIntent().getStringExtra(URL);
      CookieManager.getInstance()
          .setAcceptCookie(true);
    } else {
      currentUrl = savedInstanceState.getString(CURRENT_URL);
    }
    webView.setWebViewClient(new WebViewClientImpl(this));

    WebSettings webSettings = webView.getSettings();
    webSettings.setDomStorageEnabled(true);
    webSettings.setJavaScriptEnabled(true);

    webView.loadUrl(currentUrl);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(CURRENT_URL, currentUrl);
  }

  @Override public void finishWithSuccess(Intent intent) {
    setResult(WebViewActivity.SUCCESS, intent);
    finish();
  }

  @Override public void setCurrentUrl(String currentUrl) {
    this.currentUrl = currentUrl;
  }

  @SuppressLint("SourceLockedOrientationActivity") private void lockCurrentPosition() {
    //setRequestedOrientation requires translucent and floating to be false to work in API 26
    int orientation = getWindowManager().getDefaultDisplay()
        .getRotation();
    switch (orientation) {
      case Surface.ROTATION_0:
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        break;
      case Surface.ROTATION_90:
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        break;
      case Surface.ROTATION_180:
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        break;
      case Surface.ROTATION_270:
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
        break;
      default:
        Log.w("WebView", "Invalid orientation value: " + orientation);
        break;
    }
  }
}
