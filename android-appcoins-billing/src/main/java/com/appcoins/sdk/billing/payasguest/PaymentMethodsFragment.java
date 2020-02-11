package com.appcoins.sdk.billing.payasguest;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.SharedPreferencesRepository;
import com.appcoins.sdk.billing.WalletInteract;
import com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper;

public class PaymentMethodsFragment extends Fragment implements PaymentMethodsView {

  private WalletInteract walletInteract;
  private IabView iabView;
  private BuyItemProperties buyItemProperties;
  private PaymentMethodsPresenter paymentMethodsPresenter;
  private int orientation;

  @Override public void onAttach(Context context) {
    super.onAttach(context);
    if (!(context instanceof IabView)) {
      throw new IllegalStateException("PaymentMethodsFragment must be attached to IabActivity");
    }
    iabView = (IabView) context;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    walletInteract = new WalletInteract(new SharedPreferencesRepository(getActivity()));
    buyItemProperties = (BuyItemProperties) getArguments().getSerializable(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);
    paymentMethodsPresenter = new PaymentMethodsPresenter(this, walletInteract);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {

    orientation = getResources().getConfiguration().orientation;

    return createLayout(orientation);
  }

  @SuppressLint("ResourceType") @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    paymentMethodsPresenter.requestWallet();
    paymentMethodsPresenter.provideSkuDetailsInformation(buyItemProperties);
  }

  @Override public void setSkuInformation(String fiatPrice, String currencyCode, String appcPrice,
      String sku) {
    Log.d("TAG123", "VIEW: " + fiatPrice + " " + currencyCode + " : " + appcPrice + " : " + sku);
  }

  @Override public void showError() {
    Log.d("TAG123", "ERROR");
  }

  private RelativeLayout createLayout(int orientation) {
    RelativeLayout layout;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      layout = buildMainLayout();
      RelativeLayout dialogLayout = buildDialogLayout();
      layout.addView(dialogLayout);
    } else {
      layout = buildMainLayout();
    }
    return layout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildPaymentMethodsHeaderLayout() {
    RelativeLayout paymentMethodHeaderLayout = new RelativeLayout(getActivity());
    paymentMethodHeaderLayout.setLayoutParams(
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
    paymentMethodHeaderLayout.setId(100);
    ImageView iconImageView;
    TextView appNameView;
    Drawable icon = null;
    String appName = "";
    PackageManager packageManager = getActivity().getApplicationContext()
        .getPackageManager();
    try {
      icon = packageManager.getApplicationIcon(buyItemProperties.getPackageName());
      appName = packageManager.getApplicationInfo(buyItemProperties.getPackageName(), 0).name;
    } catch (Exception e) {
      e.printStackTrace();
    }
    iconImageView = createAppIconLayout(icon);
    appNameView = createAppNameLayout(appName);
    TextView skuView = createSkuLayout(buyItemProperties.getSku());
    TextView fiatPriceView = createFiatPriceView();
    TextView appcPriceView = createAppcPriceView();
    paymentMethodHeaderLayout.addView(iconImageView);
    paymentMethodHeaderLayout.addView(appNameView);
    paymentMethodHeaderLayout.addView(skuView);
    paymentMethodHeaderLayout.addView(fiatPriceView);
    paymentMethodHeaderLayout.addView(appcPriceView);
    return paymentMethodHeaderLayout;
  }

  @SuppressLint("ResourceType") private TextView createAppcPriceView() {
    TextView textView = new TextView(getActivity());
    textView.setId(10);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(0, 0, dpToPx(16), 0);
    layoutParams.addRule(RelativeLayout.BELOW, 11);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
    } else {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }
    textView.setTextColor(Color.parseColor("#828282"));
    textView.setTextSize(12);
    textView.setText("DEBUG");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private TextView createFiatPriceView() {
    TextView textView = new TextView(getActivity());
    textView.setId(11);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(0, dpToPx(17), dpToPx(16), 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
    } else {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    }
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(15);
    textView.setText("DEBUG");
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private TextView createSkuLayout(String sku) {
    TextView textView = new TextView(getActivity());
    textView.setId(9);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, 8);
    layoutParams.setMargins(dpToPx(10), 0, dpToPx(12), 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.START_OF, 10);
    } else {
      layoutParams.addRule(RelativeLayout.LEFT_OF, 10);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.END_OF, 7);
      layoutParams.setMarginStart(dpToPx(10));
    } else {
      layoutParams.addRule(RelativeLayout.RIGHT_OF, 7);
    }
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTextColor(Color.parseColor("#8a000000"));
    textView.setTextSize(12);
    textView.setText(sku);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private TextView createAppNameLayout(String appName) {
    TextView textView = new TextView(getActivity());
    textView.setId(8);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(dpToPx(10), dpToPx(15), dpToPx(12), 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.START_OF, 11);
    } else {
      layoutParams.addRule(RelativeLayout.LEFT_OF, 11);
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.END_OF, 7);
      layoutParams.setMarginStart(dpToPx(10));
    } else {
      layoutParams.addRule(RelativeLayout.RIGHT_OF, 7);
    }
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTextColor(Color.parseColor("#de000000"));
    textView.setTextSize(16);
    if (appName == null) {
      appName = "DEBUG";
    }
    textView.setText(appName);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private ImageView createAppIconLayout(Drawable icon) {
    ImageView imageView = new ImageView(getActivity());
    imageView.setId(7);
    if (icon != null) {
      imageView.setImageDrawable(icon);
    }
    RelativeLayout.LayoutParams imageParams =
        new RelativeLayout.LayoutParams(dpToPx(48), dpToPx(48));
    imageParams.setMargins(dpToPx(12), dpToPx(12), 0, 0);
    imageView.setLayoutParams(imageParams);
    return imageView;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildMainLayout() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(getActivity());
    backgroundLayout.setId(5);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildDialogLayout() {
    RelativeLayout dialogLayout = new RelativeLayout(getActivity());
    dialogLayout.setClipToPadding(false);
    dialogLayout.setId(6);

    dialogLayout.setBackgroundColor(Color.WHITE);

    RelativeLayout.LayoutParams dialogLayoutParams =
        new RelativeLayout.LayoutParams(dpToPx(340), ViewGroup.LayoutParams.WRAP_CONTENT);
    dialogLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    dialogLayout.setLayoutParams(dialogLayoutParams);

    RelativeLayout paymentMethodsHeaderLayout = buildPaymentMethodsHeaderLayout();
    View headerSeparator = buildHeaderSeparatorLayout();
    RelativeLayout paymentMethodsLayout = buildPaymentMethodsLayout();
    LinearLayout buttonsView = buildButtonsView();

    dialogLayout.addView(paymentMethodsHeaderLayout);
    dialogLayout.addView(headerSeparator);
    dialogLayout.addView(paymentMethodsLayout);
    dialogLayout.addView(buttonsView);
    return dialogLayout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildPaymentMethodsLayout() {
    RelativeLayout parentLayout = new RelativeLayout(getActivity());
    parentLayout.setId(13);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, 14);
    parentLayout.setLayoutParams(layoutParams);

    TextView payAsGuestView = buildPayAsGuestTextView();
    RadioGroup radioGroup = buildRadioGroupView();

    parentLayout.addView(payAsGuestView);
    parentLayout.addView(radioGroup);

    return parentLayout;
  }

  private LinearLayout buildButtonsView() {
    LinearLayout linearLayout = new LinearLayout(getActivity());
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, 13);
    layoutParams.setMargins(dpToPx(12), dpToPx(24), 0, dpToPx(24));
    linearLayout.setGravity(Gravity.END);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setClipChildren(false);
    linearLayout.setClipToPadding(false);
    linearLayout.setLayoutParams(layoutParams);

    Button cancelButton = buildCancelButtonLayout();
    Button buyButton = buildBuyButtonLayout();

    linearLayout.addView(cancelButton);
    linearLayout.addView(buyButton);
    return linearLayout;
  }

  private Button buildBuyButtonLayout() {
    Button button = new Button(getActivity());
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(32));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.setMarginEnd(16);
    } else {
      layoutParams.setMargins(0, 0, dpToPx(16), 0);
    }
    int[] gradientColors = { Color.parseColor("#FC9D48"), Color.parseColor("#FF578C") };
    GradientDrawable background =
        new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#00000000"));
    background.setCornerRadius(dpToPx(16));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackgroundDrawable(background);

    button.setMaxWidth(dpToPx(126));
    button.setMinWidth(dpToPx(80));

    button.setPadding(0, 0, dpToPx(4), 0);
    button.setTextColor(Color.WHITE);
    button.setTextSize(14);
    button.setText("NEXT".toUpperCase());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private Button buildCancelButtonLayout() {
    Button button = new Button(getActivity());
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(32));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#00000000"));
    background.setCornerRadius(dpToPx(6));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackgroundDrawable(background);
    button.setMaxWidth(dpToPx(126));
    button.setMinWidth(dpToPx(80));

    button.setPadding(0, 0, dpToPx(4), 0);
    button.setTextColor(Color.parseColor("#8a000000"));
    button.setTextSize(14);
    button.setText("Cancel".toUpperCase());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private RadioGroup buildRadioGroupView() {
    RadioGroup radioGroup = new RadioGroup(getActivity());
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, 15);
    layoutParams.setMargins(dpToPx(16), dpToPx(12), dpToPx(16), 0);
    radioGroup.setLayoutParams(layoutParams);

    RelativeLayout creditCardWrapperLayout = buildCreditCardWrapperLayout();
    RelativeLayout paypalWrapperLayout = buildPaypalWrapperLayout();
    RelativeLayout installWrapperLayout = buildInstallWrapperLayout();

    radioGroup.addView(creditCardWrapperLayout);
    radioGroup.addView(paypalWrapperLayout);
    radioGroup.addView(installWrapperLayout);

    return radioGroup;
  }

  private RelativeLayout buildInstallWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(getActivity());
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);

    layoutParams.setMargins(0, dpToPx(12), 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView installImage1 = buildInstallImage1();
    ImageView installImage2 = buildInstallImage2();
    TextView installTextView1 = buildInstallTextView1();
    TextView installTextView2 = buildInstallTextView2();
    RadioButton radioButton = buildRadioButton();

    relativeLayout.addView(installImage1);
    relativeLayout.addView(installImage2);
    relativeLayout.addView(installTextView1);
    relativeLayout.addView(installTextView2);
    relativeLayout.addView(radioButton);
    return relativeLayout;
  }

  @SuppressLint("ResourceType") private TextView buildInstallTextView1() {
    TextView textView = new TextView(getActivity());
    textView.setId(22);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(dpToPx(10), dpToPx(12), 0, 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.END_OF, 21);
      layoutParams.setMarginStart(dpToPx(10));
    } else {
      layoutParams.addRule(RelativeLayout.RIGHT_OF, 21);
      layoutParams.setMargins(dpToPx(10), dpToPx(12), 0, 0);
    }
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Using the AppCoins Wallet");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView buildInstallTextView2() {
    TextView textView = new TextView(getActivity());
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    layoutParams.addRule(RelativeLayout.BELOW, 22);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.ALIGN_START, 22);
    } else {
      layoutParams.addRule(RelativeLayout.ALIGN_LEFT, 22);
    }
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#FA6249"));
    textView.setTextSize(10);
    textView.setText("Get up to 30% bonus");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private ImageView buildInstallImage1() {
    ImageView imageView = new ImageView(getActivity());
    imageView.setId(20);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(24), dpToPx(24));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = getActivity().getPackageManager()
          .getApplicationIcon(buyItemProperties.getPackageName());
      imageView.setImageDrawable(icon);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    layoutParams.setMargins(dpToPx(8), 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  @SuppressLint("ResourceType") private ImageView buildInstallImage2() {
    ImageView imageView = new ImageView(getActivity());
    imageView.setId(21);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(20), dpToPx(20));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = getActivity().getPackageManager()
          .getApplicationIcon(buyItemProperties.getPackageName());
      imageView.setImageDrawable(icon);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.END_OF, 20);
      layoutParams.setMarginStart(dpToPx(1));
    } else {
      layoutParams.addRule(RelativeLayout.RIGHT_OF, 20);
      layoutParams.setMargins(dpToPx(1), 0, 0, 0);
    }
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildPaypalWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(getActivity());
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);
    layoutParams.setMargins(0, dpToPx(12), 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView paypalImage = buildPaypalImage();
    TextView paypalTextView = buildPaypalTextView();
    RadioButton radioButton = buildRadioButton();

    relativeLayout.addView(paypalImage);
    relativeLayout.addView(paypalTextView);
    relativeLayout.addView(radioButton);
    return relativeLayout;
  }

  @SuppressLint("ResourceType") private TextView buildPaypalTextView() {
    TextView textView = new TextView(getActivity());
    textView.setId(19);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    layoutParams.setMargins(dpToPx(20), 0, 0, 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.END_OF, 18);
      layoutParams.setMarginStart(dpToPx(20));
    } else {
      layoutParams.addRule(RelativeLayout.RIGHT_OF, 18);
    }
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Using Paypal");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private ImageView buildPaypalImage() {
    ImageView imageView = new ImageView(getActivity());
    imageView.setId(18);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = getActivity().getPackageManager()
          .getApplicationIcon(buyItemProperties.getPackageName());
      imageView.setImageDrawable(icon);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    layoutParams.setMargins(dpToPx(18), 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildCreditCardWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(getActivity());
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView creditCardImage = buildCreditCardImage();
    TextView creditCardText = buildCreditCardTextView();
    RadioButton radioButton = buildRadioButton();

    relativeLayout.addView(creditCardImage);
    relativeLayout.addView(creditCardText);
    relativeLayout.addView(radioButton);
    return relativeLayout;
  }

  private RadioButton buildRadioButton() {
    RadioButton radioButton = new RadioButton(getActivity());
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_END);
      layoutParams.setMarginEnd(dpToPx(20));
    } else {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
      layoutParams.setMargins(0, 0, dpToPx(20), 0);
    }
    int[] colors = { Color.parseColor("#d4d4d4"), Color.parseColor("#e44b46") };
    ColorStateList colorStateList = new ColorStateList(new int[][] {
        new int[] { -android.R.attr.state_enabled }, new int[] { android.R.attr.state_enabled }
    }, colors);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      radioButton.setButtonTintList(colorStateList);
    } else {
      radioButton.setHighlightColor(Color.parseColor("#e44b46"));
    }
    radioButton.setLayoutParams(layoutParams);
    return radioButton;
  }

  @SuppressLint("ResourceType") private ImageView buildCreditCardImage() {
    ImageView imageView = new ImageView(getActivity());
    imageView.setId(16);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = getActivity().getPackageManager()
          .getApplicationIcon(buyItemProperties.getPackageName());
      imageView.setImageDrawable(icon);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    layoutParams.setMargins(dpToPx(18), 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  @SuppressLint("ResourceType") private TextView buildCreditCardTextView() {
    TextView textView = new TextView(getActivity());
    textView.setId(17);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    layoutParams.setMargins(dpToPx(20), 0, 0, 0);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(RelativeLayout.END_OF, 16);
      layoutParams.setMarginStart(dpToPx(20));
    } else {
      layoutParams.addRule(RelativeLayout.RIGHT_OF, 16);
    }
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Using Credit Card");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private TextView buildPayAsGuestTextView() {
    TextView textView = new TextView(getActivity());
    textView.setId(15);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.setMargins(dpToPx(12), dpToPx(12), 0, 0);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Pay as Guest");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private View buildHeaderSeparatorLayout() {
    View view = new View(getActivity());
    view.setId(14);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
    layoutParams.setMargins(dpToPx(16), dpToPx(20), dpToPx(16), 0);
    view.setBackgroundColor(Color.parseColor("#eaeaea"));
    layoutParams.addRule(RelativeLayout.BELOW, 100);
    view.setLayoutParams(layoutParams);
    return view;
  }

  private int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
        .getDisplayMetrics());
  }
}
