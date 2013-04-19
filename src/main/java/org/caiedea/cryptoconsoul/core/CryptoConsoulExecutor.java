package org.caiedea.cryptoconsoul.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.abstractthis.consoul.commands.AbstractCommandExecutor;
import com.abstractthis.consoul.commands.ConsoleCommand;

public class CryptoConsoulExecutor extends AbstractCommandExecutor {
	private static final Logger log = LoggerFactory.getLogger(CryptoConsoulExecutor.class);

	@Override
	protected void handleCommandInitException(ConsoleCommand command, Throwable t) {
		String cmdName = command.getCommandName();
		String errMsg = String.format("Problems initializing command '%s'", cmdName);
		log.error(errMsg, t);
	}

	@Override
	protected void handleCommandNotFoundException(ConsoleCommand command, Throwable t) {
		String cmdName = command.getCommandName();
		String errMsg = String.format("Could not find command '%s'", cmdName);
		log.error(errMsg, t);
	}

	@Override
	protected void handleCommandPerformException(ConsoleCommand command, Throwable t) {
		String cmdName = command.getCommandName();
		String errMsg = String.format("Performance problem for command '%s'", cmdName);
		log.error(errMsg, t);
	}

	@Override
	protected void handleCommandPermissionException(ConsoleCommand command, Throwable t) {
		String cmdName = command.getCommandName();
		String errMsg = String.format("Permission problem for command '%s'", cmdName);
		log.error(errMsg, t);
	}

}
