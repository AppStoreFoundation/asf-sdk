# ASF SDK

The ASF SDK lets you sell in-app items for AppCoins (APPC) tokens.

## Abstract

This tutorial will guide through the process of integrating the ASF SDK.
The integration should be simple enough to be done in under 10 minutes.
If this is not the case for you, let us know.

### Prerequisites

+ In order for the ASF SDK to work, you must have an [AppCoins compliant wallet](https://github.com/Aptoide/asf-wallet-android/tree/dev) installed.
+ The Android minimum API Level to use ASF SDK is 21 (Android 5.0).
+ Basic understanding of RxJava is advised but now required.

## Build Script

In your **project's buildscript**, make sure you have the following:

```
allprojects {
    repositories {
        google()
        jcenter()
        maven { url "https://dl.bintray.com/asf/asf" }
    }
}
```
And in your **app's buildscript**, add the following dependency:

```
dependencies {
    api 'com.asfoundation:sdk-android:0.1.1a'
}
```

## Getting Started

To integrate the ASF SDK, you only need an instance of the AppCoinsSdk interface.
For the sake of simplicity, in the sample code we just hold a static referecence to the ASF SDK instance in the Application class.

```
  public static AppCoinsSdk appCoinsSdk;

  public static final String SKU_GAS = "gas";
  public static final String SKU_PREMIUM = "premium";
  public static final String SKU_GAS_NAME = SKU_GAS;
  public static final String SKU_PREMIUM_NAME = SKU_PREMIUM;

  private final String developerAddress = "0x4fbcc5ce88493c3d9903701c143af65f54481119";

  @Override public void onCreate() {
    super.onCreate();

    appCoinsSdk = new AppCoinsSdkBuilder(developerAddress).withSkus(buildSkus())
        .withDebug(true)
        .createAppCoinsSdk();
  }

  private List<SKU> buildSkus() {
    List<SKU> skus = new LinkedList<>();

    skus.add(new SKU(SKU_GAS_NAME, SKU_GAS, BigDecimal.valueOf(5)));
    skus.add(new SKU(SKU_PREMIUM_NAME, SKU_PREMIUM, BigDecimal.TEN));

    return skus;
  }
```

Here we use a convenient builder to create our AppCoinsSDK instance with a list consisting of two products.
The debug flag will set the ASF SDK to use the testnet (Ropsten) instead of the mainnet.

Given the Android architecture, you will have to let the SDK know each time a Purchase Flow is finished. For that, just inform the sdk:

```
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Application.appCoinsSdk.onActivityResult(requestCode, requestCode, data);
}
```

AppCoinsSdk's onActivityResult will return **true** if handled, and **false** otherwise. This may be useful if you want to include further logic at this point.

To start the purchase flow, you have to pass one of the previously defined *SKU_IDs*, and the activity that will be used to call the Wallet:

```
Application.appCoinsSdk.buy(SKU_GAS, this);
```

And finally, we want to react to the purchase, so we can reflect that change in our App's state.

Below we provide a good pattern to follow:

```
@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (Application.appCoinsSdk.onActivityResult(requestCode, requestCode, data)) {
      Application.appCoinsSdk.getCurrentPayment()
          .subscribe(paymentDetails -> runOnUiThread(() -> {
            if (paymentDetails.getPaymentStatus() == PaymentStatus.SUCCESS) {
              String skuId = paymentDetails.getSkuId();
              // Now we tell the sdk to consume the skuId.
              Application.appCoinsSdk.consume(skuId);

              // Purchase successfully done. Release the prize.
            }
          }));
    }
  }
```

First we check if it was the ASF SDK who made us leave the current activity. If so, we will react to the purchase result.
In order to complete the purchase flow, we must consume the purchase.