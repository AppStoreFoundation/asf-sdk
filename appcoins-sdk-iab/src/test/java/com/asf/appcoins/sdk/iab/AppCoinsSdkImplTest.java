package com.asf.appcoins.sdk.iab;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.util.UriBuilder;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.web3j.protocol.Web3j;

import static org.hamcrest.CoreMatchers.is;

/**
 * Created by neuro on 02-03-2018.
 */
public class AppCoinsSdkImplTest {

  private final String developerAddress = "0x4fbcc5ce88493c3d9903701c143af65f54481119";

  @Mock Web3j web3j;

  @Test public void buildUri() {
    MockitoAnnotations.initMocks(this);

    AsfWeb3j asfWeb3j = new AsfWeb3jImpl(web3j);
    AppCoinsSdk appCoinsSdk = new AppCoinsSdkBuilder(developerAddress).withSkus(buildSkus())
        .createAppCoinsSdk();

    String uriString = UriBuilder.
        buildUriString("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3",
        new BigDecimal("1000000000000000000"), developerAddress, "com.cenas.product", 3);

    Assert.assertThat(uriString,
        is("ethereum:0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3@3/buy?uint256=1000000000000000000&address=0x4fbcc5ce88493c3d9903701c143af65f54481119&data=0x636f6d2e63656e61732e70726f64756374"));
  }

  private List<SKU> buildSkus() {
    List<SKU> skus = new LinkedList<>();

    skus.add(new SKU("smallpack", "com.marceloporto.bombastic.smallpack", BigDecimal.ONE));

    return skus;
  }
}