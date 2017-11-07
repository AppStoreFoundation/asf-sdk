package cm.aptoide.pt.ws.etherscan.transactionreceipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"address", "blockHash", "blockNumber", "data", "logIndex", "topics",
				"transactionHash", "transactionIndex", "transactionLogIndex", "type"})
public class Log {

	@JsonProperty("address") public String address;
	@JsonProperty("blockHash") public String blockHash;
	@JsonProperty("blockNumber") public String blockNumber;
	@JsonProperty("data") public String data;
	@JsonProperty("logIndex") public String logIndex;
	@JsonProperty("topics") public List<String> topics;
	@JsonProperty("transactionHash") public String transactionHash;
	@JsonProperty("transactionIndex") public String transactionIndex;
	@JsonProperty("transactionLogIndex") public String transactionLogIndex;
	@JsonProperty("type") public String type;

	@Override
	public String toString() {
		return "Log{" + "address='" + this.address + '\'' + ", blockHash='" + this.blockHash + '\'' +
						", " + "blockNumber='" + this.blockNumber + '\'' + ", data='" + this.data + '\'' + ", " +
						"logIndex='" + this.logIndex + '\'' + ", topics=" + this.topics + ", " +
						"transactionHash='" + this.transactionHash + '\'' + ", transactionIndex='" + this
						.transactionIndex + '\'' + ", transactionLogIndex='" + this.transactionLogIndex + '\''
						+ ", type='" + this.type + '\'' + '}';
	}
}
