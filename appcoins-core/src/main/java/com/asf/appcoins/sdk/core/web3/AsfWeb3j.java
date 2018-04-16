package com.asf.appcoins.sdk.core.web3;

import com.asf.appcoins.sdk.core.transaction.Transaction;
import io.reactivex.Observable;
import java.math.BigDecimal;
import org.web3j.abi.datatypes.Address;

/**
 * Created by neuro on 26-02-2018.
 */
public interface AsfWeb3j {

  Observable<Long> getNonce(Address address);

  Observable<Long> getGasPrice(Address address);

  Observable<BigDecimal> getBalance(Address address);

  Observable<String> sendRawTransaction(String rawData);

  Observable<Transaction> getTransactionByHash(String txHash);

  Observable<String> call(org.web3j.protocol.core.methods.request.Transaction transaction);
}
