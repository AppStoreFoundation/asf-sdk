package cm.aptoide.pt.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({"status", "message", "result"})
public class TransactionsList {

	@JsonProperty("status") public String status;
	@JsonProperty("message") public String message;
	@JsonProperty("result") public List<Transaction> transaction;

	@Override
	public String toString() {
		return "TransactionsList{" + "status='" + this.status + '\'' + ", message='" + this.message +
						'\'' + ", " + "transaction=" + this.transaction + '}';
	}
}