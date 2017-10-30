package cm.aptoide.pt.erc20;

public interface Erc20 {

	byte[] encode();

	enum Type {
		address,
		uint256
	}
}
