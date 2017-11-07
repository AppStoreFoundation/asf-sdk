package cm.aptoide.pt.ws.etherscan.transactionreceipt;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"jsonrpc", "result", "id"})
public class TransactionReceipt {

	@JsonProperty("jsonrpc") public String jsonrpc;
	@JsonProperty("result") public Result result;
	@JsonProperty("id") public Integer id;

	@Override
	public String toString() {
		return "TransactionReceipt{" + "jsonrpc='" + this.jsonrpc + '\'' + ", result=" + this.result +
						", id=" + this.id + '}';
	}
}
