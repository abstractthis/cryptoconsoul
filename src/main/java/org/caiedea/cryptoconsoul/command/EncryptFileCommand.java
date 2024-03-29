package org.caiedea.cryptoconsoul.command;

import java.util.HashMap;
import java.util.Map;

import org.caiedea.cryptoconsoul.biz.EncryptionService;
import org.caiedea.cryptoconsoul.util.ManualUtils;

import com.abstractthis.consoul.ConsoleOutPipe;
import com.abstractthis.consoul.commands.ConsoleCommand;
import com.abstractthis.consoul.commands.command.Command;
import com.abstractthis.consoul.commands.exception.CommandPerformException;
import com.abstractthis.consoul.widgets.BusyWidget;

public class EncryptFileCommand implements Command {
	private static final String MAN_PAGE_PATH = "man/encryptFile";
	private static final String IN_FILE_ARG = "-infile";
	private static final String OUT_FILE_ARG = "-outfile";
	private static final String COMPRESS_ARG = "-compress";
	private static final String REMOVE_INPUT_ARG = "-rmin";
	private static final int MAX_ARG_COUNT = 4;
	private static final int MIN_ARG_COUNT = 2;
	
	// Map of the argument values
	private Map<String,String> argMap = new HashMap<String,String>(MAX_ARG_COUNT);
	
	public EncryptFileCommand() {
		argMap.put(IN_FILE_ARG, null);
		argMap.put(OUT_FILE_ARG, null);
		argMap.put(COMPRESS_ARG, "true");
		argMap.put(REMOVE_INPUT_ARG, "false");
	}

	@Override
	public String getManual() {
		return ManualUtils.getManPageFromFile(MAN_PAGE_PATH);
	}

	@Override
	public String getUsage() {
		return "encryptFile [-infile=<filename>] [-outfile=<filename>] [-compress=true|false|yes|no] [-rmin=true|false|yes|no]";
	}

	@Override
	public void perform(ConsoleCommand cmd) throws CommandPerformException {
		String targetFilePath = argMap.get(IN_FILE_ARG);
		String resultFilePath = argMap.get(OUT_FILE_ARG);
		boolean compressTargetFirst = Boolean.valueOf(argMap.get(COMPRESS_ARG));
		boolean removeInput = Boolean.valueOf(argMap.get(REMOVE_INPUT_ARG));
		ConsoleOutPipe out = cmd.getCommandOutputPipe();
		BusyWidget spinner = new BusyWidget();
		try {
			out.displayWidget(spinner);
			EncryptionService service = new EncryptionService();
			service.encryptFile(targetFilePath, resultFilePath, compressTargetFirst, removeInput);
			spinner.stop();
			out.sendAndFlush("Encryption complete. File Location: " + resultFilePath);
		}
		catch(RuntimeException e) {
			spinner.stop();
			out.useNewline(true);
			out.send("Problems performing encryption!!");
			out.send(e.getMessage());
			out.flush();
			throw new CommandPerformException(e);
		}
	}

	@Override
	public boolean verifyArguments(String[] cmdArgs) {
		int numOfArgs = cmdArgs.length;
		if (numOfArgs > MAX_ARG_COUNT || numOfArgs < MIN_ARG_COUNT) {
			return false;
		}
		for(String arg : cmdArgs) {
			boolean valid = this.validArgument(arg);
			if (!valid) return valid;
		}
		return true;
	}
	
	private boolean validArgument(String arg) {
		String[] splitOnEqual = arg.split("=");
		// Check to see if there was no equal sign
		if (splitOnEqual.length == 1) {
			return false;
		}
		// Check to make sure it's one of the valid arguments
		String key = splitOnEqual[0];
		if (!argMap.containsKey(key)) {
			return false;
		}
		else {
			String value = this.morphValue(splitOnEqual[1]);
			argMap.remove(key);
			argMap.put(key, value);
		}
		return true;
	}
	
	private String morphValue(String argVal) {
		if ("yes".equalsIgnoreCase(argVal)) {
			return "true";
		}
		if ("no".equalsIgnoreCase(argVal)) {
			return "false";
		}
		return argVal;
	}

}
