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

    byte[] data = buildTransferDataField(sender, receiverBytes);

    transferFunction.encode(receiver.getValue(), value.getValue(), data);

    TransactionResultResponse response =
        ethereumApi.getCurrentNonce(Hex.toHexString(ecKey.getAddress()))
            .flatMap(nonce -> ethereumApi.call(nonce, ecKey, GAS_PRICE, GAS_LIMIT, tokenAddress,
                transferFunction.encode(channelManagerContractAddress.getValue(), value.getValue(),
                    data)))
            .toBlocking()
            .first();

    System.out.println(TAG + " transfer returned: " + response);

    return response != null && response.result != null;
  }

  private byte[] buildTransferDataField(byte[] sender, byte[] receiver) {
    byte[] result = new byte[sender.length + receiver.length];
    System.arraycopy(sender, 0, result, 0, sender.length);
    System.arraycopy(receiver, 0, result, sender.length, receiver.length);
    return result;
  }
}

