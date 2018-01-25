package cm.aptoide.pt.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionCountResponse {

	public final String jsonrpc;
	public final String result;
	public final int id;

	public TransactionCountResponse(@JsonProperty("jsonrpc") String jsonrpc, @JsonProperty("result")
					String result, @JsonProperty("id") int id) {
		this.jsonrpc = jsonrpc;
		this.result = result;
		this.id = id;
	}

	@Override
	public String toString() {
		return "TransactionCountResponse{" + "jsonrpc='" + jsonrpc + '\'' + ", result='" + result +
						'\'' + ", id=" + id + '}';
	}
}
