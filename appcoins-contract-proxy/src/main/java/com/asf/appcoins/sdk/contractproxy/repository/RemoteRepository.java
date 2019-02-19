package com.asf.appcoins.sdk.contractproxy.repository;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RemoteRepository {
  private final ApiProvider apiProvider;

  public RemoteRepository(ApiProvider apiProvider) {
    this.apiProvider = apiProvider;
  }

  public Single<String> getAddress(Contracts contract, int chainId) {
    return apiProvider.getApi(chainId)
        .getAddress(contract.value)
        .map(ProxyResponse::getWallet);
  }

  public enum Contracts {
    APPCOINS_IAB("appcoinsiab"), APPCOINS("appcoins"), APPCOINS_ADS("advertisement");
    private final String value;

    Contracts(String value) {
      this.value = value;
    }
  }

  public interface Api {
    @GET("appc/proxyaddress") Single<ProxyResponse> getAddress(@Query("contract") String contract);
  }

  private static class ProxyResponse {
    private String wallet;

    public ProxyResponse() {
    }

    public String getWallet() {
      return wallet;
    }

    public void setWallet(String wallet) {
      this.wallet = wallet;
    }
  }
}
