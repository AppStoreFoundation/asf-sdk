package com.asf.appcoins.sdk.ads.poa.campaign;

import android.util.Log;
import com.appcoins.net.AppcoinsClientResponse;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;

public class CampainMapper {

  public static Campaign mapCampaign(AppcoinsClientResponse response) throws JSONException {
    String jsonResponse = response.getMsg();
    Log.d("Result Response: ", jsonResponse);
    BigInteger bigId = new BigInteger(GetIntValue("bidId",jsonResponse));
    String packageName = GetStringValue("packageName",jsonResponse);
    Log.d("Result: ",packageName+" "+bigId.toString());
    Campaign campaign = new Campaign(bigId,packageName);
    return campaign;
  }

  public static String GetIntValue(String paramName,String response) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)([\\d]*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(response);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      return val;
    }

    //TODO throw error
    return "";
  }

  public static float GetFloatValue(String paramName,String response) {

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

  private static String GetStringValue(String paramName,String response) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)(.*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(response);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      //TODO should remove quotes and commas on the regex itself!
      val = val.replaceAll("\"","");
      val = val.replaceAll(",","");
      return val;
    }

    //TODO throw error
    return "";
  }
}

