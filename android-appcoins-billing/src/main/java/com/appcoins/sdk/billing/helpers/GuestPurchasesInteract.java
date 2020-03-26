package com.appcoins.sdk.billing.helpers;

import android.os.Bundle;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.analytics.WalletAddressProvider;
import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;
import com.appcoins.sdk.billing.payasguest.BillingRepository;
import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.wallet.WalletGenerationMapper;
import com.appcoins.sdk.billing.service.wallet.WalletRepository;
import java.util.ArrayList;

import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_DATA_SIGNATURE_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_DATA_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ID_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ITEM_LIST;

class GuestPurchasesInteract {

  private BillingRepository billingRepository;

  GuestPurchasesInteract(BillingRepository billingRepository) {

    this.billingRepository = billingRepository;
  }

  Bundle mapGuestPurchases(Bundle bundleResponse, String walletId, String packageName,
      String type) {
    return mapGuestPurchases(bundleResponse, walletId, packageName, type, new ArrayList<String>(),
        new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>());
  }

  Bundle mapGuestPurchases(final Bundle bundleResponse, String walletId, final String packageName,
      final String type, final ArrayList<String> idsList, final ArrayList<String> skuList,
      final ArrayList<String> dataList, final ArrayList<String> signatureDataList) {

    WalletGenerationModel walletGenerationModel = requestWallet(walletId);

    PurchasesModel purchasesModel =
        billingRepository.getPurchasesSync(packageName, walletGenerationModel.getWalletAddress(),
            walletGenerationModel.getSignature(), type);

    buildPurchaseBundle(bundleResponse, purchasesModel, idsList, skuList, dataList,
        signatureDataList);

    return bundleResponse;
  }

  int consumeGuestPurchase(String walletId, final String packageName, final String purchaseToken) {
    WalletGenerationModel walletGenerationModel = requestWallet(walletId);
    return billingRepository.consumePurchaseSync(walletGenerationModel.getWalletAddress(),
        walletGenerationModel.getSignature(), packageName, purchaseToken);
  }

  private void buildPurchaseBundle(Bundle bundle, PurchasesModel purchasesModel,
      ArrayList<String> idsList, ArrayList<String> skuList, ArrayList<String> dataList,
      ArrayList<String> signatureDataList) {
    for (SkuPurchase skuPurchase : purchasesModel.getSkuPurchases()) {
      idsList.add(skuPurchase.getUid());
      dataList.add(skuPurchase.getSignature()
          .getMessage()
          .toString());
      signatureDataList.add(skuPurchase.getSignature()
          .getValue());
      skuList.add(skuPurchase.getProduct()
          .getName());
    }
    bundle.putInt(Utils.RESPONSE_CODE, ResponseCode.OK.getValue());
    bundle.putStringArrayList(INAPP_PURCHASE_ID_LIST, idsList);
    bundle.putStringArrayList(INAPP_PURCHASE_ITEM_LIST, skuList);
    bundle.putStringArrayList(INAPP_PURCHASE_DATA_LIST, dataList);
    bundle.putStringArrayList(INAPP_DATA_SIGNATURE_LIST, signatureDataList);
  }

  private WalletGenerationModel requestWallet(String walletId) {
    WalletRepository walletRepository = new WalletRepository(
        new BdsService(BuildConfig.BACKEND_BASE, BdsService.TIME_OUT_IN_MILLIS),
        new WalletGenerationMapper(), WalletAddressProvider.provideWalletAddressProvider());

    return walletRepository.requestWalletSync(walletId);
  }
}
