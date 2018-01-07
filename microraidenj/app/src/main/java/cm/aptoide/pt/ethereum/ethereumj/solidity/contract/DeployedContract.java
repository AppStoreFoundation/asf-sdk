package cm.aptoide.pt.ethereum.ethereumj.solidity.contract;

import org.web3j.abi.datatypes.Address;

/**
 * Created by neuro on 07-01-2018.
 */

public abstract class DeployedContract implements Contract {

  private final Address address;

  protected DeployedContract(Address address) {
    this.address = address;
  }

  public Address getAddress() {
    return address;
  }
}
