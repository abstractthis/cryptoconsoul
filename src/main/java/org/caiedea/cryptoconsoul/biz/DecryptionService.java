package org.caiedea.cryptoconsoul.biz;

import java.io.File;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.decrypt.Decryptor;
import org.caiedea.cryptopression.decrypt.FilePassThroughDecryptor;
import org.caiedea.cryptopression.decrypt.aes.FileAesDecryptor;
import org.caiedea.cryptopression.decrypt.decompress.DecompressFileDecryptor;

public class DecryptionService {
	private static final String CRYPTO_LIB_INPUT_PROP_KEY = "cryptopression.keepRawInput";
	
	public void decryptFile(String targetPath, String resultPath, boolean decompress, boolean rmInput) {
		try {
			// If the input should be deleted set it so in the cryptopression lib
			Utils.propertyOverride(CRYPTO_LIB_INPUT_PROP_KEY, "0");
			if (decompress) {
				this.decryptFileWithDecompression(targetPath, resultPath);
			}
			else {
				this.decryptFileNoDecompression(targetPath, resultPath);
			}
		}
		finally {
			// Revert the change we made back to the default setting
			Utils.propertyOverride(CRYPTO_LIB_INPUT_PROP_KEY, "1");
		}
	}
	
	private void decryptFileWithDecompression(String targetPath, String resultPath) {
		Decryptor<File> noAlgoDecryptor = new FilePassThroughDecryptor(targetPath);
		Decryptor<File> fileDecryptor = new FileAesDecryptor(noAlgoDecryptor, resultPath + ".dec");
		Decryptor<File> decompressionDecryptor =
				new DecompressFileDecryptor(fileDecryptor, resultPath);
		decompressionDecryptor.decrypt();
	}
	
	private void decryptFileNoDecompression(String targetPath, String resultPath) {
		Decryptor<File> noAlgoDecryptor = new FilePassThroughDecryptor(targetPath);
		Decryptor<File> fileDecryptor = new FileAesDecryptor(noAlgoDecryptor, resultPath);
		fileDecryptor.decrypt();
	}
	
}
