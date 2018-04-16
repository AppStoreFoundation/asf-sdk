package com.asf.appcoins.sdk.ads.campaign.manager;

import com.asf.appcoins.sdk.ads.campaign.contract.CampaignContract;
import com.asf.appcoins.sdk.core.web3.AsfWeb3j;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.DynamicArray;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Uint;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes2;
import org.web3j.abi.datatypes.generated.Bytes32;

import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

class CampaignContractImpl implements CampaignContract {

  private final AsfWeb3j asfWeb3j;
  private final Address address;

  CampaignContractImpl(AsfWeb3j asfWeb3j, Address contractAddress) {
    this.asfWeb3j = asfWeb3j;
    this.address = contractAddress;
  }

  @Override public String getPackageNameOfCampaign(BigInteger bidId) {
    Function function = new Function("getPackageNameOfCampaign",
        Collections.singletonList(new Bytes32(Arrays.copyOf(bidId.toByteArray(), 32))),
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
      for (Type type : response) {
        if (!((List) type.getValue()).isEmpty()) {
          BigInteger campaingId =
              new BigInteger(((Bytes32) ((List) type.getValue()).get(0)).getValue());
          ids.add(campaingId);
        }
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
      for (Type type : response) {
        String country =
            String.valueOf(new String(((Bytes2) ((ArrayList) type.getValue()).get(0)).getValue()));
        countries.add(country);
      }
      return countries;
    } else {
      throw new IllegalArgumentException("Failed to getCountryList!");
    }
  }

  @Override public List<BigInteger> getVercodesOfCampaign(BigInteger bidId) {
    Function function = new Function("getVercodesOfCampaign",
        Collections.singletonList(new Bytes32(Arrays.copyOf(bidId.toByteArray(), 32))),
        Collections.singletonList(new TypeReference<DynamicArray<Uint>>() {
        }));

    String result =
        callSmartContractFunction(function, address.getValue(), Address.DEFAULT.getValue());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<BigInteger> vercodes = new LinkedList<>();
      for (Type type : response) {
        BigInteger vercode = ((Uint) ((ArrayList) type.getValue()).get(0)).getValue();
        vercodes.add(vercode);
      }
      return vercodes;
    } else {
      throw new IllegalArgumentException("Failed to getVercodesOfCampaign!");
    }
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
