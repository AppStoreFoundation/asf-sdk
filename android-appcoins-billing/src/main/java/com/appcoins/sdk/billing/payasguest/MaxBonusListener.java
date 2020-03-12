package com.appcoins.sdk.billing.payasguest;

import com.appcoins.sdk.billing.models.GamificationModel;

interface MaxBonusListener {

  void onBonusReceived(GamificationModel gamificationModel);
}
