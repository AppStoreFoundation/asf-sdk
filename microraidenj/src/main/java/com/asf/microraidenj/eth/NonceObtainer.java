package com.asf.microraidenj.eth;

import com.asf.microraidenj.type.Address;
import java.math.BigInteger;

public interface NonceObtainer {

  BigInteger getNonce(Address address);
}
