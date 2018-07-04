package com.asf.appcoins.sdk.contractproxy;

import com.asf.appcoins.sdk.contractproxy.proxy.WalletAddressProvider;
import com.asf.appcoins.sdk.contractproxy.proxy.Web3jProvider;
import com.asf.appcoins.sdk.contractproxy.proxy.Web3jProxyContract;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import java.util.concurrent.ConcurrentHashMap;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

/**
 * Created by Joao Raimundo on 01-03-2018.
 */
public final class AppCoinsAddressProxyBuilder {

  private static final String TAG = AppCoinsAddressProxyBuilder.class.getSimpleName();

  public AppCoinsAddressProxySdk createAddressProxySdk() {
    return createAddressProxySdk(() -> Single.just(Address.DEFAULT.getValue()), chainId -> {
      switch (chainId) {
        case 3:
          return Web3jFactory.build(
              new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
        default:
          return Web3jFactory.build(
              new HttpService("https://mainnet.infura.io/1YsvKO0VH5aBopMYJzcy"));
      }
    });
  }

  public AppCoinsAddressProxySdk createAddressProxySdk(WalletAddressProvider walletAddressProvider,
      @NonNull Web3jProvider web3jProvider) {
    return new ContractAddressProvider(walletAddressProvider,
        new Web3jProxyContract(web3jProvider, chainId -> {
          switch (chainId) {
            case 3:
              return BuildConfig.ROPSTEN_NETWORK_PROXY_CONTRACT_ADDRESS;
            default:
              return BuildConfig.MAIN_NETWORK_PROXY_CONTRACT_ADDRESS;
          }
        }), new ConcurrentHashMap<>());
  }
}