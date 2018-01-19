package cm.aptoide.pt.microraidenj;

import cm.aptoide.pt.ethereum.EthereumApi;
import cm.aptoide.pt.ethereum.ethereumj.core.CallTransaction.Function;
import cm.aptoide.pt.ethereum.ethereumj.crypto.ECKey;
import cm.aptoide.pt.ethereum.ws.etherscan.TransactionResultResponse;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.utils.Convert.Unit;

/**
 * Created by neuro on 18-01-2018.
 */

public class TokenContract {

  private static final long GAS_PRICE = 50 * Unit.GWEI.getWeiFactor()
      .longValue();
  private static final long GAS_LIMIT = 200_000;

  private static final String TAG = TokenContract.class.getSimpleName();
  private static final Function transferFunction =
      Function.fromSignature("transfer", "address", "uint256", "bytes");

  private final Address tokenAddress;
  private final Address channelManagerContractAddress;
  private final EthereumApi ethereumApi;

  public TokenContract(Address tokenAddress, Address channelManagerContractAddress,
      EthereumApi ethereumApi) {
    this.tokenAddress = tokenAddress;
    this.channelManagerContractAddress = channelManagerContractAddress;
    this.ethereumApi = ethereumApi;
  }

  public boolean transfer(ECKey ecKey, Address receiver, Uint256 value) {
    byte[] sender = ecKey.getAddress();
    byte[] receiverBytes = Hex.decode(receiver.getValue()
        .substring(2, receiver.getValue()
            .length()));

    return transfer(ecKey, receiver, value, buildTransferDataField(sender, receiverBytes));
  }

  private boolean transfer(ECKey ecKey, Address receiver, Uint256 value, byte[] data) {
    transferFunction.encode(receiver.getValue(), value.getValue(), data);

    TransactionResultResponse response = ethereumApi.getCurrentNonce(Hex.toHexString(ecKey.getAddress()))
        .flatMap(nonce -> ethereumApi.call(nonce, ecKey, GAS_PRICE, GAS_LIMIT, tokenAddress,
            transferFunction.encode(channelManagerContractAddress.getValue(), value.getValue(), data)))
        .toBlocking()
        .first();

    System.out.println(TAG + " transfer returned: " + response);

    return response != null && response.result != null;
  }

  public boolean topUp(ECKey ecKey, Address receiver, Uint256 value, int blockNumber) {
    byte[] sender = ecKey.getAddress();
    byte[] receiverBytes = Hex.decode(receiver.getValue()
        .substring(2, receiver.getValue()
            .length()));

    return transfer(ecKey, receiver, value,
        buildTopUpDataField(sender, receiverBytes, blockNumber));
  }

  private byte[] buildTransferDataField(byte[] sender, byte[] receiver) {
    byte[] result = new byte[sender.length + receiver.length];
    System.arraycopy(sender, 0, result, 0, sender.length);
    System.arraycopy(receiver, 0, result, sender.length, receiver.length);
    return result;
  }

  private byte[] buildTopUpDataField(byte[] sender, byte[] receiver, int blockNumber) {
    byte[] data = buildTransferDataField(sender, receiver);
    int blockNumberHeaderSize = 4;
    byte[] result = new byte[data.length + blockNumberHeaderSize];

    System.arraycopy(data, 0, result, 0, data.length);
    System.arraycopy(buildBlockNumberBytes(blockNumber), 0, result, data.length,
        blockNumberHeaderSize);

    return result;
  }

  private byte[] buildBlockNumberBytes(int blockNumber) {
    byte[] result = new byte[4];

    byte[] decodedNumber = Hex.decode(Integer.toHexString(blockNumber));
    int length = decodedNumber.length;
    System.arraycopy(decodedNumber, 0, result, 4 - length, length);

    return result;
  }
}

