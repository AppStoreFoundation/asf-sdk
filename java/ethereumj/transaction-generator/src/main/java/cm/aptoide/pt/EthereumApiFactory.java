package cm.aptoide.pt;

public class EthereumApiFactory {

	public static EthereumApi createEthereumApi() {
		return new EthereumApiImpl();
	}
}
