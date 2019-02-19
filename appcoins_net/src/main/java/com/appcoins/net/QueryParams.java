package com.appcoins.net;

public class QueryParams {

  private final String sort;
  private final String by;
  private final String valid;
  private final String type;

  public QueryParams(String sort, String by, String valid, String type) {
    this.sort = sort;
    this.by = by;
    this.valid = valid;
    this.type = type;
  }

  public String getSort() {
    return sort;
  }

  public String getBy() {
    return by;
  }

  public String getValid() {
    return valid;
  }

  public String getType() {
    return type;
  }
}
