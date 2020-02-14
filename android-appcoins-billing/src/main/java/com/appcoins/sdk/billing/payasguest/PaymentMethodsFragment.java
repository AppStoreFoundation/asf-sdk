package com.appcoins.sdk.billing.payasguest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SharedPreferencesRepository;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;
import com.appcoins.sdk.billing.helpers.WalletInstallationIntentBuilder;
import com.appcoins.sdk.billing.layouts.PaymentMethodsFragmentLayout;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class PaymentMethodsFragment extends Fragment implements PaymentMethodsView {

  private WalletInteract walletInteract;
  private IabView iabView;
  private BuyItemProperties buyItemProperties;
  private PaymentMethodsPresenter paymentMethodsPresenter;
  private PaymentMethodsFragmentLayout layout;
  private String selectedRadioButton;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    attach(context);
  }

  @Override public void onAttach(Activity activity) {
    super.onAttach(activity);
    attach(activity);
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    walletInteract = new WalletInteract(new SharedPreferencesRepository(getActivity()));
    buyItemProperties = (BuyItemProperties) getArguments().getSerializable(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);
    paymentMethodsPresenter = new PaymentMethodsPresenter(this, walletInteract,
        new WalletInstallationIntentBuilder(getActivity().getPackageManager(),
            getActivity().getPackageName()));
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    layout = new PaymentMethodsFragmentLayout(getActivity(),
        getResources().getConfiguration().orientation, buyItemProperties);

    return layout.build();
  }

  @SuppressLint("ResourceType") @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Button cancelButton = layout.getCancelButton();
    Button positiveButton = layout.getBuyButton();
    paymentMethodsPresenter.onCancelButtonClicked(cancelButton);
    paymentMethodsPresenter.onPositiveButtonClicked(positiveButton, selectedRadioButton);
    paymentMethodsPresenter.requestWallet();
    paymentMethodsPresenter.provideSkuDetailsInformation(buyItemProperties);
  }

  @Override public void setSkuInformation(String fiatPrice, String currencyCode, String appcPrice,
      String sku) {
    TextView fiatPriceView = layout.getFiatPriceView();
    TextView appcPriceView = layout.getAppcPriceView();
    DecimalFormat df = new DecimalFormat("0.00");
    String fiatText = df.format(new BigDecimal(fiatPrice));
    String appcText = df.format(new BigDecimal(appcPrice));
    fiatPriceView.setText(String.format("%s %s", fiatText, currencyCode));
    appcPriceView.setText(String.format("%s %s", appcText, "APPC"));
  }

  @Override public void showError() {
    Log.d("TAG123", "ERROR");
  }

  @Override public void close() {
    iabView.close();
  }

  @Override public void showAlertNoBrowserAndStores() {
    iabView.showAlertNoBrowserAndStores();
  }

  @Override public void redirectToWalletInstallation(Intent intent) {
    iabView.redirectToWalletInstallation(intent);
  }

  @Override public void navigateToAdyen(String selectedRadioButton) {
    iabView.navigateToAdyen(selectedRadioButton);
  }

  private void attach(Context context) {
    if (!(context instanceof IabView)) {
      throw new IllegalStateException("PaymentMethodsFragment must be attached to IabActivity");
    }
    iabView = (IabView) context;
  }
}
