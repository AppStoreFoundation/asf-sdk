package cm.aptoide.pt.ethereum.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BalanceResponse {

  public final String status;
  public final String message;
  public final String result;

  @JsonCreator public BalanceResponse(@JsonProperty("status") String status,
      @JsonProperty("message") String message, @JsonProperty("result") String result) {
    this.status = status;
    this.message = message;
    this.result = result;
  }

  @Override public String toString() {
    return "BalanceResponse{"
        + "status='"
        + status
        + '\''
        + ", message='"
        + message
        + '\''
        + ", result='"
        + result
        + '\''
        + '}';
  }
}
