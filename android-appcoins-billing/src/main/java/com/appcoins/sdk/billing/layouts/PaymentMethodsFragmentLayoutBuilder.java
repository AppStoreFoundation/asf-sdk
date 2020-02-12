package com.appcoins.sdk.billing.layouts;

import android.annotation.SuppressLint;
import android.app.Activity;
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

public class PaymentMethodsFragmentLayoutBuilder {

  private static String BUTTONS_RESOURCE_PATH = "appcoins-wallet/resources/buttons/";
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

  public PaymentMethodsFragmentLayoutBuilder(Activity activity, int orientation,
      BuyItemProperties buyItemProperties) {

    this.activity = activity;
    this.orientation = orientation;
    this.buyItemProperties = buyItemProperties;
  }

  public View build() {
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
    paymentMethodHeaderLayout.setId(100);
    Drawable icon = null;
    String appName = "";
    PackageManager packageManager = activity.getApplicationContext()
        .getPackageManager();
    try {
      icon = packageManager.getApplicationIcon(buyItemProperties.getPackageName());
      appName = packageManager.getApplicationInfo(buyItemProperties.getPackageName(), 0).name;
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

  @SuppressLint("ResourceType") private TextView createAppcPriceView() {
    TextView textView = new TextView(activity);
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
    TextView textView = new TextView(activity);
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
    TextView textView = new TextView(activity);
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
    TextView textView = new TextView(activity);
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
    ImageView imageView = new ImageView(activity);
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
    RelativeLayout backgroundLayout = new RelativeLayout(activity);
    backgroundLayout.setId(5);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildDialogLayout() {
    RelativeLayout dialogLayout = new RelativeLayout(activity);
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
    RelativeLayout parentLayout = new RelativeLayout(activity);
    parentLayout.setId(13);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, 14);
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
    Button button = new Button(activity);
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
    Button button = new Button(activity);
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
    RadioGroup radioGroup = new RadioGroup(activity);
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
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);

    layoutParams.setMargins(0, dpToPx(12), 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    installCreditCardImage = buildInstallImage1();
    installPaypalImage = buildInstallImage2();
    installMainText = buildInstallTextView1();
    installSecondaryText = buildInstallTextView2();
    installRadioButton = buildRadioButton();

    relativeLayout.addView(installCreditCardImage);
    relativeLayout.addView(installPaypalImage);
    relativeLayout.addView(installMainText);
    relativeLayout.addView(installSecondaryText);
    relativeLayout.addView(installRadioButton);
    return relativeLayout;
  }

  @SuppressLint("ResourceType") private TextView buildInstallTextView1() {
    TextView textView = new TextView(activity);
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
    TextView textView = new TextView(activity);
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
    ImageView imageView = new ImageView(activity);
    imageView.setId(20);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(24), dpToPx(24));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = activity.getPackageManager()
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
    ImageView imageView = new ImageView(activity);
    imageView.setId(21);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(20), dpToPx(20));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = activity.getPackageManager()
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
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    RadioGroup.LayoutParams layoutParams =
        new RadioGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(52));
    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#e3e3e3"));
    background.setCornerRadius(dpToPx(6));
    relativeLayout.setBackgroundDrawable(background);
    layoutParams.setMargins(0, dpToPx(12), 0, 0);
    relativeLayout.setLayoutParams(layoutParams);

    paypalImage = buildPaypalImage();
    paypalTextView = buildPaypalTextView();
    paypalRadioButton = buildRadioButton();

    relativeLayout.addView(paypalImage);
    relativeLayout.addView(paypalTextView);
    relativeLayout.addView(paypalRadioButton);
    return relativeLayout;
  }

  @SuppressLint("ResourceType") private TextView buildPaypalTextView() {
    TextView textView = new TextView(activity);
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
    ImageView imageView = new ImageView(activity);
    imageView.setId(18);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = activity.getPackageManager()
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
    creditCardRadioButton = buildRadioButton();

    relativeLayout.addView(creditCardImage);
    relativeLayout.addView(creditCardText);
    relativeLayout.addView(creditCardRadioButton);
    return relativeLayout;
  }

  private RadioButton buildRadioButton() {
    RadioButton radioButton = new RadioButton(activity);
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
    imageView.setId(16);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(25), dpToPx(25));
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    try {
      Drawable icon = activity.getPackageManager()
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
    TextView textView = new TextView(activity);
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
    TextView textView = new TextView(activity);
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
    View view = new View(activity);
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
