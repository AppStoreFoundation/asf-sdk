package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.models.AdyenTransactionResponse;
import com.appcoins.sdk.billing.models.PaymentMethodsResponse;
import com.appcoins.sdk.billing.models.TransactionResponse;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.adyen.AdyenMapper;
import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AdyenMapperTest {

  private AdyenMapper adyenMapper;

  @Before public void setupTest() {
    adyenMapper = new AdyenMapper();
  }

  @Test public void transactionRequestTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"SUCCESS\"}";
    TransactionResponse transactionResponse =
        adyenMapper.mapTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(transactionResponse.hasError());
    Assert.assertNull(transactionResponse.getHash());
    Assert.assertEquals("uid", transactionResponse.getUid());
    Assert.assertEquals("reference", transactionResponse.getOrderReference());
    Assert.assertEquals("SUCCESS", transactionResponse.getStatus());
  }

  @Test public void transactionRequestErrorTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"FAIlED\"}";
    TransactionResponse transactionResponse =
        adyenMapper.mapTransactionResponse(new RequestResponse(400, response, null));
    Assert.assertTrue(transactionResponse.hasError());
  }

  @Test public void adyenCreditCardTransactionRequestTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"SUCCESS\",\"payment\": { \"pspReference\": \"pspReference\", "
        + "\"resultCode\": \"Authorised\", \"merchantReference\": \"merchant\"}}";
    AdyenTransactionResponse adyenTransactionResponse =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(adyenTransactionResponse.hasError());
    Assert.assertNull(adyenTransactionResponse.getHash());
    Assert.assertEquals("uid", adyenTransactionResponse.getUid());
    Assert.assertEquals("reference", adyenTransactionResponse.getOrderReference());
    Assert.assertEquals("SUCCESS", adyenTransactionResponse.getStatus());
    Assert.assertEquals("pspReference", adyenTransactionResponse.getPspReference());
    Assert.assertEquals("Authorised", adyenTransactionResponse.getResultCode());
    Assert.assertNull(adyenTransactionResponse.getUrl());
    Assert.assertNull(adyenTransactionResponse.getPaymentData());
    Assert.assertNull(adyenTransactionResponse.getRefusalReason());
    Assert.assertNull(adyenTransactionResponse.getRefusalReasonCode());
  }

  @Test public void adyenPaypalTransactionRequest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"SUCCESS\",\"payment\": { "
        + "\"action\": { \"paymentData\": \"data\", \"paymentMethodType\": \"paypal\", \"url\": "
        + "\"url\", \"method\": \"GET\", \"type\": \"redirect\"}, \"details\": [{ \"key\": "
        + "\"payload\", \"type\": \"text\"}], \"paymentData\": \"data\", \"redirect\": { "
        + "\"method\": \"GET\", \"url\": \"url\"}}}";
    AdyenTransactionResponse adyenTransactionResponse =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(adyenTransactionResponse.hasError());
    Assert.assertNull(adyenTransactionResponse.getHash());
    Assert.assertEquals("uid", adyenTransactionResponse.getUid());
    Assert.assertEquals("reference", adyenTransactionResponse.getOrderReference());
    Assert.assertEquals("SUCCESS", adyenTransactionResponse.getStatus());
    Assert.assertEquals("url", adyenTransactionResponse.getUrl());
    Assert.assertEquals("data", adyenTransactionResponse.getPaymentData());
    Assert.assertNull(adyenTransactionResponse.getPspReference());
    Assert.assertNull(adyenTransactionResponse.getResultCode());
    Assert.assertNull(adyenTransactionResponse.getRefusalReason());
    Assert.assertNull(adyenTransactionResponse.getRefusalReasonCode());
  }

  @Test public void adyenTransactionRequestErrorTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"FAILED\",\"payment\": { \"resultCode\": \"RedirectShopper\", "
        + "\"action\": { \"paymentData\", \"merchantReference\": \"merchant\"}}";
    AdyenTransactionResponse adyenTransactionResponse =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(400, response, null));
    Assert.assertTrue(adyenTransactionResponse.hasError());
  }

  @Test public void adyenCreditCardTransactionWrongCvcRequestTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"FAILED\",\"payment\": { \"pspReference\": \"pspReference\", "
        + "\"refusalReason\": \"CVC Declined\", \"refusalReasonCode\": \"24\","
        + "\"resultCode\": \"Refused\", \"merchantReference\": \"merchant\"}}";
    AdyenTransactionResponse adyenTransactionResponse =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(adyenTransactionResponse.hasError());
    Assert.assertNull(adyenTransactionResponse.getHash());
    Assert.assertEquals("uid", adyenTransactionResponse.getUid());
    Assert.assertEquals("reference", adyenTransactionResponse.getOrderReference());
    Assert.assertEquals("FAILED", adyenTransactionResponse.getStatus());
    Assert.assertEquals("pspReference", adyenTransactionResponse.getPspReference());
    Assert.assertEquals("Refused", adyenTransactionResponse.getResultCode());
    Assert.assertEquals("CVC Declined", adyenTransactionResponse.getRefusalReason());
    Assert.assertEquals("24", adyenTransactionResponse.getRefusalReasonCode());
    Assert.assertNull(adyenTransactionResponse.getUrl());
    Assert.assertNull(adyenTransactionResponse.getPaymentData());
  }

  @Test public void paymentMethodsRequestCreditCardTest() {
    String response = "{\"price\":{\"value\":\"9.06\",\"currency\":\"EUR\"},"
        + "\"payment\":{\"groups\":[{\"name\":\"Credit Card\",\"types\":[\"visa\",\"mc\","
        + "\"amex\",\"maestro\",\"diners\",\"discover\"]}],"
        + "\"paymentMethods\":[{\"brands\":[\"visa\",\"mc\",\"amex\",\"maestro\",\"diners\","
        + "\"discover\"],\"details\":[{\"key\":\"encryptedCardNumber\",\"type\":\"cardToken\"},"
        + "{\"key\":\"encryptedSecurityCode\",\"type\":\"cardToken\"},"
        + "{\"key\":\"encryptedExpiryMonth\",\"type\":\"cardToken\"},"
        + "{\"key\":\"encryptedExpiryYear\",\"type\":\"cardToken\"},{\"key\":\"holderName\","
        + "\"optional\":true,\"type\":\"text\"}],\"name\":\"Credit Card\",\"type\":\"scheme\"}]}}";
    PaymentMethodsResponse paymentMethodsResponse =
        adyenMapper.mapPaymentMethodsResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(paymentMethodsResponse.hasError());
    Assert.assertEquals(new BigDecimal("9.06"), paymentMethodsResponse.getValue());
    Assert.assertEquals("EUR", paymentMethodsResponse.getCurrency());
    Assert.assertEquals("{\"brands\":[\"visa\",\"mc\",\"amex\",\"maestro\",\"diners\","
            + "\"discover\"],\"name\":\"Credit Card\",\"details\":[{\"type\":\"cardToken\","
            + "\"key\":\"encryptedCardNumber\"},{\"type\":\"cardToken\","
            + "\"key\":\"encryptedSecurityCode\"},{\"type\":\"cardToken\","
            + "\"key\":\"encryptedExpiryMonth\"},{\"type\":\"cardToken\","
            + "\"key\":\"encryptedExpiryYear\"},{\"optional\":true,\"type\":\"text\","
            + "\"key\":\"holderName\"}],\"type\":\"scheme\"}",
        paymentMethodsResponse.getPaymentMethod());
  }

  @Test public void paymentMethodsRequestPaypalTest() {
    String response = "{\"price\":{\"value\":\"9.06\",\"currency\":\"EUR\"},"
        + "\"payment\":{\"paymentMethods\":[{\"name\":\"PayPal\",\"supportsRecurring\":true,"
        + "\"type\":\"paypal\"}]}}";
    PaymentMethodsResponse paymentMethodsResponse =
        adyenMapper.mapPaymentMethodsResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(paymentMethodsResponse.hasError());
    Assert.assertEquals(new BigDecimal("9.06"), paymentMethodsResponse.getValue());
    Assert.assertEquals("EUR", paymentMethodsResponse.getCurrency());
    Assert.assertEquals("{\"supportsRecurring\":true,\"name\":\"PayPal\",\"type\":\"paypal\"}",
        paymentMethodsResponse.getPaymentMethod());
  }

  @Test public void paymentMethodsRequestErrorTest() {
    String response = "{\"price\":{\"value\":\"9.06\",\"currency\":\"EUR\"},"
        + "\"payment\":{\"paymentMethods\":[{\"name\":\"PayPal\",\"supportsRecurring\":true,"
        + "\"type\":\"paypal\"}]}}";
    PaymentMethodsResponse paymentMethodsResponse =
        adyenMapper.mapPaymentMethodsResponse(new RequestResponse(400, response, null));
    Assert.assertTrue(paymentMethodsResponse.hasError());
  }

  @Test public void paymentMethodsRequestNoPaymentTest() {
    String response = "{\"price\":{\"value\":\"9.06\",\"currency\":\"EUR\"},"
        + "\"payment\":{\"paymentMethods\":[]}}";
    PaymentMethodsResponse paymentMethodsResponse =
        adyenMapper.mapPaymentMethodsResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(paymentMethodsResponse.hasError());
    Assert.assertEquals(new BigDecimal("9.06"), paymentMethodsResponse.getValue());
    Assert.assertEquals("EUR", paymentMethodsResponse.getCurrency());
    Assert.assertNotEquals("{\"supportsRecurring\":true,\"name\":\"PayPal\",\"type\":\"paypal\"}",
        paymentMethodsResponse.getPaymentMethod());
  }
}