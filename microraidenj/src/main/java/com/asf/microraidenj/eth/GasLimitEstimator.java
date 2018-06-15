package com.asf.microraidenj.eth;

import com.asf.microraidenj.exception.EstimateGasException;
import com.asf.microraidenj.type.Address;
import java.math.BigInteger;

public interface GasLimitEstimator {

  BigInteger estimate(Address from, Address to, byte[] data) throws EstimateGasException;
}
