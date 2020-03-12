package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.mappers.PurchaseMapper;
import com.appcoins.sdk.billing.models.billing.PurchaseModel;
import com.appcoins.sdk.billing.models.billing.Signature;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.service.RequestResponse;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PurchaseMapperTest {

  private PurchaseMapper purchaseMapper;

  @Before public void setup() {
    purchaseMapper = new PurchaseMapper();
  }

  @Test public void purchaseSingleSkuResponseSuccessTest() {
    String response = "{\n"
        + "\t\"uid\": \"catappult.inapp.purchase.teste\",\n"
        + "\t\"status\": \"STOCKED\",\n"
        + "\t\"package\": {\n"
        + "\t\t\"name\": \"com.appcoins.toolbox\"\n"
        + "\t},\n"
        + "\t\"product\": {\n"
        + "\t\t\"name\": \"gas\"\n"
        + "\t},\n"
        + "\t\"signature\": {\n"
        + "\t\t\"value\": \"teste\",\n"
        + "\t\t\"message\": {\n"
        + "\t\t\t\"orderId\": \"teste\",\n"
        + "\t\t\t\"packageName\": \"com.appcoins.toolbox\",\n"
        + "\t\t\t\"productId\": \"gas\",\n"
        + "\t\t\t\"purchaseTime\": 115838430105971,\n"
        + "\t\t\t\"purchaseToken\": \"catappult.inapp.purchase.teste\",\n"
        + "\t\t\t\"purchaseState\": 0\n"
        + "\t\t}\n"
        + "\t}\n"
        + "}";
    PurchaseModel purchaseModel = purchaseMapper.map(new RequestResponse(200, response, null));
    SkuPurchase purchase = purchaseModel.getPurchase();
    Assert.assertEquals("com.appcoins.toolbox", purchase.getPackageName());
    Assert.assertEquals("STOCKED", purchase.getStatus());
    Assert.assertEquals("catappult.inapp.purchase.teste", purchase.getUid());
    Assert.assertEquals("gas", purchase.getProduct()
        .getName());
    Signature signature = purchase.getSignature();
    Assert.assertEquals("teste", signature.getValue());
    String message = "{"
        + "\"purchaseToken\":\"catappult.inapp.purchase.teste\","
        + "\"productId\":\"gas\","
        + "\"orderId\":\"teste\","
        + "\"purchaseTime\":115838430105971,"
        + "\"packageName\":\"com.appcoins.toolbox\","
        + "\"purchaseState\":0"
        + "}";
    Assert.assertEquals(message, signature.getMessage()
        .toString());
  }

  @Test public void purchaseSkuListResponseSuccessTest() {
    String response = "{\n"
        + "\t\"next\": null,\n"
        + "\t\"items\": [{\n"
        + "\t\t\"uid\": \"catappult.inapp.purchase.teste\",\n"
        + "\t\t\"status\": \"STOCKED\",\n"
        + "\t\t\"package\": {\n"
        + "\t\t\t\"name\": \"com.appcoins.toolbox\"\n"
        + "\t\t},\n"
        + "\t\t\"product\": {\n"
        + "\t\t\t\"name\": \"gas\"\n"
        + "\t\t},\n"
        + "\t\t\"signature\": {\n"
        + "\t\t\t\"value\": \"teste\",\n"
        + "\t\t\t\"message\": {\n"
        + "\t\t\t\t\"orderId\": \"teste\",\n"
        + "\t\t\t\t\"packageName\": \"com.appcoins.toolbox\",\n"
        + "\t\t\t\t\"productId\": \"gas\",\n"
        + "\t\t\t\t\"purchaseTime\": 115838489360321,\n"
        + "\t\t\t\t\"purchaseToken\": \"catappult.inapp.purchase.teste\",\n"
        + "\t\t\t\t\"purchaseState\": 0\n"
        + "\t\t\t}\n"
        + "\t\t}\n"
        + "\t}]\n"
        + "}";
    PurchasesModel purchasesModel =
        purchaseMapper.mapList(new RequestResponse(200, response, null));
    List<SkuPurchase> skuPurchases = purchasesModel.getSkuPurchases();
    Assert.assertEquals(1, skuPurchases.size());
    SkuPurchase purchase = skuPurchases.get(0);
    Assert.assertEquals("com.appcoins.toolbox", purchase.getPackageName());
    Assert.assertEquals("STOCKED", purchase.getStatus());
    Assert.assertEquals("catappult.inapp.purchase.teste", purchase.getUid());
    Assert.assertEquals("gas", purchase.getProduct()
        .getName());
    Signature signature = purchase.getSignature();
    Assert.assertEquals("teste", signature.getValue());
    String message = "{"
        + "\"purchaseToken\":\"catappult.inapp.purchase.teste\","
        + "\"productId\":\"gas\","
        + "\"orderId\":\"teste\","
        + "\"purchaseTime\":115838489360321,"
        + "\"packageName\":\"com.appcoins.toolbox\","
        + "\"purchaseState\":0"
        + "}";
    Assert.assertEquals(message, signature.getMessage()
        .toString());
  }
}
