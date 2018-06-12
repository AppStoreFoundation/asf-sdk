package com.asf.appcoins.toolbox;

import com.asf.appcoins.sdk.core.microraidenj.DefaultNonceObtainer;
import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.DefaultChannelBlockObtainer;
import com.bds.microraidenj.DefaultGasLimitEstimator;
import com.bds.microraidenj.DefaultMicroRaidenBDS;
import com.bds.microraidenj.DefaultMicroRaidenClient;
import com.bds.microraidenj.MicroRaidenBDS;
import com.bds.microraidenj.util.DefaultTransactionSender;
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

  private static MicroRaidenBDS instance = create(false);

  private BDSMicroRaidenjSingleton() {
  }

  public static MicroRaidenBDS create(boolean debug) {
    Web3j web3j =
        Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
    AsfWeb3jImpl asfWeb3j = new AsfWeb3jImpl(web3j);

    TransactionSender transactionSender =
        new DefaultTransactionSender(web3j, () -> BigInteger.valueOf(50000000000L),
            new DefaultNonceObtainer(asfWeb3j), new DefaultGasLimitEstimator(web3j));

    return new DefaultMicroRaidenBDS(
        new DefaultMicroRaidenClient(channelManagerAddr, BigInteger.valueOf(13),
            new DefaultChannelBlockObtainer(web3j, 3, 1500),
            new MicroRaidenContract(channelManagerAddr, tokenAddr, transactionSender)),
        BDSMicroRaidenApi.create(debug));
  }

  public static MicroRaidenBDS getInstance() {
    return instance;
  }
}
