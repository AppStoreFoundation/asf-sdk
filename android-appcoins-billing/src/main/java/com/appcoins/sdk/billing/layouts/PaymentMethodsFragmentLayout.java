package com.appcoins.sdk.billing.layouts;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.sdk.billing.BuyItemProperties;
import java.io.IOException;
import java.io.InputStream;

public class PaymentMethodsFragmentLayout {

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
  private static String BUTTONS_RESOURCE_PATH = "appcoins-wallet/resources/buttons/";
  private static String IMAGES_RESOURCE_PATH = "appcoins-wallet/resources/images/";
  private Activity activity;
  private int orientation;
  private BuyItemProperties buyItemProperties;
  private ImageView iconImageView;
  private TextView appNameView;
  private TextView skuView;
  private TextView fiatPriceView;
  private TextView appcPriceView;
  private TextView payAsGuestView;
  private RadioGroup radioGroup;
  private ImageView creditCardImage;
  private TextView creditCardText;
  private RadioButton creditCardRadioButton;
  private ImageView paypalImage;
  private TextView paypalTextView;
  private RadioButton paypalRadioButton;
  private ImageView installCreditCardImage;
  private ImageView installPaypalImage;
  private TextView installMainText;
  private TextView installSecondaryText;
  private RadioButton installRadioButton;
  private String densityPath;

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
    RelativeLayout dialogLayout = buildDialogLayout();
    mainLayout.addView(dialogLayout);
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
    iconImageView = createAppIconLayout(icon);
    appNameView = createAppNameLayout(appName);
    skuView = createSkuLayout(buyItemProperties.getSku());
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
    setMargins(imageParams, 12, 12, 0, 0);

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
    RelativeLayout parentLayout = new RelativeLayout(activity);
    parentLayout.setId(PAYMENT_METHODS_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, HEADER_ID);
    parentLayout.setLayoutParams(layoutParams);

    payAsGuestView = buildPayAsGuestTextView();
    radioGroup = buildRadioGroupView();

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
    setMargins(layoutParams, 12, 24, 0, 24);
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
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(32));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    setMargins(layoutParams, 0, 0, 12, 0);
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
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(32));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.WHITE);
    background.setCornerRadius(dpToPx(6));
    button.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    button.setBackgroundDrawable(background);
    button.setMaxWidth(dpToPx(126));
    button.setMinWidth(dpToPx(80));

    button.setPaddingRelative(0, 0, dpToPx(4), 0);
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
    layoutParams.addRule(RelativeLayout.BELOW, PAY_AS_GUEST_TEXT_ID);
    setMargins(layoutParams, 12, 12, 12, 0);
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
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);
    setMargins(layoutParams, 0, 12, 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    installCreditCardImage = buildInstallCreditCardImage();
    installPaypalImage = buildInstallPaypalImage();
    installMainText = buildInstallMainText();
    installSecondaryText = buildInstallSecondaryText();
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
    setConstraint(layoutParams, RelativeLayout.END_OF, INSTALL_PAYPAL_ID);
    setConstraint(layoutParams, RelativeLayout.START_OF, INSTALL_RADIO_BUTTON_ID);
    setMargins(layoutParams, 10, 12, 8, 0);

    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setLines(1);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText("Using the AppCoins Wallet");
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  @SuppressLint("InlinedApi") private TextView buildInstallSecondaryText() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    layoutParams.addRule(RelativeLayout.BELOW, INSTALL_MAIN_TEXT_ID);
    setConstraint(layoutParams, RelativeLayout.ALIGN_START, INSTALL_MAIN_TEXT_ID);
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
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/" + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    setMargins(layoutParams, 8, 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private ImageView buildInstallPaypalImage() {
    ImageView imageView = new ImageView(activity);
    imageView.setId(INSTALL_PAYPAL_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(20), dpToPx(20));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    Drawable paypal =
        convertAssetDrawable(IMAGES_RESOURCE_PATH + "paypal/" + densityPath + "ic_paypal.png");
    imageView.setImageDrawable(paypal);
    setConstraint(layoutParams, RelativeLayout.END_OF, INSTALL_CREDIT_CARD_ID);
    setMargins(layoutParams, 1, 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildPaypalWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);
    setMargins(layoutParams, 0, 12, 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    paypalImage = buildPaypalImage();
    paypalTextView = buildPaypalTextView();
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
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    setConstraint(layoutParams, RelativeLayout.END_OF, PAYPAL_IMAGE_ID);
    setMargins(layoutParams, 20, 0, 0, 0);
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
    ImageView imageView = new ImageView(activity);
    imageView.setId(PAYPAL_IMAGE_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    Drawable paypal =
        convertAssetDrawable(IMAGES_RESOURCE_PATH + "paypal/" + densityPath + "ic_paypal.png");
    imageView.setImageDrawable(paypal);
    setMargins(layoutParams, 18, 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private RelativeLayout buildCreditCardWrapperLayout() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);
    relativeLayout.setLayoutParams(layoutParams);

    creditCardImage = buildCreditCardImage();
    creditCardText = buildCreditCardTextView();
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
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_END);
    setMargins(layoutParams, 0, 0, 20, 0);
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
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/" + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    setMargins(layoutParams, 18, 0, 0, 0);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  @SuppressLint({ "ResourceType", "InlinedApi" }) private TextView buildCreditCardTextView() {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    setConstraint(layoutParams, RelativeLayout.END_OF, CREDIT_CARD_IMAGE_ID);
    setMargins(layoutParams, 20, 0, 0, 0);
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
    TextView textView = new TextView(activity);
    textView.setId(PAY_AS_GUEST_TEXT_ID);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setMargins(layoutParams, 12, 12, 0, 0);
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
    setMargins(layoutParams, 16, 20, 16, 0);
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

  public ImageView getIconImageView() {
    return iconImageView;
  }

  public TextView getAppNameView() {
    return appNameView;
  }

  public TextView getSkuView() {
    return skuView;
  }

  public TextView getFiatPriceView() {
    return fiatPriceView;
  }

  public TextView getAppcPriceView() {
    return appcPriceView;
  }

  public TextView getPayAsGuestView() {
    return payAsGuestView;
  }

  public RadioGroup getRadioGroup() {
    return radioGroup;
  }

  public ImageView getCreditCardImage() {
    return creditCardImage;
  }

  public TextView getCreditCardText() {
    return creditCardText;
  }

  public RadioButton getCreditCardRadioButton() {
    return creditCardRadioButton;
  }

  public ImageView getPaypalImage() {
    return paypalImage;
  }

  public TextView getPaypalTextView() {
    return paypalTextView;
  }

  public RadioButton getPaypalRadioButton() {
    return paypalRadioButton;
  }

  public ImageView getInstallCreditCardImage() {
    return installCreditCardImage;
  }

  public ImageView getInstallPaypalImage() {
    return installPaypalImage;
  }

  public TextView getInstallMainText() {
    return installMainText;
  }

  public TextView getInstallSecondaryText() {
    return installSecondaryText;
  }

  public RadioButton getInstallRadioButton() {
    return installRadioButton;
  }
}
