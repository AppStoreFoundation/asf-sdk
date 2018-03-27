package cm.aptoide.pt.web3j.abi.datatypes;

import cm.aptoide.pt.web3j.abi.TypeReference;
import java.util.List;

import static cm.aptoide.pt.web3j.abi.Utils.convert;

/**
 * Function type.
 */
public class Function {
  private final String name;
  private final List<Type> inputParameters;
  private final List<TypeReference<Type>> outputParameters;

  public Function(String name, List<Type> inputParameters,
      List<TypeReference<?>> outputParameters) {
    this.name = name;
    this.inputParameters = inputParameters;
    this.outputParameters = convert(outputParameters);
  }

  public String getName() {
    return name;
  }

  public List<Type> getInputParameters() {
    return inputParameters;
  }

  public List<TypeReference<Type>> getOutputParameters() {
    return outputParameters;
  }
}
