package cm.aptoide.pt.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionResultResponse {

	@JsonProperty("jsonrpc") public String jsonrpc;
	@JsonProperty("result") public String result;
	@JsonProperty("id") public Integer id;

	@Override
	public String toString() {
		return "TransactionResultResponse{" + "jsonrpc='" + jsonrpc + '\'' + ", result='" + result +
						'\'' + ", id=" + id + '}';
	}
}
