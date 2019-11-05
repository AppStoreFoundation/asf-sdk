package com.appcoins.sdk.billing;

import java.util.List;

public interface PurchasesUpdatedListener {
  /**
   * Implement this method to get notifications for purchases updates. Both purchases initiated by
   * your app and the ones initiated by Play Store will be reported here.
   *
   * @param responseCode All calls will give a response code with the following possible values
   * <p>OK = 0 - Success
   * <p>USER_CANCELED = 1 - User pressed back or canceled a dialog
   * <p>SERVICE_UNAVAILABLE = 2 - The network connection is down
   * <p>BILLING_UNAVAILABLE = 3 - This billing API version is not supported for the type requested
   * <p>ITEM_UNAVAILABLE = 4 - Requested SKU is not available for purchase
   * <p>DEVELOPER_ERROR = 5 - Invalid arguments provided to the API
   * <p>ERROR = 6 - Fatal error during the API action
   * <p>ITEM_ALREADY_OWNED = 7 - Failure to purchase since item is already owned
   * <p>ITEM_NOT_OWNED = 8 - Failure to consume since item is not owned
   * @param purchases List of updated {@link Purchase} purchases if present.
   */
  void onPurchasesUpdated(int responseCode, List<Purchase> purchases);
}
