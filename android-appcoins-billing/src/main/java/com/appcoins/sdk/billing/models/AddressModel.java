package com.appcoins.sdk.billing.models;

public class AddressModel {

  private final String address;
  private final boolean hasError;

  public AddressModel(String address, boolean hasError) {

    this.address = address;
    this.hasError = hasError;
  }

  public AddressModel(String address) {
    this.address = address;
    this.hasError = true;
  }

  public String getAddress() {
    return address;
  }

  public boolean hasError() {
    return hasError;
  }
}
