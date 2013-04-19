package org.caiedea.cryptoconsoul.biz;

import java.io.File;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.encrypt.Encryptor;
import org.caiedea.cryptopression.encrypt.FilePassThroughEncryptor;
import org.caiedea.cryptopression.encrypt.aes.FileAesEncryptor;
import org.caiedea.cryptopression.encrypt.compress.CompressFileEncryptor;

public class EncryptionService {
	private static final String CRYPTO_LIB_INPUT_PROP_KEY = "cryptopression.keepRawInput";

	public void encryptFile(String targetPath, String resultPath, boolean compress, boolean rmInput) {
		try {
			// If the input should be deleted set it so in the cryptopression lib
			Utils.propertyOverride(CRYPTO_LIB_INPUT_PROP_KEY, "0");
			if (compress) {
				this.encryptFileWithCompression(targetPath, resultPath);
			}
			else {
				this.encryptFileNoCompression(targetPath, resultPath);
			}
		}
		finally {
			// Revert the change we made back to the default setting
			Utils.propertyOverride(CRYPTO_LIB_INPUT_PROP_KEY, "1");
		}
	}
	
	private void encryptFileWithCompression(String targetPath, String resultPath) {
		Encryptor<File> noAlgoEncryptor = new FilePassThroughEncryptor(targetPath);
		Encryptor<File> compressEncryptor = new CompressFileEncryptor(noAlgoEncryptor);
		Encryptor<File> fileEncryptor = new FileAesEncryptor(compressEncryptor, resultPath);
		fileEncryptor.encrypt();
	}
	
	private void encryptFileNoCompression(String targetPath, String resultPath) {
		Encryptor<File> noAlgoEncryptor = new FilePassThroughEncryptor(targetPath);
		Encryptor<File> fileEncryptor = new FileAesEncryptor(noAlgoEncryptor, resultPath);
		fileEncryptor.encrypt();
	}
	
}
