package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.mappers.PaymentMethodsResponseMapper;
import com.appcoins.sdk.billing.models.payasguest.PaymentMethod;
import com.appcoins.sdk.billing.models.payasguest.PaymentMethodsModel;
import com.appcoins.sdk.billing.service.RequestResponse;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PaymentAdyenPaymentMethodResponseMapperTest {

  private PaymentMethodsResponseMapper paymentMethodsResponseMapper;

  @Before public void setup() {
    paymentMethodsResponseMapper = new PaymentMethodsResponseMapper();
  }

  @Test public void paymentMethodsResponseSuccessTest() {
    String response = "{\n"
        + "\t\"next\": null,\n"
        + "\t\"items\": [{\n"
        + "\t\t\"name\": \"credit_card\",\n"
        + "\t\t\"label\": \"Credit Card\",\n"
        + "\t\t\"icon\": \"https:\\/\\/cdn6.aptoide"
        + ".com\\/imgs\\/b\\/4\\/b\\/b4bd2e30853b976ec80544b7f8c0a0d7_logo.png\",\n"
        + "\t\t\"status\": \"AVAILABLE\",\n"
        + "\t\t\"message\": null,\n"
        + "\t\t\"gateway\": {\n"
        + "\t\t\t\"name\": \"adyen\"\n"
        + "\t\t}\n"
        + "\t}, {\n"
        + "\t\t\"name\": \"paypal\",\n"
        + "\t\t\"label\": \"PayPal\",\n"
        + "\t\t\"icon\": \"https:\\/\\/cdn6.aptoide"
        + ".com\\/imgs\\/0\\/2\\/b\\/02b57118b06b81958ab1baf4788ce09d_logo.png\",\n"
        + "\t\t\"status\": \"AVAILABLE\",\n"
        + "\t\t\"message\": null,\n"
        + "\t\t\"gateway\": {\n"
        + "\t\t\t\"name\": \"adyen\"\n"
        + "\t\t}\n"
        + "\t}]\n"
        + "}";
    PaymentMethodsModel paymentMethodsModel =
        paymentMethodsResponseMapper.map(new RequestResponse(200, response, null));
    List<PaymentMethod> paymentMethods = paymentMethodsModel.getPaymentMethods();
    List<String> paymentNames = Arrays.asList("credit_card", "paypal");
    for (int i = 0; i < paymentMethods.size(); i++) {
      Assert.assertEquals(paymentNames.get(i), paymentMethods.get(i)
          .getName());
      Assert.assertTrue(paymentMethods.get(i)
          .isAvailable());
    }
  }

  @Test public void paymentMethodsResponseNotAvailableTest() {
    String response = "{\n"
        + "\t\"next\": null,\n"
        + "\t\"items\": [{\n"
        + "\t\t\"name\": \"credit_card\",\n"
        + "\t\t\"label\": \"Credit Card\",\n"
        + "\t\t\"icon\": \"https:\\/\\/cdn6.aptoide"
        + ".com\\/imgs\\/b\\/4\\/b\\/b4bd2e30853b976ec80544b7f8c0a0d7_logo.png\",\n"
        + "\t\t\"status\": \"UNAVAILABLE\",\n"
        + "\t\t\"message\": null,\n"
        + "\t\t\"gateway\": {\n"
        + "\t\t\t\"name\": \"adyen\"\n"
        + "\t\t}\n"
        + "\t}, {\n"
        + "\t\t\"name\": \"paypal\",\n"
        + "\t\t\"label\": \"PayPal\",\n"
        + "\t\t\"icon\": \"https:\\/\\/cdn6.aptoide"
        + ".com\\/imgs\\/0\\/2\\/b\\/02b57118b06b81958ab1baf4788ce09d_logo.png\",\n"
        + "\t\t\"status\": \"UNAVAILABLE\",\n"
        + "\t\t\"message\": null,\n"
        + "\t\t\"gateway\": {\n"
        + "\t\t\t\"name\": \"adyen\"\n"
        + "\t\t}\n"
        + "\t}]\n"
        + "}";
    PaymentMethodsModel paymentMethodsModel =
        paymentMethodsResponseMapper.map(new RequestResponse(200, response, null));
    List<PaymentMethod> paymentMethods = paymentMethodsModel.getPaymentMethods();
    List<String> paymentNames = Arrays.asList("credit_card", "paypal");
    for (int i = 0; i < paymentMethods.size(); i++) {
      Assert.assertEquals(paymentNames.get(i), paymentMethods.get(i)
          .getName());
      Assert.assertFalse(paymentMethods.get(i)
          .isAvailable());
    }
  }
}
