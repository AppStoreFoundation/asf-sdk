package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.models.billing.AdyenPaymentMethodsModel;
import com.appcoins.sdk.billing.models.billing.AdyenTransactionModel;
import com.appcoins.sdk.billing.models.billing.TransactionResponse;
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
    AdyenTransactionModel adyenTransactionModel =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(adyenTransactionModel.hasError());
    Assert.assertNull(adyenTransactionModel.getHash());
    Assert.assertEquals("uid", adyenTransactionModel.getUid());
    Assert.assertEquals("reference", adyenTransactionModel.getOrderReference());
    Assert.assertEquals("SUCCESS", adyenTransactionModel.getStatus());
    Assert.assertEquals("pspReference", adyenTransactionModel.getPspReference());
    Assert.assertEquals("Authorised", adyenTransactionModel.getResultCode());
    Assert.assertNull(adyenTransactionModel.getUrl());
    Assert.assertNull(adyenTransactionModel.getPaymentData());
    Assert.assertNull(adyenTransactionModel.getRefusalReason());
    Assert.assertNull(adyenTransactionModel.getRefusalReasonCode());
  }

  @Test public void adyenPaypalTransactionRequest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"SUCCESS\",\"payment\": { "
        + "\"action\": { \"paymentData\": \"data\", \"paymentMethodType\": \"paypal\", \"url\": "
        + "\"url\", \"method\": \"GET\", \"type\": \"redirect\"}, \"details\": [{ \"key\": "
        + "\"payload\", \"type\": \"text\"}], \"paymentData\": \"data\", \"redirect\": { "
        + "\"method\": \"GET\", \"url\": \"url\"}}}";
    AdyenTransactionModel adyenTransactionModel =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(adyenTransactionModel.hasError());
    Assert.assertNull(adyenTransactionModel.getHash());
    Assert.assertEquals("uid", adyenTransactionModel.getUid());
    Assert.assertEquals("reference", adyenTransactionModel.getOrderReference());
    Assert.assertEquals("SUCCESS", adyenTransactionModel.getStatus());
    Assert.assertEquals("url", adyenTransactionModel.getUrl());
    Assert.assertEquals("data", adyenTransactionModel.getPaymentData());
    Assert.assertNull(adyenTransactionModel.getPspReference());
    Assert.assertNull(adyenTransactionModel.getResultCode());
    Assert.assertNull(adyenTransactionModel.getRefusalReason());
    Assert.assertNull(adyenTransactionModel.getRefusalReasonCode());
  }

  @Test public void adyenTransactionRequestErrorTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"FAILED\",\"payment\": { \"resultCode\": \"RedirectShopper\", "
        + "\"action\": { \"paymentData\", \"merchantReference\": \"merchant\"}}";
    AdyenTransactionModel adyenTransactionModel =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(400, response, null));
    Assert.assertTrue(adyenTransactionModel.hasError());
  }

  @Test public void adyenCreditCardTransactionWrongCvcRequestTest() {
    String response = "{\"uid\":\"uid\",\"hash\":\"null\",\"reference\":\"reference\","
        + "\"status\":\"FAILED\",\"payment\": { \"pspReference\": \"pspReference\", "
        + "\"refusalReason\": \"CVC Declined\", \"refusalReasonCode\": \"24\","
        + "\"resultCode\": \"Refused\", \"merchantReference\": \"merchant\"}}";
    AdyenTransactionModel adyenTransactionModel =
        adyenMapper.mapAdyenTransactionResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(adyenTransactionModel.hasError());
    Assert.assertNull(adyenTransactionModel.getHash());
    Assert.assertEquals("uid", adyenTransactionModel.getUid());
    Assert.assertEquals("reference", adyenTransactionModel.getOrderReference());
    Assert.assertEquals("FAILED", adyenTransactionModel.getStatus());
    Assert.assertEquals("pspReference", adyenTransactionModel.getPspReference());
    Assert.assertEquals("Refused", adyenTransactionModel.getResultCode());
    Assert.assertEquals("CVC Declined", adyenTransactionModel.getRefusalReason());
    Assert.assertEquals("24", adyenTransactionModel.getRefusalReasonCode());
    Assert.assertNull(adyenTransactionModel.getUrl());
    Assert.assertNull(adyenTransactionModel.getPaymentData());
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
    AdyenPaymentMethodsModel paymentMethodsResponse =
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
    AdyenPaymentMethodsModel paymentMethodsResponse =
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
    AdyenPaymentMethodsModel paymentMethodsResponse =
        adyenMapper.mapPaymentMethodsResponse(new RequestResponse(400, response, null));
    Assert.assertTrue(paymentMethodsResponse.hasError());
  }

  @Test public void paymentMethodsRequestNoPaymentTest() {
    String response = "{\"price\":{\"value\":\"9.06\",\"currency\":\"EUR\"},"
        + "\"payment\":{\"paymentMethods\":[]}}";
    AdyenPaymentMethodsModel paymentMethodsResponse =
        adyenMapper.mapPaymentMethodsResponse(new RequestResponse(200, response, null));
    Assert.assertFalse(paymentMethodsResponse.hasError());
    Assert.assertEquals(new BigDecimal("9.06"), paymentMethodsResponse.getValue());
    Assert.assertEquals("EUR", paymentMethodsResponse.getCurrency());
    Assert.assertNotEquals("{\"supportsRecurring\":true,\"name\":\"PayPal\",\"type\":\"paypal\"}",
        paymentMethodsResponse.getPaymentMethod());
  }
}