package com.appcoins.sdk.billing.wallet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appcoins.sdk.android.billing.BuildConfig;
import com.appcoins.sdk.android.billing.R;
import com.appcoins.sdk.billing.helpers.InstallDialogActivity;
import com.appcoins.sdk.billing.helpers.Utils;
import com.appcoins.sdk.billing.helpers.WalletUtils;

import static android.graphics.Typeface.BOLD;
import static com.appcoins.sdk.billing.helpers.WalletUtils.context;

/**
 * Here is important to know in advance if the host app has feature graphic,
 * 1- this boolean hasImage is needed to change layout dynamically
 * 2- if so, we need to get  url of this image and then when copy this code to  apk-migrator
 * as Smali,
 * the correct dialog_wallet_install_graphic needs to be write
 */
public class DialogWalletInstall extends Dialog {

  private Button dialog_wallet_install_button_cancel;
  private Button dialog_wallet_install_button_download;
  private TextView dialog_wallet_install_text_message;
  private ImageView dialog_wallet_install_image_icon;
  private ImageView dialog_wallet_install_image_graphic;
  private int RESULT_USER_CANCELED = 1;

  private boolean hasImage;

  private final String URL_APTOIDE = "market://details?id="
      + BuildConfig.BDS_WALLET_PACKAGE_NAME
      + "&utm_source=appcoinssdk&app_source="
      + getContext().getPackageName();
  private static Context mContext;

  public static DialogWalletInstall with(Context context) {
    mContext = context;
    return new DialogWalletInstall(context);
  }

  public DialogWalletInstall(Context context) {
    super(context);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    requestWindowFeature(Window.FEATURE_NO_TITLE);

    setContentView(R.layout.wallet_install_dialog);
    setCancelable(false);

    buildTop();
    buildMessage();
    buildCancelButton();
    buildDownloadButton();
  }

  private void buildTop() {
    dialog_wallet_install_image_icon = findViewById(R.id.dialog_wallet_install_image_icon);
    dialog_wallet_install_image_graphic = findViewById(R.id.dialog_wallet_install_image_graphic);

    dialog_wallet_install_image_graphic.setOutlineProvider(new ViewOutlineProvider() {
      @Override public void getOutline(View view, Outline outline) {
        outline.setRoundRect(0, 0, view.getWidth(), (view.getHeight() + dp(12)), dp(12));
        view.setClipToOutline(true);
      }
    });

    hasImage = getContext().getResources()
        .getBoolean(R.bool.dialog_wallet_install_has_image);

    if (hasImage) {
      dialog_wallet_install_image_icon.setVisibility(View.INVISIBLE);
      RelativeLayout.LayoutParams lp =
          new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(124));
      dialog_wallet_install_image_graphic.setLayoutParams(lp);
      dialog_wallet_install_image_graphic.setImageDrawable(
          getContext().getDrawable(R.drawable.dialog_wallet_install_graphic));
    } else {
      dialog_wallet_install_image_icon.setVisibility(View.VISIBLE);
      dialog_wallet_install_image_icon.setImageDrawable(
          getContext().getDrawable(R.drawable.dialog_wallet_install_icon));
      RelativeLayout.LayoutParams lp =
          new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp(100));
      dialog_wallet_install_image_graphic.setLayoutParams(lp);
      dialog_wallet_install_image_graphic.setImageDrawable(
          getContext().getDrawable(R.drawable.dialog_wallet_install_empty_image));
    }
  }

  private void buildMessage() {
    dialog_wallet_install_text_message = findViewById(R.id.dialog_wallet_install_text_message);
    String dialog_message = getContext().getString(R.string.app_wallet_install_wallet_from_ads);

    SpannableStringBuilder messageStylized = new SpannableStringBuilder(dialog_message);

    messageStylized.setSpan(new StyleSpan(BOLD), dialog_message.indexOf("AppCoins"),
        dialog_message.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

    dialog_wallet_install_text_message.setText(messageStylized);
  }

  private void buildDownloadButton() {
    dialog_wallet_install_button_download =
        findViewById(R.id.dialog_wallet_install_button_download);
    dialog_wallet_install_button_download.setOnClickListener(new View.OnClickListener() {

      @Override public void onClick(View v) {
        redirectToStore();
        DialogWalletInstall.this.dismiss();
        if (mContext instanceof InstallDialogActivity) {
          ((Activity) mContext).setResult(RESULT_USER_CANCELED);
          ((Activity) mContext).finish();
        }
      }
    });
  }

  private void buildCancelButton() {
    dialog_wallet_install_button_cancel = findViewById(R.id.dialog_wallet_install_button_cancel);
    dialog_wallet_install_button_cancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        DialogWalletInstall.this.dismiss();
        if (mContext instanceof InstallDialogActivity) {
          ((Activity) mContext).setResult(RESULT_USER_CANCELED);
          ((Activity) mContext).finish();
        }
      }
    });
  }

  private void redirectToStore() {
    //https://developer.android.com/distribute/marketing-tools/linking-to-google-play
    getContext().startActivity(buildStoreViewIntent(URL_APTOIDE));
  }

  private Intent buildStoreViewIntent(String action) {

    final Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(action));
    if (WalletUtils.hasAptoideInstalled()) {
      appStoreIntent.setPackage(BuildConfig.APTOIDE_PACKAGE_NAME);
    }
    return appStoreIntent;
  }

  private int dp(int px) {
    int pixels = (int) (px * getContext().getResources()
        .getDisplayMetrics().density);
    DisplayMetrics displayMetrics = getContext().getResources()
        .getDisplayMetrics();
    return Math.round(pixels / (displayMetrics.xdpi / displayMetrics.densityDpi));
  }
}