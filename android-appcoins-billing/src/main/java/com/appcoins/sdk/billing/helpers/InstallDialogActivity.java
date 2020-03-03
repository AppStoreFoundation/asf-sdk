package com.appcoins.sdk.billing.helpers;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.listeners.StartPurchaseAfterBindListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;

import static android.graphics.Typeface.BOLD;
import static com.appcoins.sdk.billing.helpers.CafeBazaarUtils.getUserCountry;
import static com.appcoins.sdk.billing.helpers.CafeBazaarUtils.userFromIran;

public class InstallDialogActivity extends Activity {

  public final static String KEY_BUY_INTENT = "BUY_INTENT";
  public final static String LOADING_DIALOG_CARD = "loading_dialog_install";
  public final static int REQUEST_CODE = 10001;
  public final static int ERROR_RESULT_CODE = 6;
  private final static int WEB_VIEW_REQUEST_CODE = 1234;
  private final static String TRANSLATIONS = "translations";
  private final static int MINIMUM_APTOIDE_VERSION = 9908;
  private final static int RESULT_USER_CANCELED = 1;
  private static final String DIALOG_WALLET_INSTALL_GRAPHIC = "dialog_wallet_install_graphic";
  private static final String DIALOG_WALLET_INSTALL_EMPTY_IMAGE =
      "dialog_wallet_install_empty_image";
  private static String installButtonColor = "#ffffbb33";
  private static String installButtonTextColor = "#ffffffff";
  private final String GOOGLE_PLAY_URL =
      "https://play.google.com/store/apps/details?id=" + BuildConfig.BDS_WALLET_PACKAGE_NAME;
  private final String CAFE_BAZAAR_APP_URL = "bazaar://details?id=com.hezardastan.wallet";
  private final String CAFE_BAZAAR_WEB_URL = "https://cafebazaar.ir/app/com.hezardastaan.wallet";
  private final String appBannerResourcePath = "appcoins-wallet/resources/app-banner";
  public AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  public BuyItemProperties buyItemProperties;
  private TranslationsModel translationsModel;
  private RelativeLayout installationDialog;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appcoinsBillingStubHelper = AppcoinsBillingStubHelper.getInstance();
    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);
    String storeUrl = "market://details?id="
        + BuildConfig.BDS_WALLET_PACKAGE_NAME
        + "&utm_source=appcoinssdk&app_source="
        + this.getPackageName();

    //This log is necessary for the automatic test that validates the wallet installation dialog
    Log.d("InstallDialogActivity",
        "com.appcoins.sdk.billing.helpers.InstallDialogActivity started");

    if (savedInstanceState != null) {
      translationsModel = (TranslationsModel) savedInstanceState.get(TRANSLATIONS);
    } else {
      fetchTranslations();
    }

    installationDialog = setupInstallationDialog(storeUrl);

    showInstallationDialog(installationDialog);
  }

  @Override protected void onResume() {
    super.onResume();
    if (WalletUtils.hasWalletInstalled()) {
      showLoadingDialog();
      appcoinsBillingStubHelper.createRepository(new StartPurchaseAfterBindListener() {
        @Override public void startPurchaseAfterBind() {
          makeTheStoredPurchase();
        }
      });
    }
  }

  @Override protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putSerializable(TRANSLATIONS, translationsModel);
  }

  @Override public void onBackPressed() {
    Bundle response = new Bundle();
    response.putInt(Utils.RESPONSE_CODE, RESULT_USER_CANCELED);
    Intent intent = new Intent();
    intent.putExtras(response);
    setResult(Activity.RESULT_CANCELED, intent);
    finish();
    super.onBackPressed();
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    finishActivity(resultCode, data);
  }

  private void showLoadingDialog() {
    int layoutOrientation = getLayoutOrientation();

    RelativeLayout backgroundLayout = buildBackground();

    RelativeLayout dialogLayout = buildDialogLayout(layoutOrientation);
    backgroundLayout.addView(dialogLayout);
    ProgressBar progressBar = new ProgressBar(this);
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    progressBar.setLayoutParams(layoutParams);
    dialogLayout.addView(progressBar);
    showInstallationDialog(backgroundLayout);
  }

  private void makeTheStoredPurchase() {
    Bundle intent = appcoinsBillingStubHelper.getBuyIntent(buyItemProperties.getApiVersion(),
        buyItemProperties.getPackageName(), buyItemProperties.getSku(), buyItemProperties.getType(),
        buyItemProperties.getDeveloperPayload()
            .getRawPayload());

    PendingIntent pendingIntent = intent.getParcelable(KEY_BUY_INTENT);
    try {
      if (pendingIntent != null) {
        startIntentSenderForResult(pendingIntent.getIntentSender(), REQUEST_CODE, new Intent(), 0,
            0, 0);
      } else {
        finishActivityWithError();
      }
    } catch (IntentSender.SendIntentException e) {
      finishActivityWithError();
    }
  }

  private void finishActivityWithError() {
    Intent response = new Intent();
    response.putExtra("RESPONSE_CODE", ERROR_RESULT_CODE);
    finishActivity(ERROR_RESULT_CODE, response);
  }

  private void finishActivity(int resultCode, Intent data) {
    this.setResult(resultCode, data);
    this.finish();
  }

  private RelativeLayout setupInstallationDialog(String storeUrl) {
    int layoutOrientation = getLayoutOrientation();

    RelativeLayout backgroundLayout = buildBackground();

    RelativeLayout dialogLayout = buildDialogLayout(layoutOrientation);
    backgroundLayout.addView(dialogLayout);

    ImageView appBanner = buildAppBanner();
    dialogLayout.addView(appBanner);

    ImageView appIcon = buildAppIcon(layoutOrientation, dialogLayout);
    backgroundLayout.addView(appIcon);

    TextView dialogBody = buildDialogBody(layoutOrientation, appIcon);
    backgroundLayout.addView(dialogBody);

    Button installButton =
        buildInstallButton(dialogLayout, translationsModel.getInstallationButtonString(), storeUrl);
    backgroundLayout.addView(installButton);

    Button skipButton = buildSkipButton(installButton, translationsModel.getSkipButtonString());
    backgroundLayout.addView(skipButton);

    showAppRelatedImagery(appIcon, appBanner, dialogBody);

    return backgroundLayout;
  }

  private void showInstallationDialog(RelativeLayout dialogLayout) {
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);

    setContentView(dialogLayout, layoutParams);
  }

  @SuppressLint("ResourceType") private RelativeLayout buildBackground() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(this);
    backgroundLayout.setId(1);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

  @SuppressLint("ResourceType")
  private Button buildSkipButton(Button installButton, String skipButtonText) {
    int skipButtonColor = Color.parseColor("#8f000000");
    Button skipButton = new Button(this);
    skipButton.setText(skipButtonText);
    skipButton.setTextSize(12);
    skipButton.setTextColor(skipButtonColor);
    skipButton.setId(7);
    skipButton.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
    skipButton.setBackgroundColor(Color.TRANSPARENT);
    skipButton.setIncludeFontPadding(false);
    skipButton.setClickable(true);
    skipButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, RESULT_USER_CANCELED);

        Intent intent = new Intent();
        intent.putExtras(response);

        setResult(Activity.RESULT_CANCELED, intent);
        finish();
      }
    });
    RelativeLayout.LayoutParams skipButtonParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, dpToPx(36));
    skipButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, installButton.getId());
    skipButtonParams.addRule(RelativeLayout.LEFT_OF, installButton.getId());
    skipButtonParams.setMargins(0, 0, dpToPx(80), 0);
    skipButton.setLayoutParams(skipButtonParams);
    return skipButton;
  }

  @SuppressLint("ResourceType")
  private Button buildInstallButton(RelativeLayout dialogLayout, String installButtonText,
      final String storeUrl) {
    Button installButton = new Button(this);
    installButton.setText(installButtonText);
    installButton.setTextSize(12);
    installButton.setTextColor(Color.parseColor(installButtonTextColor));
    installButton.setId(6);
    installButton.setGravity(Gravity.CENTER);
    installButton.setIncludeFontPadding(false);
    installButton.setPadding(0, 0, 0, 0);

    GradientDrawable installButtonDrawable = new GradientDrawable();
    installButtonDrawable.setColor(Color.parseColor(installButtonColor));
    installButtonDrawable.setCornerRadius(dpToPx(16));
    installButton.setBackground(installButtonDrawable);

    RelativeLayout.LayoutParams installButtonParams =
        new RelativeLayout.LayoutParams(dpToPx(110), dpToPx(36));
    installButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, dialogLayout.getId());
    installButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, dialogLayout.getId());
    installButtonParams.setMargins(0, 0, dpToPx(20), dpToPx(16));
    installButton.setLayoutParams(installButtonParams);
    installButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        redirectToWalletInstallation(storeUrl);
      }
    });
    return installButton;
  }

  private void redirectToWalletInstallation(final String storeUrl) {
    final Intent cafeBazaarIntent = buildBrowserIntent(CAFE_BAZAAR_APP_URL);
    if (WalletUtils.isCafeBazaarWalletAvailable()) {
      cafeBazaarFlow(cafeBazaarIntent, storeUrl);
    } else {
      redirectToRemainingStores(storeUrl);
    }
  }

  private void cafeBazaarFlow(Intent cafeBazaarIntent, String storeUrl) {
    if (WalletUtils.isAppInstalled(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME, getPackageManager())
        && isAbleToRedirect(cafeBazaarIntent)) {
      cafeBazaarIntent.setPackage(BuildConfig.CAFE_BAZAAR_PACKAGE_NAME);
      startActivity(cafeBazaarIntent);
    } else if (userFromIran(getUserCountry(getApplicationContext()))) {
      startActivityForBrowser(CAFE_BAZAAR_WEB_URL);
    } else {
      redirectToRemainingStores(storeUrl);
    }
  }

  private void redirectToRemainingStores(String storeUrl) {
    Intent storeIntent = buildStoreViewIntent(storeUrl);
    if (isAbleToRedirect(storeIntent)) {
      startActivity(storeIntent);
    } else {
      startActivityForBrowser(GOOGLE_PLAY_URL);
    }
  }

  private void startActivityForBrowser(String url) {
    Intent browserIntent = buildBrowserIntent(url);
    if (isAbleToRedirect(browserIntent)) {
      startActivity(browserIntent);
    } else {
      buildAlertNoBrowserAndStores();
    }
  }

  @SuppressLint("ResourceType")
  private TextView buildDialogBody(int layoutOrientation, ImageView appIcon) {
    int dialogBodyColor = Color.parseColor("#4a4a4a");
    TextView dialogBody = new TextView(this);
    dialogBody.setId(5);
    dialogBody.setMaxLines(2);
    dialogBody.setTextColor(dialogBodyColor);
    dialogBody.setTextSize(16);
    dialogBody.setGravity(Gravity.CENTER_HORIZONTAL);
    int dialogBodyWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
    int textMarginTop = dpToPx(20);
    if (layoutOrientation == Configuration.ORIENTATION_LANDSCAPE) {
      dialogBodyWidth = dpToPx(384);
      textMarginTop = dpToPx(10);
    }
    RelativeLayout.LayoutParams bodyParams =
        new RelativeLayout.LayoutParams(dialogBodyWidth, RelativeLayout.LayoutParams.WRAP_CONTENT);
    bodyParams.addRule(RelativeLayout.BELOW, appIcon.getId());
    bodyParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    bodyParams.setMargins(dpToPx(32), textMarginTop, dpToPx(32), 0);
    dialogBody.setLayoutParams(bodyParams);
    dialogBody.setText(setHighlightDialogBody());
    return dialogBody;
  }

  private SpannableStringBuilder setHighlightDialogBody() {
    String dialogBody = String.format(translationsModel.getInstallationDialogBody(),
        translationsModel.getDialogStringHighlight());
    SpannableStringBuilder messageStylized = new SpannableStringBuilder(dialogBody);
    messageStylized.setSpan(new StyleSpan(BOLD),
        dialogBody.indexOf(translationsModel.getDialogStringHighlight()),
        dialogBody.indexOf(translationsModel.getDialogStringHighlight())
            + translationsModel.getDialogStringHighlight()
            .length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    return messageStylized;
  }

  private void fetchTranslations() {
    Locale locale = Locale.getDefault();
    if (translationsModel == null || !translationsModel.getLanguageCode()
        .equalsIgnoreCase(locale.getLanguage()) || !translationsModel.getCountryCode()
        .equalsIgnoreCase(locale.getCountry())) {
      TranslationsXmlParser translationsParser = new TranslationsXmlParser(this);
      translationsModel =
          translationsParser.parseTranslationXml(locale.getLanguage(), locale.getCountry());
    }
  }

  @SuppressLint("ResourceType")
  private ImageView buildAppIcon(int layoutOrientation, RelativeLayout dialogLayout) {
    ImageView appIcon = new ImageView(this);
    appIcon.setId(4);
    appIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
    int appIconMarginTop = dpToPx(85);
    int appIconSize = dpToPx(66);
    if (layoutOrientation == Configuration.ORIENTATION_LANDSCAPE) {
      appIconMarginTop = dpToPx(80);
      appIconSize = dpToPx(80);
    }
    RelativeLayout.LayoutParams appIconParams =
        new RelativeLayout.LayoutParams(appIconSize, appIconSize);
    appIconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    appIconParams.addRule(RelativeLayout.ALIGN_TOP, dialogLayout.getId());
    appIconParams.setMargins(0, appIconMarginTop, 0, 0);
    appIcon.setLayoutParams(appIconParams);
    return appIcon;
  }

  @SuppressLint("ResourceType") private ImageView buildAppBanner() {
    ImageView appBanner = new ImageView(this);
    appBanner.setId(3);
    appBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);
    RelativeLayout.LayoutParams appBannerParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dpToPx(120));
    appBannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    appBanner.setLayoutParams(appBannerParams);
    return appBanner;
  }

  @SuppressLint("ResourceType") private RelativeLayout buildDialogLayout(int layoutOrientation) {
    RelativeLayout dialogLayout = new RelativeLayout(this);
    dialogLayout.setClipToPadding(false);
    dialogLayout.setId(2);

    dialogLayout.setBackgroundColor(Color.WHITE);

    int dialogLayoutMargins = dpToPx(12);
    int cardWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
    if (layoutOrientation == Configuration.ORIENTATION_LANDSCAPE) {
      cardWidth = dpToPx(384);
    }
    RelativeLayout.LayoutParams dialogLayoutParams =
        new RelativeLayout.LayoutParams(cardWidth, dpToPx(288));
    dialogLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    dialogLayoutParams.setMargins(dialogLayoutMargins, 0, dialogLayoutMargins, 0);
    dialogLayout.setLayoutParams(dialogLayoutParams);
    return dialogLayout;
  }

  private int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
        .getDisplayMetrics());
  }

  private Intent buildStoreViewIntent(String storeUrl) {
    final Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(storeUrl));
    if (WalletUtils.getAptoideVersion() >= MINIMUM_APTOIDE_VERSION) {
      appStoreIntent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }
    return appStoreIntent;
  }

  private void showAppRelatedImagery(ImageView appIcon, ImageView appBanner,
      TextView dialogLayout) {
    String packageName = getPackageName();
    Drawable icon = null;

    try {
      icon = this.getPackageManager()
          .getApplicationIcon(packageName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    boolean hasImage = isAppBannerAvailable();
    Drawable appBannerDrawable;

    if (hasImage) {
      appIcon.setVisibility(View.INVISIBLE);
      appBannerDrawable = fetchAppGraphicDrawable(
          appBannerResourcePath + "/" + DIALOG_WALLET_INSTALL_GRAPHIC + ".png");
      RelativeLayout.LayoutParams dialogParams =
          (RelativeLayout.LayoutParams) dialogLayout.getLayoutParams();
      int textMarginTop = dpToPx(5);
      dialogParams.setMargins(dpToPx(32), textMarginTop, dpToPx(32), 0);
    } else {
      appIcon.setVisibility(View.VISIBLE);
      appIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      appIcon.setImageDrawable(icon);
      appBannerDrawable = fetchAppGraphicDrawable(
          appBannerResourcePath + "/" + DIALOG_WALLET_INSTALL_EMPTY_IMAGE + ".png");
    }
    appBanner.setImageDrawable(appBannerDrawable);
  }

  private boolean isAppBannerAvailable() {
    boolean hasImage;
    try {
      hasImage = Arrays.asList(getAssets().list(appBannerResourcePath))
          .contains(DIALOG_WALLET_INSTALL_GRAPHIC + ".png");
    } catch (IOException e) {
      e.printStackTrace();
      hasImage = false;
    }
    return hasImage;
  }

  private Drawable fetchAppGraphicDrawable(String path) {
    InputStream inputStream = null;
    try {
      inputStream = this.getResources()
          .getAssets()
          .open(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return Drawable.createFromStream(inputStream, null);
  }

  private int getLayoutOrientation() {
    return getResources().getConfiguration().orientation;
  }

  private boolean isAbleToRedirect(Intent intent) {
    ActivityInfo activityInfo = intent.resolveActivityInfo(getPackageManager(), 0);
    return activityInfo != null;
  }

  private Intent buildBrowserIntent(String url) {
    return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
  }

  private void buildAlertNoBrowserAndStores() {
    AlertDialog.Builder alert = new AlertDialog.Builder(this);
    String value = translationsModel.getAlertDialogMessage();
    String dismissValue = translationsModel.getAlertDialogDismissButton();
    alert.setMessage(value);
    alert.setCancelable(true);
    alert.setPositiveButton(dismissValue, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        Bundle response = new Bundle();
        response.putInt(Utils.RESPONSE_CODE, RESULT_USER_CANCELED);
        Intent intent = new Intent();
        intent.putExtras(response);
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
      }
    });
    AlertDialog alertDialog = alert.create();
    alertDialog.show();
  }
}