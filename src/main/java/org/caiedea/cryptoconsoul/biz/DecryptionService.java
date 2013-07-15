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
	public String decryptFile(String targetPath, String resultPath, boolean decompress,
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
			File targetFile = new File(targetPath);
			// Determine if the target is a directory and if so
			// loop over all the files contained in the directory
			// and decrypt them. This means that all the files contained
			// in the directory should be encrypted files. Subdirectories
			// will not be traversed.
			if (targetFile.isDirectory()) {
				File[] encFiles = targetFile.listFiles();
				for(File encFile : encFiles) {
					if (encFile.isFile()) {
						String outName = this.getOutputName(targetPath, encFile.getName());
						String outPath = resultPath != null && !"".equals(resultPath) ?
								outName + "." + resultPath : outName;
						this.processDirFile(encFile, outPath, decompress);
					}
				}
				return targetPath + "/dec/*";
			}
			else {
				this.processFile(targetPath, resultPath, decompress);
				return resultPath;
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
	
	private String getOutputName(String rootDir, String inputName) {
		File decDir = new File(rootDir + "/dec");
		if (!decDir.exists()) {
			decDir.mkdir();
		}
		int extIndex = inputName.lastIndexOf(".");
		String outName = extIndex == -1 ? inputName : inputName.substring(0, extIndex);
		return decDir.getAbsolutePath() + "/" + outName;
	}
	
	private void processDirFile(File targetFile, String resultPath, boolean decompress) {
		this.processFile(targetFile.getAbsolutePath(), resultPath, decompress);
	}
	
	private void processFile(String targetPath, String resultPath, boolean decompress) {
		if (decompress) {
			this.decryptFileWithDecompression(targetPath, resultPath);
		}
		else {
			this.decryptFileNoDecompression(targetPath, resultPath);
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
