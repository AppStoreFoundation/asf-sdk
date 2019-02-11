package com.appcoins.net;

import java.util.regex.*;

public class AppcoinsClientResponse {

  private String _msg;

  public AppcoinsClientResponse(String msg) {
    _msg = msg;
  }

  public int GetIntValue(String paramName) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)([\\d]*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(_msg);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      return Integer.parseInt(val);
    }

    //TODO throw error
    return -1;
  }

  public float GetFloatValue(String paramName) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)([\\d]*(.|,)?[\\d]*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(_msg);
    boolean found = matcher.find();

    if (found) {
      String val = matcher.group(1);
      return Float.parseFloat(val);
    }

    //TODO throw error
    return -1.0f;
  }

  public String GetStringValue(String paramName) {

    String patternStr = "(?:\"" + paramName + "\"" + "[\\s]*:[\\s]*)(.*)";

    Pattern pattern = Pattern.compile(patternStr);
    Matcher matcher = pattern.matcher(_msg);
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
