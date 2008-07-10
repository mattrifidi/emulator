package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

import edu.uark.csce.llrp.EnableEventsAndReports;
import edu.uark.csce.llrp.ErrorMessage;
import edu.uark.csce.llrp.GetReport;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.ROAccessReport;
import edu.uark.csce.llrp.TagReportData;

public class LLRPReports {
	
	/*
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(LLRPReports.class);
	
	public CommandObject getReport(CommandObject arg, AbstractReaderSharedResources asr){
		logger.debug("In the get report handler");
		
		ROAccessReport reportMsg = new ROAccessReport();
		
		//decode the message
		Message m = null;
		byte[] rawMsg = (byte[])arg.getArguments().get(0);
		try{
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		}catch(IOException e){
			ErrorMessage em = new ErrorMessage();
			LLRPStatus stat = new LLRPStatus();
			stat.setErrorCode((short)101);
			stat.setErrorDescription("Malformed GET_REPORT Message");
			logger.error("error when deserializing the GET_REPORT");
			em.setLLRPStatusParam(stat);
			arg.getReturnValue().add(em);
			return arg;
		}
		
		GetReport getReport = (GetReport) m;
		
		ArrayList<TagReportData> data = ((LLRPReaderSharedResources)asr).getTagReportDataEntries().getAllDataEntries();
		
		for(TagReportData d : data){
			reportMsg.addTagReportDataParam(d);
		}

		reportMsg.setMessageID(getReport.getMessageID());
		arg.getReturnValue().add(reportMsg);
		return arg;
	}
	
	public CommandObject keepAliveAck(CommandObject arg, AbstractReaderSharedResources asr){
		logger.debug("Got a KeepAliveAck");
		//decode the message
		Message m = null;
		byte[] rawMsg = (byte[])arg.getArguments().get(0);
		try{
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		}catch(IOException e){
			ErrorMessage em = new ErrorMessage();
			LLRPStatus stat = new LLRPStatus();
			stat.setErrorCode((short)101);
			stat.setErrorDescription("Malformed KEEPALIVE_ACK Message");
			logger.error("error when deserializing the KEEPALIVE_ACK");
			em.setLLRPStatusParam(stat);
			arg.getReturnValue().add(em);
			return arg;
		}
		return arg;
	}
	
	public CommandObject enableEventsAndReports(CommandObject arg, AbstractReaderSharedResources asr){
		logger.debug("Got a enable events and reports message");
		
		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources)asr;
		
		//decode the message
		Message m = null;
		byte[] rawMsg = (byte[])arg.getArguments().get(0);
		try{
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		}catch(IOException e){
			ErrorMessage em = new ErrorMessage();
			LLRPStatus stat = new LLRPStatus();
			stat.setErrorCode((short)101);
			stat.setErrorDescription("Malformed ENABLE_EVENTS_AND_REPORTS Message");
			logger.error("error when deserializing the ENABLE_EVENTS_AND_REPORTS");
			em.setLLRPStatusParam(stat);
			arg.getReturnValue().add(em);
			return arg;
		}
		EnableEventsAndReports eear = (EnableEventsAndReports)m;
		
		if(llrpsr.getProperties().supportsEventAndReportHolding){
			ErrorMessage em = new ErrorMessage();
			LLRPStatus stat = new LLRPStatus();
			stat.setErrorCode((short)101);
			stat.setErrorDescription("This reader does not support event and report holding");
			logger.error("This reader does not support event and report holding");
			em.setLLRPStatusParam(stat);
			arg.getReturnValue().add(em);
			em.setMessageID(eear.getMessageID());
			return arg;
		}else{
			//TODO: send reports
		}
		
		return arg;
	}

}
