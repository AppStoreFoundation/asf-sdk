package com.asf.appcoins.sdk.iab.entity;

import java.math.BigDecimal;

/**
 * Created by neuro on 04-03-2018.
 */
public class SKU {

  private final String name;
  private final String id;
  private final BigDecimal value;

  public SKU(String name, String id, BigDecimal value) {
    this.name = name;
    this.id = id;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public String getId() {
    return id;
  }

  public BigDecimal getValue() {
    return value;
  }
}
