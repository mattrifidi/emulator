/**
 * 
 */
package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.llrp.aispec._AISpec;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.properties.GPISettings;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecController;
import org.rifidi.emulator.reader.llrp.rospec.ROSpecControllerFactory;
import org.rifidi.emulator.reader.llrp.rospec._ROSpec;
import org.rifidi.emulator.reader.llrp.trigger.DurationTrigger;
import org.rifidi.emulator.reader.llrp.trigger.GPIWithTimeoutTrigger;
import org.rifidi.emulator.reader.llrp.trigger.NullTrigger;
import org.rifidi.emulator.reader.llrp.trigger.PeriodicTrigger;
import org.rifidi.emulator.reader.llrp.trigger.TagObservationTrigger;
import org.rifidi.emulator.reader.llrp.trigger.Trigger;
import org.rifidi.emulator.reader.llrp.trigger.ROSpecStart.ROSpecStartImmediateTrigger;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

import edu.uark.csce.llrp.AISpec;
import edu.uark.csce.llrp.AISpecStopTrigger;
import edu.uark.csce.llrp.AddROSpec;
import edu.uark.csce.llrp.AddROSpecResponse;
import edu.uark.csce.llrp.C1G2EPCMemorySelector;
import edu.uark.csce.llrp.DeleteROSpec;
import edu.uark.csce.llrp.DeleteROSpecResponse;
import edu.uark.csce.llrp.DisableROSpec;
import edu.uark.csce.llrp.DisableROSpecResponse;
import edu.uark.csce.llrp.EnableROSpec;
import edu.uark.csce.llrp.EnableROSpecResponse;
import edu.uark.csce.llrp.GPITriggerValue;
import edu.uark.csce.llrp.GetROSpecs;
import edu.uark.csce.llrp.GetROSpecsResponse;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.PeriodicTriggerValue;
import edu.uark.csce.llrp.RFSurveySpec;
import edu.uark.csce.llrp.ROReportSpec;
import edu.uark.csce.llrp.ROSpec;
import edu.uark.csce.llrp.ROSpecStartTrigger;
import edu.uark.csce.llrp.ROSpecStopTrigger;
import edu.uark.csce.llrp.StartROSpec;
import edu.uark.csce.llrp.StartROSpecResponse;
import edu.uark.csce.llrp.StopROSpec;
import edu.uark.csce.llrp.StopROSpecResponse;
import edu.uark.csce.llrp.TagReportContentSelector;
import edu.uark.csce.llrp.UTCTimestamp;

/**
 * The Operation Control - Command Handler takes care of incoming messages like
 * addRoSpec, getRoSpec etc. and performs the necessary operations
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class LLRPReaderOperationControl {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPReaderOperationControl.class);

	/**
	 * This is the handler method for adding a rospec. It goes through the
	 * incoming ADD_ROSPEC message and builds a rospec data structure that will
	 * be run.
	 * 
	 * @param arg
	 *            The Command object for this handler. Contains the ADD_ROSPEC
	 *            message
	 * @param asr
	 *            The shared resources for the LLRP reader
	 * @return A command object with an ADD_ROSPEC_RESPONCE message as argument
	 *         0.
	 */
	public CommandObject addROSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		logger.debug("Inside ADDROSPEC Handler");

		// Change ConfigurationStateVariable
		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;

		// the status of this message
		LLRPStatus stat = new LLRPStatus();

		// Responce to ADD_ROSPEC message
		AddROSpecResponse arsr = new AddROSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed ADD_ROSPEC Message");
			logger.error("error when deserializing the ADD_ROSPEC Message");
			arsr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(arsr);
			return arg;
		}

		// The incoming ADD_ROSPEC message and rospec param
		AddROSpec aros = (AddROSpec) m;

		ROSpec roparam = aros.getROSpecParam();

		// ROspec is in disabled state
		roparam.setCurrentState((byte) 0);

		// Find out start trigger for this rospec
		Trigger roStartTrig = null;

		ROSpecStartTrigger roStartTrigParam = roparam.getROBoundarySpecParam()
				.getROSpecStartTriggerParam();

		if (roStartTrigParam.getROSpecStartTriggerType() == 0) {
			// null trigger
			logger.debug("RoSpecStartTrigger is nullTrigger");
			roStartTrig = new NullTrigger();
		} else if (roStartTrigParam.getROSpecStartTriggerType() == 1) {
			// Imediate Trigger
			logger.debug("RoSpecStartTrigger is Imediate");
			roStartTrig = new ROSpecStartImmediateTrigger();
		} else if (roStartTrigParam.getROSpecStartTriggerType() == 2) {
			// Periodic Trigger
			logger.debug("ROSpecStartTrigger is periodic");
			PeriodicTriggerValue ptv = roStartTrigParam
					.getPeriodicTriggerValueParam();
			long offset = 0;
			if (llrpsr.getProperties().hasUTCClockCapabilities
					&& ptv.getUTCTimestampParam() != null) {
				UTCTimestamp time = ptv.getUTCTimestampParam();
				long startTime = time.getMicroseconds() / 1000;
				long now = System.currentTimeMillis();
				if (startTime > now) {
					offset = startTime - now;
				} else {

					stat.setErrorCode((short) 101);
					stat.setErrorDescription("UTC time specified for "
							+ "periodic start trigger is before current time");

				}
				offset = offset + ptv.getOffset();
			} else {
				offset = ptv.getOffset();
			}

			roStartTrig = new PeriodicTrigger(offset, ptv.getPeriod(), roparam
					.getROSpecID());

		} else if (roStartTrigParam.getROSpecStartTriggerType() == 3) {

			// get Trigger specification
			GPITriggerValue trigSpec = roStartTrigParam
					.getGPITriggerValueParam();

			// get the GPIPort
			GPISettings gpiPort = llrpsr.getProperties().getGPISettingAt(
					trigSpec.getGPIPortNum());

			// construct trigger with no timeout
			roStartTrig = new GPIWithTimeoutTrigger(trigSpec.getGPIPortNum(),
					trigSpec.getGPIEvent(), 0, gpiPort);

		} else {
			// invalid
			logger.error("Invalid Start Trigger for ROSPec.  Value is set to "
					+ roStartTrigParam.getROSpecStartTriggerType());
			stat.setErrorCode((byte) 200);
			stat.setErrorDescription("Invalid Start Trigger for ROSpec.  "
					+ "Valid values are 0-3.  Value set to "
					+ roStartTrigParam.getROSpecStartTriggerType());
		}

		logger.debug("Set Start Trigger successfully");

		Trigger roStopTrig = null;
		ROSpecStopTrigger roStopTrigParam = null;
		// Find out stop Trigger for this ROSpec
		roStopTrigParam = roparam.getROBoundarySpecParam()
				.getROSpecStopTriggerParam();

		if (roStopTrigParam.getROSpecStopTriggerType() == 0) {
			logger.debug("ROSPec Stop Trigger is null trigger");
			roStopTrig = new NullTrigger();

		} else if (roStopTrigParam.getROSpecStopTriggerType() == 1) {
			logger.debug("ROSPec Stop Trigger is a Duration trigger");
			roStopTrig = new DurationTrigger(roStopTrigParam
					.getDurationTriggerValue());

		} else if (roStopTrigParam.getROSpecStopTriggerType() == 2) {
			// GPI stop Trigger
			if (roStopTrigParam.getGPITriggerValueParam() == null) {
				stat.setErrorCode((short) 101);
				stat.setErrorDescription("No GPI Trigger parameter found");
			}
			if (llrpsr.getProperties().numGPIs() > 0) {
				GPITriggerValue trig = roStopTrigParam
						.getGPITriggerValueParam();
				GPISettings gpi = llrpsr.getProperties().getGPISettingAt(
						trig.getGPIPortNum());
				roStopTrig = new GPIWithTimeoutTrigger(trig.getGPIPortNum(),
						trig.getGPIEvent(), trig.getTimeout(), gpi);
			} else {
				stat.setErrorCode((short) 101);
				stat.setErrorDescription("This reader does "
						+ "not support GPI Triggers for ROSpec Stop Trigger");
			}

		} else {
			// invalid
			logger.error("Invalid Stop Trigger for ROSPec");

			stat.setErrorCode((byte) 200);
			stat.setErrorDescription("Invalid Stop Trigger for ROSpec.  "
					+ "Valid values are 0-2.  Value set to "
					+ roStopTrigParam.getROSpecStopTriggerType());

		}

		logger.debug("Set Stop Trigger successfully");

		ROReportSpec roReportSpec = roparam.getROReportSpecParam();
		ROReportFormat roReportFormat = null;
		if (roReportSpec != null) {
			logger.debug("Setting custom roReportFormat");
			roReportFormat = new ROReportFormat();
			roReportFormat.reportTrigger = roReportSpec.getROReportTrigger();
			roReportFormat.N = roReportSpec.getN();
			TagReportContentSelector trcs = roReportSpec
					.getTagReportContentSelectorParam();
			roReportFormat.enableAccessSpecID = trcs.getEnableAccessSpecID();
			roReportFormat.enableAntennaID = trcs.getEnableAntennaID();
			roReportFormat.enableChannelIndex = trcs.getEnableChannelIndex();
			roReportFormat.enableFirstSeenTimestamp = trcs
					.getEnableFirstSeenTimestamp();
			roReportFormat.enableInventoryParamaterSpecID = trcs
					.getEnableInventoryParameterSpecID();
			roReportFormat.enableLastSeenTimestamp = trcs
					.getEnableLastSeenTimestamp();
			roReportFormat.enablePeakRSSI = trcs.getEnablePeakRSSI();
			roReportFormat.enableROSpecID = trcs.getEnableROSpecID();
			roReportFormat.enableSpecIndex = trcs.getEnableSpecIndex();
			roReportFormat.enableTagSeenCount = trcs.getEnableTagSeenCount();

			try {

				Object memsel = trcs
						.getAirProtocolSpecificEPCMemorySelectorParam(0);
				if (memsel instanceof C1G2EPCMemorySelector) {
					C1G2EPCMemorySelector c1g2memsel = (C1G2EPCMemorySelector) memsel;
					roReportFormat.enableCRC = c1g2memsel.getEnableCRC();
					roReportFormat.enablePC = c1g2memsel.getEnablePCBits();
				} else {
					stat.setErrorCode((short) 100);
					stat.setErrorDescription("ROReport EPC Memory"
							+ " Selector does not contain a valid"
							+ " air protocol specific EPC Memory "
							+ "Selector Param");
				}
			} catch (IndexOutOfBoundsException ex) {
				logger.error("Malformed paramater: "
						+ "ROReport param doesn't contain an "
						+ "C1G2memoryselector");

				stat.setErrorCode((short) 100);
				stat.setErrorDescription("Malformed parameter: ROReport "
						+ "Spec does not have an air protocol Specific "
						+ "EPC Memory Selector param");

			}

		}

		logger.debug("Set ROReport successfully");
		// Create ROSpec
		_ROSpec rs = new _ROSpec(roparam.getROSpecID(), roparam.getPriority(),
				roStartTrig, roStopTrig, roReportFormat, asr.getReaderName(),
				llrpsr);

		// Accumulate specs to execute (AI and RF specs)
		ArrayList<_AISpec> specsToExecute = new ArrayList<_AISpec>();

		for (int i = 0; i < roparam.getNumSpecParams(); i++) {

			Object spec = roparam.getSpecParam(i);

			// Set up AISpecs
			if (spec instanceof AISpec) {

				logger.debug("Creating an AISpec...");
				AISpec aiparam = (AISpec) spec;
				Trigger stopTrigger = null;
				AISpecStopTrigger aist = aiparam.getAISpecStopTriggerParam();

				// set up AIStopTrigger
				if (aist.getAISpecStopTriggerType() == 0) {
					stopTrigger = new NullTrigger();
				} else if (aist.getAISpecStopTriggerType() == 1) {
					// duration trigger
					stopTrigger = new DurationTrigger(aist.getDurationTrigger());
				} else if (aist.getAISpecStopTriggerType() == 2) {
					if (llrpsr.getProperties().numGPIs() > 0) {

						// get trigger param
						GPITriggerValue gpitrig = aist
								.getGPITriggerValueParam();

						// get gpi port
						GPISettings gpi = llrpsr.getProperties()
								.getGPISettingAt(gpitrig.getGPIPortNum());

						// construct trigger
						stopTrigger = new GPIWithTimeoutTrigger(gpitrig
								.getGPIPortNum(), gpitrig.getGPIEvent(),
								gpitrig.getTimeout(), gpi);
					} else {
						stat.setErrorCode((short) 101);
						stat.setErrorDescription("This reader does not support"
								+ "GPI Triggers for AISpec Stop Trigger");

					}

				} else if (aist.getAISpecStopTriggerType() == 3) {
					logger.debug("create new TagObservation Trigger");
					stopTrigger = new TagObservationTrigger(aist
							.getTagObservationTriggerParam().getTriggerType(),
							aist.getTagObservationTriggerParam()
									.getNumberOfTags(), aist
									.getTagObservationTriggerParam()
									.getNumberOfAttempts(), aist
									.getTagObservationTriggerParam().getT(),
							aist.getTagObservationTriggerParam().getTimeout());

				} else {
					logger.error("Invalid Trigger for AISpec");

					stat.setErrorCode((short) 200);
					stat.setErrorDescription("Invalid Trigger for AISpec. "
							+ "Valid values are 0-2.  Value set to "
							+ aist.getAISpecStopTriggerType());

				}

				logger.debug("Set AIStopTrigger sucessfully");
				// gateher antenna IDs into an array

				// number of antennas specified in AISpec
				int antennas = aiparam.getAntennaCount();

				// List of antenna Indexes to be used
				ArrayList<Integer> antennaIDs = new ArrayList<Integer>();

				// Max number of antennas supported by this reader
				int maxAntennas = llrpsr.getProperties().maxAntennasSupported();

				// if all antennas are used
				if (antennas >= 1 && aiparam.getAntennaElement(0) == 0) {

					for (int j = 0; j < maxAntennas; j++) {
						antennaIDs.add(j);

					}
				} else {
					for (int j = 0; j < antennas; j++) {
						int antennaIndex = (int) aiparam.getAntennaElement(j);
						if (antennaIndex <= maxAntennas) {
							if (antennaIndex != 0) {
								antennaIDs.add(antennaIndex - 1);
							}
						} else {
							stat.setErrorCode((short) 100);
							stat.setErrorDescription("Antenna "
									+ aiparam.getAntennaElement(j)
									+ "is not a valid antenna."
									+ " The maximum number of antennas is "
									+ maxAntennas);
						}
					}
				}

				// convert antennaIDs to array of ints
				int[] intArrayOfAntennaIDs = new int[antennaIDs.size()];
				for (int j = 0; j < antennaIDs.size(); j++) {
					intArrayOfAntennaIDs[j] = antennaIDs.get(j);
				}

				logger.debug("number of antennas supported: " + antennas);

				logger.debug("Set AI Antennas sucessfully");

				if (intArrayOfAntennaIDs.length > 0) {
					_AISpec aispec = new _AISpec(i, stopTrigger,
							intArrayOfAntennaIDs, roparam.getROSpecID(),
							(LLRPReaderSharedResources) asr, roReportFormat);

					specsToExecute.add(aispec);

					logger.debug("Added AISpec to ROSpec Sucessfully");
				} else {
					stat.setErrorCode((short) 100);
					stat.setErrorDescription("No valid antennas found in"
							+ " Antenna IDs in AISpec");
				}

				// TODO == InventoryParameterSpec == it's just reported that
				// there is a InventoryParameterSpec missing
				// This parameter defines the inventory operation to be
				// performed at all antennas specified in the corresponding
				// AISpec

				int numInventoryParameterSpecs = aiparam
						.getNumInventoryParameterSpecParams();
				if (numInventoryParameterSpecs < 1) {
					logger.error("No InventoryParameterSpec found");

					stat.setErrorCode((short) 200);
					stat
							.setErrorDescription("InventoryParameterSpec not found. Valid AddRoSpec needs to have at least one InventoryParameterSpec");

				} else {
					logger
							.error("InventoryParameterSpec found but not supported yet");
				}
				// for (int numIPS = 0; numIPS < numInventoryParameterSpecs;
				// numIPS++) {
				// InventoryParameterSpec inventoryParameterSpec = aiparam
				// .getInventoryParameterSpecParam(numIPS);
				// if (inventoryParameterSpec.getInventoryParameterSpecID() ==
				// 0) {
				// // error handling 0 is not a valid number
				// logger.debug("Insert code for error handling");
				// }
				// int numAntennaConfigurations = inventoryParameterSpec
				// .getNumAntennaConfigurationParams();
				// for (int numAC = 0; numAC < numAntennaConfigurations;
				// numAC++) {
				// AntennaConfiguration antennaConfiguration =
				// inventoryParameterSpec
				// .getAntennaConfigurationParam(numAC);
				//
				// // If antennaID equals zero it applies to every antenna
				// int antennaID = antennaConfiguration.getAntennaId();
				// logger.debug("Changeing configuration for Antenna "
				// + antennaID);
				// int numAirProtocolInventoryCommandSettings =
				// antennaConfiguration
				// .getNumAirProtocolInventoryCommandSettingsParams();
				// for (int numAPICSP = 0; numAPICSP <
				// numAirProtocolInventoryCommandSettings; numAPICSP++) {
				// // AIRProtocolInventoryCommandSetting
				// AirProtocolInventoryCommandSettings
				// airProtocolInventoryCommandSettings = antennaConfiguration
				// .getAirProtocolInventoryCommandSettingsParam(numAPICSP);
				// // TODO implement functionality
				// airProtocolInventoryCommandSettings.getParamType();
				//
				// }
				// // TODO implement functionality
				// antennaConfiguration.getRFReceiverParam();
				// antennaConfiguration.getRFTransmitterParam();
				// }
				// }

			}
			// Set up RFSurveySpecs
			else if (spec instanceof RFSurveySpec) {
				logger.error("RFSurveySpec not supported yet");

				stat.setErrorCode((byte) 200);
				stat
						.setErrorDescription("RFSurveySpec not yet supported for ROSpec");

				// TODO: add logic for RFSurvey Spec

			} else {

				logger.error("Custom Spec not supported yet");

				stat.setErrorCode((byte) 200);
				stat.setErrorDescription("Custom Spec not yet "
						+ "supported for ROSpec");

				// TODO: handle coustome spec
			}
		}
		rs.setSpecsToExecute(specsToExecute);

		// If no errors, add ROSpec to Inactive queue
		if (stat.getErrorCode() == 0) {

			ROSpecController rosc = ROSpecControllerFactory.getInstance()
					.getReportController(asr.getReaderName());
			boolean successful = rosc.addROSpec(rs);

			if (successful) {
				llrpsr.getAdded_Rospecs().put(roparam.getROSpecID(), roparam);
				stat.setErrorCode((short) 0);
				llrpsr.getProperties().LLRPConfiguraitonStateVariable++;
				stat.setErrorDescription("ROSPec successfully added");
				logger.debug("ROSpec successfully added");

			} else {
				stat.setErrorCode((short) 100);
				stat.setErrorDescription("Threre was an error when "
						+ "adding the spec with ID " + roparam.getROSpecID());
			}
		}

		logger.debug("messageID: " + aros.getMessageID());
		arsr.setMessageID(aros.getMessageID());
		arsr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(arsr);
		return arg;
	}

	/**
	 * This is the handler method for an ENABLE_ROSPEC message. It is sent from
	 * the client to the reader to move a rospec to the "enabled" queue
	 * 
	 * @param arg
	 *            The command object for this handler. Contains ENABLE_ROSPEC
	 *            message.
	 * @param asr
	 *            The shared resources for the LLRP Reader
	 * @return A command object with an ENABLE_ROSPEC_REPSONCE message as
	 *         argument 0. It will return an error message in the responce
	 *         message if the rospec cannot be enabled
	 */
	public CommandObject enableROSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("ROSPec was successfully enabled");

		// The response for this
		EnableROSpecResponse ersr = new EnableROSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed ENABLE_ROSPEC Message");
			logger.error("error when deserializing the ENABLE_ROSPEC Message");
			ersr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(ersr);
			return arg;
		}

		EnableROSpec eros = (EnableROSpec) m;

		boolean success = ROSpecControllerFactory.getInstance()
				.getReportController(asr.getReaderName()).enableROSpec(
						eros.getROSpecID());
		if (!success) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("There was a problem when "
					+ "enabling ROSpec with ID " + eros.getROSpecID());
			logger.error("There was a problem when enabling ROSPec with ID"
					+ eros.getROSpecID());
		}

		ersr.setLLRPStatusParam(stat);
		ersr.setMessageID(eros.getMessageID());
		arg.getReturnValue().add(ersr);
		return arg;
	}

	/**
	 * This is the handler method for an START_ROSPEC message. It is sent from
	 * the client to the reader to begin a rospec execution
	 * 
	 * @param arg
	 *            The command object for this handler. Contains START_ROSPEC
	 *            message.
	 * @param asr
	 *            The shared resources for the LLRP Reader
	 * @return A command object with an START_ROSPEC_REPSONCE message as
	 *         argument 0.t will return an error message in the responce message
	 *         if the rospec cannot be started
	 */
	public CommandObject startROSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("ROSPec was successfully started");

		// The response for this
		StartROSpecResponse srsr = new StartROSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed START_ROSPEC Message");
			logger.error("error when deserializing the START_ROSPEC Message");
			srsr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(srsr);
			return arg;
		}

		StartROSpec sros = (StartROSpec) m;

		boolean success = ROSpecControllerFactory.getInstance()
				.getReportController(asr.getReaderName()).startROSpec(
						sros.getROSpecID());
		if (!success) {

			stat.setErrorCode((short) 101);
			stat.setErrorDescription("There was a problem when "
					+ "starting ROSpec with ID " + sros.getROSpecID());

			logger.error("There was a problem when starting ROSPec with ID"
					+ sros.getROSpecID());
		}

		srsr.setMessageID(sros.getMessageID());
		srsr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(srsr);
		return arg;
	}

	public CommandObject stopROSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("ROSPec was successfully stopped");

		StopROSpecResponse srsr = new StopROSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed STOP_ROSPEC Message");
			logger.error("error when deserializing the STOP_ROSPEC Message");
			srsr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(srsr);
			return arg;
		}

		StopROSpec sros = (StopROSpec) m;

		boolean success = ROSpecControllerFactory.getInstance()
				.getReportController(asr.getReaderName()).stopROSpec(
						sros.getROSpecID());
		if (!success) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("There was a problem when "
					+ "stopping ROSpec with ID " + sros.getROSpecID());
			logger.error("There was a problem when stoping ROSPec with ID"
					+ sros.getROSpecID());
		}

		srsr.setMessageID(sros.getMessageID());
		srsr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(srsr);
		return arg;
	}

	public CommandObject deleteROSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("ROSpec was sucessfully deleted");

		DeleteROSpecResponse drsr = new DeleteROSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed DELETE_ROSPEC Message");
			logger.error("error when deserializing the DELETE_ROSPEC Message");
			drsr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(drsr);
			return arg;
		}

		DeleteROSpec delete_rs = (DeleteROSpec) m;

		boolean success = ROSpecControllerFactory.getInstance()
				.getReportController(asr.getReaderName()).deleteROSpec(
						delete_rs.getROSpecID());
		if (!success) {

			stat.setErrorCode((short) 101);
			stat.setErrorDescription("There was a problem when "
					+ "deleting ROSpec with ID " + delete_rs.getROSpecID());

			logger.error("There was a problem when deleting ROSPec with ID "
					+ delete_rs.getROSpecID());
		}
		if (stat.getErrorCode() == 0) {

			// Change ConfigurationStateVariable
			LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;
			llrpsr.getProperties().LLRPConfiguraitonStateVariable++;

			llrpsr.getAdded_Rospecs().remove(delete_rs.getROSpecID());

		}

		drsr.setMessageID(delete_rs.getMessageID());
		drsr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(drsr);
		return arg;
	}

	public CommandObject disableROSpec(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("ROSpec was sucessfully disabled");

		DisableROSpecResponse drsr = new DisableROSpecResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed DISABLE_ROSPEC Message");
			logger.error("error when deserializing the DISABLE_ROSPEC Message");
			drsr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(drsr);
			return arg;
		}

		DisableROSpec delete_rs = (DisableROSpec) m;

		boolean success = ROSpecControllerFactory.getInstance()
				.getReportController(asr.getReaderName()).disableROSpec(
						delete_rs.getROSpecID());

		if (!success) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("There was a problem when "
					+ "disabling ROSpec with ID " + delete_rs.getROSpecID());
			logger.error("There was a problem when disabling ROSPec with ID "
					+ delete_rs.getROSpecID());
		}

		drsr.setMessageID(delete_rs.getMessageID());
		drsr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(drsr);
		return arg;
	}

	public CommandObject getROSpecs(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;

		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("sucess");

		GetROSpecsResponse grsr = new GetROSpecsResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			stat.setErrorCode((short) 101);
			stat.setErrorDescription("Malformed GET_ROSPECS Message");
			logger.error("error when deserializing the GET_ROSPECS Message");
			grsr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(grsr);
			return arg;
		}

		GetROSpecs getROSpecs = (GetROSpecs) m;

		// Get all rifidi rospecs from the rospec controller
		HashMap<Integer, _ROSpec> rifidiROSpecs = ROSpecControllerFactory
				.getInstance().getReportController(asr.getReaderName())
				.getROSpecs();

		// iterate through the rifidi rospecs, update the current state of each
		// one, and add to repsonce
		for (Integer i : rifidiROSpecs.keySet()) {
			_ROSpec rifidiROSpec = rifidiROSpecs.get(i);

			ROSpec llrpTKROSpec = llrpsr.getAdded_Rospecs().get(i);

			llrpTKROSpec.setCurrentState((byte) rifidiROSpec.getCurrentState());

			grsr.addROSpecParam(llrpTKROSpec);

		}

		grsr.setLLRPStatusParam(stat);
		grsr.setMessageID(getROSpecs.getMessageID());
		arg.getReturnValue().add(grsr);
		return arg;
	}

}
