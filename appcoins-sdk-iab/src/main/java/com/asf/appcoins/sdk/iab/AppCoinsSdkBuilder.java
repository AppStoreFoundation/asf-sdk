package com.asf.appcoins.sdk.iab;

import com.asf.appcoins.sdk.iab.entity.SKU;
import com.asf.appcoins.sdk.iab.payment.PaymentService;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.LinkedList;
import java.util.List;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public final class AppCoinsSdkBuilder {

  private static final int DEFAULT_PERIOD = 5;

  private Integer networkId;
  private String developerAddress;
  private List<SKU> skus;
  private int period = DEFAULT_PERIOD;
  private Scheduler scheduler;
  private SkuManager skuManager;
  private PaymentService paymentService;
  private boolean debug;
  private AsfWeb3j asfWeb3j;

  public AppCoinsSdkBuilder(String developerAddress) {
    this.developerAddress = developerAddress;
  }

  public AppCoinsSdkBuilder withDeveloperAddress(String developerAddress) {
    this.developerAddress = developerAddress;
    return this;
  }

  public AppCoinsSdkBuilder withSkus(List<SKU> skus) {
    this.skus = new LinkedList<>(skus);
    return this;
  }

  public AppCoinsSdkBuilder withPeriod(int period) {
    this.period = period;
    return this;
  }

  public AppCoinsSdkBuilder withScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
    return this;
  }

  public AppCoinsSdkBuilder withSkuManager(SkuManager skuManager) {
    this.skuManager = skuManager;
    return this;
  }

  public AppCoinsSdkBuilder withDebug(boolean debug) {
    this.debug = debug;
    return this;
  }

  public AppCoinsSdk createAppCoinsSdk() {
    Web3j web3;

    if (debug) {
      networkId = 3;
      web3 = Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
    } else {
      networkId = 1;
      web3 = Web3jFactory.build(new HttpService("https://mainnet.infura.io/1YsvKO0VH5aBopMYJzcy"));
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
      this.paymentService = new PaymentService(networkId, skuManager, developerAddress, asfWeb3j);
    }

    return new AppCoinsSdkImpl(period, scheduler, skuManager, paymentService, debug);
  }
}