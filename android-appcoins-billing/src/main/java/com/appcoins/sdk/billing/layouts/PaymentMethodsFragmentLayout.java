package com.appcoins.sdk.billing.layouts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.payasguest.PaymentMethodsFragment;
import java.io.IOException;
import java.io.InputStream;

public class PaymentMethodsFragmentLayout {

  private static final int ERROR_MESSAGE_ID = 26;
  private static final int ERROR_TITLE_ID = 25;
  private static final int PAYPAL_WRAPPER_ID = 24;
  private static final int CREDIT_CARD_WRAPPER_ID = 23;
  private static final int INSTALL_MAIN_TEXT_ID = 22;
  private static final int INSTALL_PAYPAL_ID = 21;
  private static final int INSTALL_CREDIT_CARD_ID = 20;
  private static final int INSTALL_RADIO_BUTTON_ID = 19;
  private static final int PAYPAL_IMAGE_ID = 18;
  private static final int PAYPAL_RADIO_BUTTON_ID = 17;
  private static final int CREDIT_CARD_IMAGE_ID = 16;
  private static final int PAY_AS_GUEST_TEXT_ID = 15;
  private static final int HEADER_ID = 14;
  private static final int PAYMENT_METHODS_ID = 13;
  private static final int PAYMENT_METHODS_HEADER = 12;
  private static final int FIAT_PRICE_VIEW_ID = 11;
  private static final int APPC_PRICE_VIEW_ID = 10;
  private static final int CREDIT_CARD_RADIO_BUTTON_ID = 9;
  private static final int APP_NAME_ID = 8;
  private static final int APP_ICON_ID = 7;
  private static final String BUTTONS_RESOURCE_PATH = "appcoins-wallet/resources/buttons/";
  private static final String IMAGES_RESOURCE_PATH = "appcoins-wallet/resources/images/";
  private Activity activity;
  private int orientation;
  private BuyItemProperties buyItemProperties;
  private TextView fiatPriceView;
  private TextView appcPriceView;
  private RadioButton creditCardRadioButton;
  private RadioButton paypalRadioButton;
  private RadioButton installRadioButton;
  private String densityPath;
  private Button cancelButton;
  private Button positiveButton;
  private RelativeLayout creditCardWrapperLayout;
  private RelativeLayout paypalWrapperLayout;
  private RelativeLayout installWrapperLayout;
  private GradientDrawable selectedBackground;
  private GradientDrawable defaultBackground;
  private ProgressBar progressBar;
  private RelativeLayout paymentMethodsLayout;
  private TextView errorMessage;
  private Button errorPositiveButton;
  private RelativeLayout errorView;
  private RelativeLayout dialogLayout;

  public PaymentMethodsFragmentLayout(Activity activity, int orientation,
      BuyItemProperties buyItemProperties) {

    this.activity = activity;
    this.orientation = orientation;
    this.buyItemProperties = buyItemProperties;
  }

  public View build() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager()
        .getDefaultDisplay()
        .getMetrics(displayMetrics);
    densityPath = mapDisplayMetrics(displayMetrics);

    RelativeLayout mainLayout = buildMainLayout();
    errorView = buildErrorView();
    errorView.setVisibility(View.INVISIBLE);
    dialogLayout = buildDialogLayout();

    mainLayout.addView(dialogLayout);
    mainLayout.addView(errorView);
    return mainLayout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildPaymentMethodsHeaderLayout() {
    RelativeLayout paymentMethodHeaderLayout = new RelativeLayout(activity);
    paymentMethodHeaderLayout.setLayoutParams(
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
    paymentMethodHeaderLayout.setId(PAYMENT_METHODS_HEADER);
    Drawable icon = null;
    String appName = "";
    PackageManager packageManager = activity.getApplicationContext()
        .getPackageManager();
    try {
      icon = packageManager.getApplicationIcon(buyItemProperties.getPackageName());
      ApplicationInfo applicationInfo =
          packageManager.getApplicationInfo(buyItemProperties.getPackageName(), 0);
      appName = packageManager.getApplicationLabel(applicationInfo)
          .toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    ImageView iconImageView = createAppIconLayout(icon);
    TextView appNameView = createAppNameLayout(appName);
    TextView skuView = createSkuLayout(buyItemProperties.getSku());
    fiatPriceView = createFiatPriceView();
    appcPriceView = createAppcPriceView();

    paymentMethodHeaderLayout.addView(iconImageView);
    paymentMethodHeaderLayout.addView(appNameView);
    paymentMethodHeaderLayout.addView(skuView);
    paymentMethodHeaderLayout.addView(fiatPriceView);
    paymentMethodHeaderLayout.addView(appcPriceView);
    return paymentMethodHeaderLayout;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView createAppcPriceView() {
    TextView textView = new TextView(activity);
    textView.setId(APPC_PRICE_VIEW_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, FIAT_PRICE_VIEW_ID);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_END);
    setMargins(layoutParams, 0, 0, 16, 0);
    textView.setTextColor(Color.parseColor("#828282"));
    textView.setTextSize(12);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView createFiatPriceView() {
    TextView textView = new TextView(activity);
    textView.setId(FIAT_PRICE_VIEW_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_END);
    setMargins(layoutParams, 0, 17, 16, 0);
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(15);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView createSkuLayout(String sku) {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, APP_NAME_ID);
    setConstraint(layoutParams, RelativeLayout.START_OF, APPC_PRICE_VIEW_ID);
    setConstraint(layoutParams, RelativeLayout.END_OF, APP_ICON_ID);
    setMargins(layoutParams, 10, 0, 12, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTextColor(Color.parseColor("#8a000000"));
    textView.setTextSize(12);
    textView.setText(sku);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" })
  private TextView createAppNameLayout(String appName) {
    TextView textView = new TextView(activity);
    textView.setId(APP_NAME_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setConstraint(layoutParams, RelativeLayout.START_OF, FIAT_PRICE_VIEW_ID);
    setConstraint(layoutParams, RelativeLayout.END_OF, APP_ICON_ID);
    setMargins(layoutParams, 10, 15, 12, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTextColor(Color.parseColor("#de000000"));
    textView.setTextSize(16);
    textView.setText(appName);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private ImageView createAppIconLayout(Drawable icon) {
    ImageView imageView = new ImageView(activity);
    imageView.setId(APP_ICON_ID);
    if (icon != null) {
      imageView.setImageDrawable(icon);
    }
    RelativeLayout.LayoutParams imageParams =
        new RelativeLayout.LayoutParams(dpToPx(48), dpToPx(48));

    int start;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 12;
    } else {
      start = 20;
    }

    setMargins(imageParams, start, 12, 0, 0);
    imageView.setLayoutParams(imageParams);
    return imageView;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildMainLayout() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(activity);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildDialogLayout() {
    RelativeLayout dialogLayout = new RelativeLayout(activity);
    dialogLayout.setClipToPadding(false);

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    dialogLayout.setBackground(gradientDrawable);

    int width;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = dpToPx(340);
    } else {
      width = dpToPx(544);
    }
    RelativeLayout.LayoutParams dialogLayoutParams =
        new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    dialogLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    dialogLayout.setLayoutParams(dialogLayoutParams);

    progressBar = buildProgressBar();

    RelativeLayout paymentMethodsHeaderLayout = buildPaymentMethodsHeaderLayout();
    View headerSeparator = buildHeaderSeparatorLayout();
    paymentMethodsLayout = buildPaymentMethodsLayout();
    paymentMethodsLayout.setVisibility(View.INVISIBLE);
    LinearLayout buttonsView = buildButtonsView();

    dialogLayout.addView(progressBar);
    dialogLayout.addView(paymentMethodsHeaderLayout);
    dialogLayout.addView(headerSeparator);
    dialogLayout.addView(paymentMethodsLayout);
    dialogLayout.addView(buttonsView);
    return dialogLayout;
  }

  private RelativeLayout buildErrorView() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    setPadding(relativeLayout, 16, 16, 16, 16);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(160));

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    relativeLayout.setBackground(gradientDrawable);

    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

    int start, end;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 16;
      end = 16;
    } else {
      start = 64;
      end = 64;
    }
    setMargins(layoutParams, start, 0, end, 0);

    relativeLayout.setLayoutParams(layoutParams);

    TextView errorTitle = buildErrorTitle();
    errorMessage = buildErrorMessage();
    errorPositiveButton = buildErrorPositiveButton();

    relativeLayout.addView(errorTitle);
    relativeLayout.addView(errorMessage);
    relativeLayout.addView(errorPositiveButton);

    return relativeLayout;
  }

  private TextView buildErrorTitle() {
    TextView textView = new TextView(activity);
    textView.setId(ERROR_TITLE_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    textView.setLayoutParams(layoutParams);
    textView.setText("Error");
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(16);

    return textView;
  }

  private TextView buildErrorMessage() {
    TextView textView = new TextView(activity);
    textView.setId(ERROR_MESSAGE_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, ERROR_TITLE_ID);
    setMargins(layoutParams, 0, 8, 0, 0);

    textView.setLayoutParams(layoutParams);
    textView.setMaxLines(3);
    textView.setText("An error as ocurred");
    textView.setTextColor(Color.parseColor("#8a8a8a"));
    textView.setTextSize(12);
    return textView;
  }

  @SuppressLint("InlinedApi") private Button buildErrorPositiveButton() {
    Button button = new Button(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(36));
    setPadding(button, 0, 0, 4, 0);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_END);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_BOTTOM);
    setMargins(layoutParams, 0, 56, 0, 0);
    int[] gradientColors = { Color.parseColor("#FC9D48"), Color.parseColor("#FF578C") };
    GradientDrawable enableBackground =
        new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
    enableBackground.setShape(GradientDrawable.RECTANGLE);
    enableBackground.setStroke(dpToPx(1), Color.WHITE);
    enableBackground.setCornerRadius(dpToPx(16));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackground(enableBackground);

    button.setMaxWidth(dpToPx(126));
    button.setMinWidth(dpToPx(80));

    button.setTextColor(Color.WHITE);
    button.setTextSize(14);
    button.setText("OK".toUpperCase());
    button.setLayoutParams(layoutParams);

    return button;
  }

  private ProgressBar buildProgressBar() {
    ProgressBar progressBar = new ProgressBar(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    progressBar.setLayoutParams(layoutParams);
    return progressBar;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildPaymentMethodsLayout() {
    RelativeLayout parentLayout = new RelativeLayout(activity);
    parentLayout.setId(PAYMENT_METHODS_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, HEADER_ID);
    parentLayout.setLayoutParams(layoutParams);

    TextView payAsGuestView = buildPayAsGuestTextView();
    RadioGroup radioGroup = buildRadioGroupView();

    parentLayout.addView(payAsGuestView);
    parentLayout.addView(radioGroup);

    return parentLayout;
  }

  private LinearLayout buildButtonsView() {
    LinearLayout linearLayout = new LinearLayout(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, PAYMENT_METHODS_ID);

    int end, top, bottom;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      end = 12;
      top = 24;
      bottom = 24;
    } else {
      top = 24;
      end = 22;
      bottom = 16;
    }

    setMargins(layoutParams, 0, top, end, bottom);
    linearLayout.setGravity(Gravity.END);
    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setClipChildren(false);
    linearLayout.setClipToPadding(false);
    linearLayout.setLayoutParams(layoutParams);

    cancelButton = buildCancelButtonLayout();
    positiveButton = buildPositiveButtonLayout();
    positiveButton.setEnabled(false);

    linearLayout.addView(cancelButton);
    linearLayout.addView(positiveButton);
    return linearLayout;
  }

  private Button buildPositiveButtonLayout() {
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(36));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    int[] gradientColors = { Color.parseColor("#FC9D48"), Color.parseColor("#FF578C") };
    GradientDrawable enableBackground =
        new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, gradientColors);
    enableBackground.setShape(GradientDrawable.RECTANGLE);
    enableBackground.setStroke(dpToPx(1), Color.WHITE);
    enableBackground.setCornerRadius(dpToPx(16));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackground(enableBackground);

    GradientDrawable disableBackground = new GradientDrawable();
    disableBackground.setShape(GradientDrawable.RECTANGLE);
    disableBackground.setStroke(dpToPx(1), Color.WHITE);
    disableBackground.setCornerRadius(dpToPx(16));
    disableBackground.setColor(Color.parseColor("#c9c9c9"));

    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));

    StateListDrawable stateListDrawable = new StateListDrawable();
    stateListDrawable.addState(new int[] { android.R.attr.state_enabled }, enableBackground);
    stateListDrawable.addState(new int[] { -android.R.attr.state_enabled }, disableBackground);

    button.setBackground(stateListDrawable);

    button.setMaxWidth(dpToPx(142));
    button.setMinWidth(dpToPx(96));

    setPadding(button, 0, 0, 4, 0);
    button.setTextColor(Color.WHITE);
    button.setTextSize(14);
    button.setText("NEXT".toUpperCase());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private Button buildCancelButtonLayout() {
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(36));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.WHITE);
    background.setCornerRadius(dpToPx(6));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackground(background);
    button.setMaxWidth(dpToPx(126));
    button.setMinWidth(dpToPx(80));

    setPadding(button, 0, 0, 4, 0);
    button.setTextColor(Color.parseColor("#8a000000"));
    button.setTextSize(14);
    button.setText("Cancel".toUpperCase());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private RadioGroup buildRadioGroupView() {
    RadioGroup radioGroup = new RadioGroup(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      radioGroup.setOrientation(LinearLayout.VERTICAL);
    } else {
      radioGroup.setOrientation(LinearLayout.HORIZONTAL);
    }

    layoutParams.addRule(RelativeLayout.BELOW, PAY_AS_GUEST_TEXT_ID);
    int start, end, top;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 12;
      top = 12;
      end = 12;
    } else {
      start = 20;
      top = 18;
      end = 20;
    }
    setMargins(layoutParams, start, top, end, 0);
    radioGroup.setLayoutParams(layoutParams);

    creditCardWrapperLayout = buildCreditCardWrapperLayout();
    paypalWrapperLayout = buildPaypalWrapperLayout();
    installWrapperLayout = buildInstallWrapperLayout();

    radioGroup.addView(creditCardWrapperLayout);
    radioGroup.addView(paypalWrapperLayout);
    radioGroup.addView(installWrapperLayout);

    return radioGroup;
  }

  private RelativeLayout buildInstallWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);

    int width, height, top, start;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = ViewGroup.LayoutParams.MATCH_PARENT;
      height = dpToPx(52);
      top = 12;
      start = 0;
    } else {
      width = dpToPx(160);
      height = dpToPx(94);
      top = 0;
      start = 12;
    }

    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(width, height);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackground(background);
    setMargins(layoutParams, start, top, 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView installCreditCardImage = buildInstallCreditCardImage();
    ImageView installPaypalImage = buildInstallPaypalImage();
    TextView installMainText = buildInstallMainText();
    TextView installSecondaryText = buildInstallSecondaryText();
    installRadioButton = buildRadioButton(INSTALL_RADIO_BUTTON_ID);

    relativeLayout.addView(installCreditCardImage);
    relativeLayout.addView(installPaypalImage);
    relativeLayout.addView(installMainText);
    relativeLayout.addView(installSecondaryText);
    relativeLayout.addView(installRadioButton);
    return relativeLayout;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView buildInstallMainText() {
    TextView textView = new TextView(activity);
    textView.setId(INSTALL_MAIN_TEXT_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int start, top, end, textSize;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 10;
      top = 12;
      end = 8;
      textSize = 12;
      setConstraint(layoutParams, RelativeLayout.END_OF, INSTALL_PAYPAL_ID);
      setConstraint(layoutParams, RelativeLayout.START_OF, INSTALL_RADIO_BUTTON_ID);
    } else {
      top = 2;
      start = 0;
      end = 0;
      textSize = 11;
      layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
      setConstraint(layoutParams, RelativeLayout.BELOW, INSTALL_CREDIT_CARD_ID);
    }
    setMargins(layoutParams, start, top, end, 0);

    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(textSize);
    textView.setText("Pay with AppCoins Wallet");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("InlinedApi") private TextView buildInstallSecondaryText() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int rule;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      rule = RelativeLayout.CENTER_VERTICAL;
      setConstraint(layoutParams, RelativeLayout.ALIGN_START, INSTALL_MAIN_TEXT_ID);
    } else {
      rule = RelativeLayout.CENTER_HORIZONTAL;
    }

    layoutParams.addRule(rule);
    layoutParams.addRule(RelativeLayout.BELOW, INSTALL_MAIN_TEXT_ID);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#FA6249"));
    textView.setTextSize(10);
    textView.setText("Get up to 30% bonus");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private ImageView buildInstallCreditCardImage() {
    ImageView imageView = new ImageView(activity);
    imageView.setId(INSTALL_CREDIT_CARD_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(24), dpToPx(24));

    int start, top;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 8;
      top = 0;
      layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    } else {
      start = 58;
      top = 8;
    }

    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/" + "portrait/" + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    setMargins(layoutParams, start, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private ImageView buildInstallPaypalImage() {
    ImageView imageView = new ImageView(activity);
    imageView.setId(INSTALL_PAYPAL_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(20), dpToPx(20));

    int top;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 0;
      layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    } else {
      top = 8;
      setConstraint(layoutParams, RelativeLayout.END_OF, INSTALL_CREDIT_CARD_ID);
    }

    Drawable paypal =
        convertAssetDrawable(IMAGES_RESOURCE_PATH + "paypal/" + densityPath + "ic_paypal.png");
    imageView.setImageDrawable(paypal);
    setConstraint(layoutParams, RelativeLayout.END_OF, INSTALL_CREDIT_CARD_ID);
    setMargins(layoutParams, 1, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildPaypalWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    relativeLayout.setId(PAYPAL_WRAPPER_ID);

    int width, height, top, start;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = ViewGroup.LayoutParams.MATCH_PARENT;
      height = dpToPx(52);
      top = 12;
      start = 0;
    } else {
      width = dpToPx(160);
      height = dpToPx(94);
      top = 0;
      start = 8;
    }

    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(width, height);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackground(background);
    setMargins(layoutParams, start, top, 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView paypalImage = buildPaypalImage();
    TextView paypalTextView = buildPaypalTextView();
    paypalRadioButton = buildRadioButton(PAYPAL_RADIO_BUTTON_ID);

    relativeLayout.addView(paypalImage);
    relativeLayout.addView(paypalTextView);
    relativeLayout.addView(paypalRadioButton);
    return relativeLayout;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView buildPaypalTextView() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int start, top, rule, textSize;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 20;
      top = 0;
      textSize = 12;
      rule = RelativeLayout.CENTER_VERTICAL;
      setConstraint(layoutParams, RelativeLayout.END_OF, PAYPAL_IMAGE_ID);
    } else {
      start = 0;
      top = 16;
      textSize = 11;
      rule = RelativeLayout.CENTER_IN_PARENT;
    }

    layoutParams.addRule(rule);
    setMargins(layoutParams, start, top, 0, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(textSize);
    textView.setText("Using Paypal");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private ImageView buildPaypalImage() {
    ImageView imageView = new ImageView(activity);
    imageView.setId(PAYPAL_IMAGE_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));

    int start, top, rule;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 18;
      top = 0;
      rule = RelativeLayout.CENTER_VERTICAL;
    } else {
      start = 0;
      top = 8;
      rule = RelativeLayout.CENTER_HORIZONTAL;
    }

    layoutParams.addRule(rule);
    Drawable paypal =
        convertAssetDrawable(IMAGES_RESOURCE_PATH + "paypal/" + densityPath + "ic_paypal.png");
    imageView.setImageDrawable(paypal);
    setMargins(layoutParams, start, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildCreditCardWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    relativeLayout.setId(CREDIT_CARD_WRAPPER_ID);
    int width, height;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = ViewGroup.LayoutParams.MATCH_PARENT;
      height = dpToPx(52);
    } else {
      width = dpToPx(160);
      height = dpToPx(94);
    }

    RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(width, height);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackground(background);
    relativeLayout.setLayoutParams(layoutParams);

    ImageView creditCardImage = buildCreditCardImage();
    TextView creditCardText = buildCreditCardTextView();
    creditCardRadioButton = buildRadioButton(CREDIT_CARD_RADIO_BUTTON_ID);

    relativeLayout.addView(creditCardImage);
    relativeLayout.addView(creditCardText);
    relativeLayout.addView(creditCardRadioButton);
    return relativeLayout;
  }

  @SuppressLint("InlinedApi") private RadioButton buildRadioButton(int id) {
    RadioButton radioButton = new RadioButton(activity);
    radioButton.setId(id);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int rule, end, bottom;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      rule = RelativeLayout.CENTER_VERTICAL;
      end = 20;
      bottom = 0;
      setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_END);
    } else {
      rule = RelativeLayout.CENTER_HORIZONTAL;
      end = 0;
      bottom = 6;
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
    }

    layoutParams.addRule(rule);
    setMargins(layoutParams, 0, 0, end, bottom);
    setRadioButtonDrawable(radioButton);
    radioButton.setLayoutParams(layoutParams);
    return radioButton;
  }

  private void setRadioButtonDrawable(RadioButton radioButton) {
    StateListDrawable stateListDrawable = new StateListDrawable();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager()
        .getDefaultDisplay()
        .getMetrics(displayMetrics);
    String densityPath = mapDisplayMetrics(displayMetrics);
    Drawable checkedRadioButton = convertAssetDrawable(
        BUTTONS_RESOURCE_PATH + "checked/" + densityPath + "ic_radio_checked.png");
    Drawable uncheckedRadioButton = convertAssetDrawable(
        BUTTONS_RESOURCE_PATH + "unchecked/" + densityPath + "ic_radio_unchecked.png");
    stateListDrawable.addState(new int[] { android.R.attr.state_checked }, checkedRadioButton);
    stateListDrawable.addState(new int[] { -android.R.attr.state_checked }, uncheckedRadioButton);
    radioButton.setButtonDrawable(stateListDrawable);
  }

  private String mapDisplayMetrics(DisplayMetrics displayMetrics) {
    String densityPath;
    switch (displayMetrics.densityDpi) {
      case DisplayMetrics.DENSITY_LOW:
        densityPath = "drawable-ldpi";
        break;
      case DisplayMetrics.DENSITY_MEDIUM:
        densityPath = "drawable-mdpi";
        break;
      case DisplayMetrics.DENSITY_HIGH:
      case DisplayMetrics.DENSITY_260:
      case DisplayMetrics.DENSITY_280:
        densityPath = "drawable-hdpi/";
        break;
      case DisplayMetrics.DENSITY_XHIGH:
      case DisplayMetrics.DENSITY_300:
      case DisplayMetrics.DENSITY_360:
      case DisplayMetrics.DENSITY_340:
      case DisplayMetrics.DENSITY_400:
        densityPath = "drawable-xhdpi/";
        break;
      case DisplayMetrics.DENSITY_XXHIGH:
      case DisplayMetrics.DENSITY_440:
      case DisplayMetrics.DENSITY_420:
        densityPath = "drawable-xxhdpi/";
        break;
      case DisplayMetrics.DENSITY_XXXHIGH:
      case DisplayMetrics.DENSITY_560:
      default:
        densityPath = "drawable-xxxhdpi/";
        break;
    }
    return densityPath;
  }

  @SuppressLint("ResourceType") private ImageView buildCreditCardImage() {
    ImageView imageView = new ImageView(activity);
    imageView.setId(CREDIT_CARD_IMAGE_ID);

    int width, height, start, rule, top;
    String path;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = 25;
      height = 25;
      start = 18;
      top = 0;
      path = "portrait/";
      rule = RelativeLayout.CENTER_VERTICAL;
    } else {
      width = 57;
      height = 10;
      start = 0;
      top = 15;
      path = "landscape/";
      rule = RelativeLayout.CENTER_HORIZONTAL;
    }

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(width), dpToPx(height));
    layoutParams.addRule(rule);
    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/" + path + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    setMargins(layoutParams, start, top, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView buildCreditCardTextView() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int start, top, rule, textSize;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      rule = RelativeLayout.CENTER_VERTICAL;
      start = 20;
      top = 0;
      textSize = 12;
      setConstraint(layoutParams, RelativeLayout.END_OF, CREDIT_CARD_IMAGE_ID);
    } else {
      start = 0;
      top = 16;
      textSize = 11;
      rule = RelativeLayout.CENTER_IN_PARENT;
    }

    layoutParams.addRule(rule);
    setMargins(layoutParams, start, top, 0, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.BLACK);
    textView.setTextSize(textSize);
    textView.setText("Using Credit Card");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private TextView buildPayAsGuestTextView() {
    TextView textView = new TextView(activity);
    textView.setId(PAY_AS_GUEST_TEXT_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    int top, start;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 12;
      start = 12;
    } else {
      top = 16;
      start = 20;
    }
    setMargins(layoutParams, start, top, 0, 0);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Pay as Guest");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("ResourceType") private View buildHeaderSeparatorLayout() {
    View view = new View(activity);
    view.setId(HEADER_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(1));
    int start, top, end;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      start = 16;
      top = 20;
      end = 16;
    } else {
      start = 20;
      top = 14;
      end = 20;
    }
    setMargins(layoutParams, start, top, end, 0);
    view.setBackgroundColor(Color.parseColor("#eaeaea"));
    layoutParams.addRule(RelativeLayout.BELOW, PAYMENT_METHODS_HEADER);
    view.setLayoutParams(layoutParams);
    return view;
  }

  private int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
        .getDisplayMetrics());
  }

  private Drawable convertAssetDrawable(String path) {
    InputStream inputStream = null;
    try {
      inputStream = activity.getResources()
          .getAssets()
          .open(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Drawable.createFromStream(inputStream, null);
  }

  private void setMargins(ViewGroup.MarginLayoutParams layoutParams, int start, int top, int end,
      int bottom) {
    layoutParams.setMargins(0, dpToPx(top), 0, dpToPx(bottom));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.setMarginStart(dpToPx(start));
      layoutParams.setMarginEnd(dpToPx(end));
    } else {
      layoutParams.setMargins(dpToPx(start), dpToPx(top), dpToPx(end), dpToPx(bottom));
    }
  }

  private void setConstraint(RelativeLayout.LayoutParams layoutParams, int rule, int id) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(rule, id);
    } else {
      if (rule == RelativeLayout.END_OF) {
        layoutParams.addRule(RelativeLayout.RIGHT_OF, id);
      } else if (rule == RelativeLayout.START_OF) {
        layoutParams.addRule(RelativeLayout.LEFT_OF, id);
      } else if (rule == RelativeLayout.ALIGN_END) {
        layoutParams.addRule(RelativeLayout.ALIGN_RIGHT, id);
      } else if (rule == RelativeLayout.ALIGN_START) {
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, id);
      }
    }
  }

  private void setConstraint(RelativeLayout.LayoutParams layoutParams, int rule) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(rule);
    } else if (rule == RelativeLayout.ALIGN_PARENT_END) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    } else if (rule == RelativeLayout.ALIGN_PARENT_START) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    }
  }

  private void setPadding(View view, int start, int top, int end, int bottom) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      view.setPaddingRelative(dpToPx(start), dpToPx(top), dpToPx(end), dpToPx(bottom));
    } else {
      view.setPadding(dpToPx(start), dpToPx(top), dpToPx(end), dpToPx(bottom));
    }
  }

  public void selectRadioButton(String selectedRadioButton) {
    if (selectedRadioButton.equals(PaymentMethodsFragment.CREDIT_CARD_RADIO)) {
      creditCardWrapperLayout.setBackground(getSelectedGradientDrawable());
      paypalWrapperLayout.setBackground(getDefaultGradientDrawable());
      installWrapperLayout.setBackground(getDefaultGradientDrawable());

      creditCardRadioButton.setChecked(true);
      paypalRadioButton.setChecked(false);
      installRadioButton.setChecked(false);
    } else if (selectedRadioButton.equals(PaymentMethodsFragment.PAYPAL_RADIO)) {
      paypalWrapperLayout.setBackground(getSelectedGradientDrawable());
      creditCardWrapperLayout.setBackground(getDefaultGradientDrawable());
      installWrapperLayout.setBackground(getDefaultGradientDrawable());

      creditCardRadioButton.setChecked(false);
      paypalRadioButton.setChecked(true);
      installRadioButton.setChecked(false);
    } else {
      installWrapperLayout.setBackground(getSelectedGradientDrawable());
      creditCardWrapperLayout.setBackground(getDefaultGradientDrawable());
      paypalWrapperLayout.setBackground(getDefaultGradientDrawable());

      creditCardRadioButton.setChecked(false);
      paypalRadioButton.setChecked(false);
      installRadioButton.setChecked(true);
    }
  }

  private GradientDrawable getSelectedGradientDrawable() {
    if (selectedBackground == null) {
      selectedBackground = new GradientDrawable();
      selectedBackground.setShape(GradientDrawable.RECTANGLE);
      selectedBackground.setStroke(dpToPx(1), Color.parseColor("#fe6e76"));
      selectedBackground.setCornerRadius(dpToPx(6));
    }
    return selectedBackground;
  }

  private GradientDrawable getDefaultGradientDrawable() {
    if (defaultBackground == null) {
      defaultBackground = new GradientDrawable();
      defaultBackground.setShape(GradientDrawable.RECTANGLE);
      defaultBackground.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
      defaultBackground.setCornerRadius(dpToPx(6));
    }
    return defaultBackground;
  }

  public TextView getFiatPriceView() {
    return fiatPriceView;
  }

  public TextView getAppcPriceView() {
    return appcPriceView;
  }

  public RadioButton getCreditCardRadioButton() {
    return creditCardRadioButton;
  }

  public RadioButton getPaypalRadioButton() {
    return paypalRadioButton;
  }

  public RadioButton getInstallRadioButton() {
    return installRadioButton;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public Button getPositiveButton() {
    return positiveButton;
  }

  public RelativeLayout getCreditCardWrapperLayout() {
    return creditCardWrapperLayout;
  }

  public RelativeLayout getPaypalWrapperLayout() {
    return paypalWrapperLayout;
  }

  public RelativeLayout getInstallWrapperLayout() {
    return installWrapperLayout;
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }

  public RelativeLayout getPaymentMethodsLayout() {
    return paymentMethodsLayout;
  }

  public TextView getErrorMessage() {
    return errorMessage;
  }

  public Button getErrorPositiveButton() {
    return errorPositiveButton;
  }

  public RelativeLayout getErrorView() {
    return errorView;
  }

  public RelativeLayout getDialogLayout() {
    return dialogLayout;
  }
}
