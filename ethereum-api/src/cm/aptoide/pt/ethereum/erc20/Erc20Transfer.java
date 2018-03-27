package cm.aptoide.pt.ethereum.erc20;

import cm.aptoide.pt.ethereumj.core.CallTransaction;

public class Erc20Transfer implements Erc20 {

	private static final String METHOD = "transfer";

	private final String receiverAddr;
	private final long amount;

	public Erc20Transfer(String receiverAddr, long amount) {
		this.receiverAddr = receiverAddr;
		this.amount = amount;
	}

	@Override
	public byte[] encode() {
		return CallTransaction.Function.fromSignature(METHOD, Type.address.name(), Type.uint256.name())
						.encode(receiverAddr, amount);
	}
}
