package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import com.appcoins.sdk.billing.listeners.payasguest.ActivityResultListener;

interface IabView {

  void close(boolean withError);

  void finishWithError();

  void showAlertNoBrowserAndStores();

  void redirectToWalletInstallation(Intent intent);

  void navigateToAdyen(String selectedRadioButton, String walletAddress, String signature,
      String fiatPrice, String fiatPriceCurrencyCode, String appcPrice, String sku);

  void startIntentSenderForResult(IntentSender intentSender, int requestCode);

  void lockRotation();

  void unlockRotation();

  void navigateToUri(String url, String uid);

  void finish(Bundle bundle);

  void navigateToPaymentSelection();

  void navigateToInstallDialog();

  void disableBack();

  void enableBack();

  void setOnActivityResultListener(ActivityResultListener activityResultListener);

  void redirectToSupportEmail(EmailInfo emailInfo);

  boolean hasEmailApplication();
}
