package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.microraidenj.DefaultMicroRaidenServer;
import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.MicroRaidenServer;
import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.eth.ChannelBlockObtainer;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.DefaultChannelBlockObtainer;
import com.bds.microraidenj.DefaultGasLimitEstimator;
import com.bds.microraidenj.DefaultMicroRaidenClient;
import com.bds.microraidenj.util.DefaultTransactionSender;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class Sample {

  public static void main(String[] args) throws TransactionFailedException {

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
    MicroRaidenServer microRaidenServer =
        new DefaultMicroRaidenServer(channelManagerAddr, microRaidenContract);

    // Put a private key
    ECKey senderECKey = ECKey.fromPrivate(new BigInteger("", 16));
    ECKey receiverEcKey = ECKey.fromPrivate(
        new BigInteger("dd615cb6205e116410272c5c885ec1fcc1728bac667704523cc79a694fd61227", 16));

    Address receiverAddress = Address.from(receiverEcKey.getAddress());

    BigInteger openBlockNumber;
    try {
      openBlockNumber =
          microRaidenClient.createChannel(senderECKey, receiverAddress, BigInteger.valueOf(1));

      log.info("Channel created on block " + openBlockNumber);

      microRaidenClient.topUpChannel(senderECKey, receiverAddress, openBlockNumber, maxDeposit);

      log.info("Channel topup");
    } catch (TransactionFailedException | DepositTooHighException e) {
      throw new RuntimeException(e);
    }

    BigInteger owedBalance = BigInteger.valueOf(1);

    String txHash;
    boolean client = true;

    if (client) {
      byte[] closingSig = microRaidenServer.createClosingMessage(receiverEcKey,
          Address.from(senderECKey.getAddress()), openBlockNumber, owedBalance);

      txHash =
          microRaidenClient.closeChannelCooperatively(senderECKey, receiverAddress, openBlockNumber,
              owedBalance, closingSig, senderECKey);
    } else {
      byte[] balanceProof =
          microRaidenClient.createBalanceProof(senderECKey, receiverAddress, openBlockNumber,
              owedBalance);

      txHash = microRaidenServer.closeChannelCooperatively(Address.from(senderECKey.getAddress()),
          receiverEcKey, openBlockNumber, owedBalance, balanceProof, senderECKey);
    }

    log.info("Channel will be closed with tx " + txHash);
  }
}