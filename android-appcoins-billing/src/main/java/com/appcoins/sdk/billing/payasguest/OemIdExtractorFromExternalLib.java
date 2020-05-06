package com.appcoins.sdk.billing.payasguest;

import android.content.Context;
import android.content.pm.PackageManager;
import com.aptoide.apk.injector.extractor.data.ExtractorV2;
import java.io.File;

public class OemIdExtractorFromExternalLib implements OemIdExtractor {

  private final Context context;

  public OemIdExtractorFromExternalLib(Context context) {
    this.context = context;
  }

  @Override public String extract(String packageName) {
    ExtractorV2 extractor = new ExtractorV2();
    String sourceDir = getApplicationDir(packageName);
    if (sourceDir == null) return null;
    return extractor.extract(new File(sourceDir));
  }

  private String getApplicationDir(String packageName) {
    try {
      return context.getPackageManager()
          .getApplicationInfo(packageName, 0).sourceDir;
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }
}
