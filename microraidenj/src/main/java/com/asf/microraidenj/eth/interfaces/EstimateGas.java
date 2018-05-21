package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.type.Address;
import io.reactivex.Single;
import java.math.BigInteger;

public interface EstimateGas {

  Single<Long> estimate(Address from, Address to, BigInteger value, byte[] data);
}
