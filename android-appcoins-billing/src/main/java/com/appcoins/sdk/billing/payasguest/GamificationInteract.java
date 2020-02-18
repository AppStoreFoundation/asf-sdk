package com.appcoins.sdk.billing.payasguest;

import com.appcoins.billing.sdk.BuildConfig;
import com.appcoins.sdk.billing.SharedPreferencesRepository;
import com.appcoins.sdk.billing.service.BdsService;
import com.appcoins.sdk.billing.service.RequestResponse;
import com.appcoins.sdk.billing.service.ServiceResponseListener;
import java.util.ArrayList;
import java.util.HashMap;

class GamificationInteract {
  private SharedPreferencesRepository sharedPreferencesRepository;
  private BdsService bdsService;

  public GamificationInteract(SharedPreferencesRepository sharedPreferencesRepository) {
    this.sharedPreferencesRepository = sharedPreferencesRepository;
    bdsService = new BdsService(BuildConfig.BACKEND_BASE);
  }

  public void loadMaxBonus(final MaxBonusListener maxBonusListener) {
    if (sharedPreferencesRepository.hasSavedBonus(System.currentTimeMillis())) {
      maxBonusListener.onBonusReceived(sharedPreferencesRepository.getMaxBonus());
    } else {
      ServiceResponseListener serviceResponseListener = new ServiceResponseListener() {
        @Override public void onResponseReceived(RequestResponse requestResponse) {
          GamificationMapper gamificationMapper = new GamificationMapper();
          int maxBonus = gamificationMapper.mapToMaxBonus(requestResponse);
          sharedPreferencesRepository.setMaxBonus(maxBonus);
          maxBonusListener.onBonusReceived(maxBonus);
        }
      };
      bdsService.makeRequest("/gamification/levels", "GET", new ArrayList<String>(),
          new HashMap<String, String>(), new HashMap<String, Object>(), true,
          serviceResponseListener);
    }
  }
}
