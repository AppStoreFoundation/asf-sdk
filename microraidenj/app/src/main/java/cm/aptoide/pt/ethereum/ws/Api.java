package cm.aptoide.pt.ethereum.ws;

import java.util.HashMap;
import java.util.Map;

public enum Api {

  Etherscan("https://api.etherscan.io/", "https://ropsten.etherscan.io/"),;

  private final Map<Network, String> networkApiMap;

  Api(String mainNet, String ropsten) {
    this.networkApiMap = new HashMap<>();
    this.networkApiMap.put(Network.MAINNET, mainNet);
    this.networkApiMap.put(Network.ROPSTEN, ropsten);
  }

  public String getEndpoint(Network network) {
    return networkApiMap.get(network);
  }

}
