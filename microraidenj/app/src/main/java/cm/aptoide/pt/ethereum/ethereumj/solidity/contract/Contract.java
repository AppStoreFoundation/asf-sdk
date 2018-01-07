package cm.aptoide.pt.ethereum.ethereumj.solidity.contract;

public interface Contract {

  byte[] encode();

  enum Type {
    address, uint256
  }
}
