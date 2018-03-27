package cm.aptoide.pt.web3j.abi.datatypes;

/**
 * Boolean type.
 */
public class Bool implements Type<Boolean> {

  public static final String TYPE_NAME = "bool";
  public static final Bool DEFAULT = new Bool(false);

  private final boolean value;

  public Bool(boolean value) {
    this.value = value;
  }

  @Override public Boolean getValue() {
    return value;
  }

  @Override public String getTypeAsString() {
    return TYPE_NAME;
  }

  @Override public int hashCode() {
    return (value ? 1 : 0);
  }

  @Override public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Bool bool = (Bool) o;

    return value == bool.value;
  }
}
