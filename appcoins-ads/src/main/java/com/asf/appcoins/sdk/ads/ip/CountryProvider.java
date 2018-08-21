package com.asf.appcoins.sdk.ads.ip;

import io.reactivex.Single;

public interface CountryProvider {
  Single<String> getCountryCode();
}
