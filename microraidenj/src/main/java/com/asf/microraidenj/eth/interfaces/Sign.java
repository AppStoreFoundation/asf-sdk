package com.asf.microraidenj.eth.interfaces;

import ethereumj.Transaction;

public interface Sign {

  void sign(Transaction transaction);
}
