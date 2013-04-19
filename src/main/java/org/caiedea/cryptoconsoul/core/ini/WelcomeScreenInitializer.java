package org.caiedea.cryptoconsoul.core.ini;

import org.caiedea.cryptoconsoul.widget.WelcomeScreenWidget;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.ini.InitializerListener;

public class WelcomeScreenInitializer implements InitializerListener {

	@Override
	public void deinitialize(ConsoleOutPipe outPipe) {
		// NOP - Nothing to see here, move along
	}

	@Override
	public void initialize(ConsoleOutPipe outPipe) {
		outPipe.displayWidget(new WelcomeScreenWidget());
	}

}
