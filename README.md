# ASF SDK

The ASF SDK gives you the possibility to integrate on your application, transactions with AppCoins 
(APPC) tokens. Either by enabling in-app purchases with APPC tokens or by rewarding users with APPC 
tokens when your application is being used.

## Abstract

This tutorial will guide you through the process of adding the ASF SDK as a dependency on your project.
Then for each separate component of the SDK you can **check our wiki pages [IAB SDK](https://github.com/AppStoreFoundation/asf-sdk/wiki/In-App-Billing-(IAB)-SDK)
and [Advertisement SDK](https://github.com/AppStoreFoundation/asf-sdk/wiki/Advertisement-SDK).**

The integration should be simple enough to be done in under 10 minutes. If this is not the case for 
you, let us know. **The SDK is able to work in the mainnet**. You can still use the Ropsten test 
network, meaning that APPC of all transactions do not have real monetary value, they are used solely 
for testing purposes.

### Prerequisites

+ In order for the ASF SDK to work, you must have an [AppCoins compliant wallet](https://github.com/Aptoide/asf-wallet-android/tree/dev) installed.
+ Minimum gradle plugin version is 3.0.1.
+ Minimum build tools version is 26.0.1.
+ The Android minimum API Level to use ASF SDK is 21 (Android 5.0).
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
    maven { url "https://dl.bintray.com/asf/asf" }
  }
}
```
And in your **app's buildscript**, add the following dependency:

```
dependencies {
    api 'com.asfoundation:appcoins:0.3.4a'
}
```

Or

```
dependencies {
    api 'com.asfoundation:appcoins-iab:0.3.4a'
    api 'com.asfoundation:appcoins-ads:0.3.4a'
}
```

As an example, every time there is an update of the SDKs where no further integration needs to be done, the **only thing that should be changed is the version**. For example, after the **release scheduled for the 2nd of May**, the dependencies that should be included in the **app's buildscript** are:

```
dependencies {
    api 'com.asfoundation:appcoins:0.3.5a'
}
```

Or

```
dependencies {
    api 'com.asfoundation:appcoins-iab:0.3.5a'
    api 'com.asfoundation:appcoins-ads:0.3.5a'
}
```

## Size
As of version [0.3.4a](https://bintray.com/asf/asf/appcoins-ads/0.3.4a), SDK weight is approximately as follows:

- SDK aar only (without dependencies) - 24KB.

App size increment after adding sdk (assuming no dependencies in common):

- Without Proguard: 4.5MB.
- With Proguard: 3.5MB.
