package com.appcoins.sdk.billing.models.payasguest;

public class PaymentMethod {

  private String name;
  private boolean isAvailable;

  public PaymentMethod(String name, boolean isAvailable) {
    this.name = name;

    this.isAvailable = isAvailable;
  }

  public String getName() {
    return name;
  }

  public boolean isAvailable() {
    return isAvailable;
  }
}
