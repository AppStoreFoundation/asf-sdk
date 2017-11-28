package cm.aptoide.pt.ws.etherscan;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(Include.NON_NULL)
@JsonPropertyOrder({
        "jsonrpc",
        "result",
        "id"
})
public class GasPriceResponse {

    @JsonProperty("jsonrpc")
    public String jsonrpc;
    @JsonProperty("result")
    public String result;
    @JsonProperty("id")
    public Integer id;

}