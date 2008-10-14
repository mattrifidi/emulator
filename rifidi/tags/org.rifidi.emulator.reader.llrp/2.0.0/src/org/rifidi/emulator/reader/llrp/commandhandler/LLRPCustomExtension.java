/**
 * 
 */
package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

import edu.uark.csce.llrp.CustomMessage;
import edu.uark.csce.llrp.ErrorMessage;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;

/**
 * @author kyle
 *
 */
public class LLRPCustomExtension {
	
	

	/**
	 * The EmuLogger instance for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPCustomExtension.class);
	
	public CommandObject unknownCustomMessage(CommandObject arg, AbstractReaderSharedResources asr) 
	{
		LLRPStatus stat = new LLRPStatus();
		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed CUSTOM Message");
			logger.error("error when deserializing the CUSTOM Message");
			ErrorMessage em = new ErrorMessage();
			em.setLLRPStatusParam(stat);
			return arg;
		}
		
		CustomMessage cm = (CustomMessage) m;
		ErrorMessage em = new ErrorMessage();
		em.setMessageID(1);
		
		LLRPStatus llrpstatus = new LLRPStatus();
		llrpstatus.setErrorCode((short)100);
		llrpstatus.setErrorDescription("Custom message unknown");
		
		em.setLLRPStatusParam(llrpstatus);
		
		em.setMessageID(cm.getMessageID());
		arg.getReturnValue().add(em);
		return arg;
	}
	
}
