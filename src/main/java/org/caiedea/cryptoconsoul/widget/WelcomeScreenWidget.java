package org.caiedea.cryptoconsoul.widget;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abstractthis.consoul.widgets.Widget;

public class WelcomeScreenWidget implements Widget {
	private final static Logger log = LoggerFactory.getLogger(WelcomeScreenWidget.class);
	private final static String WELCOME_SCREEN_PATH = "/iedea-crypto-banner";
	
	private InputStream fileStream =
			WelcomeScreenWidget.class.getResourceAsStream(WELCOME_SCREEN_PATH);

	@Override
	public void render(PrintStream stream) {
		try {
			byte[] buffer = new byte[2048];
			int bytesRead = 0;
			while ((bytesRead = fileStream.read(buffer)) != -1) {
				stream.write(buffer, 0, bytesRead);
			}
		}
		catch(IOException ioe) {
			log.error("Problems writing out welcome banner! Location:" + WELCOME_SCREEN_PATH);
		}
		finally {
			try {
				if (fileStream != null) {
					fileStream.close();
				}
			}
			catch(IOException ioe) { /* NOP */ }
		}
	}

}
