package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import android.content.IntentSender;
import com.appcoins.sdk.billing.helpers.TranslationsModel;

interface IabView {

  TranslationsModel getTranslationsModel();

  void close();

  void finishWithError();

  void showAlertNoBrowserAndStores();

  void redirectToWalletInstallation(Intent intent);

  void navigateToAdyen(String selectedRadioButton, String walletAddress, String ewt,
      String signature, String fiatPrice, String fiatPriceCurrencyCode, String appcPrice,
      String sku);

  void startIntentSenderForResult(IntentSender intentSender, int requestCode);

  void lockRotation();

  void unlockRotation();

  void navigateToUri(String url, ActivityResultListener activityResultListener);
}
