package com.appcoins.sdk.billing.payasguest;

import android.content.Context;
import android.content.pm.PackageManager;
import com.aptoide.apk.injector.extractor.domain.IExtract;
import java.io.File;

public class OemIdExtractorV2 implements IExtractOemId {

  private final Context context;
  private final IExtract extractor;

  public OemIdExtractorV2(Context context, IExtract iExtract) {

    this.context = context;
    this.extractor = iExtract;
  }

  @Override public String extract(String packageName) {
    String oemId = "";
    try {
      String sourceDir = getPackageName(context, packageName);
      String rawOemId = extractor.extract(new File(sourceDir));
      String[] split = rawOemId.split(",");
      if (split.length > 0) {
        oemId = split[0];
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return oemId;
  }

  private String getPackageName(Context context, String packageName)
      throws PackageManager.NameNotFoundException {
    return context.getPackageManager()
        .getPackageInfo(packageName, 0).applicationInfo.sourceDir;
  }
}
