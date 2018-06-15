package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.eth.ChannelBlockObtainer;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.DefaultChannelBlockObtainer;
import com.bds.microraidenj.DefaultGasLimitEstimator;
import com.bds.microraidenj.DefaultMicroRaidenBDS;
import com.bds.microraidenj.DefaultMicroRaidenClient;
import com.bds.microraidenj.MicroRaidenBDS;
import com.bds.microraidenj.channel.BDSChannel;
import com.bds.microraidenj.channel.InsufficientFundsException;
import com.bds.microraidenj.util.DefaultTransactionSender;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class SampleBDS {

  public static void main(String[] args)
      throws InsufficientFundsException, TransactionFailedException {

    Web3j web3j =
        Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
    AsfWeb3jImpl asfWeb3j = new AsfWeb3jImpl(web3j);

    Address channelManagerAddr = Address.from("0x97a3e71e4d9cb19542574457939a247491152e81");
    Address tokenAddr = Address.from("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3");
    Logger log = Logger.getLogger(MicroRaidenClient.class.getSimpleName());
    BigInteger maxDeposit = BigInteger.valueOf(10);
    TransactionSender transactionSender =
        new DefaultTransactionSender(web3j, () -> BigInteger.valueOf(50000000000L),
            new DefaultNonceObtainer(asfWeb3j), new DefaultGasLimitEstimator(web3j));

    ChannelBlockObtainer channelBlockObtainer =
        createChannelTxHash -> new DefaultChannelBlockObtainer(web3j, 3, 1500).get(
            createChannelTxHash);

    MicroRaidenContract microRaidenContract =
        new MicroRaidenContract(channelManagerAddr, tokenAddr, transactionSender);
    MicroRaidenClient microRaidenClient =
        new DefaultMicroRaidenClient(channelManagerAddr, maxDeposit, channelBlockObtainer,
            microRaidenContract);
    BDSMicroRaidenApi bdsMicroRaidenApi = BDSMicroRaidenApi.create(true);
    MicroRaidenBDS microRaidenBDS = new DefaultMicroRaidenBDS(microRaidenClient, bdsMicroRaidenApi);

    // Put a private key
    ECKey senderECKey = ECKey.fromPrivate(new BigInteger("", 16));

    Address receiverAddress = Address.from("0x31a16aDF2D5FC73F149fBB779D20c036678b1bBD");

    BDSChannel bdsChannel =
        microRaidenBDS.createChannel(senderECKey, receiverAddress, maxDeposit)
            .blockingGet();

    BigInteger openBlockNumber = bdsChannel.getOpenBlockNumber();

    log.info("Channel created on block " + openBlockNumber);

    //bdsChannel.topUp(maxDeposit.divide(BigInteger.valueOf(2)));

    //log.info("Channel topup");

    Address devAddress = receiverAddress;
    Address storeAddress = receiverAddress;
    Address oemAddress = receiverAddress;

    bdsChannel.makePayment(BigInteger.valueOf(1), devAddress, storeAddress, oemAddress);

    BigInteger owedBalance = BigInteger.valueOf(1);

    try {
      bdsChannel.closeCooperatively(senderECKey);
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
  }
}