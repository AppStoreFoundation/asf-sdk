package com.appcoins.sdk.billing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import com.appcoins.sdk.billing.utils.LayoutUtils;

import static com.appcoins.sdk.billing.utils.LayoutUtils.generateRandomId;

public class WebViewActivity extends Activity {

  public static final int SUCCESS = 1;
  public static final int FAIL = 0;
  private static final String ADYEN_PAYMENT_SCHEMA = "adyencheckout://";
  private static final String CURRENT_URL = "current_url";
  private static final String URL = "url";
  private static int WEB_VIEW_ACTIVITY_MAIN_LAYOUT_ID = 2;
  private static int WEB_VIEW_ID = 3;
  private String currentUrl;

  public static Intent newIntent(Activity activity, String url) {
    Intent intent = new Intent(activity, WebViewActivity.class);
    intent.putExtra(URL, url);
    return intent;
  }

  @SuppressLint("ResourceType") @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);

    RelativeLayout mainLayout = new RelativeLayout(this);
    WEB_VIEW_ACTIVITY_MAIN_LAYOUT_ID = generateRandomId(WEB_VIEW_ACTIVITY_MAIN_LAYOUT_ID);
    mainLayout.setId(WEB_VIEW_ACTIVITY_MAIN_LAYOUT_ID);
    mainLayout.setLayoutParams(layoutParams);
    WebView webView = new WebView(this);
    WEB_VIEW_ID = generateRandomId(WEB_VIEW_ID);
    webView.setId(WEB_VIEW_ID);
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
    webView.setWebViewClient(new WebViewClient() {

      @Override public boolean shouldOverrideUrlLoading(WebView view, String clickUrl) {
        if (clickUrl.contains(ADYEN_PAYMENT_SCHEMA)) {
          currentUrl = clickUrl;
          Intent intent = new Intent();
          intent.setData(Uri.parse(clickUrl));
          setResult(WebViewActivity.SUCCESS, intent);
          finish();
        } else {
          currentUrl = clickUrl;
          return false;
        }
        return true;
      }

      @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        return super.shouldOverrideUrlLoading(view, request);
      }

      @Override public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
      }

      @Override
      public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
      }
    });

    WebSettings webSettings = webView.getSettings();
    webSettings.setDomStorageEnabled(true);
    webSettings.setJavaScriptEnabled(true);

    webView.loadUrl(currentUrl);
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(CURRENT_URL, currentUrl);
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
