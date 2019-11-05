package com.appcoins.sdk.billing;

public interface PurchaseFinishedListener {
  /**
   *
   * @param responseCode  All calls will give a response code with the following possible values
   * <p>{@link ResponseCode#OK} = 0 - Success
   * <p>{@link ResponseCode#USER_CANCELED} = 1 - User pressed back or canceled a dialog
   * <p>{@link ResponseCode#SERVICE_UNAVAILABLE} = 2 - The network connection is down
   * <p>{@link ResponseCode#BILLING_UNAVAILABLE} = 3 - This billing API version is not supported for the type
   *  requested
   * <p>{@link ResponseCode#ITEM_UNAVAILABLE} = 4 - Requested SKU is not available for purchase
   * <p>{@link ResponseCode#DEVELOPER_ERROR} = 5 - Invalid arguments provided to the API
   * <p>{@link ResponseCode#ERROR} = 6 - Fatal error during the API action
   * <p>{@link ResponseCode#ITEM_ALREADY_OWNED} = 7 - Failure to purchase since item is already owned
   * <p>{@link ResponseCode#ITEM_NOT_OWNED} = 8 - Failure to consume since item is not owned
   * @param message Additional info about the response code
   * @param token Token that identifies the purchase on server
   * @param sku - Sku of the purchase
   */
  void onPurchaseFinished(int responseCode, String message, String token, String sku);
}
