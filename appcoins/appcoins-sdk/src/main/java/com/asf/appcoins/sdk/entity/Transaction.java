package com.asf.appcoins.sdk.entity;

/**
 * Created by neuro on 26-02-2018.
 */

public class Transaction {

  private final String hash;
  private final String from;
  private final String to;
  private final String value;
  private final State state;

  public Transaction(String hash, String from, String to, String value, State state) {
    this.hash = hash;
    this.from = from;
    this.to = to;
    this.value = value;
    this.state = state;
  }

  public enum State {
    PENDING, ACCEPTED, FAILED
  }
}
