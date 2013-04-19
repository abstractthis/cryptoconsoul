package org.caiedea.cryptoconsoul.command;

import org.caiedea.cryptoconsoul.util.Util;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;

public class PwdCommand implements Command {
	private static final String MAN_PAGE_PATH = "man/pwd";

	@Override
	public String getManual() {
		return Util.getManPageFromFile(MAN_PAGE_PATH);
	}

	@Override
	public String getUsage() {
		return "pwd";
	}

	@Override
	public void perform(ConsoleCommand cmd) throws CommandPerformException {
		ConsoleOutPipe out = cmd.getCommandOutputPipe();
		out.sendAndFlush(System.getProperty("user.dir"));
	}

	@Override
	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 0;
	}

}
