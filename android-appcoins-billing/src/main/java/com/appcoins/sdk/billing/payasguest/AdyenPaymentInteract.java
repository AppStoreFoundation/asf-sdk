package com.appcoins.sdk.billing.payasguest;

import android.os.AsyncTask;
import android.util.Log;
import com.appcoins.sdk.billing.listeners.AddressRetrievedListener;
import com.appcoins.sdk.billing.listeners.NoInfoResponseListener;
import com.appcoins.sdk.billing.listeners.billing.GetTransactionListener;
import com.appcoins.sdk.billing.listeners.billing.LoadPaymentInfoListener;
import com.appcoins.sdk.billing.listeners.billing.MakePaymentListener;
import com.appcoins.sdk.billing.listeners.billing.PurchaseListener;
import com.appcoins.sdk.billing.models.AddressModel;
import com.appcoins.sdk.billing.models.billing.AdyenPaymentParams;
import com.appcoins.sdk.billing.models.billing.PurchaseModel;
import com.appcoins.sdk.billing.models.billing.TransactionInformation;
import com.appcoins.sdk.billing.models.billing.TransactionWallets;
import com.appcoins.sdk.billing.service.address.AddressService;
import com.appcoins.sdk.billing.service.adyen.AdyenPaymentMethod;
import com.appcoins.sdk.billing.service.adyen.AdyenRepository;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;

public class AdyenPaymentInteract {

  private AdyenRepository adyenRepository;
  private BillingRepository billingRepository;
  private AddressService addressService;
  private List<AsyncTask> asyncTasks;

  AdyenPaymentInteract(AdyenRepository adyenRepository, BillingRepository billingRepository,
      AddressService addressService) {
    this.adyenRepository = adyenRepository;
    this.billingRepository = billingRepository;
    this.addressService = addressService;
    this.asyncTasks = new ArrayList<>();
  }

  void loadPaymentInfo(AdyenPaymentMethod method, String fiatPrice, String fiatCurrency,
      String walletAddress, LoadPaymentInfoListener loadPaymentInfoListener) {
    adyenRepository.loadPaymentInfo(method.getTransactionType(), fiatPrice, fiatCurrency,
        walletAddress, loadPaymentInfoListener);
  }

  void makePayment(final AdyenPaymentParams adyenPaymentParams,
      final TransactionInformation transactionInformation, final String userWalletAddress,
      String packageName, final MakePaymentListener makePaymentListener) {

    AddressRetrievedListener addressRetrievedListener = new AddressRetrievedListener() {
      @Override public void onAddressRetrieved(String oemAddress, String storeAddress,
          String developerAddress) {
        adyenRepository.makePayment(adyenPaymentParams, transactionInformation,
            new TransactionWallets(userWalletAddress, developerAddress, oemAddress, storeAddress,
                userWalletAddress), makePaymentListener);
      }
    };
    AddressAsyncTask addressAsyncTask =
        new AddressAsyncTask(addressService, addressRetrievedListener, packageName);
    addressAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    asyncTasks.add(addressAsyncTask);
  }

  void submitRedirect(String uid, String walletAddress, JSONObject details, String paymentData,
      MakePaymentListener makePaymentListener) {
    adyenRepository.submitRedirect(uid, walletAddress, details, paymentData, makePaymentListener);
  }

  void getTransaction(String uid, String walletAddress, String signature,
      GetTransactionListener getTransactionListener) {
    adyenRepository.getTransaction(uid, walletAddress, signature, getTransactionListener);
  }

  void getCompletedPurchaseBundle(String transactionType, String packageName, String sku,
      String walletAddress, String walletSignature, PurchaseListener purchaseListener) {
    if (transactionType.equalsIgnoreCase("inapp")) {
      billingRepository.getSkuPurchase(packageName, sku, walletAddress, walletSignature,
          purchaseListener);
    } else {
      Log.w("TAG", "Unknown transaction type"); //This shouldn't happen as we are verifying before
      purchaseListener.onResponse(PurchaseModel.createErrorPurchaseModel());
    }
  }

  void cancelRequests() {
    for (AsyncTask asyncTask : asyncTasks) {
      asyncTask.cancel(true);
    }
    adyenRepository.cancelRequests();
    billingRepository.cancelRequests();
  }

  void forgetCard(String walletAddress, NoInfoResponseListener noInfoResponseListener) {
    adyenRepository.disablePayments(walletAddress, noInfoResponseListener);
  }

  public interface AddressListener {

    void onResponse(AddressModel addressModel);
  }
}
