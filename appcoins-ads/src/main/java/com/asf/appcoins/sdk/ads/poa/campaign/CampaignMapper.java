package com.asf.appcoins.sdk.ads.poa.campaign;

import android.util.Log;
import com.asf.appcoins.sdk.ads.net.responses.AppCoinsClientResponse;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampaignMapper {

  public static Campaign mapCampaign(AppCoinsClientResponse response) {
    String jsonResponse = response.getMsg();
    String bigIdString = GetBigIntValue("bidId", jsonResponse);
    if (!bigIdString.isEmpty()) {
      BigInteger bigId = new BigInteger(bigIdString);
      String packageName = GetStringValue("packageName", jsonResponse);
      if (!packageName.isEmpty()) {
        Campaign campaign = new Campaign(bigId, packageName);
        return campaign;
      }
    }
    return new Campaign(new BigInteger("-1"), "");
  }

  public static String GetBigIntValue(String paramName, String response) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)([\\d]*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(response);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      return val;
    }

    return "";
  }

  private static String GetStringValue(String paramName, String response) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)(\".*?\")";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(response);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      val = val.replaceAll("\"", "");
      return val;
    }

    return "";
  }
}

