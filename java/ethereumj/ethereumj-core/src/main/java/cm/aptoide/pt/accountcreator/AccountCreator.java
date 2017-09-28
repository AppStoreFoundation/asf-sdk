package cm.aptoide.pt.accountcreator;

import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AccountCreator {

	private static final String FILE_NAME = "generatedAccounts";
	private static String scriptName = "fundAccount.sh";
	private String sourceAddr;
	private String amount;

	public static void main(String[] args) {

		if (args.length != 2) {
			throw new IllegalArgumentException(
							"Wrong number os arguments! Expected 2, received " + args.length);
		}

		for (int i = 0; i < 3; i++) {
			new AccountCreator().run(args);
		}
	}

	public Account createEthereumAccount() {
		ECKey ecKey = new ECKey();

		byte[] publicKey = ecKey.getPubKey();
		byte[] privateKey = ecKey.getPrivKeyBytes();
		byte[] address = ecKey.getAddress();

		return new Account(Hex.toHexString(publicKey), Hex.toHexString(privateKey),
						Hex.toHexString(address));
	}

	public int transferEthereum(String sourceAddr, String destAddr, String amount) {
		ProcessBuilder pb;
		int exitValue;

		String pwd = System.getProperty("user.dir");

		pb = new ProcessBuilder((pwd.concat("/" + scriptName)), sourceAddr, destAddr, amount);
		try {
			Process p = pb.start();
			int strm = 0;
			while (strm != -1) {
				System.out.print(((char) (strm = p.getInputStream()
								.read())));
			}

			exitValue = -1;
			while (exitValue == -1) {
				try {
					exitValue = p.exitValue();
				} catch (Exception _e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			exitValue = -1;
		}
		return exitValue;
	}

	private void loadArguments(String[] args) {
		sourceAddr = args[0];
		amount = args[1];
	}

	private void run(String[] args) {
		loadArguments(args);
		Account ethereumAccount = createEthereumAccount();
		System.out.println("Account created!");
		System.out.println(ethereumAccount);
		saveAccount(ethereumAccount);
		transferEthereum(sourceAddr, ethereumAccount.getAddress(), amount);
	}

	private void saveAccount(Account account) {
		BufferedWriter writer = null;
		try {
			String fileName = FILE_NAME;
			File file = new File(fileName);

			System.out.println(file.getCanonicalPath());

			writer = new BufferedWriter(new FileWriter(file, true));

			writer.write(
							account.getAddress() + "," + account.getPrivateKey() + "," + account.getPublicKey()
											+ "\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
			}
		}
	}
}
