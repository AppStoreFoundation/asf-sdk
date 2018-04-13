package com.asf.appcoins.sdk.core.factory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by neuro on 28-02-2018.
 */
public final class TransactionFactoryTest {

  private static final String address = "0xab949343e6c369c6b17c7ae302c1debd4b7b61c3";
  private static final String valueHex =
      "0000000000000000000000000000000000000000000000000de0b6b3a7640000";
  private static final String input =
      "0xa9059cbb000000000000000000000000ab949343e6c369c6b17c7ae302c1debd4b7b61c30000000000000000000000000000000000000000000000000de0b6b3a7640000";

  @Test public void extractValueFromEthTransaction() {
    String s = TransactionFactory.extractValueFromEthTransaction(input);

    assertThat(s, is(Long.toString((long) (1 * Math.pow(10, 18)))));
  }

  @Test public void extractToFromEthTransaction() {
    String s = TransactionFactory.extractToFromEthTransaction(input);

    assertThat(s, is(address));
  }

  @Test public void extractValueFromEthGetTransactionReceipt() {
    String s = TransactionFactory.extractValueFromEthGetTransactionReceipt(valueHex);

    assertThat(s, is("1000000000000000000"));
  }
}
