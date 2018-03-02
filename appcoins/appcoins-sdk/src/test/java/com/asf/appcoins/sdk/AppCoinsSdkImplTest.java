package com.asf.appcoins.sdk;

import java.math.BigDecimal;
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

  @Mock Web3j web3j;

  @Test public void buildUri() throws Exception {
    MockitoAnnotations.initMocks(this);

    AsfWeb3j asfWeb3j = new AsfWeb3jImpl(web3j);
    AppCoinsSdkImpl appCoinsSdk = new AppCoinsSdkImpl(asfWeb3j);

    String uriString = appCoinsSdk.buildUriString("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3", 3,
        "0x4fbcc5ce88493c3d9903701c143af65f54481119", BigDecimal.ONE);

    Assert.assertThat(uriString,
        is("ethereum:0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3@3/transfer?address=0x4fbcc5ce88493c3d9903701c143af65f54481119&uint256=1"));
  }
}