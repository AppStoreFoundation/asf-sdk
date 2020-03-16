package com.appcoins.sdk.billing.helpers;

import android.os.Bundle;
import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.ResponseCode;
import com.appcoins.sdk.billing.WalletInteractListener;
import com.appcoins.sdk.billing.listeners.PurchasesListener;
import com.appcoins.sdk.billing.listeners.PurchasesModel;
import com.appcoins.sdk.billing.models.billing.SkuPurchase;
import com.appcoins.sdk.billing.models.payasguest.WalletGenerationModel;
import com.appcoins.sdk.billing.payasguest.BillingRepository;
import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.wallet.WalletGenerationMapper;
import com.appcoins.sdk.billing.service.wallet.WalletRepository;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_DATA_SIGNATURE_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_DATA_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ID_LIST;
import static com.appcoins.sdk.billing.helpers.AppcoinsBillingStubHelper.INAPP_PURCHASE_ITEM_LIST;

class GuestPurchasesInteract {

  public static final int RESPONSE_SUCCESS = 0;
  public static final int RESPONSE_ERROR = 6;
  private BillingRepository billingRepository;

  GuestPurchasesInteract(BillingRepository billingRepository) {

    this.billingRepository = billingRepository;
  }

  void mapGuestPurchases(Bundle bundleResponse, String walletId, String packageName, String type,
      CountDownLatch countDownLatch) {
    mapGuestPurchases(bundleResponse, walletId, packageName, type, countDownLatch,
        new ArrayList<String>(), new ArrayList<String>(), new ArrayList<String>(),
        new ArrayList<String>());
  }

  void mapGuestPurchases(final Bundle bundleResponse, String walletId, final String packageName,
      final String type, final CountDownLatch countDownLatch, final ArrayList<String> idsList,
      final ArrayList<String> skuList, final ArrayList<String> dataList,
      final ArrayList<String> signatureDataList) {
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {

        PurchasesListener purchasesListener =
            getPurchasesListener(countDownLatch, bundleResponse, idsList, skuList, dataList,
                signatureDataList);

        billingRepository.getPurchases(packageName, walletGenerationModel.getWalletAddress(),
            walletGenerationModel.getSignature(), type, purchasesListener);
      }
    };
    requestWallet(walletId, walletInteractListener);
  }

  void consumeGuestPurchase(String walletId, final String packageName, final String purchaseToken,
      final int[] responseCode, final CountDownLatch countDownLatch) {
    WalletInteractListener walletInteractListener = new WalletInteractListener() {
      @Override public void walletIdRetrieved(WalletGenerationModel walletGenerationModel) {

        ConsumePurchaseListener consumePurchaseListener =
            getConsumePurchaseListener(countDownLatch, responseCode);

        billingRepository.consumePurchase(walletGenerationModel.getWalletAddress(),
            walletGenerationModel.getSignature(), packageName, purchaseToken,
            consumePurchaseListener);
      }
    };
    requestWallet(walletId, walletInteractListener);
  }

  private ConsumePurchaseListener getConsumePurchaseListener(final CountDownLatch countDownLatch,
      final int[] responseCode) {
    return new ConsumePurchaseListener() {
      @Override public void onConsumed(boolean wasConsumed) {
        if (wasConsumed) {
          responseCode[0] = RESPONSE_SUCCESS;
        } else {
          responseCode[0] = RESPONSE_ERROR;
        }
        countDownLatch.countDown();
      }
    };
  }

  private PurchasesListener getPurchasesListener(final CountDownLatch countDownLatch,
      final Bundle bundle, final ArrayList<String> idsList, final ArrayList<String> skuList,
      final ArrayList<String> dataList, final ArrayList<String> signatureDataList) {
    return new PurchasesListener() {
      @Override public void onResponse(PurchasesModel purchasesModel) {
        if (!purchasesModel.hasError()) {
          buildPurchaseBundle(bundle, purchasesModel, idsList, skuList, dataList,
              signatureDataList);
        }
        countDownLatch.countDown();
      }
    };
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

  private void requestWallet(String walletId, WalletInteractListener walletInteractListener) {
    WalletRepository walletRepository =
        new WalletRepository(new BdsService(BuildConfig.BACKEND_BASE, 30000),
            new WalletGenerationMapper());

    walletRepository.requestWallet(walletId, walletInteractListener);
  }
}
