package org.caiedea.cryptoconsoul.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ManualUtils {
	private static final Logger log = LoggerFactory.getLogger(ManualUtils.class);
	private static final String LINE_ENDING = System.getProperty("line.separator");

	public static String getManPageFromFile(String pathToPage) {
		InputStream manStream =
				ClassLoader.getSystemResourceAsStream(pathToPage);
		BufferedReader br = new BufferedReader(new InputStreamReader(manStream));
		StringBuilder manPage = new StringBuilder(2048);
		String line = null;
		try {
			while ((line = br.readLine()) != null) {
				manPage.append(line).append(LINE_ENDING);
			}
		}
		catch(IOException ioe) {
			String msg = "Couldn't output man page: " + pathToPage;
			log.error(msg);
			return msg;
		}
		finally {
			try {
				if (manStream != null) manStream.close();
				if (br != null) br.close();
			}
			catch(IOException ioe) {
				log.warn("Failed to cleanup writing man page.");
			}
		}
		return manPage.toString();
	}
}
