package cm.aptoide.pt.ethereum.ws;

public enum Network {

  MAINNET("https://api.etherscan.io/", 1), ROPSTEN("https://ropsten.etherscan.io/", 3), KOVAN(
      "https://kovan.etherscan.io/", 42),;

  private final String host;
  private final int networkId;

  Network(String host, int networkId) {
    this.host = host;
    this.networkId = networkId;
  }

  public String getHost() {
    return host;
  }

  public int getNetworkId() {
    return networkId;
  }
}
