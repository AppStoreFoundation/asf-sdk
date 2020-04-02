package com.sdk.appcoins_adyen;

import com.appcoins.sdk.billing.models.Transaction;
import com.appcoins.sdk.billing.utils.EnumMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EnumMapperTest {

  private EnumMapper enumMapper;

  @Before public void setupTest() {
    enumMapper = new EnumMapper();
  }

  @Test public void transactionStatusMappingTest() {
    String[] transactionStatusString = {
        "PENDING", "PENDING SERVICE AUTHORIZATION", "SETTLED", "PROCESSING", "COMPLETED",
        "PENDING USER PAYMENT", "INVALID TRANSACTION", "FAILED", "CANCELED", "ERROR"
    };
    Transaction.Status[] transactionStatus = Transaction.Status.values();
    Assert.assertEquals(transactionStatusString.length, transactionStatus.length);
    for (int i = 0; i < transactionStatusString.length; i++) {
      Assert.assertEquals(
          enumMapper.parseToEnum(Transaction.Status.class, transactionStatusString[i],
              Transaction.Status.ERROR), transactionStatus[i]);
    }
  }

  @Test public void transactionStatusMappingNoMapValueTest() {
    Assert.assertEquals(
        enumMapper.parseToEnum(Transaction.Status.class, "RANDOMVALUE", Transaction.Status.ERROR),
        Transaction.Status.ERROR);
  }
}
