package com.asf.appcoins.toolbox;

import com.asf.appcoins.sdk.core.microraidenj.GasLimitImpl;
import com.asf.appcoins.sdk.core.microraidenj.GetChannelBlockImpl;
import com.asf.appcoins.sdk.core.microraidenj.GetNonceImpl;
import com.asf.appcoins.sdk.core.microraidenj.TransactionSenderImpl;
import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.microraidenj.DefaultMicroRaidenClient;
import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.MicroRaidenBDS;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import java.math.BigInteger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class BDSMicroRaidenjSingleton {

  private static final Address channelManagerAddr =
      Address.from("0x97a3e71e4d9cb19542574457939a247491152e81");
  private static final Address tokenAddr =
      Address.from("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3");

  private static MicroRaidenBDS instance = create();

  private BDSMicroRaidenjSingleton() {
  }

  public static MicroRaidenBDS create() {
    Web3j web3j =
        Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
    AsfWeb3jImpl asfWeb3j = new AsfWeb3jImpl(web3j);

    TransactionSender transactionSender =
        new TransactionSenderImpl(new AsfWeb3jImpl(web3j), () -> BigInteger.valueOf(50000000000L),
            new GetNonceImpl(asfWeb3j), new GasLimitImpl(web3j));

    return new MicroRaidenBDS(
        new DefaultMicroRaidenClient(channelManagerAddr, BigInteger.valueOf(13),
            new GetChannelBlockImpl(web3j, 3, 1500),
            new MicroRaidenContract(channelManagerAddr, tokenAddr, transactionSender)),
        BDSMicroRaidenApi.create());
  }

  public static MicroRaidenBDS getInstance() {
    return instance;
  }
}
