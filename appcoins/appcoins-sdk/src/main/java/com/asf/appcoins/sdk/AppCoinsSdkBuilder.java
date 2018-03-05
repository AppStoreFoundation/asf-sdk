package com.asf.appcoins.sdk;

import com.asf.appcoins.sdk.entity.SKU;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.util.List;

public class AppCoinsSdkBuilder {
  private static final int DEFAULT_PERIOD = 5;

  private AsfWeb3j asfWeb3j;
  private String developerAddress;
  private String oemAddress;
  private String storeAddress;
  private List<SKU> skus;
  private int period = DEFAULT_PERIOD;
  private Scheduler scheduler = Schedulers.io();
  private SkuManager skuManager;
  private boolean debug;

  public AppCoinsSdkBuilder(String developerAddress, String oemAddress, String storeAddress) {
    this.developerAddress = developerAddress;
    this.oemAddress = oemAddress;
    this.storeAddress = storeAddress;
  }

  public AppCoinsSdkBuilder setAsfWeb3j(AsfWeb3j asfWeb3j) {
    this.asfWeb3j = asfWeb3j;
    return this;
  }

  public AppCoinsSdkBuilder setDeveloperAddress(String developerAddress) {
    this.developerAddress = developerAddress;
    return this;
  }

  public AppCoinsSdkBuilder setOemAddress(String oemAddress) {
    this.oemAddress = oemAddress;
    return this;
  }

  public AppCoinsSdkBuilder setStoreAddress(String storeAddress) {
    this.storeAddress = storeAddress;
    return this;
  }

  public AppCoinsSdkBuilder setSkus(List<SKU> skus) {
    this.skus = skus;
    return this;
  }

  public AppCoinsSdkBuilder setPeriod(int period) {
    this.period = period;
    return this;
  }

  public AppCoinsSdkBuilder setScheduler(Scheduler scheduler) {
    this.scheduler = scheduler;
    return this;
  }

  public AppCoinsSdkBuilder setSkuManager(SkuManager skuManager) {
    this.skuManager = skuManager;
    return this;
  }

  public AppCoinsSdkBuilder setDebug(boolean debug) {
    this.debug = debug;
    return this;
  }

  public AppCoinsSdk createAppCoinsSdk() {
    if (this.skuManager == null) {
      this.skuManager = new SkuManager(skus);
    }

    return new AppCoinsSdkImpl(asfWeb3j, period, scheduler, developerAddress, oemAddress,
        storeAddress, skuManager, debug);
  }
}