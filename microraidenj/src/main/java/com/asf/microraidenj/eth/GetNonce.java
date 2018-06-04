package com.asf.microraidenj.eth;

import com.asf.microraidenj.type.Address;
import java.math.BigInteger;

public interface GetNonce {

  BigInteger get(Address address);
}
