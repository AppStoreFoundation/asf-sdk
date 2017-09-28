package cm.aptoide.pt.accountcreator;

public class Account {

	private final String publicKey;
	private final String privateKey;
	private final String address;

	public Account(String publicKey, String privateKey, String address) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.address = address;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public String getAddress() {
		return address;
	}

	@Override
	public String toString() {
		return "Account{" + "publicKey='" + publicKey + '\'' + ", privateKey='" + privateKey + '\'' +
						", address='" + address + '\'' + '}';
	}
}
