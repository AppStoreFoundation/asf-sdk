package cm.aptoide.pt.web3j.abi;

import cm.aptoide.pt.web3j.abi.datatypes.Event;
import cm.aptoide.pt.web3j.abi.datatypes.Type;
import cm.aptoide.pt.web3j.crypto.Hash;
import cm.aptoide.pt.web3j.utils.Numeric;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>Ethereum filter encoding.
 * Further limited details are available
 * <a href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI#events">here</a>.
 * </p>
 */
public class EventEncoder {

  private EventEncoder() {
  }

  public static String encode(Event function) {
    List<TypeReference<Type>> indexedParameters = function.getIndexedParameters();
    List<TypeReference<Type>> nonIndexedParameters = function.getNonIndexedParameters();

    String methodSignature =
        buildMethodSignature(function.getName(), indexedParameters, nonIndexedParameters);

    return buildEventSignature(methodSignature);
  }

  static <T extends Type> String buildMethodSignature(String methodName,
      List<TypeReference<T>> indexParameters, List<TypeReference<T>> nonIndexedParameters) {

    List<TypeReference<T>> parameters = new ArrayList<>(indexParameters);
    parameters.addAll(nonIndexedParameters);

    StringBuilder result = new StringBuilder();
    result.append(methodName);
    result.append("(");

    ArrayList<String> arr = new ArrayList<>(parameters.size());
    for (TypeReference<T> p : parameters) {
      String typeName = Utils.getTypeName(p);
      arr.add(typeName);
    }
    String params = StringUtils.join(arr, ",");
    result.append(params);
    result.append(")");
    return result.toString();
  }

  public static String buildEventSignature(String methodSignature) {
    byte[] input = methodSignature.getBytes();
    byte[] hash = Hash.sha3(input);
    return Numeric.toHexString(hash);
  }
}
