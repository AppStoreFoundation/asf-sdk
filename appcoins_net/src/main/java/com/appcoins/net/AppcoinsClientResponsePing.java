package com.appcoins.net;

public class AppcoinsClientResponsePing extends AppcoinsClientResponse {

  private boolean hasConnection;

  public AppcoinsClientResponsePing(boolean hasConnection) {
    super(null);
    this.hasConnection = hasConnection;
  }

  public boolean HasConnection() {
    return hasConnection;
  }
}
