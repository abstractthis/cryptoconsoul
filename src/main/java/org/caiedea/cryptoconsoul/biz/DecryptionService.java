package org.caiedea.cryptoconsoul.biz;

import java.io.File;

import org.caiedea.cryptopression.Utils;
import org.caiedea.cryptopression.decrypt.Decryptor;
import org.caiedea.cryptopression.decrypt.FilePassThroughDecryptor;
import org.caiedea.cryptopression.decrypt.aes.FileAesDecryptor;
import org.caiedea.cryptopression.decrypt.decompress.DecompressFileDecryptor;

import com.abstractthis.consoul.ConsoleProperties;

/**
 * Handles the interaction with the cryptopression library to perform
 * the decryption.
 * 
 * @author dsmith
 *
 */
public class DecryptionService {
	private static final String CRYPTO_LIB_INPUT_PROP_KEY = "cryptopression.keepRawInput";
	private static final String CRYPTO_LIB_PWD_KEY = "cryptopression.password";
	
	/**
	 * Decrypts the <code>File</code> located at <code>targetPath</code> and places
	 * the result in a <code>File</code> located at <code>resultPath</code>. If
	 * <code>decompress</code> is <code>true</code> the <code>File</code> located at
	 * <code>targetPath</code> will be inflated after it's decrypted. If
	 * <code>rmInput</code> is <code>true</code> the encrypted file will be
	 * deleted after successful decryption.
	 * @param targetPath
	 * @param resultPath
	 * @param decompress
	 * @param rmInput
	 * @param override
	 */
	public void decryptFile(String targetPath, String resultPath, boolean decompress,
			boolean rmInput, String override) {
		ConsoleProperties conProps = ConsoleProperties.getConsoleProperties();
		try {
			if (override != null) {
				String overrideValue = conProps.getProperty(override + ".override");
				if (overrideValue == null) {
					throw new RuntimeException("Override provided not found. Override: " + override);
				}
				Utils.propertyOverride(CRYPTO_LIB_PWD_KEY, overrideValue);
			}
			// If the input should be deleted set it so in the cryptopression lib
			if (rmInput) {
				Utils.propertyOverride(CRYPTO_LIB_INPUT_PROP_KEY, "0");
			}
			if (decompress) {
				this.decryptFileWithDecompression(targetPath, resultPath);
			}
			else {
				this.decryptFileNoDecompression(targetPath, resultPath);
			}
		}
		finally {
			// Revert the change we made back to the default setting
			if (rmInput) {
				Utils.propertyOverride(CRYPTO_LIB_INPUT_PROP_KEY, "1");
			}
			if (override != null) {
				Utils.propertyOverride(CRYPTO_LIB_PWD_KEY, conProps.getProperty("general.override"));
			}
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
