package com.asf.microraidenj.eth.interfaces;

import java.math.BigInteger;

public interface GetChannelBlock {

  BigInteger get(String createChannelTxHash);
}
