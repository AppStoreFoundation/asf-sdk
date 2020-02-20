package com.appcoins.sdk.billing.payasguest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;
import com.appcoins.sdk.billing.layouts.AdyenPaymentFragmentLayout;

public class AdyenPaymentFragment extends Fragment implements AdyenPaymentView {

  private IabView iabView;
  private AdyenPaymentInfo adyenPaymentInfo;
  private AdyenPaymentPresenter adyenPaymentPresenter;
  private AdyenPaymentFragmentLayout layout;

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

    adyenPaymentInfo = extractBundleInfo();
    adyenPaymentPresenter =
        new AdyenPaymentPresenter(this, adyenPaymentInfo, new AdyenPaymentInteract());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    layout = new AdyenPaymentFragmentLayout(getActivity(),
        getResources().getConfiguration().orientation);
    BuyItemProperties buyItemProperties = adyenPaymentInfo.getBuyItemProperties();
    return layout.build(adyenPaymentInfo.getFiatPrice(), adyenPaymentInfo.getFiatCurrency(),
        adyenPaymentInfo.getAppcPrice(), buyItemProperties.getSku(),
        buyItemProperties.getPackageName());
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    if (adyenPaymentInfo.getPaymentMethod()
        .equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      layout.getButtonsView()
          .setVisibility(View.VISIBLE);
    }
  }

  @Override public void onDestroyView() {
    layout = null;
    super.onDestroyView();
  }

  private AdyenPaymentInfo extractBundleInfo() {
    String paymentMethod = getBundleString(IabActivity.PAYMENT_METHOD_KEY);
    String walletAddress = getBundleString(IabActivity.WALLET_ADDRESS_KEY);
    String ewt = getBundleString(IabActivity.EWT_KEY);
    String fiatPrice = getBundleString(IabActivity.FIAT_VALUE_KEY);
    String fiatCurrency = getBundleString(IabActivity.FIAT_CURRENCY_KEY);
    String appcPrice = getBundleString(IabActivity.APPC_VALUE_KEY);
    BuyItemProperties buyItemProperties =
        getBundleBuyItemProperties(AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    return new AdyenPaymentInfo(paymentMethod, walletAddress, ewt, fiatPrice, fiatCurrency,
        appcPrice, buyItemProperties);
  }

  private void attach(Context context) {
    if (!(context instanceof IabView)) {
      throw new IllegalStateException("AdyenPaymentFragment must be attached to IabActivity");
    }
    iabView = (IabView) context;
  }

  private String getBundleString(String key) {
    if (getArguments().containsKey(key)) {
      return getArguments().getString(key);
    }
    throw new IllegalArgumentException(key + "data not found");
  }

  private BuyItemProperties getBundleBuyItemProperties(String key) {
    if (getArguments().containsKey(key)) {
      return (BuyItemProperties) getArguments().getSerializable(key);
    }
    throw new IllegalArgumentException(key + "data not found");
  }
}
