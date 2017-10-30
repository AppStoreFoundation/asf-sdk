package cm.aptoide.pt.erc20;

import cm.aptoide.pt.ethereumj.core.CallTransaction;

public abstract class Erc20 {

	protected final CallTransaction.Function function;

	protected Erc20(CallTransaction.Function function) {
		this.function = function;
	}

	protected enum Type {
		address,
		uint256
	}
}
