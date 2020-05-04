package com.appcoins.sdk.billing.service.address;

import android.content.Context;
import android.util.Log;
import com.appcoins.sdk.billing.models.AddressModel;
import com.appcoins.sdk.billing.payasguest.AdyenPaymentInteract.AddressListener;

public class AddressService {

  private final WalletAddressService walletAddressService;
  private final String deviceInfo;
  private final String deviceManufacturer;
  private final OemIdExtractorService oemIdExtractorService;
  private Context context;
  private DeveloperAddressService developerAddressService;

  public AddressService(Context context, WalletAddressService walletAddressService,
      DeveloperAddressService developerAddressService, String deviceInfo, String deviceManufacturer,
      OemIdExtractorService oemIdExtractorService) {
    this.context = context;
    this.walletAddressService = walletAddressService;
    this.developerAddressService = developerAddressService;
    this.deviceInfo = deviceInfo;
    this.deviceManufacturer = deviceManufacturer;
    this.oemIdExtractorService = oemIdExtractorService;
  }

  public void getStoreAddressForPackage(String packageName, AddressListener addressListener) {
    if (packageName == null) {
      addressListener.onResponse(
          new AddressModel(walletAddressService.getDefaultStoreAddress(), true));
    } else {
      String installerPackageName = getInstallerPackageName(packageName);
      String oemId = oemIdExtractorService.extractOemId(packageName);
      walletAddressService.getStoreAddressForPackage(installerPackageName, deviceManufacturer,
          deviceInfo, oemId, addressListener);
    }
  }

  public void getOemAddressForPackage(String packageName, AddressListener addressListener) {
    if (packageName == null) {
      addressListener.onResponse(
          new AddressModel(walletAddressService.getDefaultOemAddress(), true));
    } else {
      String installerPackageName = getInstallerPackageName(packageName);
      String oemId = oemIdExtractorService.extractOemId(packageName);
      Log.e("123456789: ", oemId);
      walletAddressService.getOemAddressForPackage(installerPackageName, deviceManufacturer,
          deviceInfo, oemId, addressListener);
    }
  }

  public void getDeveloperAddress(String packageName, AddressListener addressListener) {
    if (packageName == null) {
      addressListener.onResponse(new AddressModel("", true));
    } else {
      developerAddressService.getDeveloperAddressForPackage(packageName, addressListener);
    }
  }

  private String getInstallerPackageName(String packageName) {
    return context.getPackageManager()
        .getInstallerPackageName(packageName);
  }
}
