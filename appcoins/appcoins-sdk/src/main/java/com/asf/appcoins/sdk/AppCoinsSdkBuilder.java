package com.asf.appcoins.sdk;

import com.asf.appcoins.sdk.entity.SKU;
import com.asf.appcoins.sdk.payment.PaymentService;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.LinkedList;
import java.util.List;

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
    if (debug) {
      networkId = 3;
    } else {
      networkId = 1;
    }

    if (this.scheduler == null) {
      this.scheduler = Schedulers.io();
    }

    if (this.skuManager == null) {
      this.skuManager = new SkuManager(skus);
    }

    if (this.paymentService == null) {
      this.paymentService = new PaymentService(networkId, skuManager, developerAddress);
    }

    return new AppCoinsSdkImpl(period, scheduler, skuManager, paymentService, debug);
  }
}