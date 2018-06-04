package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.microraidenj.DefaultMicroRaidenClient;
import com.asf.microraidenj.MicroRaidenClient;
import com.asf.microraidenj.contract.MicroRaidenContract;
import com.asf.microraidenj.eth.GetChannelBlock;
import com.asf.microraidenj.eth.TransactionSender;
import com.asf.microraidenj.type.Address;
import com.bds.microraidenj.MicroRaidenBDS;
import com.bds.microraidenj.ws.BDSMicroRaidenApi;
import ethereumj.crypto.ECKey;
import io.reactivex.disposables.Disposable;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class SampleBDS {

  public static void main(String[] args) {

    Web3j web3j =
        Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
    AsfWeb3jImpl asfWeb3j = new AsfWeb3jImpl(web3j);

    Address channelManagerAddr = Address.from("0x97a3e71e4d9cb19542574457939a247491152e81");
    Address tokenAddr = Address.from("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3");
    Logger log = Logger.getLogger(MicroRaidenClient.class.getSimpleName());
    BigInteger maxDeposit = BigInteger.valueOf(10);
    TransactionSender transactionSender =
        new TransactionSenderImpl(asfWeb3j, () -> BigInteger.valueOf(50000000000L),
            new GetNonceImpl(asfWeb3j), new GasLimitImpl(web3j));

    GetChannelBlock getChannelBlock =
        createChannelTxHash -> new GetChannelBlockImpl(web3j, 3, 1500).get(createChannelTxHash);

    MicroRaidenContract microRaidenContract =
        new MicroRaidenContract(channelManagerAddr, tokenAddr, transactionSender);
    MicroRaidenClient microRaidenClient =
        new DefaultMicroRaidenClient(channelManagerAddr, maxDeposit, getChannelBlock,
            microRaidenContract);
    BDSMicroRaidenApi bdsMicroRaidenApi = BDSMicroRaidenApi.create();
    MicroRaidenBDS microRaidenBDS = new MicroRaidenBDS(microRaidenClient, bdsMicroRaidenApi);

    // Put a private key
    ECKey senderECKey = ECKey.fromPrivate(new BigInteger("", 16));
    ECKey receiverEcKey = ECKey.fromPrivate(
        new BigInteger("dd615cb6205e116410272c5c885ec1fcc1728bac667704523cc79a694fd61227", 16));

    Address receiverAddress = Address.from(receiverEcKey.getAddress());

    BigInteger openBlockNumber =
        microRaidenBDS.createChannel(senderECKey, receiverAddress, BigInteger.valueOf(1))
            .blockingGet();

    log.info("Channel created on block " + openBlockNumber);

    microRaidenBDS.topUpChannel(senderECKey, receiverAddress, openBlockNumber, maxDeposit)
        .blockingAwait();

    log.info("Channel topup");

    BigInteger owedBalance = BigInteger.valueOf(1);

    Disposable disposable =
        microRaidenBDS.closeChannel(senderECKey, receiverAddress, openBlockNumber, owedBalance)
            .subscribe(txHash -> log.info("Channel closed with tx " + txHash));
  }
}