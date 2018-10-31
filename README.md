# BDS SDK

The BDS SDK gives you the possibility to integrate on your application, transactions with AppCoins
(APPC) tokens, by rewarding users with APPC tokens when your application is being used.

## Abstract

This tutorial will guide you through the process of adding the BDS SDK as a dependency on your project.
Then you can **check our wiki page [User Acquisition SDK](https://github.com/AppStoreFoundation/asf-sdk/wiki/User-Acquisition-SDK) for more details**

The integration should be simple enough to be done in under 10 minutes. If this is not the case for 
you, let us know. **The SDK is able to work in the mainnet**. You can still use the Ropsten test 
network, meaning that APPC of all transactions do not have real monetary value, they are used solely 
for testing purposes.

### Prerequisites

+ In order for the BDS SDK to work, you must have an [AppCoins compliant wallet](https://github.com/Aptoide/asf-wallet-android/tree/dev) installed.
+ Minimum gradle plugin version is 3.0.1.
+ Minimum build tools version is 26.0.1.
+ The Android minimum API Level to use BDS SDK is 21 (Android 5.0).
+ Basic understanding of RxJava not required but is recommended.

## Build Script

In your **project's buildscript**, make sure you have the following:

```
buildscript {
  repositories {
    jcenter()
    google()
  }
  dependencies {
    classpath 'com.android.tools.build:gradle:3.1.0'

    // NOTE: Do not place your application dependencies here; they belong
    // in the individual module build.gradle files
  }
}

allprojects {
  repositories {
    jcenter()
    google()
    maven { url "https://dl.bintray.com/blockchainds/bds" }
  }
}
```
And in your **app's buildscript**, add the following dependency:

```
dependencies {
    api 'com.blockchainds:appcoins-ads:0.4.6.20b'
}
```


As an example, every time there is an update of the SDKs where no further integration needs to be done, the **only thing that should be changed is the version**. For example, when the next version is released, the dependencies that should be included in the **app's buildscript** are:

```
dependencies {
    api 'com.blockchainds:appcoins:0.4.7.21b'
}
```

## Size
As of version [0.4.6.20b](https://bintray.com/blockchainds/bds/appcoins-ads/0.4.6.20b), SDK weight is approximately as follows:

- SDK aar only (without dependencies) - 18KB.

App size increment after adding sdk (assuming no dependencies in common):

- Without Proguard: 4.5MB.
- With Proguard: 3.5MB.
