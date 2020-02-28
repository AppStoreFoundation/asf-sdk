package com.appcoins.sdk.billing;

import com.appcoins.sdk.billing.models.WalletGenerationModel;

public interface WalletInteractListener {

  void walletIdRetrieved(WalletGenerationModel walletGenerationModel);
}
