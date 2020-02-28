package com.appcoins.sdk.billing.payasguest;

import android.content.Intent;
import com.appcoins.sdk.billing.helpers.TranslationsModel;

interface IabView {

  TranslationsModel getTranslationsModel();

  void close();

  void showAlertNoBrowserAndStores();

  void redirectToWalletInstallation(Intent intent);

  void navigateToAdyen(String selectedRadioButton);
}
