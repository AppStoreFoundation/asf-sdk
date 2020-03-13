package com.appcoins.sdk.billing.layouts;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.appcoins.sdk.billing.helpers.translations.TranslationsModel;
import com.appcoins.sdk.billing.helpers.translations.TranslationsRepository;

import static com.appcoins.sdk.billing.utils.LayoutUtils.dpToPx;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setMargins;

class CompletedPurchaseLayout {

  private final Activity activity;
  private final int orientation;
  private TranslationsModel translationModel;

  CompletedPurchaseLayout(Activity activity, int orientation) {

    this.activity = activity;
    this.orientation = orientation;
  }

  ViewGroup buildView(String fiatPrice, String fiatCurrency, String sku,
      String packageName) {
    translationModel = TranslationsRepository.getInstance(activity)
        .getTranslationsModel();
    LinearLayout purchaseLayout = new LinearLayout(activity);
    purchaseLayout.setClipToPadding(false);

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    purchaseLayout.setBackground(gradientDrawable);

    int width, layoutOrientation;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      layoutOrientation = LinearLayout.VERTICAL;
      width = dpToPx(340);
    } else {
      layoutOrientation = LinearLayout.HORIZONTAL;
      width = dpToPx(544);
    }
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    purchaseLayout.setOrientation(layoutOrientation);

    ImageView appIcon = createAppIconLayout(packageName);
    TextView purchaseDoneView = createPurchaseDoneView();
    TextView purchaseDetailsView = createPurchaseDetailsView(fiatPrice, fiatCurrency, sku);

    purchaseLayout.setLayoutParams(layoutParams);
    purchaseLayout.addView(appIcon);
    purchaseLayout.addView(purchaseDoneView);
    purchaseLayout.addView(purchaseDetailsView);

    return purchaseLayout;
  }

  private TextView createPurchaseDetailsView(String fiatPrice, String fiatCurrency, String sku) {
    TextView textView = new TextView(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
    setMargins(layoutParams, 0, 6, 0, 24);
    textView.setText(String.format("%s - %s %s", sku, fiatPrice, fiatCurrency));
    textView.setTextColor(Color.BLACK);
    textView.setTypeface(null, Typeface.BOLD);
    textView.setTextSize(12);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private TextView createPurchaseDoneView() {
    TextView textView = new TextView(activity);
    LinearLayout.LayoutParams layoutParams =
        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
    setMargins(layoutParams, 0, 16, 0, 0);
    textView.setText(translationModel.getDoneTitleLong());
    textView.setTextColor(Color.parseColor("#de000000"));
    textView.setTypeface(null, Typeface.BOLD);
    textView.setTextSize(18);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView createAppIconLayout(String packageName) {
    Drawable icon = getIcon(packageName);

    ImageView imageView = new ImageView(activity);
    if (icon != null) {
      imageView.setImageDrawable(icon);
    }
    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(64), dpToPx(64));
    imageParams.gravity = Gravity.CENTER_HORIZONTAL;
    setMargins(imageParams, 0, 24, 0, 0);

    imageView.setLayoutParams(imageParams);
    return imageView;
  }

  private Drawable getIcon(String packageName) {
    PackageManager packageManager = activity.getApplicationContext()
        .getPackageManager();
    Drawable icon = null;
    try {
      icon = packageManager.getApplicationIcon(packageName);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
    return icon;
  }
}
