package org.rifidi.emulator.reader.llrp.command.exception;

import java.io.IOException;
import java.util.ArrayList;

import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.command.exception.GenericExceptionHandler;
import org.rifidi.emulator.reader.llrp.util.LLRPUtilities;

import edu.uark.csce.llrp.ErrorMessage;
import edu.uark.csce.llrp.LLRPStatus;

public class LLRPExceptionHandler extends GenericExceptionHandler {

	@Override
	public ArrayList<Object> commandNotFoundError(ArrayList<Object> arg,
			CommandObject obj) {
		ErrorMessage em = new ErrorMessage();
		LLRPStatus llrpstat = new LLRPStatus();
		llrpstat.setErrorCode((short) 100);
		char[] c = obj.getCurrentQueryName().toCharArray();
		byte[] com = new byte[c.length];
		for (int i = 0; i < c.length; i++) {
			com[i] = (byte) c[i];
		}

		llrpstat.setErrorDescription("Invalid message type: "
				+ LLRPUtilities.calculateMessageNumber(com));
		em.setLLRPStatusParam(llrpstat);

		byte[] errorMsg = null;
		try {
			errorMsg = em.serialize();
		} catch (IOException e) {
			return null;
		}
		ArrayList<Object> retval = new ArrayList<Object>();
		retval.add(errorMsg);

		return retval;
	}

	@Override
	public ArrayList<Object> invalidCommandError(ArrayList<Object> arg,
			String value, CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Object> malformedMessageError(ArrayList<Object> arg,
			CommandObject obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandObject methodInvocationError(ArrayList<Object> arg,
			CommandObject obj) {
		ErrorMessage em = new ErrorMessage();
		LLRPStatus llrpstat = new LLRPStatus();
		llrpstat.setErrorCode((short) 100);
		char[] c = obj.getCurrentQueryName().toCharArray();
		byte[] com = new byte[c.length];
		for (int i = 0; i < c.length; i++) {
			com[i] = (byte) c[i];
		}

		llrpstat.setErrorDescription("Internal error in Rifidi "
				+ "when processing a message of type "
				+ LLRPUtilities.calculateMessageNumber(com));
		em.setLLRPStatusParam(llrpstat);

		
		obj.getReturnValue().add(em);
		return obj;
	}

}
