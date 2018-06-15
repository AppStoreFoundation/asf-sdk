package com.bds.microraidenj.ws;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class ListAllChannelsResponse {

  @JsonProperty("result") private List<Result> result;

  @JsonProperty("result") public List<Result> getResult() {
    return result;
  }

  @JsonProperty("result") public void setResult(List<Result> result) {
    this.result = result;
  }

  public static class Result {

    @JsonProperty("block") private Integer block;
    @JsonProperty("closed") private Boolean closed;
    @JsonProperty("receiver") private String receiver;
    @JsonProperty("receiver_balance") private BigInteger receiverBalance;
    @JsonProperty("sender") private String sender;
    @JsonProperty("sender_balance") private BigInteger senderBalance;
    @JsonProperty("ts") private Date ts;

    public Integer getBlock() {
      return block;
    }

    public void setBlock(Integer block) {
      this.block = block;
    }

    public Boolean getClosed() {
      return closed;
    }

    public void setClosed(Boolean closed) {
      this.closed = closed;
    }

    public String getReceiver() {
      return receiver;
    }

    public void setReceiver(String receiver) {
      this.receiver = receiver;
    }

    public String getSender() {
      return sender;
    }

    public void setSender(String sender) {
      this.sender = sender;
    }

    public BigInteger getReceiverBalance() {
      return receiverBalance;
    }

    public void setReceiverBalance(BigInteger receiverBalance) {
      this.receiverBalance = receiverBalance;
    }

    public BigInteger getSenderBalance() {
      return senderBalance;
    }

    public void setSenderBalance(BigInteger senderBalance) {
      this.senderBalance = senderBalance;
    }

    public Date getTs() {
      return ts;
    }

    public void setTs(Date ts) {
      this.ts = ts;
    }

    @Override public String toString() {
      return "Result{"
          + "block="
          + block
          + ", closed="
          + closed
          + ", receiver='"
          + receiver
          + '\''
          + ", receiverBalance="
          + receiverBalance
          + ", sender='"
          + sender
          + '\''
          + ", senderBalance="
          + senderBalance
          + ", ts='"
          + ts
          + '\''
          + '}';
    }
  }
}