package com.appcoins.net;

import java.io.IOException;

interface AppcoinsConnectionQuerys {
  String Get() throws IOException;
  boolean Ping();
}