package com.asf.microraidenj.eth.interfaces;

import com.asf.microraidenj.type.Address;
import io.reactivex.Single;

public interface Nonce {

  Single<Long> get(Address address);
}
