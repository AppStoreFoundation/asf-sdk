package cm.aptoide.pt.erc20;

import cm.aptoide.pt.ethereumj.core.CallTransaction;

public class Erc20Transfer extends Erc20 {

	private static final String METHOD = "transfer";

	public Erc20Transfer() {
		super(CallTransaction.Function.fromSignature(METHOD, Type.address.name(), Type.uint256.name
						()));
	}

	public byte[] encode(String receiverAddr, long amount) {
		return function.encode(receiverAddr, amount);
	}
}
