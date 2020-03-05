package com.appcoins.sdk.billing.utils;

import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class LayoutUtils {

  public static final String BUTTONS_RESOURCE_PATH = "appcoins-wallet/resources/buttons/";
  public static final String IMAGES_RESOURCE_PATH = "appcoins-wallet/resources/images/";

  public static String mapDisplayMetrics(DisplayMetrics displayMetrics) {
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

  public static void setPadding(View view, int start, int top, int end, int bottom) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      view.setPaddingRelative(dpToPx(start), dpToPx(top), dpToPx(end), dpToPx(bottom));
    } else {
      view.setPadding(dpToPx(start), dpToPx(top), dpToPx(end), dpToPx(bottom));
    }
  }

  public static int dpToPx(int dp) {
    return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem()
        .getDisplayMetrics());
  }

  public static void setMargins(ViewGroup.MarginLayoutParams layoutParams, int start, int top,
      int end, int bottom) {
    layoutParams.setMargins(0, dpToPx(top), 0, dpToPx(bottom));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.setMarginStart(dpToPx(start));
      layoutParams.setMarginEnd(dpToPx(end));
    } else {
      layoutParams.setMargins(dpToPx(start), dpToPx(top), dpToPx(end), dpToPx(bottom));
    }
  }

  public static void setConstraint(RelativeLayout.LayoutParams layoutParams, int rule, int id) {
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

  public static void setConstraint(RelativeLayout.LayoutParams layoutParams, int rule) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      layoutParams.addRule(rule);
    } else if (rule == RelativeLayout.ALIGN_PARENT_END) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
    } else if (rule == RelativeLayout.ALIGN_PARENT_START) {
      layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    }
  }

  public static int generateRandomId(int currentId) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
      return View.generateViewId();
    }
    return currentId;
  }
}
