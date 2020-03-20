package com.appcoins.sdk.billing.analytics;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import cm.aptoide.analytics.AnalyticsManager;
import cm.aptoide.analytics.EventLogger;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.service.BdsService;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

class RakamEventLogger implements EventLogger {

  private BdsService rakamService;
  private Context context;

  RakamEventLogger(BdsService rakamService, Context context) {

    this.rakamService = rakamService;
    this.context = context;
  }

  /**
   * Get IP address from first non-localhost interface
   *
   * @return address or empty string
   */
  private static String getIPAddress() {
    try {
      List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
      for (NetworkInterface networkInterface : interfaces) {
        List<InetAddress> addresses = Collections.list(networkInterface.getInetAddresses());
        for (InetAddress address : addresses) {
          if (!address.isLoopbackAddress()) {
            return address.getHostAddress();
          }
        }
      }
    } catch (Exception ignored) {
    }
    return null;
  }

  @Override
  public void log(String eventName, Map<String, Object> data, AnalyticsManager.Action action,
      String context) {

    Map<String, Object> body = new LinkedHashMap<>();
    Map<String, Object> api = buildApiHashMap();
    Map<String, Object> properties = buildPropertiesHashMap(data);

    body.put("api", api);
    body.put("properties", properties);

    rakamService.makeRequest("", "POST", Collections.<String>emptyList(),
        new HashMap<String, String>(), new HashMap<String, String>(), body, null);
  }

  @Override public void setup() {

  }

  private Map<String, Object> buildPropertiesHashMap(Map<String, Object> data) {
    Map<String, Object> properties = new LinkedHashMap<>();
    putIfNotNull(properties, "__ip", "190.113.102.123"); //Not used in wallet
    putIfNotNull(properties, "_android_adid",
        "89d12e45-bfac-40b1-a465-43259fff6796");//gms id, not used in wallet
    putIfNotNull(properties, "_carrier", getCarrier());
    putIfNotNull(properties, "_city", "Claro");
    putIfNotNull(properties, "_country_code", getCountry());
    properties.put("_device_brand", Build.BRAND);
    properties.put("_device_id", Build.ID);
    properties.put("_device_manufacturer", Build.MANUFACTURER);
    properties.put("_device_model", Build.MODEL);
    properties.put("_gps_enabled", isGpsEnabled());
    properties.put("_id", UUID.randomUUID()
        .toString());
    properties.put("_language", getLanguage());
    properties.put("_latitude", 9.9004); //Not used in wallet
    properties.put("_library_name", "rakam-android"); //Maybe rakam-android
    properties.put("_library_version", "2.7.14"); //Maybe 2.7.14, SDKs?
    properties.put("_limit_ad_tracking", false);
    properties.put("_local_id", 685);
    properties.put("_longitude", -84.0726); //Not used in wallet
    properties.put("_os_name", "android");
    properties.put("_os_version", Build.VERSION.RELEASE);
    properties.put("_platform", "Android");
    properties.put("_region", "North America"); //Not used in wallet
    properties.put("_session_id", 1584408082950f); //No session in this case
    properties.put("_timezone", "America/Costa_Rica"); //Not used in wallet
    putIfNotNull(properties, "_version_name", getVersionName()); //SDKs?
    properties.put("version_code", getVersionCode()); //SDKs?
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
    library.put("version", "1"); //SDKs?

    api.put("api_key", BuildConfig.RAKAM_API_KEY);
    api.put("library", library);
    api.put("api_version", "1");
    api.put("upload_time", System.currentTimeMillis());
    api.put("uuid", "user_id"); //Not used in wallet
    return api;
  }

  private String getCarrier() {
    try {
      TelephonyManager manager =
          (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
      return manager.getNetworkOperatorName();
    } catch (Exception ignored) {
    }
    return null;
  }

  private String getCountry() {
    return Locale.getDefault()
        .getCountry();
  }

  private String getLanguage() {
    return Locale.getDefault()
        .getLanguage();
  }

  private boolean isGpsEnabled() {
    final LocationManager manager =
        (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    return !manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
  }

  private void putIfNotNull(Map<String, Object> map, String key, Object value) {
    if (value != null) {
      map.put(key, value);
    }
  }

  private String getVersionName() {
    PackageInfo packageInfo;
    try {
      packageInfo = context.getPackageManager()
          .getPackageInfo(context.getPackageName(), 0);
      return packageInfo.versionName;
    } catch (PackageManager.NameNotFoundException ignored) {
    }
    return null;
  }

  private long getVersionCode() {
    PackageInfo packageInfo;
    try {
      packageInfo = context.getPackageManager()
          .getPackageInfo(context.getPackageName(), 0);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        return packageInfo.getLongVersionCode();
      } else {
        return packageInfo.versionCode;
      }
    } catch (PackageManager.NameNotFoundException ignored) {
    }
    return -1;
  }
}
