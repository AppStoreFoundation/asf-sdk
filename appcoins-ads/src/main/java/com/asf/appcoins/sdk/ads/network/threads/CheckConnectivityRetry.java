package com.asf.appcoins.sdk.ads.network.threads;

import com.asf.appcoins.sdk.ads.poa.manager.PoAManager;

public class CheckConnectivityRetry implements Runnable {
  private PoAManager poAManager;

  public CheckConnectivityRetry(PoAManager poAManager) {
    this.poAManager = poAManager;
  }

  @Override public void run() {
    poAManager.startProcess();
  }
}
