package com.appcoins.sdk.billing.helpers;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.BuyItemProperties;
import com.appcoins.sdk.billing.StartPurchaseAfterBindListener;
import java.util.Locale;

import static android.graphics.Typeface.BOLD;

public class InstallDialogActivity extends Activity {

  public final static String KEY_BUY_INTENT = "BUY_INTENT";
  public final static String LOADING_DIALOG_CARD = "loading_dialog_install";
  public final static int REQUEST_CODE = 10001;
  public final static int ERROR_RESULT_CODE = 6;
  private final static int MINIMUM_APTOIDE_VERSION = 9908;
  private final static int RESULT_USER_CANCELED = 1;
  private static String dialogInstallationBody;
  private static String installButtonText;
  private static String skipButtonText;
  private static String dialogHighlightString;
  private static String DIALOG_WALLET_INSTALL_GRAPHIC = "dialog_wallet_install_graphic";
  private static String DIALOG_WALLET_INSTALL_EMPTY_IMAGE = "dialog_wallet_install_empty_image";
  private static Boolean hasImage = false;
  private static String installButtonColor = "#ffffbb33";
  private static String installButtonTextColor = "#ffffffff";

  public AppcoinsBillingStubHelper appcoinsBillingStubHelper;
  public BuyItemProperties buyItemProperties;
  private String URL_APTOIDE;
  private View loadingDialogInstall;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    appcoinsBillingStubHelper = AppcoinsBillingStubHelper.getInstance();
    buyItemProperties = (BuyItemProperties) getIntent().getSerializableExtra(
        AppcoinsBillingStubHelper.BUY_ITEM_PROPERTIES);

    URL_APTOIDE = "market://details?id="
        + BuildConfig.BDS_WALLET_PACKAGE_NAME
        + "&utm_source=appcoinssdk&app_source="
        + this.getPackageName();

    RelativeLayout installationDialog = setupInstallationDialog();

    showInstallationDialog(installationDialog);
  }

  @Override protected void onResume() {
    super.onResume();
    if (WalletUtils.hasWalletInstalled()) {
      showLoadingDialog();
      loadingDialogInstall.setVisibility(View.VISIBLE);
      appcoinsBillingStubHelper.createRepository(new StartPurchaseAfterBindListener() {
        @Override public void startPurchaseAfterBind() {
          makeTheStoredPurchase();
        }
      });
    }
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
    this.setContentView(this.getResources()
        .getIdentifier(LOADING_DIALOG_CARD, "layout", this.getPackageName()));
    loadingDialogInstall = this.findViewById(this.getResources()
        .getIdentifier(LOADING_DIALOG_CARD, "id", this.getPackageName()));
    loadingDialogInstall.setVisibility(View.VISIBLE);
  }

  public void makeTheStoredPurchase() {
    Bundle intent = appcoinsBillingStubHelper.getBuyIntent(buyItemProperties.getApiVersion(),
        buyItemProperties.getPackageName(), buyItemProperties.getSku(), buyItemProperties.getType(),
        buyItemProperties.getDeveloperPayload());

    PendingIntent pendingIntent = intent.getParcelable(KEY_BUY_INTENT);
    try {
      loadingDialogInstall.setVisibility(View.INVISIBLE);
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

  private RelativeLayout setupInstallationDialog() {
    int layoutOrientation = getLayoutOrientation();

    RelativeLayout backgroundLayout = buildBackground();

    CardView cardLayout = buildCardView(layoutOrientation);
    backgroundLayout.addView(cardLayout);

    ImageView appBanner = buildAppBanner();
    cardLayout.addView(appBanner);

    ImageView appIcon = buildAppIcon(layoutOrientation, cardLayout);
    backgroundLayout.addView(appIcon);

    TextView dialogBody = buildDialogBody(layoutOrientation, appIcon);
    backgroundLayout.addView(dialogBody);

    Button installButton = buildInstallButton(cardLayout, installButtonText);
    backgroundLayout.addView(installButton);

    Button skipButton = buildSkipButton(installButton, skipButtonText);
    backgroundLayout.addView(skipButton);

    showAppRelatedImagery(appIcon, appBanner);

    return backgroundLayout;
  }

  private void showInstallationDialog(RelativeLayout dialogLayout) {
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.MATCH_PARENT);

    setContentView(dialogLayout, layoutParams);
  }

  private RelativeLayout buildBackground() {
    int backgroundColor = Color.parseColor("#64000000");
    RelativeLayout backgroundLayout = new RelativeLayout(this);
    backgroundLayout.setId(1);
    backgroundLayout.setBackgroundColor(backgroundColor);
    return backgroundLayout;
  }

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

  private Button buildInstallButton(CardView cardLayout, String installButtonText) {
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
    installButtonParams.addRule(RelativeLayout.ALIGN_BOTTOM, cardLayout.getId());
    installButtonParams.addRule(RelativeLayout.ALIGN_RIGHT, cardLayout.getId());
    installButtonParams.setMargins(0, 0, dpToPx(20), dpToPx(16));
    installButton.setLayoutParams(installButtonParams);
    installButton.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        redirectToStore();
      }
    });
    return installButton;
  }

  private TextView buildDialogBody(int layoutOrientation, ImageView appIcon) {
    setupTranslations();
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
    formatDialogBody(dialogBody);
    return dialogBody;
  }

  private void formatDialogBody(TextView dialogBody) {
    SpannableStringBuilder messageStylized = new SpannableStringBuilder(dialogInstallationBody);
    messageStylized.setSpan(new StyleSpan(BOLD),
        dialogInstallationBody.indexOf(dialogHighlightString),
        dialogInstallationBody.indexOf("AppCoins") + dialogHighlightString.length(),
        Spannable.SPAN_INCLUSIVE_INCLUSIVE);
    dialogBody.setText(messageStylized);
  }

  private void setupTranslations() {
    switch (Locale.getDefault()
        .getCountry()) {
      case "por":
        dialogInstallationBody = "Para completar a venda, você deve instalar AppCoins wallet";
        installButtonText = "INSTALAR";
        skipButtonText = "PULAR";
        dialogHighlightString = "AppCoins wallet";
        break;
      case "spa":
        dialogInstallationBody = "Para completar la venta, debe instalar AppCoins wallet";
        installButtonText = "INSTALACIÓN";
        skipButtonText = "OMITIR";
        dialogHighlightString = "AppCoins wallet";
        break;
      case "deu":
        dialogInstallationBody =
            "Um den Verkauf abzuschließen, müssen Sie die AppCoins-Geldbörse installieren";
        installButtonText = "INSTALLIEREN";
        skipButtonText = "ÜBERSPRINGEN";
        dialogHighlightString = "AppCoins-Geldbörse";
        break;
      default:
        dialogInstallationBody =
            "To complete your purchase, you have to install an AppCoins wallet";
        installButtonText = "INSTALL";
        skipButtonText = "SKIP";
        dialogHighlightString = "AppCoins wallet";
    }
    Log.d("tag123", "Country: " + Locale.getDefault()
        .getCountry()
        .toLowerCase());
  }

  private ImageView buildAppIcon(int layoutOrientation, CardView cardView) {
    ImageView appIcon = new ImageView(this);
    appIcon.setId(4);
    ViewCompat.setElevation(appIcon, 30);
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
    appIconParams.addRule(RelativeLayout.ALIGN_TOP, cardView.getId());
    appIconParams.setMargins(0, appIconMarginTop, 0, 0);
    appIcon.setLayoutParams(appIconParams);
    return appIcon;
  }

  private ImageView buildAppBanner() {
    ImageView appBanner = new ImageView(this);
    appBanner.setId(3);
    appBanner.setScaleType(ImageView.ScaleType.CENTER_CROP);
    RelativeLayout.LayoutParams appBannerParams =
        new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, dpToPx(120));
    appBannerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
    appBanner.setLayoutParams(appBannerParams);
    return appBanner;
  }

  private CardView buildCardView(int layoutOrientation) {
    CardView cardLayout = new CardView(this);
    cardLayout.setClipToPadding(false);
    cardLayout.setId(2);
    cardLayout.setRadius(dpToPx(12));
    ViewCompat.setElevation(cardLayout, 0);
    cardLayout.setCardBackgroundColor(Color.WHITE);
    int cardLayoutMargins = dpToPx(12);
    int cardWidth = RelativeLayout.LayoutParams.MATCH_PARENT;
    if (layoutOrientation == Configuration.ORIENTATION_LANDSCAPE) {
      cardWidth = dpToPx(384);
    }
    RelativeLayout.LayoutParams cardLayoutParams =
        new RelativeLayout.LayoutParams(cardWidth, dpToPx(288));
    cardLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    cardLayoutParams.setMargins(cardLayoutMargins, 0, cardLayoutMargins, 0);
    cardLayout.setLayoutParams(cardLayoutParams);
    return cardLayout;
  }

  private int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
        .getDisplayMetrics());
  }

  private void redirectToStore() {
    this.startActivity(buildStoreViewIntent(URL_APTOIDE));
  }

  private Intent buildStoreViewIntent(String action) {
    final Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(action));
    if (WalletUtils.getAptoideVersion() >= MINIMUM_APTOIDE_VERSION) {
      appStoreIntent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }
    return appStoreIntent;
  }

  private void showAppRelatedImagery(ImageView appIcon, ImageView appBanner) {
    String packageName = getPackageName();
    Drawable icon = null;
    try {
      icon = this.getPackageManager()
          .getApplicationIcon(packageName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    if (hasImage) {
      appIcon.setVisibility(View.INVISIBLE);
      int resourceId = this.getResources()
          .getIdentifier(DIALOG_WALLET_INSTALL_GRAPHIC, "raw", packageName);
      appBanner.setImageDrawable(this.getResources()
          .getDrawable(resourceId));
    } else {
      appIcon.setVisibility(View.VISIBLE);
      appIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);
      appIcon.setImageDrawable(icon);
      int resourceId = this.getResources()
          .getIdentifier(DIALOG_WALLET_INSTALL_EMPTY_IMAGE, "raw", packageName);
      appBanner.setBackgroundResource(resourceId);
    }
  }

  private int getLayoutOrientation() {
    return getResources().getConfiguration().orientation;
  }
}