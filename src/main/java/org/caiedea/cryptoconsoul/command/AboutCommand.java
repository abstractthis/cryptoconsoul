package org.caiedea.cryptoconsoul.command;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.ConsoleProperties;
import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;
import com.abstractthis.consoul.widgets.AboutWidget;

public class AboutCommand implements Command {

	@Override
	public String getManual() {
		StringBuilder manPage = new StringBuilder(128);
		manPage.append("Displays the application name and version information.\n")
		       .append('\n')
		       .append("Parameters:\n")
		       .append("No parameters accepted for this command.");
		return manPage.toString();
	}

	@Override
	public String getUsage() {
		return "about";
	}

	@Override
	public void perform(ConsoleCommand command) throws CommandPerformException {
		ConsoleProperties props = ConsoleProperties.getConsoleProperties();
		ConsoleOutPipe outPipe = command.getCommandOutputPipe();
		outPipe.displayWidget(new AboutWidget(props));
	}

	@Override
	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 0;
	}

}
