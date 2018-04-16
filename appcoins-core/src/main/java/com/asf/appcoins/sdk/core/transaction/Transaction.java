package com.asf.appcoins.sdk.core.transaction;

/**
 * Created by neuro on 26-02-2018.
 */
public final class Transaction {

  private final String to;
  private final String value;
  private final Status status;
  private String hash;
  private String from;

  public Transaction(String hash, String from, String to, String value, Status status) {
    this.hash = hash;
    this.from = from;
    this.to = to;
    this.value = value;
    this.status = status;
  }

  public String getHash() {
    return hash;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {

    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public String getValue() {
    return value;
  }

  public Status getStatus() {
    return status;
  }

  public enum Status {
    PENDING(-1), ACCEPTED(1), FAILED(0),;

    private final String value;

    Status(int value) {
      this.value = Integer.toString(value);
    }

    Status(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }
}
