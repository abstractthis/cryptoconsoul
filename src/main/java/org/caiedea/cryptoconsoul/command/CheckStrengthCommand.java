package org.caiedea.cryptoconsoul.command;

import org.caiedea.cryptoconsoul.biz.StrengthChecker;
import org.caiedea.cryptoconsoul.util.Util;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;

public class CheckStrengthCommand implements Command {
	private static final String MAN_PAGE_PATH = "man/checkStrength";

	@Override
	public String getManual() {
		return Util.getManPageFromFile(MAN_PAGE_PATH);
	}

	@Override
	public String getUsage() {
		return "checkStrength";
	}

	@Override
	public void perform(ConsoleCommand cmd) throws CommandPerformException {
		StrengthChecker checker = new StrengthChecker();
		ConsoleOutPipe out = cmd.getCommandOutputPipe();
		try {
			boolean isUnlimited = checker.isUnlimitedStrength();
			if (isUnlimited) {
				out.sendAndFlush("System has policy files installed that allow UNLIMITED strength.");
			}
			else {
				out.sendAndFlush("Upgrade your systems policy files. Unlimited strength not available.");
			}
		}
		catch(RuntimeException re) {
			out.sendAndFlush("Problems!! JCE not available or dated.");
		}
	}

	@Override
	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 0;
	}

}
