package com.appcoins.sdk.billing.analytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import cm.aptoide.analytics.AnalyticsManager;
import cm.aptoide.analytics.EventLogger;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.helpers.WalletUtils;
import com.appcoins.sdk.billing.service.BdsService;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

class RakamEventLogger implements EventLogger {

  private BdsService rakamService;
  private WalletAddressProvider walletAddressProvider;
  private Context context;

  RakamEventLogger(BdsService rakamService, WalletAddressProvider walletAddressProvider,
      Context context) {

    this.rakamService = rakamService;
    this.walletAddressProvider = walletAddressProvider;
    this.context = context;
  }

  @Override
  public void log(String eventName, Map<String, Object> data, AnalyticsManager.Action action,
      String context) {

    Map<String, Object> body = new LinkedHashMap<>();
    Map<String, Object> api = buildApiHashMap();
    Map<String, Object> properties = buildPropertiesHashMap(data);
    body.put("collection", eventName);
    body.put("api", api);
    body.put("properties", properties);

    rakamService.makeRequest("", "POST", Collections.<String>emptyList(),
        new HashMap<String, String>(), new HashMap<String, String>(), body, null);
  }

  @Override public void setup() {

  }

  private Map<String, Object> buildPropertiesHashMap(Map<String, Object> data) {
    Map<String, Object> properties = new LinkedHashMap<>();
    putIfNotNull(properties, "_ip", true);
    putIfNotNull(properties, "_carrier", getCarrier());
    String libraryPackageName = BuildConfig.LIBRARY_PACKAGE_NAME;
    if (BuildConfig.DEBUG) {
      libraryPackageName = libraryPackageName.concat(".dev");
    }
    properties.put("aptoide_package", libraryPackageName);
    properties.put("_device_brand", Build.BRAND);
    properties.put("_device_id", Build.ID);
    properties.put("_device_manufacturer", Build.MANUFACTURER);
    properties.put("_device_model", Build.MODEL);
    properties.put("_id", UUID.randomUUID()
        .toString());
    putIfNotNull(properties, "_language", getLanguage());
    properties.put("_library_name", "rakam-android");
    properties.put("_library_version", "2.7.14");
    properties.put("_os_name", "android");
    properties.put("_os_version", Build.VERSION.RELEASE);
    properties.put("_platform", "Android");
    properties.put("_session_id", WalletUtils.getPayAsGuestSessionId());
    putIfNotNull(properties, "_user", walletAddressProvider.getWalletAddress());
    putIfNotNull(properties, "_version_name", getVersionName());
    properties.put("version_code", getVersionCode());
    addData(properties, data);
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      putIfNotNull(properties, entry.getKey(), entry.getValue());
    }
    return properties;
  }

  private void addData(Map<String, Object> properties, Map<String, Object> data) {
    for (Map.Entry<String, Object> entry : data.entrySet()) {
      Object value = entry.getValue();
      if (value instanceof Map) {
        addData(properties, data);
      }
      putIfNotNull(properties, entry.getKey(), entry.getValue());
    }
  }

  private Map<String, Object> buildApiHashMap() {
    Map<String, Object> api = new LinkedHashMap<>();
    Map<String, Object> library = new LinkedHashMap<>();

    library.put("name", "appcoins-guest-sdk");
    library.put("version", "2.7.14");

    api.put("api_key", BuildConfig.RAKAM_API_KEY);
    api.put("library", library);
    api.put("upload_time", System.currentTimeMillis());
    return api;
  }

  private String getCarrier() {
    String carrier = null;
    try {
      TelephonyManager manager =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      carrier = manager.getNetworkOperatorName();
    } catch (Exception ignored) {
    }
    return carrier;
  }

  private String getLanguage() {
    return Locale.getDefault()
        .getLanguage();
  }

  private void putIfNotNull(Map<String, Object> map, String key, Object value) {
    if (value != null) {
      map.put(key, value);
    }
  }

  private String getVersionName() {
    String versionName = null;
    try {
      PackageInfo packageInfo = getPackageInfo(context);
      versionName = packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException ignored) {
    }
    return versionName;
  }

  private long getVersionCode() {
    long versionCode = -1;
    try {
      PackageInfo packageInfo = getPackageInfo(context);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        versionCode = packageInfo.getLongVersionCode();
      } else {
        versionCode = packageInfo.versionCode;
      }
    } catch (PackageManager.NameNotFoundException ignored) {
    }
    return versionCode;
  }

  private PackageInfo getPackageInfo(Context context) throws PackageManager.NameNotFoundException {
    return context.getPackageManager()
        .getPackageInfo(context.getPackageName(), 0);
  }
}
