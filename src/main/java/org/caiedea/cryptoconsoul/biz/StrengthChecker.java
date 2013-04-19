package org.caiedea.cryptoconsoul.biz;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrengthChecker {
	private static final Logger log = LoggerFactory.getLogger(StrengthChecker.class);

	public boolean isUnlimitedStrength() {
		boolean unlimitedStrength = true;
		try {
			int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");
			if (maxKeyLen <= 128) {
				unlimitedStrength = false;
			}
		}
		catch(NoSuchAlgorithmException algoEx) {
			log.error("Java Cryptography Extension version is dated or not available!!!");
			throw new RuntimeException(algoEx);
		}
		return unlimitedStrength;
	}
}
