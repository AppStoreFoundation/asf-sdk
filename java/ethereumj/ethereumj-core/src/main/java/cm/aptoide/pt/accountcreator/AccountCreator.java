package cm.aptoide.pt.accountcreator;

import org.ethereum.crypto.ECKey;
import org.spongycastle.util.encoders.Hex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class AccountCreator {

	private static final String FILE_NAME = "generatedAccounts";
	private static String fundAccountScriptName = "fundAccount.sh";
	private static String fundAppCoinAccountScriptName = "fundAppCoinAccount.sh";
	private String sourceAddr;
	private String contractAddr;

	public static void main(String[] args) {

		if (args.length != 3) {
			throw new IllegalArgumentException(
							"Wrong number os arguments! Expected 3, received " + args.length);
		}

			new AccountCreator().run(args);
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

		pb = new ProcessBuilder((pwd.concat("/" + fundAccountScriptName)), sourceAddr, destAddr,
						amount);
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
		contractAddr = args[1];
	}

	private void run(String[] args) {
		loadArguments(args);
		Account ethereumAccount = createEthereumAccount();
		System.out.println("Account created!");
		System.out.println(ethereumAccount);
		saveAccount(ethereumAccount);
		transferEthereum(sourceAddr, ethereumAccount.getAddress(), "100000000000000000");
		transferAppcoins(contractAddr, ethereumAccount.getAddress());
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

	private int transferAppcoins(String contractAddr, String destAddr) {
		ProcessBuilder pb;
		int exitValue;

		String pwd = System.getProperty("user.dir");

		pb = new ProcessBuilder((pwd.concat("/" + fundAppCoinAccountScriptName)), contractAddr,
						destAddr);
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
}
