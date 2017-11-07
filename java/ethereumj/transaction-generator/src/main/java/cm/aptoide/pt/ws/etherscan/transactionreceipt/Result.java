package cm.aptoide.pt.ws.etherscan.transactionreceipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(Include.NON_NULL)
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
	@JsonProperty("status") public Object status;
	@JsonProperty("transactionHash") public String transactionHash;
	@JsonProperty("transactionIndex") public String transactionIndex;

	@Override
	public String toString() {
		return "Result{" + "blockHash='" + this.blockHash + '\'' + ", blockNumber='" + this
						.blockNumber + '\'' + ", contractAddress=" + this.contractAddress + ", " +
						"cumulativeGasUsed='" + this.cumulativeGasUsed + '\'' + ", gasUsed='" + this.gasUsed +
						'\'' + ", logs=" + this.logs + ", logsBloom='" + this.logsBloom + '\'' + ", root='" +
						this.root + '\'' + ", status=" + this.status + ", transactionHash='" + this
						.transactionHash + '\'' + ", transactionIndex='" + this.transactionIndex + '\'' + '}';
	}
}
