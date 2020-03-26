package com.appcoins.sdk.billing.layouts;

import android.app.Activity;
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
import com.appcoins.sdk.billing.helpers.translations.TranslationsRepository;
import java.io.IOException;
import java.io.InputStream;

import static com.appcoins.sdk.billing.helpers.translations.TranslationsKeys.iab_purchase_done_title_long;
import static com.appcoins.sdk.billing.utils.LayoutUtils.COMPLETED_RESOURCE_PATH;
import static com.appcoins.sdk.billing.utils.LayoutUtils.dpToPx;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setBackground;
import static com.appcoins.sdk.billing.utils.LayoutUtils.setMargins;

class CompletedPurchaseLayout {

  private final Activity activity;
  private final int orientation;
  private String densityPath;
  private TranslationsRepository translations;

  CompletedPurchaseLayout(Activity activity, int orientation, String densityPath) {

    this.activity = activity;
    this.orientation = orientation;
    this.densityPath = densityPath;
  }

  ViewGroup buildView(String fiatPrice, String fiatCurrency, String sku) {
    translations = TranslationsRepository.getInstance(activity);
    LinearLayout purchaseLayout = new LinearLayout(activity);
    purchaseLayout.setClipToPadding(false);

    GradientDrawable gradientDrawable = new GradientDrawable();
    gradientDrawable.setColor(Color.WHITE);
    gradientDrawable.setCornerRadius(dpToPx(8));
    setBackground(purchaseLayout, gradientDrawable);

    int width;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      width = dpToPx(340);
    } else {
      width = dpToPx(544);
    }
    RelativeLayout.LayoutParams layoutParams =
        new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
    purchaseLayout.setOrientation(LinearLayout.VERTICAL);

    ImageView completedImage = createCompletedImageLayout();
    TextView purchaseDoneView = createPurchaseDoneView();
    TextView purchaseDetailsView = createPurchaseDetailsView(fiatPrice, fiatCurrency, sku);

    purchaseLayout.setLayoutParams(layoutParams);
    purchaseLayout.addView(completedImage);
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
    int bottom;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      bottom = 106;
    } else {
      bottom = 56;
    }
    setMargins(layoutParams, 0, 6, 0, bottom);
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
    int top;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 20;
    } else {
      top = 24;
    }
    setMargins(layoutParams, 0, top, 0, 0);
    textView.setText(translations.getString(iab_purchase_done_title_long));
    textView.setTextColor(Color.parseColor("#de000000"));
    textView.setTypeface(null, Typeface.BOLD);
    textView.setTextSize(18);
    textView.setLayoutParams(layoutParams);
    return textView;
  }

  private ImageView createCompletedImageLayout() {

    ImageView imageView = new ImageView(activity);

    LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(dpToPx(106), dpToPx(106));
    Drawable supportImage =
        convertAssetDrawable(COMPLETED_RESOURCE_PATH + densityPath + "success.png");
    imageView.setImageDrawable(supportImage);

    imageParams.gravity = Gravity.CENTER_HORIZONTAL;
    int top;
    if (orientation == Configuration.ORIENTATION_PORTRAIT) {
      top = 76;
    } else {
      top = 48;
    }
    setMargins(imageParams, 0, top, 0, 0);

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
}
