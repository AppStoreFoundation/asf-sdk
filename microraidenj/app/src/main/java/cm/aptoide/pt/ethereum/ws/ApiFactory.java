package cm.aptoide.pt.ethereum.ws;

import cm.aptoide.pt.ethereum.ws.etherscan.EtherscanApi;

public class ApiFactory {

  private final WebServiceFactory webServiceFactory;

  public ApiFactory(WebServiceFactory webServiceFactory) {
    this.webServiceFactory = webServiceFactory;
  }

  public EtherscanApi createEtherscanApi(Network network) {
    return webServiceFactory.createWebService(network.getHost(), EtherscanApi.class);
  }
}
