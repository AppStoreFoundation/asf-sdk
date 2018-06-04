package com.asf.microraidenj;

import com.asf.microraidenj.eth.interfaces.GetChannelBlock;
import com.asf.microraidenj.eth.interfaces.TransactionSender;
import com.asf.microraidenj.exception.DepositTooHighException;
import com.asf.microraidenj.exception.TransactionFailedException;
import com.asf.microraidenj.type.Address;
import com.asf.microraidenj.type.HexStr;
import ethereumj.crypto.ECKey;
import java.math.BigInteger;
import java.util.logging.Logger;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MicroRaidenImplTest {

  // Ropsten network 21/05/2018
  private static final Address CHANNEL_MANAGER_ADRESS =
      Address.from("0x74434527b8E6C8296506D61d0faF3D18c9e4649A");
  private static final Address TOKEN_ADRESS =
      Address.from("0xff24d15afb9eb080c089053be99881dd18aa1090");

  private static final String RECEIVER_ADDRESS = "0xd95c64c6eee9164539d679354f349779a04f57cb";
  private static final BigInteger MAX_DEPOSIT = BigInteger.valueOf(1000);

  @Test public void createChannel() throws TransactionFailedException, DepositTooHighException {
    Logger logger = Logger.getLogger(MicroRaidenImplTest.class.getSimpleName());
    TransactionSender transactionSender = Mockito.mock(TransactionSender.class);
    GetChannelBlock getChannelBlock = Mockito.mock(GetChannelBlock.class);

    Mockito.when(transactionSender.send(any(), any(), any(), any()))
        .thenReturn("0xa04391a989f95c09cb3d553b42341fab3f38d4b7d9eed8585a646c44d8f2f54d");

    Mockito.when(getChannelBlock.get(
        HexStr.from("0xa04391a989f95c09cb3d553b42341fab3f38d4b7d9eed8585a646c44d8f2f54d")))
        .thenReturn(BigInteger.ONE);

    MicroRaidenImpl microRaiden =
        new MicroRaidenImpl(CHANNEL_MANAGER_ADRESS, TOKEN_ADRESS, logger, MAX_DEPOSIT,
            transactionSender, getChannelBlock);

    microRaiden.createChannel(ECKey.fromPrivate(
        new BigInteger("c7b2735a7aed53b3668f0dc7f1d67833c0adf526cfcdb50f63d8bfe7bfc8c9b6", 16)),
        Address.from(RECEIVER_ADDRESS), BigInteger.TEN);

    verify(transactionSender, times(2)).send(any(), any(), any(), any());
  }
}