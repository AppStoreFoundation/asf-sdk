# ASF SDK

The ASF SDK gives you the possibility to integrate on your application, transactions with AppCoins 
(APPC) tokens. Either by enabling in-app purchases with APPC tokens or by rewarding users with APPC 
tokens when your application is being used.

## Abstract

This tutorial will guide you through the process of adding the ASF SDK as a dependency on your project.
Then for each separate component of the SDK you can check our wiki pages [IAB SDK](https://github.com/AppStoreFoundation/asf-sdk/wiki/In-App-Billing-(IAB)-SDK)
and [Advertisement SDK](https://github.com/AppStoreFoundation/asf-sdk/wiki/Advertisement-SDK).

The integration should be simple enough to be done in under 10 minutes. If this is not the case for 
you, let us know. The SDK is able to work in the mainnet. You can still use the Ropsten test 
network, meaning that APPC of all transactions do not have real monetary value, they are used solely 
for testing purposes.

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
    api 'com.asfoundation:appcoins-sdk:0.2.1a'
}
```

Or

```
dependencies {
    api 'com.asfoundation:appcoins-sdk-iab:0.1.1a'
    api 'com.asfoundation:appcoins-sdk-ads:0.1.1a'
}
```

## Size
As of version [0.2.1a](https://bintray.com/asf/asf/appcoins-sdk/0.1.1a), SDK weight is approximately as follows:

- SDK aar only (without dependencies) - 24KB.

App size increment after adding sdk (assuming no dependencies in common):

- Without Proguard: 4.5MB.
- With Proguard: 3.5MB.