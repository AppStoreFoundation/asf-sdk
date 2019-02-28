package com.appcoins.sdk.billing;

public class ConsumeAsync implements Runnable {

  private final String token;
  private final ConsumeResponseListener listener;
  private final Repository repository;

  public ConsumeAsync(String token, ConsumeResponseListener listener, Repository repository) {
    this.token = token;
    this.listener = listener;
    this.repository = repository;
  }

  @Override public void run() {

    if (token == null || token.isEmpty()) {
      listener.onConsumeResponse(-1, null);
    }

    try {
      int response = repository.consumeAsync(token);

      listener.onConsumeResponse(response, token);
    } catch (ServiceConnectionException e) {
      e.printStackTrace();
    }
  }
}
