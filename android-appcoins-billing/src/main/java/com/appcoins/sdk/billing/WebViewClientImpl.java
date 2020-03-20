package com.appcoins.sdk.billing;

import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewClientImpl extends WebViewClient {

  private static final String ADYEN_PAYMENT_SCHEMA = "adyencheckout://";
  private WebViewView webViewView;

  WebViewClientImpl(WebViewView webViewView) {
    super();
    this.webViewView = webViewView;
  }

  @Override public boolean shouldOverrideUrlLoading(WebView view, String clickUrl) {
    if (clickUrl.contains(ADYEN_PAYMENT_SCHEMA)) {
      webViewView.setCurrentUrl(clickUrl);
      Intent intent = new Intent();
      intent.setData(Uri.parse(clickUrl));
      webViewView.finishWithSuccess(intent);
    } else {
      webViewView.setCurrentUrl(clickUrl);
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

  @Override public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
    handler.proceed();
  }
}
