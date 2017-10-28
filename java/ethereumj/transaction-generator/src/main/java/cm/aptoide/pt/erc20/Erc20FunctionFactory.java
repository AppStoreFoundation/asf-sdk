package cm.aptoide.pt.erc20;

import cm.aptoide.pt.ethereumj.core.CallTransaction;

public class Erc20FunctionFactory {

	public CallTransaction.Function createTransferFunction() {
		return CallTransaction.Function.fromSignature(Method.transfer.name(), Type.address.name(),
						Type.uint256.name());
	}

	private enum Method {
		transfer
	}

	private enum Type {
		address,
		uint256
	}
}
