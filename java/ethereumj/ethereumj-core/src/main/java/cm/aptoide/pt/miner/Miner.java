/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package cm.aptoide.pt.miner;

import com.typesafe.config.ConfigFactory;

import org.ethereum.config.SystemProperties;
import org.ethereum.facade.EthereumFactory;
import org.ethereum.mine.Ethash;
import org.ethereum.samples.BasicSample;
import org.ethereum.solidity.compiler.SolidityCompiler;
import org.springframework.context.annotation.Bean;

import cm.aptoide.pt.AptoideAccounts;
import cm.aptoide.pt.TransactionManager;

/**
 * The sample creates a small private net with two peers: one is the miner, another is a regular
 * peer which is directly connected to the miner peer and starts submitting transactions which are
 * then included to blocks by the miner.
 * <p>
 * Another concept demonstrated by this sample is the ability to run two independently configured
 * EthereumJ peers in a single JVM. For this two Spring ApplicationContext's are created which are
 * mostly differed by the configuration supplied
 * <p>
 * Created by Anton Nashatyrev on 05.02.2016.
 */
public class Miner {

	/**
	 * Creating two EthereumJ instances with different config classes
	 */
	public static void main(String[] args) throws Exception {
		if (Runtime.getRuntime()
						.maxMemory() < (1250L << 20)) {
			MinerNode.sLogger.error("Not enough JVM heap (" + (Runtime.getRuntime()
							.maxMemory() >> 20) + "Mb) to generate DAG for mining (DAG requires min 1G). For " +
							"this sample it is recommended to set -Xmx2G JVM option");
			return;
		}

		BasicSample.sLogger.info("Starting EthtereumJ miner instance!");
		EthereumFactory.createEthereum(MinerConfig.class);
	}

	/**
	 * Spring configuration class for the Miner peer
	 */
	private static class MinerConfig {

		private final String config =
						// no need for discovery in that small network
						"peer.discovery.enabled = false \n" + "peer.listen.port = 30335 \n" +
										// need to have different nodeId's for the peers
										"peer.privateKey = " +
										"6ef8da380c27cea8fdf7448340ea99e8e2268fc2950d79ed47cbf6f85dc977ec \n" +
										// our private net ID
										"peer.networkId = 555 \n" +
										// we have no peers to sync with
										"sync.enabled = true \n" +
										// genesis with a lower initial difficulty and some predefined known funded
										// accounts
										"genesis = sample-genesis.json \n" +
										// two peers need to have separate database dirs
										"database.dir = sampleDB-1 \n" +
										// when more than 1 miner exist on the network extraData helps to identify the
										// block creator
										"mine.extraDataHex = cccccccccccccccccccc \n" + "mine.cpuMineThreads = 2 \n" +
										"cache.flush.blocks = 1";

		@Bean
		public MinerNode node() {
			return new MinerNode();
		}

		/**
		 * Instead of supplying properties via config file for the peer we are substituting the
		 * corresponding bean which returns required config for this instance.
		 */
		@Bean
		public SystemProperties systemProperties() {
			SystemProperties props = new SystemProperties();
			props.overrideParams(ConfigFactory.parseString(config.replaceAll("'", "\"")));
			return props;
		}
	}

	/**
	 * Miner bean, which just start a miner upon creation and prints miner events
	 */
	static class MinerNode extends BasicSample {

		public MinerNode() {
			// peers need different loggers
			super("sampleMiner");
		}

		// overriding run() method since we don't need to wait for any discovery,
		// networking or sync events
		@Override
		public void run() {
			if (config.isMineFullDataset()) {
				logger.info("Generating Full Dataset (may take up to 10 min if not cached)...");
				// calling this just for indication of the dataset generation
				// basically this is not required
				Ethash ethash = Ethash.getForBlock(config, ethereum.getBlockchain()
								.getBestBlock()
								.getNumber());
				ethash.getFullDataset();
				logger.info("Full dataset generated (loaded).");
			}
			ethereum.getBlockMiner()
							.addListener(new GenerateTransactionOnBlockMinedListener(logger, ethereum,
											AptoideAccounts.MAIN_MINER, AptoideAccounts.MAIN_REGULAR));
			ethereum.getBlockMiner()
							.addListener(
											new DeployContractListener(logger, ethereum, SolidityCompiler.getInstance(),
															new TransactionManager(ethereum, logger),
															AptoideAccounts.MAIN_MINER.getPrivateKey()));
			ethereum.getBlockMiner()
							.startMining();
		}
	}
}
