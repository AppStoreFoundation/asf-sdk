package cm.aptoide.pt.ethereum.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionByHashResponse {

  @JsonProperty("jsonrpc") public String jsonrpc;
  @JsonProperty("result") public Result result;
  @JsonProperty("id") public Integer id;

  public class Result {

    @JsonProperty("blockHash") public String blockHash;
    @JsonProperty("blockNumber") public String blockNumber;
    @JsonProperty("condition") public Object condition;
    @JsonProperty("creates") public Object creates;
    @JsonProperty("from") public String from;
    @JsonProperty("gas") public String gas;
    @JsonProperty("gasPrice") public String gasPrice;
    @JsonProperty("hash") public String hash;
    @JsonProperty("input") public String input;
    @JsonProperty("networkId") public Object networkId;
    @JsonProperty("nonce") public String nonce;
    @JsonProperty("publicKey") public String publicKey;
    @JsonProperty("r") public String r;
    @JsonProperty("raw") public String raw;
    @JsonProperty("s") public String s;
    @JsonProperty("standardV") public String standardV;
    @JsonProperty("to") public String to;
    @JsonProperty("transactionIndex") public String transactionIndex;
    @JsonProperty("v") public String v;
    @JsonProperty("value") public String value;

    @Override public String toString() {
      return "Result{"
          + "blockHash='"
          + blockHash
          + '\''
          + ", blockNumber='"
          + blockNumber
          + '\''
          + ", condition="
          + condition
          + ", creates="
          + creates
          + ", from='"
          + from
          + '\''
          + ", gas='"
          + gas
          + '\''
          + ", gasPrice='"
          + gasPrice
          + '\''
          + ", hash='"
          + hash
          + '\''
          + ", input='"
          + input
          + '\''
          + ", networkId="
          + networkId
          + ", nonce='"
          + nonce
          + '\''
          + ", publicKey='"
          + publicKey
          + '\''
          + ", r='"
          + r
          + '\''
          + ", raw='"
          + raw
          + '\''
          + ", s='"
          + s
          + '\''
          + ", standardV='"
          + standardV
          + '\''
          + ", to='"
          + to
          + '\''
          + ", transactionIndex='"
          + transactionIndex
          + '\''
          + ", v='"
          + v
          + '\''
          + ", "
          + "value='"
          + value
          + '\''
          + '}';
    }
  }

  @Override public String toString() {
    return "TransactionByHashResponse{"
        + "jsonrpc='"
        + jsonrpc
        + '\''
        + ", result="
        + result
        + ","
        + ""
        + ""
        + ""
        + " id="
        + id
        + '}';
  }
}

