package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.type.HexStr;
import java.math.BigInteger;

public interface GetChannelBlock {

  BigInteger get(HexStr createChannelTxHash);
}
