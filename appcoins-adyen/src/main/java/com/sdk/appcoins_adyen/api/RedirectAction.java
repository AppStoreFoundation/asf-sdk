package com.sdk.appcoins_adyen.api;

import android.os.Parcel;
import com.sdk.appcoins_adyen.exceptions.ModelSerializationException;
import com.sdk.appcoins_adyen.utils.JsonUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RedirectAction extends Action {

  public static final Creator<RedirectAction> CREATOR = new Creator<>(RedirectAction.class);

  public static final String ACTION_TYPE = "redirect";

  private static final String METHOD = "method";
  private static final String URL = "url";

  public static final Serializer<RedirectAction> SERIALIZER = new Serializer<RedirectAction>() {

    @Override public JSONObject serialize(RedirectAction modelObject) {
      final JSONObject jsonObject = new JSONObject();
      try {
        // Get parameters from parent class
        jsonObject.putOpt(Action.TYPE, modelObject.getType());
        jsonObject.putOpt(Action.PAYMENT_DATA, modelObject.getPaymentData());
        jsonObject.putOpt(Action.PAYMENT_METHOD_TYPE, modelObject.getPaymentMethodType());

        jsonObject.putOpt(METHOD, modelObject.getMethod());
        jsonObject.putOpt(URL, modelObject.getUrl());
      } catch (JSONException e) {
        throw new ModelSerializationException(RedirectAction.class, e);
      }
      return jsonObject;
    }

    @Override public RedirectAction deserialize(JSONObject jsonObject) {
      final RedirectAction redirectAction = new RedirectAction();

      // getting parameters from parent class
      redirectAction.setType(jsonObject.optString(Action.TYPE, ""));
      redirectAction.setPaymentData(jsonObject.optString(Action.PAYMENT_DATA, ""));
      redirectAction.setPaymentMethodType(jsonObject.optString(Action.PAYMENT_METHOD_TYPE, ""));

      redirectAction.setMethod(jsonObject.optString(METHOD, ""));
      redirectAction.setUrl(jsonObject.optString(URL, ""));
      return redirectAction;
    }
  };

  private String method;
  private String url;

  @Override public void writeToParcel(Parcel dest, int flags) {
    JsonUtils.writeToParcel(dest, SERIALIZER.serialize(this));
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
