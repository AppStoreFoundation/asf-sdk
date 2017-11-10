package cm.aptoide.pt.ws.etherscan.transactionreceipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"blockHash", "blockNumber", "contractAddress", "cumulativeGasUsed",
				"gasUsed", "logs", "logsBloom", "root", "status", "transactionHash", "transactionIndex"})
public class Result {

	@JsonProperty("blockHash") public String blockHash;
	@JsonProperty("blockNumber") public String blockNumber;
	@JsonProperty("contractAddress") public Object contractAddress;
	@JsonProperty("cumulativeGasUsed") public String cumulativeGasUsed;
	@JsonProperty("gasUsed") public String gasUsed;
	@JsonProperty("logs") public List<Log> logs;
	@JsonProperty("logsBloom") public String logsBloom;
	@JsonProperty("root") public String root;
	@JsonProperty("status") public Integer status;
	@JsonProperty("transactionHash") public String transactionHash;
	@JsonProperty("transactionIndex") public String transactionIndex;

	@Override
	public String toString() {
		return "Result{" + "blockHash='" + blockHash + '\'' + ", blockNumber='" + blockNumber + '\'' +
						", contractAddress=" + contractAddress + ", " + "cumulativeGasUsed='" +
						cumulativeGasUsed + '\'' + ", gasUsed='" + gasUsed + '\'' + ", logs=" + logs + ", " +
						"logsBloom='" + logsBloom + '\'' + ", root='" + root + '\'' + ", status=" + status +
						", transactionHash='" + transactionHash + '\'' + ", transactionIndex='" +
						transactionIndex + '\'' + '}';
	}
}
