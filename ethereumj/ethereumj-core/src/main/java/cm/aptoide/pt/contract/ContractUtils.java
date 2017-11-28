package cm.aptoide.pt.contract;

import org.ethereum.solidity.compiler.CompilationResult;
import org.ethereum.solidity.compiler.SolidityCompiler;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class ContractUtils {

	private final Logger logger;
	private final SolidityCompiler solidityCompiler;

	public ContractUtils(Logger logger, SolidityCompiler solidityCompiler) {
		this.logger = logger;
		this.solidityCompiler = solidityCompiler;
	}

	public CompilationResult.ContractMetadata compileContract(String contractPath) throws
					IOException {

		logger.info("Compiling contract...");

		SolidityCompiler.Result result = solidityCompiler.compileSrc(new File(contractPath), true,
						true,
						SolidityCompiler.Options.ABI, SolidityCompiler.Options.BIN);
		if (result.isFailed()) {
			throw new RuntimeException("Contract compilation failed:\n" + result.errors);
		}
		System.out.println(new File(contractPath).getAbsolutePath());
		CompilationResult res = CompilationResult.parse(result.output);
		if (res.contracts.isEmpty()) {
			throw new RuntimeException("Compilation failed, no contracts returned:\n" + result.errors);
		}
		Iterator<CompilationResult.ContractMetadata> iterator = res.contracts.values()
						.iterator();
		CompilationResult.ContractMetadata metadata = iterator.next();
		metadata = iterator.next();
		if (metadata.bin == null || metadata.bin.isEmpty()) {
			throw new RuntimeException("Compilation failed, no binary returned:\n" + result.errors);
		}

		return metadata;
	}
}
