package cm.aptoide.pt.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"blockNumber", "timeStamp", "hash", "nonce", "blockHash", "transactionIndex",
				"from", "to", "value", "gas", "gasPrice", "isError", "input", "contractAddress",
				"cumulativeGasUsed", "gasUsed", "confirmations"})
public class Transaction {

	@JsonProperty("blockNumber") public String blockNumber;
	@JsonProperty("timeStamp") public String timeStamp;
	@JsonProperty("hash") public String hash;
	@JsonProperty("nonce") public String nonce;
	@JsonProperty("blockHash") public String blockHash;
	@JsonProperty("transactionIndex") public String transactionIndex;
	@JsonProperty("from") public String from;
	@JsonProperty("to") public String to;
	@JsonProperty("value") public String value;
	@JsonProperty("gas") public String gas;
	@JsonProperty("gasPrice") public String gasPrice;
	@JsonProperty("isError") public String isError;
	@JsonProperty("input") public String input;
	@JsonProperty("contractAddress") public String contractAddress;
	@JsonProperty("cumulativeGasUsed") public String cumulativeGasUsed;
	@JsonProperty("gasUsed") public String gasUsed;
	@JsonProperty("confirmations") public String confirmations;

	@Override
	public String toString() {
		return "Transaction{" + "blockNumber='" + this.blockNumber + '\'' + ", timeStamp='" + this
						.timeStamp + '\'' + ", hash='" + this.hash + '\'' + ", nonce='" + this.nonce + '\'' +
						", blockHash='" + this.blockHash + '\'' + ", transactionIndex='" + this
						.transactionIndex + '\'' + ", from='" + this.from + '\'' + ", to='" + this.to + '\'' +
						", value='" + this.value + '\'' + ", gas='" + this.gas + '\'' + ", gasPrice='" + this
						.gasPrice + '\'' + ", isError='" + this.isError + '\'' + ", input='" + this.input +
						'\'' + ", contractAddress='" + this.contractAddress + '\'' + ", " +
						"cumulativeGasUsed='" + this.cumulativeGasUsed + '\'' + ", gasUsed='" + this.gasUsed +
						'\'' + "," +

						" confirmations='" + this.confirmations + '\'' + '}';
	}
}