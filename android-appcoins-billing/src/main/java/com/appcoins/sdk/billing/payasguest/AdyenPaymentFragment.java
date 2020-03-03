package com.appcoins.sdk.billing.payasguest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;
import com.appcoins.sdk.billing.layouts.AdyenPaymentFragmentLayout;
import com.appcoins.sdk.billing.layouts.CardNumberEditText;
import com.appcoins.sdk.billing.layouts.FieldValidationListener;
import com.appcoins.sdk.billing.models.StoredMethodDetails;
import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.Service;
import com.appcoins.sdk.billing.service.adyen.AdyenListenerProvider;
import com.appcoins.sdk.billing.service.adyen.AdyenMapper;
import com.appcoins.sdk.billing.service.adyen.AdyenRepository;
import com.sdk.appcoins_adyen.models.ExpiryDate;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;
import com.sdk.appcoins_adyen.utils.RedirectUtils;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AdyenPaymentFragment extends Fragment implements AdyenPaymentView {

  private static final String CARD_NUMBER_KEY = "credit_card";
  private static final String EXPIRY_DATE_KEY = "expiry_date";
  private static final String CVV_KEY = "cvv_key";
  private IabView iabView;
  private AdyenPaymentInfo adyenPaymentInfo;
  private AdyenPaymentPresenter presenter;
  private AdyenPaymentFragmentLayout layout;
  private String serverCurrency;
  private String storedPaymentId = "";
  private BigDecimal serverFiatPrice;

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
    AdyenRepository adyenRepository =
        new AdyenRepository(new BdsService(BuildConfig.HOST_WS + "/broker/"),
            new AdyenListenerProvider(new AdyenMapper()));
    Service apiService = new BdsService(BuildConfig.HOST_WS);
    Service ws75Service = new BdsService(BuildConfig.BDS_BASE_HOST);
    IExtractOemId extractorV1 = new OemIdExtractorV1(getActivity().getApplicationContext());

    BillingRepository billingRepository = new BillingRepository(apiService);

    AddressService addressService = new AddressService(getActivity().getApplicationContext(),
        new WalletAddressService(apiService), new DeveloperAddressService(ws75Service),
        Build.MANUFACTURER, Build.MODEL, new OemIdExtractorService(extractorV1));

    presenter = new AdyenPaymentPresenter(this, adyenPaymentInfo,
        new AdyenPaymentInteract(adyenRepository, billingRepository, addressService),
        RedirectUtils.getReturnUrl(getActivity().getApplicationContext()));
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
    Button positiveButton = layout.getPositiveButton();
    setFieldChangeListener(positiveButton);
    if (savedInstanceState != null) {
      onSavedInstance(savedInstanceState);

      presenter.onSavedInstance(savedInstanceState);
    }
    handleLayoutVisibility(adyenPaymentInfo.getPaymentMethod());
    Button cancelButton = layout.getCancelButton();
    Button errorButton = layout.getPaymentErrorViewLayout()
        .getErrorPositiveButton();
    TextView changeCardView = layout.getChangeCardView();
    TextView morePaymentsText = layout.getMorePaymentsText();
    onPositiveButtonClick(positiveButton);
    onCancelButtonClick(cancelButton);
    onErrorButtonClick(errorButton);
    onChangeCardClick(changeCardView);
    onMorePaymentsClick(morePaymentsText);

    presenter.loadPaymentInfo();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    setRotationSavedCardValues(outState);
    presenter.onSaveInstanceState(outState);
  }

  @Override public void onDestroyView() {
    layout.onDestroyView();
    layout = null;
    super.onDestroyView();
  }

  @Override public void onDestroy() {
    presenter.onDestroy();
    presenter = null;
    super.onDestroy();
  }

  private void onSavedInstance(Bundle savedInstanceState) {
    layout.getCardNumberEditText()
        .setText(savedInstanceState.getString(CARD_NUMBER_KEY, ""));
    String expiryDate = savedInstanceState.getString(EXPIRY_DATE_KEY, "");
    if (!expiryDate.equals("")) {
      EditText expiryEditText = layout.getExpiryDateEditText();
      expiryEditText.setVisibility(View.VISIBLE);
      expiryEditText.setText(expiryDate);
    }
    String cvv = savedInstanceState.getString(CVV_KEY, "");
    if (!cvv.equals("")) {
      EditText cvvEditText = layout.getCvvEditText();
      cvvEditText.setVisibility(View.VISIBLE);
      cvvEditText.setText(savedInstanceState.getString(CVV_KEY, ""));
    }
  }

  private void setRotationSavedCardValues(Bundle outState) {
    CardNumberEditText cardNumberEditText = layout.getCardNumberEditText();
    if (CardValidationUtils.isShortenCardNumber(cardNumberEditText.getText()
        .toString())) {
      outState.putString(CARD_NUMBER_KEY, cardNumberEditText.getCacheSavedNumber());
    } else {
      outState.putString(CARD_NUMBER_KEY, cardNumberEditText.getText()
          .toString());
    }
    outState.putString(EXPIRY_DATE_KEY, layout.getExpiryDateEditText()
        .getText()
        .toString());
    outState.putString(CVV_KEY, layout.getCvvEditText()
        .getText()
        .toString());
  }

  private void setFieldChangeListener(final Button positiveButton) {
    layout.getCreditCardEditTextLayout()
        .setFieldValidationListener(new FieldValidationListener() {
          @Override public void onFieldChanged(boolean isCardNumberValid, boolean isExpiryDateValid,
              boolean isCvvValid, String paymentId) {
            if ((isCardNumberValid && isExpiryDateValid && isCvvValid)
                || !paymentId.equals("") && isCvvValid) {
              hideKeyboard();
              positiveButton.setEnabled(true);
            } else {
              positiveButton.setEnabled(false);
            }
          }
        });
  }

  @Override public void close() {
    iabView.close();
  }

  @Override public void showError() {
    layout.getDialogLayout()
        .setVisibility(View.INVISIBLE);
    layout.getPaypalLoading()
        .setVisibility(View.INVISIBLE);
    layout.getErrorView()
        .setVisibility(View.VISIBLE);
  }

  @Override public void showLoading() {
    layout.getPaypalLoading()
        .setVisibility(View.VISIBLE);
    layout.getErrorView()
        .setVisibility(View.INVISIBLE);
    layout.getDialogLayout()
        .setVisibility(View.INVISIBLE);
  }

  @Override public void updateFiatPrice(BigDecimal value, String currency) {
    serverFiatPrice = value;
    serverCurrency = currency;
    String fiatPrice = new Formatter().format(Locale.getDefault(), "%(,.2f", value.doubleValue())
        .toString();
    layout.getFiatPriceView()
        .setText(String.format("%s %s", fiatPrice, currency));
  }

  @Override public void showCreditCardView(StoredMethodDetails storedMethodDetails) {
    layout.getPaypalLoading()
        .setVisibility(View.INVISIBLE);
    layout.getErrorView()
        .setVisibility(View.INVISIBLE);
    layout.getDialogLayout()
        .setVisibility(View.VISIBLE);
    if (storedMethodDetails != null) {
      setStoredPaymentMethodDetails(storedMethodDetails);
    }
  }

  @Override public void lockRotation() {
    iabView.lockRotation();
  }

  @Override public void unlockRotation() {
    iabView.unlockRotation();
  }

  @Override public void navigateToUri(String url, ActivityResultListener activityResultListener) {
    iabView.navigateToUri(url, activityResultListener);
  }

  @Override public void finish(Bundle bundle) {
    iabView.finish(bundle);
  }

  @Override public void navigateToPaymentSelection() {
    iabView.navigateToPaymentSelection();
  }

  private void setStoredPaymentMethodDetails(StoredMethodDetails storedMethodDetails) {
    layout.getChangeCardView()
        .setVisibility(View.VISIBLE);
    CardNumberEditText cardNumberEditText = layout.getCardNumberEditText();
    cardNumberEditText.setText(String.format("••••%s", storedMethodDetails.getCardNumber()));
    cardNumberEditText.setStoredCard(true);
    cardNumberEditText.setEnabled(false);
    EditText expiryText = layout.getExpiryDateEditText();
    ExpiryDate expiryDate =
        new ExpiryDate(storedMethodDetails.getExpiryMonth(), storedMethodDetails.getExpiryYear());
    setDate(expiryDate, expiryText);
    expiryText.setVisibility(View.VISIBLE);
    expiryText.setEnabled(false);
    EditText editText = layout.getCvvEditText();
    editText.setVisibility(View.VISIBLE);
    editText.requestFocus();
    layout.getCreditCardEditTextLayout()
        .setStoredPaymentId(storedMethodDetails.getPaymentId());
  }

  private AdyenPaymentInfo extractBundleInfo() {
    String paymentMethod = getBundleString(IabActivity.PAYMENT_METHOD_KEY);
    String walletAddress = getBundleString(IabActivity.WALLET_ADDRESS_KEY);
    String ewt = getBundleString(IabActivity.EWT_KEY);
    String signature = getBundleString(IabActivity.SIGNATURE_KEY);
    String fiatPrice = getBundleString(IabActivity.FIAT_VALUE_KEY);
    String fiatCurrency = getBundleString(IabActivity.FIAT_CURRENCY_KEY);
    String appcPrice = getBundleString(IabActivity.APPC_VALUE_KEY);
    BuyItemProperties buyItemProperties =
        getBundleBuyItemProperties(AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    return new AdyenPaymentInfo(paymentMethod, walletAddress, ewt, signature, fiatPrice,
        fiatCurrency, appcPrice, buyItemProperties);
  }

  private void attach(Context context) {
    if (!(context instanceof IabView)) {
      throw new IllegalStateException("AdyenPaymentFragment must be attached to IabActivity");
    }
    iabView = (IabView) context;
  }

  private void handleLayoutVisibility(String paymentMethod) {
    if (paymentMethod.equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      layout.getButtonsView()
          .setVisibility(View.VISIBLE);
    } else if (paymentMethod.equals(PaymentMethodsFragment.PAYPAL_RADIO)) {
      layout.getDialogLayout()
          .setVisibility(View.INVISIBLE);
      layout.getPaypalLoading()
          .setVisibility(View.VISIBLE);
    }
  }

  private void onMorePaymentsClick(TextView morePaymentsText) {
    morePaymentsText.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        presenter.onMorePaymentsClick();
      }
    });
  }

  private void onChangeCardClick(TextView changeCardView) {
    changeCardView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        presenter.onChangeCardClick();
      }
    });
  }

  private void onCancelButtonClick(Button cancelButton) {
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        presenter.onCancelClick();
      }
    });
  }

  private void onPositiveButtonClick(Button positiveButton) {
    positiveButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        String cardNumber = layout.getCardNumberEditText()
            .getCacheSavedNumber();
        String expiryDate = layout.getExpiryDateEditText()
            .getText()
            .toString();
        String cvv = layout.getCvvEditText()
            .getText()
            .toString();
        String paymentId = layout.getCreditCardEditTextLayout()
            .getStoredPaymentId();
        presenter.onPositiveClick(cardNumber, expiryDate, cvv, paymentId, serverFiatPrice,
            serverCurrency);
      }
    });
  }

  private void onErrorButtonClick(Button errorButton) {
    errorButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        presenter.onErrorButtonClick();
      }
    });
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

  private void hideKeyboard() {
    InputMethodManager inputMethodManager =
        (InputMethodManager) getActivity().getApplicationContext()
            .getSystemService(Context.INPUT_METHOD_SERVICE);
    if (inputMethodManager != null) {
      View view = getView();
      if (view != null) {
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
      }
    }
  }

  private void setDate(ExpiryDate expiryDate, EditText editText) {
    SimpleDateFormat simpleDateFormat =
        new SimpleDateFormat(CardValidationUtils.DATE_FORMAT, Locale.ROOT);
    if (expiryDate != null && expiryDate != ExpiryDate.EMPTY_DATE) {
      final Calendar calendar = GregorianCalendar.getInstance();
      calendar.clear();
      // first day of month, GregorianCalendar month is 0 based.
      calendar.set(expiryDate.getExpiryYear(), expiryDate.getExpiryMonth() - 1, 1);
      editText.setText(simpleDateFormat.format(calendar.getTime()));
    } else {
      editText.setText("");
    }
  }
}
