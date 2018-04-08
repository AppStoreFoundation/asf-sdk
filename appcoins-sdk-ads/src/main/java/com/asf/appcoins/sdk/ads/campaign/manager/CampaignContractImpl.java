package com.asf.appcoins.sdk.ads.campaign.manager;

import com.asf.appcoins.sdk.ads.campaign.contract.CampaignContract;
import java.io.IOException;
import java.math.BigInteger;
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
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthCall;

import static org.web3j.protocol.core.methods.request.Transaction.createEthCallTransaction;

class CampaignContractImpl implements CampaignContract {

  private final Web3j web3j;
  private final Address address;

  CampaignContractImpl(Web3j web3j, Address contractAddress) {
    this.web3j = web3j;
    this.address = contractAddress;
  }

  @Override public String getPackageNameOfCampaign(BigInteger bidId) throws IOException {
    Function function = new Function("getPackageNameOfCampaign",
        Collections.singletonList(new Bytes32(bidId.toByteArray())),
        Collections.singletonList(new TypeReference<Utf8String>() {
        }));

    String result = callSmartContractFunction(function, address.getTypeAsString(),
        Address.DEFAULT.getTypeAsString());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());
    if (!response.isEmpty()) {
      return String.valueOf(response.get(0));
    } else {
      throw new IllegalArgumentException("Failed to getPackageNameOfCampaign!");
    }
  }

  @Override public List<BigInteger> getCampaignsByCountry(String countryId) throws IOException {
    Function function =
        new Function("getCampaignsByCountry", Collections.singletonList(new Utf8String(countryId)),
            Collections.singletonList(new TypeReference<DynamicArray<Bytes32>>() {
            }));

    String result = callSmartContractFunction(function, address.getTypeAsString(),
        Address.DEFAULT.getTypeAsString());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<BigInteger> ids = new LinkedList<>();
      for (Type type : response) {
        ids.add(new BigInteger(((Bytes32) type).getValue()));
      }
      return ids;
    } else {
      throw new IllegalArgumentException("Failed to getCampaignsByCountry!");
    }
  }

  @Override public List<String> getCountryList() throws IOException {
    Function function = new Function("getCountryList", Collections.emptyList(),
        Collections.singletonList(new TypeReference<DynamicArray<Bytes2>>() {
        }));

    String result = callSmartContractFunction(function, address.getTypeAsString(),
        Address.DEFAULT.getTypeAsString());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<String> countries = new LinkedList<>();
      for (Type type : response) {
        countries.add(String.valueOf(type));
      }
      return countries;
    } else {
      throw new IllegalArgumentException("Failed to getCountryList!");
    }
  }

  @Override public List<BigInteger> getVercodesOfCampaign(BigInteger bidId) throws IOException {
    Function function = new Function("getVercodesOfCampaign",
        Collections.singletonList(new Bytes32(bidId.toByteArray())),
        Collections.singletonList(new TypeReference<DynamicArray<Uint>>() {
        }));

    String result = callSmartContractFunction(function, address.getTypeAsString(),
        Address.DEFAULT.getTypeAsString());
    List<Type> response = FunctionReturnDecoder.decode(result, function.getOutputParameters());

    if (!response.isEmpty()) {
      List<BigInteger> vercodes = new LinkedList<>();
      for (Type type : response) {
        vercodes.add(((Uint) type).getValue());
      }
      return vercodes;
    } else {
      throw new IllegalArgumentException("Failed to getVercodesOfCampaign!");
    }
  }

  private String callSmartContractFunction(Function function, String contractAddress,
      String walletAddress) throws IOException {
    String encodedFunction = FunctionEncoder.encode(function);
    org.web3j.protocol.core.methods.request.Transaction transaction =
        createEthCallTransaction(walletAddress, contractAddress, encodedFunction);
    EthCall response = web3j.ethCall(transaction, DefaultBlockParameterName.LATEST)
        .send();

    return response.getValue();
  }
}
