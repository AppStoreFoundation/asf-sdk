package com.appcoins.sdk.billing;

import android.content.Intent;

interface WebViewView {

  void finishWithSuccess(Intent intent);

  void setCurrentUrl(String currentUrl);
}
