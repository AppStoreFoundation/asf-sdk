package com.appcoins.sdk.billing.layouts;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;
import com.appcoins.sdk.billing.helpers.translations.TranslationsRepository;
import com.appcoins.sdk.billing.listeners.payasguest.CardNumberFocusChangeListener;
import com.appcoins.sdk.billing.listeners.payasguest.CardNumberTextWatcher;
import com.appcoins.sdk.billing.listeners.payasguest.CvvTextWatcher;
import com.appcoins.sdk.billing.listeners.payasguest.ExpiryDateTextWatcher;
import com.appcoins.sdk.billing.utils.PaymentErrorViewLayout;
import com.sdk.appcoins_adyen.utils.CardValidationUtils;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;

import static com.appcoins.sdk.billing.utils.LayoutUtils.IMAGES_RESOURCE_PATH;
import static com.appcoins.sdk.billing.utils.LayoutUtils.SUPPORT_RESOURCE_PATH;
import static com.appcoins.sdk.billing.utils.LayoutUtils.dpToPx;
import static com.appcoins.sdk.billing.utils.LayoutUtils.generateRandomId;
import static com.appcoins.sdk.billing.utils.LayoutUtils.getCornerRadiusArray;
import static com.appcoins.sdk.billing.utils.LayoutUtils.mapDisplayMetrics;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setConstraint;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setMargins;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setPadding;

public class AdyenPaymentFragmentLayout {
  private final Activity activity;
  private final int orientation;
  private int buttonsViewId;
  private int genericCardId;
  private int creditCardInputId;
  private int creditCardHeaderId;
  private int creditCardViewId;
  private int headerId;
  private int appcPriceViewId;
  private int fiatPriceViewId;
  private int appNameId;
  private int appIconId;
  private int paymentMethodsHeaderId;
  private String densityPath;
  private ViewGroup errorView;
  private ViewGroup dialogLayout;
  private TextView fiatPriceView;
  private Button cancelButton;
  private Button positiveButton;
  private ViewGroup buttonsView;
  private TextView morePaymentsText;
  private TextView changeCard;
  private ViewGroup loadingView;
  private PaymentErrorViewLayout paymentErrorViewLayout;
  private CardNumberEditText cardNumberEditText;
  private EditText expiryDateEditText;
  private EditText cvvEditText;
  private CreditCardLayout creditCardEditTextLayout;
  private TranslationsModel translationModel;
  private ViewGroup completedPurchaseView;
  private TextView helpText;

  public AdyenPaymentFragmentLayout(Activity activity, int orientation) {
    this.activity = activity;
    this.orientation = orientation;
  }

  public View build(String fiatPrice, String fiatCurrency, String appcPrice, String sku,
      String packageName) {
    translationModel = TranslationsRepository.getInstance(activity)
        .getTranslationsModel();
    DisplayMetrics displayMetrics = new DisplayMetrics();
    activity.getWindowManager()
        .getDefaultDisplay()
        .getMetrics(displayMetrics);
    densityPath = mapDisplayMetrics(displayMetrics);

    RelativeLayout mainLayout = buildMainLayout();
    paymentErrorViewLayout = new PaymentErrorViewLayout(activity, orientation);
    errorView = paymentErrorViewLayout.buildErrorView();
    errorView.setVisibility(View.INVISIBLE);

    CompletedPurchaseLayout completedPurchaseLayout =
        new CompletedPurchaseLayout(activity, orientation);
    completedPurchaseView =
        completedPurchaseLayout.buildView(fiatPrice, fiatCurrency, sku, packageName);
    completedPurchaseView.setVisibility(View.INVISIBLE);

    loadingView = buildLoadingView();
    loadingView.setVisibility(View.INVISIBLE);

    dialogLayout = buildDialogLayout(fiatPrice, fiatCurrency, appcPrice, sku, packageName);
    dialogLayout.setVisibility(View.INVISIBLE);
    mainLayout.addView(dialogLayout);
    mainLayout.addView(errorView);
    mainLayout.addView(loadingView);
    mainLayout.addView(completedPurchaseView);
    return mainLayout;
  }

  public void onDestroyView() {
    cardNumberEditText = null;
    expiryDateEditText = null;
    cvvEditText = null;
    errorView = null;
    dialogLayout = null;
    fiatPriceView = null;
    cancelButton = null;
    positiveButton = null;
    buttonsView = null;
    morePaymentsText = null;
    changeCard = null;
    loadingView = null;
    paymentErrorViewLayout = null;
    creditCardEditTextLayout = null;
    completedPurchaseView = null;
  }

  private RelativeLayout buildLoadingView() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(262), dpToPx(254));
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    relativeLayout.setLayoutParams(layoutParams);

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    relativeLayout.setBackground(gradientDrawable);

    ProgressBar progressBar = buildProgressBar();

    relativeLayout.addView(progressBar);
    return relativeLayout;
  }

  private RelativeLayout buildDialogLayout(String fiatPrice, String fiatCurrency, String appcPrice,
      String sku, String packageName) {
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

    ProgressBar progressBar = buildProgressBar();
    progressBar.setVisibility(View.INVISIBLE);

    RelativeLayout paymentMethodsHeaderLayout =
        buildPaymentMethodsHeaderLayout(packageName, sku, fiatPrice, fiatCurrency, appcPrice);
    View headerSeparator = buildHeaderSeparatorLayout();
    RelativeLayout creditCardLayout = buildCreditCardLayout();
    buttonsView = buildButtonsView();
    buttonsView.setVisibility(View.INVISIBLE);

    LinearLayout supportHookView = buildSupportHook();

    dialogLayout.addView(progressBar);
    dialogLayout.addView(paymentMethodsHeaderLayout);
    dialogLayout.addView(headerSeparator);
    dialogLayout.addView(creditCardLayout);
    dialogLayout.addView(buttonsView);
    dialogLayout.addView(supportHookView);
    return dialogLayout;
  }

  private LinearLayout buildSupportHook() {
    LinearLayout linearLayout = new LinearLayout(activity);

    float[] radius;
    RelativeLayout.LayoutParams layoutParams;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      layoutParams =
          new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(32));
      radius = getCornerRadiusArray(0, 0, 8, 8);
      layoutParams.addRule(RelativeLayout.BELOW, buttonsViewId);
    } else {
      layoutParams =
          new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(32));
      radius = getCornerRadiusArray(16, 16, 16, 16);
      layoutParams.addRule(RelativeLayout.BELOW, creditCardViewId);
      setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_LEFT);
      setMargins(layoutParams, 18, 42, 0, 16);
    }

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.parseColor("#f0f0f0"));
    gradientDrawable.setCornerRadii(radius);
    linearLayout.setBackground(gradientDrawable);

    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
    linearLayout.setGravity(Gravity.CENTER);
    ImageView supportImage = buildSupportImage();
    helpText = buildHelpText();

    linearLayout.addView(supportImage);
    linearLayout.addView(helpText);
    linearLayout.setLayoutParams(layoutParams);
    return linearLayout;
  }

  private TextView buildHelpText() {
    TextView textView = new TextView(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setMargins(layoutParams, 0, 0, 14, 0);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#202020"));
    textView.setTextSize(12);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView buildSupportImage() {
    ImageView imageView = new ImageView(activity);
    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpToPx(18), dpToPx(18));
    setMargins(layoutParams, 14, 0, 8, 0);
    Drawable supportImage =
        convertAssetDrawable(SUPPORT_RESOURCE_PATH + densityPath + "ic_settings_support.png");
    imageView.setImageDrawable(supportImage);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private LinearLayout buildButtonsView() {
    LinearLayout linearLayout = new LinearLayout(activity);

    buttonsViewId = generateRandomId();
    linearLayout.setId(buttonsViewId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, creditCardViewId);

    int end, top, bottom;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      end = 12;
      top = 60;
      bottom = 24;
    } else {
      top = 42;
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
    button.setText(translationModel.getBuyButton());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private Button buildCancelButtonLayout() {
    Button button = new Button(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dpToPx(36));
    layoutParams.gravity = Gravity.CENTER_VERTICAL;
    setMargins(layoutParams, 0, 0, 16, 0);
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
    button.setText(translationModel.getCancelButton());
    button.setLayoutParams(layoutParams);
    return button;
  }

  private RelativeLayout buildCreditCardLayout() {
    RelativeLayout parentLayout = new RelativeLayout(activity);
    creditCardViewId = generateRandomId();
    parentLayout.setId(creditCardViewId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, headerId);
    parentLayout.setLayoutParams(layoutParams);

    RelativeLayout creditCardHeader = buildCreditCardHeader();
    creditCardEditTextLayout = buildCreditCardEditTextLayout();
    changeCard = buildChangeCardTextLayout();
    morePaymentsText = buildMorePaymentsView();

    changeCard.setVisibility(View.GONE);
    parentLayout.addView(creditCardHeader);
    parentLayout.addView(creditCardEditTextLayout);
    parentLayout.addView(changeCard);
    parentLayout.addView(morePaymentsText);

    return parentLayout;
  }

  private TextView buildChangeCardTextLayout() {
    TextView textView = new TextView(activity);

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int top, constraint;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      constraint = RelativeLayout.ALIGN_END;
      top = 26;
    } else {
      constraint = RelativeLayout.ALIGN_START;
      top = 16;
    }
    layoutParams.addRule(RelativeLayout.BELOW, creditCardInputId);
    setConstraint(layoutParams, constraint, creditCardInputId);
    setMargins(layoutParams, 0, top, 0, 0);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#fd786b"));
    textView.setTextSize(12);
    textView.setText(translationModel.getChangeCard());
    textView.setLayoutParams(layoutParams);

    return textView;
  }

  private TextView buildMorePaymentsView() {
    TextView textView = new TextView(activity);

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    int end, top, belowId;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      belowId = creditCardInputId;
      end = 14;
      top = 88;
    } else {
      belowId = headerId;
      end = 38;
      top = 66;
      textView.setMaxWidth(dpToPx(152));
      textView.setGravity(Gravity.CENTER);
    }
    layoutParams.addRule(RelativeLayout.BELOW, belowId);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_END);
    setMargins(layoutParams, 0, top, end, 0);
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#fd786b"));
    textView.setTextSize(12);
    textView.setText(translationModel.getMorePaymentMethods());
    textView.setLayoutParams(layoutParams);

    return textView;
  }

  private CreditCardLayout buildCreditCardEditTextLayout() {
    CreditCardLayout creditCardLayout = new CreditCardLayout(activity);

    creditCardInputId = generateRandomId();
    creditCardLayout.setId(creditCardInputId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpToPx(44));
    layoutParams.addRule(RelativeLayout.BELOW, creditCardHeaderId);
    setConstraint(layoutParams, RelativeLayout.ALIGN_LEFT, creditCardHeaderId);
    setConstraint(layoutParams, RelativeLayout.ALIGN_RIGHT, creditCardHeaderId);
    setMargins(layoutParams, 0, 28, 0, 0);

    GradientDrawable background = new GradientDrawable();
    background.setShape(GradientDrawable.RECTANGLE);
    background.setStroke(dpToPx(1), Color.parseColor("#fd7a6a"));
    background.setCornerRadius(dpToPx(6));
    creditCardLayout.setBackground(background);

    ImageView genericCardView = buildGenericCardView();
    cardNumberEditText = buildCardNumberEditText();
    expiryDateEditText = buildExpiryDateEditText();
    cvvEditText = buildCvvEditText();

    CardNumberFocusChangeListener cardNumberFocusChangeListener =
        new CardNumberFocusChangeListener(cardNumberEditText, expiryDateEditText, cvvEditText);
    cardNumberEditText.addTextChangedListener(
        new CardNumberTextWatcher(creditCardLayout, cardNumberEditText, expiryDateEditText,
            cvvEditText));
    cardNumberEditText.setOnFocusChangeListener(cardNumberFocusChangeListener);
    expiryDateEditText.addTextChangedListener(
        new ExpiryDateTextWatcher(creditCardLayout, expiryDateEditText, cvvEditText,
            cardNumberEditText));
    cvvEditText.addTextChangedListener(
        new CvvTextWatcher(creditCardLayout, cvvEditText, expiryDateEditText));

    expiryDateEditText.setVisibility(View.INVISIBLE);
    cvvEditText.setVisibility(View.INVISIBLE);
    creditCardLayout.addView(genericCardView);
    creditCardLayout.addView(cardNumberEditText);
    creditCardLayout.addView(expiryDateEditText);
    creditCardLayout.addView(cvvEditText);

    creditCardLayout.setLayoutParams(layoutParams);
    return creditCardLayout;
  }

  private ImageView buildGenericCardView() {
    ImageView imageView = new ImageView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(30), dpToPx(19));
    genericCardId = generateRandomId();
    imageView.setId(genericCardId);
    setConstraint(layoutParams, RelativeLayout.CENTER_VERTICAL);
    setMargins(layoutParams, 8, 0, 0, 0);
    Drawable genericCreditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "generic_card/" + densityPath + "generic_card.png");
    setMargins(layoutParams, 10, 0, 0, 0);
    imageView.setImageDrawable(genericCreditCard);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private EditText buildCvvEditText() {
    EditText editText = new EditText(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(60), ViewGroup.LayoutParams.MATCH_PARENT);
    editText.setFilters(new InputFilter[] {
        new InputFilter.LengthFilter(CardValidationUtils.CVV_MAX_LENGTH)
    });
    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);
    setMargins(layoutParams, 0, 0, 12, 0);
    editText.setHint(translationModel.getCardCvv());
    editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    editText.setHintTextColor(Color.parseColor("#9d9d9d"));
    editText.setLayoutParams(layoutParams);
    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    editText.setBackgroundColor(Color.parseColor("#00000000"));
    editText.setTextSize(14);
    editText.setTextColor(Color.parseColor("#292929"));
    editText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

    return editText;
  }

  private EditText buildExpiryDateEditText() {
    EditText editText = new EditText(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(70), ViewGroup.LayoutParams.MATCH_PARENT);
    editText.setFilters(new InputFilter[] {
        new InputFilter.LengthFilter(CardValidationUtils.DATE_MAX_LENGTH)
    });
    editText.setHint(translationModel.getExpiryDate());
    editText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    editText.setHintTextColor(Color.parseColor("#9d9d9d"));
    layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    setMargins(layoutParams, 0, 0, 104, 0);
    editText.setLayoutParams(layoutParams);
    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
    editText.setBackgroundColor(Color.parseColor("#00000000"));
    editText.setTextSize(14);
    editText.setTextColor(Color.parseColor("#292929"));
    editText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));

    return editText;
  }

  private CardNumberEditText buildCardNumberEditText() {
    CardNumberEditText cardNumberEditText = new CardNumberEditText(activity);
    cardNumberEditText.setFilters(new InputFilter[] {
        new InputFilter.LengthFilter(CardValidationUtils.MAXIMUM_CARD_NUMBER_LENGTH
            + CardValidationUtils.MAX_DIGIT_SEPARATOR_COUNT)
    });
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(140), ViewGroup.LayoutParams.MATCH_PARENT);

    layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
    setConstraint(layoutParams, RelativeLayout.RIGHT_OF, genericCardId);
    setMargins(layoutParams, 12, 0, 8, 0);

    cardNumberEditText.setTextSize(14);
    cardNumberEditText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
    cardNumberEditText.setTextColor(Color.parseColor("#292929"));
    cardNumberEditText.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
    cardNumberEditText.setHint(translationModel.getCardNumber());
    cardNumberEditText.setHintTextColor(Color.parseColor("#9d9d9d"));
    cardNumberEditText.setLayoutParams(layoutParams);
    cardNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
    cardNumberEditText.setBackgroundColor(Color.parseColor("#00000000"));

    return cardNumberEditText;
  }

  private RelativeLayout buildCreditCardHeader() {
    RelativeLayout relativeLayout = new RelativeLayout(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    creditCardHeaderId = generateRandomId();
    relativeLayout.setId(creditCardHeaderId);
    layoutParams.addRule(RelativeLayout.BELOW, headerId);

    int top, start, end;

    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 8;
      start = 14;
      end = 18;
    } else {
      top = 14;
      start = 20;
      end = 226;
    }
    setMargins(layoutParams, start, top, end, 0);
    relativeLayout.setLayoutParams(layoutParams);

    TextView payAsGuestText = buildPayAsGuestText();
    ImageView creditCardImage = buildCreditCardImage();

    relativeLayout.addView(payAsGuestText);
    relativeLayout.addView(creditCardImage);

    return relativeLayout;
  }

  private ImageView buildCreditCardImage() {
    ImageView imageView = new ImageView(activity);

    int height;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      height = 10;
    } else {
      height = 12;
    }

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(dpToPx(56), dpToPx(height));
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);

    Drawable creditCard = convertAssetDrawable(
        IMAGES_RESOURCE_PATH + "credit_card/landscape/" + densityPath + "ic_credit_card.png");
    imageView.setImageDrawable(creditCard);
    imageView.setLayoutParams(layoutParams);
    return imageView;
  }

  private TextView buildPayAsGuestText() {
    TextView textView = new TextView(activity);

    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);

    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(14);
    textView.setText(translationModel.getPayAsGuestTitle());
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private View buildHeaderSeparatorLayout() {
    View view = new View(activity);
    headerId = generateRandomId();
    view.setId(headerId);
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
    layoutParams.addRule(RelativeLayout.BELOW, paymentMethodsHeaderId);
    view.setLayoutParams(layoutParams);
    return view;
  }

  private RelativeLayout buildPaymentMethodsHeaderLayout(String packageName, String sku,
      String fiatPrice, String fiatCurrency, String appcPrice) {
    RelativeLayout paymentMethodHeaderLayout = new RelativeLayout(activity);
    paymentMethodHeaderLayout.setLayoutParams(
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT));
    paymentMethodsHeaderId = generateRandomId();
    paymentMethodHeaderLayout.setId(paymentMethodsHeaderId);
    Drawable icon = null;
    String appName = "";
    PackageManager packageManager = activity.getApplicationContext()
        .getPackageManager();
    try {
      icon = packageManager.getApplicationIcon(packageName);
      ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
      appName = packageManager.getApplicationLabel(applicationInfo)
          .toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    fiatPriceView = createFiatPriceView(fiatPrice, fiatCurrency);
    TextView appcPriceView = createAppcPriceView(appcPrice);
    ImageView iconImageView = createAppIconLayout(icon);
    TextView appNameView = createAppNameLayout(appName);
    TextView skuView = createSkuLayout(sku);

    paymentMethodHeaderLayout.addView(iconImageView);
    paymentMethodHeaderLayout.addView(appNameView);
    paymentMethodHeaderLayout.addView(skuView);
    paymentMethodHeaderLayout.addView(fiatPriceView);
    paymentMethodHeaderLayout.addView(appcPriceView);
    return paymentMethodHeaderLayout;
  }

  private TextView createAppcPriceView(String appcPrice) {
    TextView textView = new TextView(activity);
    appcPriceViewId = generateRandomId();
    textView.setId(appcPriceViewId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, fiatPriceViewId);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);
    setMargins(layoutParams, 0, 0, 16, 0);
    textView.setTextColor(Color.parseColor("#828282"));
    DecimalFormat df = new DecimalFormat("0.00");
    String appcText = df.format(new BigDecimal(appcPrice));
    textView.setText(String.format("%s APPC", appcText));
    textView.setTextSize(12);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createFiatPriceView(String fiatPrice, String fiatCurrency) {
    TextView textView = new TextView(activity);
    fiatPriceViewId = generateRandomId();
    textView.setId(fiatPriceViewId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setConstraint(layoutParams, RelativeLayout.ALIGN_PARENT_RIGHT);
    setMargins(layoutParams, 0, 17, 16, 0);
    textView.setTextColor(Color.parseColor("#000000"));
    textView.setTextSize(15);
    textView.setText(String.format("%s %s", fiatPrice, fiatCurrency));
    textView.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createSkuLayout(String sku) {
    TextView textView = new TextView(activity);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.BELOW, appNameId);
    setConstraint(layoutParams, RelativeLayout.LEFT_OF, appcPriceViewId);
    setConstraint(layoutParams, RelativeLayout.RIGHT_OF, appIconId);
    setMargins(layoutParams, 10, 0, 12, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTextColor(Color.parseColor("#8a000000"));
    textView.setTextSize(12);
    textView.setText(sku);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createAppNameLayout(String appName) {
    TextView textView = new TextView(activity);
    appNameId = generateRandomId();
    textView.setId(appNameId);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    setConstraint(layoutParams, RelativeLayout.LEFT_OF, fiatPriceViewId);
    setConstraint(layoutParams, RelativeLayout.RIGHT_OF, appIconId);
    setMargins(layoutParams, 10, 15, 12, 0);
    textView.setEllipsize(TextUtils.TruncateAt.END);
    textView.setMaxLines(1);
    textView.setTextColor(Color.parseColor("#de000000"));
    textView.setTextSize(16);
    textView.setText(appName);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView createAppIconLayout(Drawable icon) {
    ImageView imageView = new ImageView(activity);
    appIconId = generateRandomId();
    imageView.setId(appIconId);
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

  private ProgressBar buildProgressBar() {
    ProgressBar progressBar = new ProgressBar(activity);
    progressBar.getIndeterminateDrawable()
        .setColorFilter(Color.parseColor("#fd786b"), PorterDuff.Mode.MULTIPLY);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    progressBar.setLayoutParams(layoutParams);
    return progressBar;
  }

  private RelativeLayout buildMainLayout() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(activity);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  public ViewGroup getErrorView() {
    return errorView;
  }

  public ViewGroup getDialogLayout() {
    return dialogLayout;
  }

  public TextView getFiatPriceView() {
    return fiatPriceView;
  }

  public CardNumberEditText getCardNumberEditText() {
    return cardNumberEditText;
  }

  public EditText getExpiryDateEditText() {
    return expiryDateEditText;
  }

  public EditText getCvvEditText() {
    return cvvEditText;
  }

  public Button getCancelButton() {
    return cancelButton;
  }

  public Button getPositiveButton() {
    return positiveButton;
  }

  public ViewGroup getButtonsView() {
    return buttonsView;
  }

  public TextView getMorePaymentsText() {
    return morePaymentsText;
  }

  public TextView getChangeCardView() {
    return changeCard;
  }

  public ViewGroup getLoadingView() {
    return loadingView;
  }

  public PaymentErrorViewLayout getPaymentErrorViewLayout() {
    return paymentErrorViewLayout;
  }

  public CreditCardLayout getCreditCardEditTextLayout() {
    return creditCardEditTextLayout;
  }

  public ViewGroup getCompletedPurchaseView() {
    return completedPurchaseView;
  }

  public TextView getHelpText() {
    return helpText;
  }
}
