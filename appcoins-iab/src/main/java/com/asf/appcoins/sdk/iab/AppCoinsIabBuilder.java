package com.asf.appcoins.sdk.iab;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.payment.PaymentService;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.LinkedList;
import java.util.List;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public final class AppCoinsIabBuilder {

  private static final int DEFAULT_PERIOD = 5;

  private String developerAddress;
  private List<SKU> skus;
  private int period = DEFAULT_PERIOD;
  private Scheduler scheduler;
  private SkuManager skuManager;
  private PaymentService paymentService;
  private boolean debug;
  private AsfWeb3j asfWeb3j;

  public AppCoinsIabBuilder(String developerAddress) {
    this.developerAddress = developerAddress;
  }

  public AppCoinsIabBuilder withDeveloperAddress(String developerAddress) {
    this.developerAddress = developerAddress;
    return this;
  }

  public AppCoinsIabBuilder withSkus(List<SKU> skus) {
    this.skus = new LinkedList<>(skus);
    return this;
  }

  public AppCoinsIabBuilder withPeriod(int period) {
    this.period = period;
    return this;
  }

  public AppCoinsIabBuilder withScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
    return this;
  }

  public AppCoinsIabBuilder withSkuManager(SkuManager skuManager) {
    this.skuManager = skuManager;
    return this;
  }

  public AppCoinsIabBuilder withDebug(boolean debug) {
    this.debug = debug;
    return this;
  }

  public AppCoinsIab createAppCoinsIab() {

    Integer networkId;
    Web3j web3;
    String tokenContractAddress;
    String iabContractAddress;

    if (debug) {
      networkId = 3;
      web3 = Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
      tokenContractAddress = "0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3";
      iabContractAddress = "0xB040e69BD4b1025EF6dA958CAc7464730933dB71";
    } else {
      networkId = 1;
      web3 = Web3jFactory.build(new HttpService("https://mainnet.infura.io/1YsvKO0VH5aBopMYJzcy"));
      tokenContractAddress = "0x1a7a8bd9106f2b8d977e08582dc7d24c723ab0db";
      iabContractAddress = "0xb015D9bBabc472BBfC990ED6A0C961a90a482C57";
    }

    if (this.scheduler == null) {
      this.scheduler = Schedulers.io();
    }

    if (this.skuManager == null) {
      this.skuManager = new SkuManager(skus);
    }

    if (this.asfWeb3j == null) {
      this.asfWeb3j = new AsfWeb3jImpl(web3);
    }

    if (this.paymentService == null) {
      this.paymentService = new PaymentService(networkId, skuManager, developerAddress, asfWeb3j,
          tokenContractAddress, iabContractAddress);
    }

    return new AppCoinsIabImpl(period, scheduler, skuManager, paymentService);
  }
}