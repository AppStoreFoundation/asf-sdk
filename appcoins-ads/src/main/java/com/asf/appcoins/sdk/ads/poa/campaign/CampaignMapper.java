package com.asf.appcoins.sdk.ads.poa.campaign;

import com.appcoins.net.AppcoinsClientResponse;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CampaignMapper {

  public static Campaign mapCampaign(AppcoinsClientResponse response) {
    String jsonResponse = response.getMsg();
    String bigIdString = GetIntValue("bidId", jsonResponse);
    if (!bigIdString.isEmpty()) {
      BigInteger bigId = new BigInteger(bigIdString);
      String packageName = GetStringValue("packageName", jsonResponse);
      if (!packageName.isEmpty()) {
        Campaign campaign = new Campaign(bigId, packageName);
        return campaign;
      }
    }
    return new Campaign(new BigInteger("0"),"");
  }

  public static String GetIntValue(String paramName, String response) {

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

  public static float GetFloatValue(String paramName, String response) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)([\\d]*(.|,)?[\\d]*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(response);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      return Float.parseFloat(val);
    }

    //TODO throw error
    return -1.0f;
  }

  private static String GetStringValue(String paramName, String response) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)(.*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(response);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      //TODO should remove quotes and commas on the regex itself!
      val = val.replaceAll("\"", "");
      val = val.replaceAll(",", "");
      return val;
    }

    //TODO throw error
    return "";
  }
}

