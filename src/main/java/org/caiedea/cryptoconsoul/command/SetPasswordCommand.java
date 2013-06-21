package org.caiedea.cryptoconsoul.command;

import java.util.Scanner;

import org.caiedea.cryptoconsoul.biz.CryptoPasswordSetter;
import org.caiedea.cryptoconsoul.util.ManualUtils;

import com.abstractthis.consoul.ApplicationContext;
import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.CommandCreds;
import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.command.ContextAwareCommand;
import com.abstractthis.consoul.commands.command.SecureCommand;
import com.abstractthis.consoul.commands.exception.CommandPerformException;
import com.abstractthis.consoul.widgets.BusyWidget;

public class SetPasswordCommand implements SecureCommand, ContextAwareCommand {
	private static final String MAN_PAGE_PATH = "man/setPwd";
	private static final String PROMPT_FOR_USERNAME = "User:";
	private static final String PROMPT_FOR_PASSWORD = "Password:";
	private static final String NEW_LINE = System.getProperty("line.separator");
	
	private ApplicationContext context;

	@Override
	public String getManual() {
		return ManualUtils.getManPageFromFile(MAN_PAGE_PATH);
	}

	@Override
	public String getUsage() {
		return "setPassword [password]";
	}

	@Override
	public void perform(ConsoleCommand command) throws CommandPerformException {
		boolean stillSpinning = false;
		BusyWidget spinner = null;
		try {
			ConsoleOutPipe out = command.getCommandOutputPipe();
			String[] cmdArgs = command.getCommandArguments();
			spinner = new BusyWidget();
			out.useNewline(false);
			out.sendAndFlush("Setting Crypto Password...");
			stillSpinning = true;
			out.displayWidget(spinner);
			CryptoPasswordSetter pwdSetter = new CryptoPasswordSetter();
			pwdSetter.setIt(cmdArgs[0]);
			spinner.stop();
			out.useNewline(true);
			out.sendAndFlush("set.");
		}
		finally {
			if( stillSpinning ) {
				spinner.stop();
			}
		}
	}

	@Override
	public boolean verifyArguments(String[] cmdArgs) {
		return cmdArgs.length == 1;
	}

	@Override
	public CommandCreds promptForCredentials(ConsoleOutPipe out) {
		out.useNewline(false);
		out.sendAndFlush(PROMPT_FOR_USERNAME);
		Scanner inputScan = new Scanner(System.in);
		inputScan.useDelimiter("");
		String username = this.captureInput(inputScan);
		out.sendAndFlush(PROMPT_FOR_PASSWORD);
		String password = this.captureInput(inputScan);
		out.useNewline(true);
		return new CommandCreds(username, password);
	}
	
	/*
	 * Helper method that reads in each character as it's typed and then erases
	 * it by omitting a backspace if <code>maskIt</code> is specified as true.
	 * Once the systems newline character is detected the input is returned.
	 * @param inputScan
	 * @return the input the user provided
	 */
	private String captureInput(Scanner inputScan) {
		StringBuilder input = new StringBuilder(16);
		while( inputScan.hasNext() ) {
			String s = inputScan.next();
			if( s.equals(NEW_LINE) ) {
				return input.toString();
			}
			input.append(s);
		}
		return "";
	}

	@Override
	public boolean verifyCredentials(CommandCreds creds) {
		return context.isSudoer(creds);
	}
	
	@Override
	public ApplicationContext getContext() {
		return this.context;
	}
	
	@Override
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

}
