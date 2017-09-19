package cm.aptoide.pt;

import org.spongycastle.util.encoders.Hex;

public class AptoideAccounts {

	public static final Account REGULAR_TRANSACTIONS;
	public static final Account CLIENT_1;
	public static final Account CLIENT_2;

	public static final Account MAIN_MINER;
	public static final Account MAIN_REGULAR;
	public static final Account SLAVE;

	static {
		REGULAR_TRANSACTIONS = new Account("382d4444d459488c40b9764ea39ab284006aec18",
						"4a27c4ce624e2e5c71e2af4371b01b5972ed3742c53c985d51952431955d244c",
						"04e9496651c07f9f9dd76e6c8ccbf34eaf1b06e66c940003e15ddf028f75a923a51ca0bd6bfff14f915712a6d6dc4e4210a63fb5373e373a0e3e748f6ebd341322");
		CLIENT_1 = new Account("72f41780d3161f73369b317805aa386cef2ea11b",
						"b9e8feca33e0beb3cf21cac6b89fa7c609f3ee9aa517299a5efc9e482a962daf",
						"040e31219c4cf22360dac3ec9b4d7f054e6eb1968b33d7e827960aecabd1738974b8b8f27bdebd49450b5920f20bb45425a3148d2f8d5315e2190db8243eda1f40");
		CLIENT_2 = new Account("6663e3daee4114e29559852d000356daeca53008",
						"903aa7f4c4b95ef04ad9a087346b418688c014f0752d0ff54dc812c4e25f95e5",
						"041cd89a2c0da299ecb2e0b88cca020401922982b706385c04bc017cf7b4dd6af1d37be762b76e28e74011535582415c0c8425fdca0bf5711634e8e53eab4657bb");

		MAIN_MINER = new Account("5c146fed4bd01a366760063f9236d6b671164535",
						"17414acfebbe32d7e16d5757292eb4233370819fa8ee5567dcb355daac51179b",
						"045725ad801d80316c5ca2deb452ce33ba180e707f51682241af0ccb12fa6297527367fd805311ef4c2edfa7c3807a6727ca8ef341e38bd1ab44b42a7b4086eab0");
		MAIN_REGULAR = new Account("f26dd44f020131b8be3ca71cad920c267ac334d4",
						"f784cd39264b35f3e18d055fe5ec6beab1945369458a10b8d420f2d06159e674",
						"041eb53f82e1f10fe77ecb2087d118fdb5daa4a7db12b20f4816d5e97dabbb8110b4a4a81aa1822067eb6f0b94570f3b27694aff7b440094ddb16283a7ddd14865");
		SLAVE = new Account("ad87708f2915b8c123b11bd21922cd323316933d",
						"b672c1491287e81592efb4153c5499954b7701e47d548b1418727fa73c04c52d",
						"04cac8b5f105a3f452cd753aed4f2902d1f3754f2638de29faff4a43c920e3daea3f4c7320cf41f4fe60311b7d549590562d10a404d5795f736385cac560d28bc5");
	}

	public static class Account {

		private final byte[] address;
		private final byte[] privateKey;
		private final byte[] publicKey;

		public Account(byte[] address, byte[] privateKey, byte[] publicKey) {
			this.address = address;
			this.privateKey = privateKey;
			this.publicKey = publicKey;
		}

		public Account(String address, String privateKey, String publicKey) {
			this(Hex.decode(address), Hex.decode(privateKey), Hex.decode(publicKey));
		}

		public byte[] getAddress() {
			return address;
		}

		public byte[] getPrivateKey() {
			return privateKey;
		}

		public byte[] getPublicKey() {
			return publicKey;
		}
	}
}
