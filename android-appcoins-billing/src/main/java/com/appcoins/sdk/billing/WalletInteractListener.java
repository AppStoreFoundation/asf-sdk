package com.appcoins.sdk.billing;

import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;

public interface WalletInteractListener {

  void walletAddressRetrieved(WalletGenerationModel walletGenerationModel);
}
