package com.asf.appcoins.sdk.core.microraidenj;

import com.asf.appcoins.sdk.core.web3.AsfWeb3jImpl;
import com.asf.microraidenj.MicroRaidenImpl;
import com.asf.microraidenj.eth.interfaces.GetTransactionReceipt;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.spongycastle.util.encoders.Hex;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.http.HttpService;

public class Sample {

  public static void main(String[] args) {
    Web3j web3j =
        Web3jFactory.build(new HttpService("https://ropsten.infura.io/1YsvKO0VH5aBopMYJzcy"));
    AsfWeb3jImpl asfWeb3j = new AsfWeb3jImpl(web3j);

    Address channelManagerAddr = new Address("0x97a3e71e4d9cb19542574457939a247491152e81");
    Address tokenAddr = new Address("0xab949343E6C369C6B17C7ae302c1dEbD4B7B61c3");
    Logger log = Logger.getLogger(MicroRaidenImpl.class.getSimpleName());
    BigInteger maxDeposit = BigInteger.valueOf(10);
    TransactionSender transactionSender =
        new TransactionSenderImpl(asfWeb3j, 50000000000L, 4000000);

    GetTransactionReceipt getTransactionReceipt = new GetTransactionReceiptImpl(web3j, 3, 1500);

    MicroRaidenImpl microRaiden =
        new MicroRaidenImpl(channelManagerAddr, tokenAddr, log, maxDeposit, transactionSender,
            getTransactionReceipt);

    ECKey ecKey = ECKey.fromPrivate(
        new BigInteger("dd615cb6205e116410272c5c885ec1fcc1728bac667704523cc79a694fd61227", 16));

    try {
      Address receiverAddress = new Address("0x1BA4cd48B9F4D159192f23c8B1cF27c4b5188d26");
      BigInteger openBlockNumber =
          microRaiden.createChannel(ecKey, receiverAddress, BigInteger.valueOf(1));

      microRaiden.topUpChannel(ecKey, receiverAddress, maxDeposit, openBlockNumber);
    } catch (TransactionFailedException | DepositTooHighException e) {
      e.printStackTrace();
    }

    byte[] closingMsgHash =
        microRaiden.getClosingMsgHash(Address.from(ecKey.getAddress()), BigInteger.valueOf(3298424),
            BigInteger.valueOf(0));

    System.out.println("Closing Msg Hash");
    System.out.println(Hex.toHexString(closingMsgHash) + ", " + Hex.toHexString(
        microRaiden.getClosingMsgHashSigned(Address.from(ecKey.getAddress()),
            BigInteger.valueOf(3298424), BigInteger.valueOf(0), ecKey)));
  }
}
