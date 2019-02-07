package com.appcoins.net;

import java.io.IOException;

interface AppcoinsConnectionQuerys {
  String Get(String params) throws IOException;
}