package com.asf.appcoins.sdk.iab.util;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class UriBuilderTest {

  private static final String developerAddress = "0x4fbcc5ce88493c3d9903701c143af65f54481119";
  private static final String tokenContractAddress = "0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3";
  private static final String iabContractAddress = "0xb015D9bBabc472BBfC990ED6A0C961a90a482C57";

  @Test public void buildUriString() {
    String uriString = UriBuilder.
        buildUriString(tokenContractAddress, iabContractAddress,
            new BigDecimal("1000000000000000000"), developerAddress, "com.cenas.product", 3);

    Assert.assertThat(uriString,
        is("ethereum:0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3@3/buy?uint256=1000000000000000000&address=0x4fbcc5ce88493c3d9903701c143af65f54481119&data=0x636f6d2e63656e61732e70726f64756374&iabContractAddress=0xb015D9bBabc472BBfC990ED6A0C961a90a482C57"));
  }
}