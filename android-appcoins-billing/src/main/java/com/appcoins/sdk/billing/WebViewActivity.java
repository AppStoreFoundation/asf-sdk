package com.appcoins.sdk.billing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import com.appcoins.sdk.billing.utils.LayoutUtils;

public class WebViewActivity extends Activity implements WebViewView {

  public static final int SUCCESS = 1;
  public static final int FAIL = 0;
  public static final String TRANSACTION_ID = "id";
  private static final String CURRENT_URL = "current_url";
  private static final String SAVED_ID = "saved_id";
  private static final String URL = "url";
  private String currentUrl;
  private String uid;

  public static Intent newIntent(Activity activity, String url, String uid) {
    Intent intent = new Intent(activity, WebViewActivity.class);
    intent.putExtra(URL, url);
    intent.putExtra(TRANSACTION_ID, uid);
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
    if (savedInstanceState == null) {
      currentUrl = getIntent().getStringExtra(URL);
      uid = getIntent().getStringExtra(TRANSACTION_ID);
      CookieManager.getInstance()
          .setAcceptCookie(true);
    } else {
      currentUrl = savedInstanceState.getString(CURRENT_URL);
      uid = savedInstanceState.getString(SAVED_ID);
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
    outState.putString(SAVED_ID, uid);
  }

  @Override public void finishWithSuccess(Intent intent) {
    intent.putExtra(TRANSACTION_ID, uid);
    setResult(WebViewActivity.SUCCESS, intent);
    finish();
  }

  @Override public void setCurrentUrl(String currentUrl) {
    this.currentUrl = currentUrl;
  }
}
