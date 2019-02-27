package com.asf.appcoins.sdk.ads.net.responses;

public class AppCoinsClientResponsePing extends AppCoinsClientResponse {

  private boolean hasConnection;

  public AppCoinsClientResponsePing(boolean hasConnection) {
    super(null);
    this.hasConnection = hasConnection;
  }

  public boolean HasConnection() {
    return hasConnection;
  }
}
