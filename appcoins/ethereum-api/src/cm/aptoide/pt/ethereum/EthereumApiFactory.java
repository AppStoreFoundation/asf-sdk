package cm.aptoide.pt.ethereum;

public class EthereumApiFactory {

	public static EthereumApi createEthereumApi() {
		return new EthereumApiImpl();
	}
}
