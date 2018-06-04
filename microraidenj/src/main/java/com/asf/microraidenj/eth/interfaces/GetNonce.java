package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.type.Address;

public interface GetNonce {

  long get(Address address);
}
