package cm.aptoide.pt.ethereum.ethereumj.solidity.contract.erc20;

import cm.aptoide.pt.ethereum.ethereumj.core.CallTransaction.Function;
import cm.aptoide.pt.ethereum.ethereumj.solidity.contract.DeployedContract;
import org.web3j.abi.datatypes.Address;

public class Erc20Transfer extends DeployedContract {

  private static final String METHOD = "transfer";

  private final String receiverAddr;
  private final long amount;

  public Erc20Transfer(Address contractAddress, String receiverAddr, long amount) {
    super(contractAddress);
    this.receiverAddr = receiverAddr;
    this.amount = amount;
  }

  @Override public byte[] encode() {
    return Function.fromSignature(METHOD, Type.address.name(), Type.uint256.name())
        .encode(receiverAddr, amount);
  }
}
