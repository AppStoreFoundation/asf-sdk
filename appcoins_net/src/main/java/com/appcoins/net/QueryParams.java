package com.appcoins.net;

class QueryParams {
  private final String packageName;
  private final String versionCode;
  private final String countryCode;
  private final String sort;
  private final String by;
  private final String valid;
  private final String type;

  public QueryParams(String packageName, String versionCode, String countryCode, String sort,
      String by, String valid, String type) {
    this.packageName = packageName;
    this.versionCode = versionCode;
    this.countryCode = countryCode;
    this.sort = sort;
    this.by = by;
    this.valid = valid;
    this.type = type;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getVersionCode() {
    return versionCode;
  }

  public String getCountryCode() {
    return countryCode;
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
