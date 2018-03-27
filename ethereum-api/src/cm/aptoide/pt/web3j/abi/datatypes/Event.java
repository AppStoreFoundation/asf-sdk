package cm.aptoide.pt.web3j.abi.datatypes;

import cm.aptoide.pt.web3j.abi.TypeReference;
import java.util.List;

import static cm.aptoide.pt.web3j.abi.Utils.convert;

/**
 * Event wrapper type.
 */
public class Event {
  private final String name;
  private final List<TypeReference<Type>> indexedParameters;
  private final List<TypeReference<Type>> nonIndexedParameters;

  public Event(String name, List<TypeReference<?>> indexedParameters,
      List<TypeReference<?>> nonIndexedParameters) {
    this.name = name;
    this.indexedParameters = convert(indexedParameters);
    this.nonIndexedParameters = convert(nonIndexedParameters);
  }

  public String getName() {
    return name;
  }

  public List<TypeReference<Type>> getIndexedParameters() {
    return indexedParameters;
  }

  public List<TypeReference<Type>> getNonIndexedParameters() {
    return nonIndexedParameters;
  }
}
