package com.sdk.appcoins_adyen.api;

public class MakePaymentResponse {

  private final String pspReference;
  private final String resultCode;
  private final RedirectAction action;
  private final String refusalReason;
  private final String refusalReasonCode;

  public MakePaymentResponse(String pspReference, String resultCode, RedirectAction action,
      String refusalReason, String refusalReasonCode) {

    this.pspReference = pspReference;
    this.resultCode = resultCode;
    this.action = action;
    this.refusalReason = refusalReason;
    this.refusalReasonCode = refusalReasonCode;
  }

  public String getPspReference() {
    return pspReference;
  }

  public String getResultCode() {
    return resultCode;
  }

  public RedirectAction getAction() {
    return action;
  }

  public String getRefusalReason() {
    return refusalReason;
  }

  public String getRefusalReasonCode() {
    return refusalReasonCode;
  }
}
