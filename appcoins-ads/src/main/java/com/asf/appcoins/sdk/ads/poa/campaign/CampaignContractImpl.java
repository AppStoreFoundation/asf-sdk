package com.asf.appcoins.sdk.ads.poa.campaign;

import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import java.math.BigInteger;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes2;
import org.web3j.abi.datatypes.generated.Bytes32;

import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

public class CampaignContractImpl implements CampaignContract {

  private final AsfWeb3j asfWeb3j;
  private final Address address;

  public CampaignContractImpl(AsfWeb3j asfWeb3j, Address contractAddress) {
    this.asfWeb3j = asfWeb3j;
    this.address = contractAddress;
  }

  @Override public String getPackageNameOfCampaign(BigInteger bidId) {
    byte[] value = new byte[32];
    System.arraycopy(bidId.toByteArray(), 0, value, value.length-bidId.toByteArray().length, bidId.toByteArray().length);

    Function function = new Function("getPackageNameOfCampaign",
        Collections.singletonList(new Bytes32(value)),
        Collections.singletonList(new TypeReference<Utf8String>() {
        }));

    String result =
        callSmartContractFunction(function, address.getValue(), Address.DEFAULT.getValue());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());
    if (!response.isEmpty()) {
      return String.valueOf(response.get(0));
    } else {
      throw new IllegalArgumentException("Failed to getPackageNameOfCampaign!");
    }
  }

  @Override public List<BigInteger> getCampaignsByCountry(String countryId) {
    Function function =
        new Function("getCampaignsByCountry", Collections.singletonList(new Utf8String(countryId)),
            Collections.singletonList(new TypeReference<DynamicArray<Bytes32>>() {
            }));

    String result =
        callSmartContractFunction(function, address.getValue(), Address.DEFAULT.getValue());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<BigInteger> ids = new LinkedList<>();

      Type type = response.get(0);
      List<Bytes32> values = (List<Bytes32>) type.getValue();

      for (Bytes32 bytes32 : values) {
        BigInteger campaignId = new BigInteger((bytes32).getValue());
        ids.add(campaignId);
      }
      return ids;
    } else {
      throw new IllegalArgumentException("Failed to getCampaignsByCountry!");
    }
  }

  @Override public List<String> getCountryList() {
    Function function = new Function("getCountryList", Collections.emptyList(),
        Collections.singletonList(new TypeReference<DynamicArray<Bytes2>>() {
        }));

    String result =
        callSmartContractFunction(function, address.getValue(), Address.DEFAULT.getValue());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<String> countries = new LinkedList<>();

      Type type = response.get(0);
      List<Bytes2> value = (List<Bytes2>) type.getValue();

      for (Bytes2 bytes2 : value) {
        String country = String.valueOf(new String((bytes2).getValue()));
        countries.add(country);
      }
      return countries;
    } else {
      throw new IllegalArgumentException("Failed to getCountryList!");
    }
  }

  @Override public List<BigInteger> getVercodesOfCampaign(BigInteger bidId) {
    byte[] value = new byte[32];
    System.arraycopy(bidId.toByteArray(), 0, value, value.length-bidId.toByteArray().length, bidId.toByteArray().length);

    Function function = new Function("getVercodesOfCampaign",
        Collections.singletonList(new Bytes32(value)),
        Collections.singletonList(new TypeReference<DynamicArray<Uint>>() {
        }));

    String result =
        callSmartContractFunction(function, address.getValue(), Address.DEFAULT.getValue());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<BigInteger> vercodes = new LinkedList<>();

      Type type = response.get(0);
      List<Uint> values = (List<Uint>) type.getValue();

      for (Uint uint : values) {
        vercodes.add(uint.getValue());
      }
      return vercodes;
    } else {
      throw new IllegalArgumentException("Failed to getVercodesOfCampaign!");
    }
  }

  @Override public boolean getCampaignValidity(BigInteger bidId) {
    byte[] value = new byte[32];
    System.arraycopy(bidId.toByteArray(), 0, value, value.length - bidId.toByteArray().length,
        bidId.toByteArray().length);

    Function function =
        new Function("getCampaignValidity", Collections.singletonList(new Bytes32(value)),
            Collections.singletonList(new TypeReference<Bool>() {
            }));

    String result =
        callSmartContractFunction(function, address.getValue(), Address.DEFAULT.getValue());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      for (Type type : response) {
        return ((Bool) type).getValue();
      }
    }
    throw new IllegalArgumentException("Failed to getCampaignValidity!");
  }

  private String callSmartContractFunction(Function function, String contractAddress,
      String walletAddress) {
    String encodedFunction = FunctionEncoder.encode(function);
    org.web3j.protocol.core.methods.request.Transaction transaction =
        createEthCallTransaction(walletAddress, contractAddress, encodedFunction);

    return asfWeb3j.call(transaction)
        .blockingFirst();
  }
}
