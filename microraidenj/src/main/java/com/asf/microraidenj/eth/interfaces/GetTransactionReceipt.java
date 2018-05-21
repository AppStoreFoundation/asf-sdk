package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.entities.TransactionReceipt;
import io.reactivex.Single;

public interface GetTransactionReceipt {

  Single<TransactionReceipt> get(String txHash);
}
