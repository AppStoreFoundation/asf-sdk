package com.appcoins.sdk.billing.models;

public class GamificationModel {

  private final int maxBonus;
  private final String status;
  private final boolean error;

  public GamificationModel(int maxBonus, String status, boolean error) {

    this.maxBonus = maxBonus;
    this.status = status;
    this.error = error;
  }

  public GamificationModel(int maxBonus) {
    this.maxBonus = maxBonus;
    this.status = "ACTIVE";
    this.error = false;
  }

  private GamificationModel() {
    this.maxBonus = -1;
    this.status = "";
    this.error = true;
  }

  public static GamificationModel createErrorGamificationModel() {
    return new GamificationModel();
  }

  public int getMaxBonus() {
    return maxBonus;
  }

  public String getStatus() {
    return status;
  }

  public boolean hasError() {
    return error;
  }
}
