/**
 * 
 */
package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.report.LLRPReportController;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.emulator.reader.llrp.util.LLRPUtilities;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

import edu.uark.csce.llrp.CloseConnectionResponse;
import edu.uark.csce.llrp.ConnectionAttemptEvent;
import edu.uark.csce.llrp.ConnectionCloseEvent;
import edu.uark.csce.llrp.ErrorMessage;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.ReaderEventNotificationData;

/**
 * @author kyle
 * 
 */
public class LLRPGeneral {

	/*
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPReaderDeviceCapabilities.class);

	public static final String DEVICE_MANUFACTURER_NAME = "device_manufacturer_name";

	public static final String READER_MODEL = "reader_model";

	public static final String FIRMWARE_VERSION = "firmware_version";

	public static final String CAN_SET_ANTENNA_PROP = "can_set_antenna_prop";

	public static final String HAS_UTC_CLOCK_CAPABILITY = "has_utc_clock_capability";

	public static final String CAN_DO_RF_SURVEY = "can_do_rf_survey";

	public static final String CAN_TAG_INVENTORY_SINGULATION = "can_tag_inventory_singulation";

	public static final String MAX_NUMBER_ANTENNAS = "max_number_antennas";

	public static final String CAN_REPORT_BUFFER_FILL = "can_report_buffer_fill";

	public static final String NUM_GPI = "num_gpi";

	public static final String NUM_GPO = "num_gpo";

	public static final String MAX_ROSPECS = "max_rospecs";

	public static final String MAX_SPECS_PER_ROSPEC = "max_specs_per_rospec";

	public static final String MAX_NUM_INVENTORY_SPECS_PER_AISPEC = "max_num_inventory_specs_per_aispec";

	public static final String MAX_PRIORITY_LEVEL_SUPPORTED = "max_priority_level_supported";

	public static final String MAX_NUM_ACCESS_SPECS = "max_num_access_specs";

	public static final String SUPPORT_CLIENT_REQUEST_OP_SPEC = "support_client_request_op_spec";

	public static final String CLIENT_REQUEST_OP_SPEC_TIMEOUT = "client_request_op_spec_timeout";

	public static final String SUPPORT_EVENT_AND_REPORT_HOLDING = "support_event_and_report_holding";

	public static final String COUNTRY_CODE = "country_code";

	public static final String COMMUNICATION_STANDARD = "communication_standard";

	public static final String MAX_NUM_OP_SPEC_PER_ACCESS_SPEC = "max_num_op_Specs_per_Access_spec";

	public static final String C1G2_CAN_SUPPORT_BLOCK_ERASE = "c1g2_can_support_block_erase";

	public static final String C1G2_CAN_SUPPORT_BLOCK_WRITE = "c1g2_can_support_block_write";

	public static final String C1G2_MAX_NUM_SELECT_FILTERS_PER_QUERY = "c1g2_max_num_select_filters_per_query";

	public CommandObject closeConnection(CommandObject arg,
			AbstractReaderSharedResources asr) {
		logger.debug("CloseConnection Method was called");

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			logger.error("error when deserializing the"
					+ " CLOSE_CONNECTION Message");
			ErrorMessage em = new ErrorMessage();
			LLRPStatus stat = new LLRPStatus();
			stat.setErrorCode((short) 100);
			stat.setErrorDescription("Invalid Message: CLOSE_CONNECTION");
			em.setLLRPStatusParam(stat);
			arg.getReturnValue().add(em);
			return arg;
		}

		//if no errors,close the connection.
		LLRPReportController controller = LLRPReportControllerFactory
				.getInstance().getReportController(asr.getReaderName());
		controller.sendCloseMessage();

		return arg;
	}
	
	public CommandObject invalidLLRPVersion(CommandObject arg,
			AbstractReaderSharedResources asr) {
		
		byte[] badMessage = (byte[])arg.getArguments().get(0);
		ErrorMessage em = new ErrorMessage();
		LLRPStatus llrpstat = new LLRPStatus();
		llrpstat.setErrorCode((short)110);
		em.setLLRPStatusParam(llrpstat);
		byte[] returnBytes=null;
		try {
			
			//copy incorrect version number
			returnBytes = em.serialize();
			byte mask = (byte)(badMessage[0] & 0x23);
			returnBytes[0] = (byte)(mask | badMessage[0]);
			
			System.arraycopy(badMessage, 6, returnBytes, 6, 4);
			
			arg.getReturnValue().add(Message.receive(new ByteArrayInputStream(returnBytes)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return arg;
	}

}
