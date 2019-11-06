package com.appcoins.sdk.billing;

public enum ResponseCode {
  /**
   * Success
   */
  OK(0),
    /**
   * User pressed back or canceled a dialog
   */
  USER_CANCELED(1),
    /**
   * The network connection is down
   */
  SERVICE_UNAVAILABLE(2),
    /**
   * This billing API version is not supported for the type requested
   */
  BILLING_UNAVAILABLE(3),
    /**
   * Requested SKU is not available for purchase
   */
  ITEM_UNAVAILABLE(4),
    /**
   * Invalid arguments provided to the API
   */
  DEVELOPER_ERROR(5),
    /**
   * Fatal error during the API action
   */
  ERROR(6),
    /**
   * Failure to purchase since item is already owned
   */
  ITEM_ALREADY_OWNED(7),
  /**
   * Failure to consume since item is not owned
   */
  ITEM_NOT_OWNED(8);

  private int value;

  ResponseCode(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}