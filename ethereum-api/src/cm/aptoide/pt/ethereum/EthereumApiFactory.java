package cm.aptoide.pt.ethereum;

import cm.aptoide.pt.ethereum.ws.Network;

public class EthereumApiFactory {

	public static EthereumApi createEthereumApi() {
		return new EthereumApiImpl(Network.MAINNET);
	}

	public static EthereumApi createEthereumApi(Network network) {
		return new EthereumApiImpl(network);
	}
}
