/**
 * 
 */
package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2Filter;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2InventoryCommand;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2InventoryMask;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2RFControl;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2SingulationControl;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2TagInventoryStateAwareFilterAction;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2TagInventoryStateAwareSingulationAction;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2.C1G2TagInventoryStateUnawareFilterAction;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderModule;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.properties.AntennaSettings;
import org.rifidi.emulator.reader.llrp.properties.GPISettings;
import org.rifidi.emulator.reader.llrp.properties.GPOSettings;
import org.rifidi.emulator.reader.llrp.properties.Properties;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

import edu.uark.csce.llrp.AccessReportSpec;
import edu.uark.csce.llrp.AirProtocolSpecificEPCMemorySelector;
import edu.uark.csce.llrp.AntennaConfiguration;
import edu.uark.csce.llrp.AntennaProperties;
import edu.uark.csce.llrp.C1G2EPCMemorySelector;
import edu.uark.csce.llrp.C1G2TagInventoryMask;
import edu.uark.csce.llrp.ErrorMessage;
import edu.uark.csce.llrp.EventNotificationState;
import edu.uark.csce.llrp.EventsAndReports;
import edu.uark.csce.llrp.GPIPortCurrentState;
import edu.uark.csce.llrp.GPOWriteData;
import edu.uark.csce.llrp.GetReaderConfig;
import edu.uark.csce.llrp.GetReaderConfigResponse;
import edu.uark.csce.llrp.Identification;
import edu.uark.csce.llrp.KeepaliveSpec;
import edu.uark.csce.llrp.LLRPConfigurationStateValue;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.RFReceiver;
import edu.uark.csce.llrp.RFTransmitter;
import edu.uark.csce.llrp.ROReportSpec;
import edu.uark.csce.llrp.ReaderEventNotificationSpec;
import edu.uark.csce.llrp.SetReaderConfig;
import edu.uark.csce.llrp.SetReaderConfigResponse;
import edu.uark.csce.llrp.TagReportContentSelector;

/**
 * @author kyle
 * 
 */
public class LLRPReaderDeviceConfiguration {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(LLRPReaderDeviceConfiguration.class);

	public CommandObject getReaderConfig(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;
		Properties props = llrpsr.getProperties();

		LLRPStatus llrpstatus = new LLRPStatus();
		llrpstatus.setErrorCode((short) 0);
		llrpstatus.setErrorDescription("Success");

		GetReaderConfigResponse grcr = new GetReaderConfigResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			llrpstatus.setErrorCode((short) 101);
			llrpstatus
					.setErrorDescription("Malformed GET_READER_CONFIG Message");
			logger
					.error("error when deserializing the GET_READER_CONFIG Message");
			grcr.setLLRPStatusParam(llrpstatus);
			arg.getReturnValue().add(grcr);
			return arg;
		}

		GetReaderConfig grc = (GetReaderConfig) m;

		byte data = grc.getRequestedData();

		// CustomExtensions are not supported yet return imediately
		if (grc.getNumCustomParams() > 0 && llrpstatus.getErrorCode() == 0) {
			llrpstatus.setErrorCode((short) 100);
			llrpstatus.setErrorDescription("unknown custom extension");
		}

		if (data < 0 || data > 11) {
			llrpstatus.setErrorCode((short) 100);
			llrpstatus.setErrorDescription("Requested Data is: " + data
					+ ". Valid values for this field are 0-11");
		}

		// Identification
		if ((data == 0 || data == 1) && llrpstatus.getErrorCode() == 0) {
			Identification id = new Identification();

			id.setIDType((byte) props.IDType);

			if (id.getIDType() == 0) {
				for (byte b : props.readerID) {
					id.addReaderIDElement(b);
				}
			} else {
				id.addReaderIDElement(props.readerID[0]);
			}
			logger.debug("Got ID config");
			grcr.setIdentificationParam(id);
		}

		// AntennaProps
		if ((data == 0 || data == 2) && llrpstatus.getErrorCode() == 0) {
			/*
			 * If grc.getAntennaID=0, get all properties, otherwise get the
			 * props of the specified antenns
			 */

			int numAntennas = props.maxAntennasSupported();

			if (grc.getAntennaID() == (short) 0) {
				for (int i = 1; i <= numAntennas; i++) {
					grcr.addAntennaPropertiesParam(getAntennaProp((short) i,
							props));
				}
			} else if (grc.getAntennaID() <= (short) props
					.maxAntennasSupported()) {
				grcr.addAntennaPropertiesParam(getAntennaProp(grc
						.getAntennaID(), props));
			} else {
				llrpstatus.setErrorCode((short) 1);
				llrpstatus.setErrorDescription("Tried to get an antenna "
						+ "property for an antenna that does not exist.  "
						+ "The highest antenna index is " + numAntennas);
			}

			logger.debug("Got Antenna props");

		}

		// Antenna Config
		if ((data == 0 || data == 3) && llrpstatus.getErrorCode() == 0) {

			int numAntennas = props.maxAntennasSupported();

			if (grc.getAntennaID() == (short) 0) {
				for (int i = 1; i <= numAntennas; i++) {
					grcr.addAntennaConfigurationParam(getAntennaConfig(
							(short) i, props));
				}
			} else if (grc.getAntennaID() <= (short) props
					.maxAntennasSupported()) {
				grcr.addAntennaConfigurationParam(getAntennaConfig(grc
						.getAntennaID(), props));
			} else {
				llrpstatus.setErrorCode((short) 1);
				llrpstatus.setErrorDescription("Tried to get an antenna "
						+ "config for an antenna that does not exist.  "
						+ "The highest antenna index is " + numAntennas);
			}

			logger.debug("GotAntennaCongfig");

		}

		// ROReportSpec
		if ((data == 0 || data == 4) && llrpstatus.getErrorCode() == 0) {
			ROReportSpec rrs = new ROReportSpec();

			rrs
					.setROReportTrigger((byte) props.roReportFormat_Global.reportTrigger);

			TagReportContentSelector trcs = new TagReportContentSelector();
			trcs.setEnableROSpecID(props.roReportFormat_Global.enableROSpecID);
			trcs
					.setEnableSpecIndex(props.roReportFormat_Global.enableSpecIndex);
			trcs
					.setEnableInventoryParameterSpecID(props.roReportFormat_Global.enableInventoryParamaterSpecID);
			trcs
					.setEnableAntennaID(props.roReportFormat_Global.enableAccessSpecID);
			trcs
					.setEnableChannelIndex(props.roReportFormat_Global.enableChannelIndex);
			trcs.setEnablePeakRSSI(props.roReportFormat_Global.enablePeakRSSI);
			trcs
					.setEnableFirstSeenTimestamp(props.roReportFormat_Global.enableFirstSeenTimestamp);
			trcs
					.setEnableLastSeenTimestamp(props.roReportFormat_Global.enableLastSeenTimestamp);
			trcs
					.setEnableTagSeenCount(props.roReportFormat_Global.enableTagSeenCount);

			C1G2EPCMemorySelector c1g2epcms = new C1G2EPCMemorySelector();
			c1g2epcms.setEnableCRC(props.roReportFormat_Global.enableCRC);
			c1g2epcms.setEnablePCBits(props.roReportFormat_Global.enablePC);
			trcs.addAirProtocolSpecificEPCMemorySelectorParam(c1g2epcms);

			trcs
					.setEnableAccessSpecID(props.roReportFormat_Global.enableAccessSpecID);
			rrs.setTagReportContentSelectorParam(trcs);
			grcr.setROReportSpecParam(rrs);

			logger.debug("Got RoReport config");
		}

		// ReaderEventNotificationSpec
		if ((data == 0 || data == 5) && llrpstatus.getErrorCode() == 0) {

			ReaderEventNotificationSpec rens = new ReaderEventNotificationSpec();

			// hop event
			EventNotificationState ens = new EventNotificationState();
			ens.setEventType((byte) 0);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(0));
			rens.addEventNotificationStateParam(ens);

			// GPI event
			ens = new EventNotificationState();
			ens.setEventType((byte) 1);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(1));
			rens.addEventNotificationStateParam(ens);

			// ROSpec event
			ens = new EventNotificationState();
			ens.setEventType((byte) 2);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(2));
			rens.addEventNotificationStateParam(ens);

			// Report buffer fill warning
			ens = new EventNotificationState();
			ens.setEventType((byte) 3);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(3));
			rens.addEventNotificationStateParam(ens);

			// Reader Event Exception
			ens = new EventNotificationState();
			ens.setEventType((byte) 4);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(4));
			rens.addEventNotificationStateParam(ens);

			// RFSurvey Event
			ens = new EventNotificationState();
			ens.setEventType((byte) 5);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(5));
			rens.addEventNotificationStateParam(ens);

			// AISpec event (end)
			ens = new EventNotificationState();
			ens.setEventType((byte) 6);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(6));
			rens.addEventNotificationStateParam(ens);

			// AISpec event (end) with singulation details
			ens = new EventNotificationState();
			ens.setEventType((byte) 7);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(7));
			rens.addEventNotificationStateParam(ens);

			// Antenna Event
			ens = new EventNotificationState();
			ens.setEventType((byte) 8);
			ens.setNotificationState(props.eventNotificaionTable
					.getEventNotificaiton(8));
			rens.addEventNotificationStateParam(ens);

			grcr.setReaderEventNotificationSpecParam(rens);
			logger.debug("Got Event config");

		}

		// AccessReportSpec
		if ((data == 0 || data == 6) && llrpstatus.getErrorCode() == 0) {
			AccessReportSpec ars = new AccessReportSpec();

			ars.setAccessReportTrigger((byte) props.accessReportTrigger);
			grcr.setAccessReportSpecParam(ars);

			logger.debug("Got Access Report Config");

		}

		// LLRPConfigurationStateValue
		if ((data == 0 || data == 7) && llrpstatus.getErrorCode() == 0) {
			LLRPConfigurationStateValue lcsv = new LLRPConfigurationStateValue();

			lcsv
					.setLLRPConfigurationStateValue(props.LLRPConfiguraitonStateVariable);
			grcr.setLLRPConfigurationStateValueParam(lcsv);

			logger.debug("Got Configuration State Value");

		}

		// keepaliveSpec
		if ((data == 0 || data == 8) && llrpstatus.getErrorCode() == 0) {
			KeepaliveSpec kas = new KeepaliveSpec();

			kas.setKeepaliveTriggerType((byte) props.keepAliveTriggerType);
			if (props.keepAliveTriggerType == 1) {
				kas.setTimeInterval(props.keepAlivePeriodicTriggerValue);
			}
			grcr.setKeepaliveSpecParam(kas);

			logger.debug("got keep alive spec");

		}

		// GPIPortCurrentState
		if ((data == 0 || data == 9) && llrpstatus.getErrorCode() == 0) {

			int numGPIs = props.numGPIs();

			if (numGPIs > 0) {
				if (grc.getGPIPortNum() == (short) 0) {
					for (short i = 1; i <= (short) numGPIs; i++)
						grcr
								.addGPIPortCurrentStateParam(getGPIPortCurrentState(
										i, props));
				} else {
					grcr.addGPIPortCurrentStateParam(getGPIPortCurrentState(grc
							.getGPIPortNum(), props));
				}
				logger.debug("Got GPI config");

			}
			// do not send an error message if all fields are requested
			else if (numGPIs < 1 && data == 9) {
				/* Send an error Message */
				ErrorMessage em = new ErrorMessage();
				LLRPStatus stat = new LLRPStatus();
				stat.setErrorCode((short) 100);
				stat.setErrorDescription("GPI is not supported");
				em.setLLRPStatusParam(stat);
				arg.addArgument(em);
				return arg;

			}

		}

		// GPOWriteData
		if ((data == 0 || data == 10) && llrpstatus.getErrorCode() == 0) {

			int numGPOs = props.numGPOs();

			if (numGPOs > 0) {
				if (grc.getGPOPortNum() == (short) 0) {
					for (short i = 1; i <= (short) numGPOs; i++)
						grcr.addGPOWriteDataParam(getGPOWriteData(i, props));
				} else {
					grcr.addGPOWriteDataParam(getGPOWriteData(grc
							.getGPOPortNum(), props));
				}

				logger.debug("Got GPO config");

			}
			// do not send an error message if all fields are requested
			else if (numGPOs < 1 && data == 10) {
				/* Send an error Message */
				ErrorMessage em = new ErrorMessage();
				LLRPStatus stat = new LLRPStatus();
				stat.setErrorCode((short) 100);
				stat.setErrorDescription("GPO is not supported");
				em.setLLRPStatusParam(stat);
				arg.addArgument(em);
				return arg;
			}
		}

		// EventsAndReports
		if ((data == 0 || data == 11) && llrpstatus.getErrorCode() == 0) {

			EventsAndReports ear = new EventsAndReports();
			ear
					.setHoldEventsAndReportsUponReconnect(props.holdEventsAndReportsUponReconnected);
			grcr.setEventsAndReportsParam(ear);
			logger.debug("Got events and reports config");

		}

		grcr.setMessageID(grc.getMessageID());
		grcr.setLLRPStatusParam(llrpstatus);
		arg.getReturnValue().add(grcr);
		return arg;

	}

	public CommandObject setReaderConfig(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;
		Properties props = llrpsr.getProperties();

		LLRPStatus status = new LLRPStatus();
		status.setErrorCode((short) 0);
		status.setErrorDescription("Sucess");
		SetReaderConfigResponse srcr = new SetReaderConfigResponse();

		// decode the message
		Message m = null;
		byte[] rawMsg = (byte[]) arg.getArguments().get(0);
		try {
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		} catch (IOException e) {
			status.setErrorCode((short) 101);
			status.setErrorDescription("Malformed SET_READER_CONFIG Message");
			logger
					.error("error when deserializing the SET_READER_CONFIG Message");
			logger.error(e.getMessage());
			srcr.setLLRPStatusParam(status);
			arg.getReturnValue().add(srcr);
			return arg;
		}

		SetReaderConfig src = (SetReaderConfig) m;

		// CustomExtensions are not supported yet return immediately
		if (src.getNumCustomParams() > 0 && status.getErrorCode() == 0) {
			status.setErrorCode((short) 100);
			status.setErrorDescription("unknown custom extension");
		}

		if (src.getResetToFactoryDefaults() && status.getErrorCode() == 0) {
			LLRPReaderModule.resetToFactorySetting(llrpsr);
		}

		// Reader Event Notification
		if (src.getReaderEventNotificationSpecParam() != null
				&& status.getErrorCode() == 0) {

			edu.uark.csce.llrp.ReaderEventNotificationSpec eventTable = src
					.getReaderEventNotificationSpecParam();

			int maxevents = eventTable.getNumEventNotificationStateParams();
			for (int i = 0; i < maxevents; i++) {
				edu.uark.csce.llrp.EventNotificationState event = eventTable
						.getEventNotificationStateParam(i);
				props.eventNotificaionTable.setEventNotificaiton(event
						.getEventType(), event.getNotificationState());
			}
			logger.debug("set reader event notification");
		}

		// Antenna Properties Parameter
		if (src.getNumAntennaPropertiesParams() != 0
				&& status.getErrorCode() == 0) {
			for (int i = 0; i < src.getNumAntennaPropertiesParams(); i++) {
				edu.uark.csce.llrp.AntennaProperties ap = src
						.getAntennaPropertiesParam(i);

				AntennaSettings as = props.antennaList.getAntenna(ap
						.getAntennaId());
				if (as != null) {
					as.setAntennaGain(ap.getAntennaGain());
					as.setAntennaConnected(ap.getAntennaConnected());
				} else {
					status.setErrorCode((short) 100);
					status
							.setErrorDescription("Tried to set Antenna Property for an "
									+ "antenna that does not exist.  Maximum number of "
									+ "antennas: "
									+ props.maxAntennasSupported());
				}
			}
			logger.debug("set antenna properties");
		}

		// Antenna Configuration Parameter
		if (src.getNumAntennaConfigurationParams() != 0
				&& status.getErrorCode() == 0) {
			for (int i = 0; i < src.getNumAntennaConfigurationParams(); i++) {
				edu.uark.csce.llrp.AntennaConfiguration ac = src
						.getAntennaConfigurationParam(i);
				AntennaSettings as = props.antennaList.getAntenna(ac
						.getAntennaId());
				if (as != null) {
					/*
					 * If any air protocols exist, clear the current ones,
					 * because we will rebuild the list
					 */
					if (ac.getNumAirProtocolInventoryCommandSettingsParams() != 0) {
						as.clearAirProtoSettingList();
					}

					/*
					 * Step through each air protocol and add each one to this
					 * antenna's settings
					 */
					for (int j = 0; j < ac
							.getNumAirProtocolInventoryCommandSettingsParams(); j++) {

						edu.uark.csce.llrp.AirProtocolInventoryCommandSettings apics = ac
								.getAirProtocolInventoryCommandSettingsParam(j);

						// If air protocol is C1G2
						if (apics instanceof edu.uark.csce.llrp.C1G2InventoryCommand) {

							as.addAirProtocolSupported(AirProtocolEnums.C1G2);

							edu.uark.csce.llrp.C1G2InventoryCommand c1g2ic = (edu.uark.csce.llrp.C1G2InventoryCommand) apics;
							C1G2InventoryCommand newC1G2IC = new C1G2InventoryCommand();

							newC1G2IC.tagInventoryStateAware = c1g2ic
									.getTagInventoryStateAware();

							/*
							 * Step through each filter
							 */
							for (int k = 0; k < c1g2ic.getNumC1G2FilterParams(); k++) {
								edu.uark.csce.llrp.C1G2Filter c1g2filter = c1g2ic
										.getC1G2FilterParam(k);
								C1G2Filter newC1G2Filter = new C1G2Filter();

								C1G2InventoryMask newTagInvMask = new C1G2InventoryMask();

								newC1G2Filter.T = c1g2filter.getT();

								// set up tag inventory mask
								newTagInvMask.tagMask = c1g2filter
										.getC1G2TagInventoryMaskParam()
										.getMask();
								newTagInvMask.length = c1g2filter
										.getC1G2TagInventoryMaskParam()
										.getMaskBitCount();
								newTagInvMask.Mb = c1g2filter
										.getC1G2TagInventoryMaskParam().getMB();
								newTagInvMask.pointer = c1g2filter
										.getC1G2TagInventoryMaskParam()
										.getPointer();
								newC1G2Filter.c1g2IM = newTagInvMask;

								// set inventory state aware or unaware action
								if (c1g2ic.getTagInventoryStateAware()) {
									C1G2TagInventoryStateAwareFilterAction stateAwareFilterAction = new C1G2TagInventoryStateAwareFilterAction();

									stateAwareFilterAction.action = c1g2filter
											.getC1G2TagInventoryStateAwareFilterActionParam()
											.getAction();

									stateAwareFilterAction.target = c1g2filter
											.getC1G2TagInventoryStateAwareFilterActionParam()
											.getTarget();

									newC1G2Filter.tagInventoryStateAware = stateAwareFilterAction;

								} else {
									C1G2TagInventoryStateUnawareFilterAction stateUnawareFilterAction = new C1G2TagInventoryStateUnawareFilterAction();

									stateUnawareFilterAction.action = c1g2filter
											.getC1G2TagInventoryStateUnawareFilterActionParam()
											.getAction();

									newC1G2Filter.tagInventoryStateUnaware = stateUnawareFilterAction;
								}
								newC1G2IC.addC1G2Filter(newC1G2Filter);
							}

							// process C1G2RFControl
							C1G2RFControl newRFC = new C1G2RFControl();
							newRFC.modeIndex = c1g2ic.getC1G2RFControlParam()
									.getModeIndex();
							newRFC.tari = c1g2ic.getC1G2RFControlParam()
									.getTari();
							newC1G2IC.c1g2RFC = newRFC;

							// Set up Singulation control
							C1G2SingulationControl newSingCon = new C1G2SingulationControl();
							newSingCon.session = c1g2ic
									.getC1G2SingulationControlParam().getS();

							newSingCon.tagPopulation = c1g2ic
									.getC1G2SingulationControlParam()
									.getTagPopulation();

							newSingCon.tagTransitTime = c1g2ic
									.getC1G2SingulationControlParam()
									.getTagTransitTime();

							C1G2TagInventoryStateAwareSingulationAction newSingAct = new C1G2TagInventoryStateAwareSingulationAction();

							newSingAct.i = Properties
									.booleanToInt(c1g2ic
											.getC1G2SingulationControlParam()
											.getC1G2TagInventoryStateAwareSingulationActionParam()
											.getI());

							newSingAct.s = Properties
									.booleanToInt(c1g2ic
											.getC1G2SingulationControlParam()
											.getC1G2TagInventoryStateAwareSingulationActionParam()
											.getS());

							newSingCon.tagInvetorSatateAwareSingulationAction = newSingAct;

							newC1G2IC.c1g2SingCon = newSingCon;
						} else {
							as
									.addAirProtocolSupported(AirProtocolEnums.Unspecified);
						}
					}
					as.setRecieverSensitivityTableIndex(ac.getRFReceiverParam()
							.getReceiverSensitivity());

					as.setChannelIndex(ac.getRFTransmitterParam()
							.getChannelIndex());
					as
							.setHopTableID(ac.getRFTransmitterParam()
									.getHopTableId());
					/**
					 * TODO: not sure if this is right
					 */
					as.setTransmitPowerTableIndex(ac.getRFTransmitterParam()
							.getTransmitPower());
				} else {
					status.setErrorCode((short) 100);
					status
							.setErrorDescription("Tried to set Antenna Configuration for an "
									+ "antenna that does not exist.  Maximum number of "
									+ "antennas: "
									+ props.maxAntennasSupported());
				}
			}

			logger.debug("set antenna config");
		}

		// ROSpec Report
		if (src.getROReportSpecParam() != null && status.getErrorCode() == 0) {
			ROReportSpec rrs = src.getROReportSpecParam();
			props.roReportFormat_Global.reportTrigger = rrs
					.getROReportTrigger();

			if (rrs.getROReportTrigger() == 1 || rrs.getROReportTrigger() == 2) {
				props.roReportFormat_Global.N = rrs.getN();
			}

			props.roReportFormat_Global.enableAccessSpecID = rrs
					.getTagReportContentSelectorParam().getEnableAccessSpecID();

			props.roReportFormat_Global.enableAntennaID = rrs
					.getTagReportContentSelectorParam().getEnableAntennaID();

			props.roReportFormat_Global.enableChannelIndex = rrs
					.getTagReportContentSelectorParam().getEnableChannelIndex();

			props.roReportFormat_Global.enableFirstSeenTimestamp = rrs
					.getTagReportContentSelectorParam()
					.getEnableFirstSeenTimestamp();

			props.roReportFormat_Global.enableInventoryParamaterSpecID = rrs
					.getTagReportContentSelectorParam()
					.getEnableInventoryParameterSpecID();

			props.roReportFormat_Global.enableLastSeenTimestamp = rrs
					.getTagReportContentSelectorParam()
					.getEnableLastSeenTimestamp();

			props.roReportFormat_Global.enablePeakRSSI = rrs
					.getTagReportContentSelectorParam().getEnablePeakRSSI();

			props.roReportFormat_Global.enableROSpecID = rrs
					.getTagReportContentSelectorParam().getEnableROSpecID();

			props.roReportFormat_Global.enableSpecIndex = rrs
					.getTagReportContentSelectorParam().getEnableSpecIndex();

			props.roReportFormat_Global.enableTagSeenCount = rrs
					.getTagReportContentSelectorParam().getEnableTagSeenCount();

			try{
				AirProtocolSpecificEPCMemorySelector memSel = rrs
						.getTagReportContentSelectorParam()
						.getAirProtocolSpecificEPCMemorySelectorParam(0);

				if (memSel instanceof C1G2EPCMemorySelector) {
					C1G2EPCMemorySelector c1g2memSel = (C1G2EPCMemorySelector) memSel;
					props.roReportFormat_Global.enableCRC = c1g2memSel
							.getEnableCRC();
					props.roReportFormat_Global.enablePC = c1g2memSel
							.getEnablePCBits();
				
			}
			}catch(IndexOutOfBoundsException e){
				status.setErrorCode((short) 100);
				status.setErrorDescription("No Air Protocol Specific EPC "
						+ "Memory Selector"
						+ " Parameter in RoReportSpec parameter");
			}
				
			logger.debug("set ro report spec");
		}

		// accessReportSpec
		if (src.getAccessReportSpecParam() != null
				&& status.getErrorCode() == 0) {
			props.accessReportTrigger = src.getAccessReportSpecParam()
					.getAccessReportTrigger();
			logger.debug("set access report spec");
		}

		// keepAliveSpec
		if (src.getKeepaliveSpecParam() != null && status.getErrorCode() == 0) {
			props.keepAliveTriggerType = src.getKeepaliveSpecParam()
					.getKeepaliveTriggerType();

			if (src.getKeepaliveSpecParam().getKeepaliveTriggerType() == 1) {
				int period = src.getKeepaliveSpecParam().getTimeInterval();
				props.keepAlivePeriodicTriggerValue = period;
				llrpsr.getKeepAliveController().startKeepAlives(period);
			} else if (src.getKeepaliveSpecParam().getKeepaliveTriggerType() == 0) {
				llrpsr.getKeepAliveController().stopKeepAlives();
			} else {
				status.setErrorCode((short) 101);
				status.setErrorDescription("Invalid Keepalive Trigger type"
						+ src.getKeepaliveSpecParam().getKeepaliveTriggerType()
						+ ". should be a 0 or 1.");
			}

			logger.debug("set keep alive spec");
		}

		// gpo spec
		if (src.getNumGPOWriteDataParams() > 0 && status.getErrorCode() == 0) {
			for (int i = 0; i < src.getNumGPOWriteDataParams(); i++) {
				GPOWriteData gpowd = src.getGPOWriteDataParam(i);
				boolean success = false;
				int gpoPortNum = gpowd.getGPOPortNumber();
				if (llrpsr.getGpioController().GPOPortExists(gpoPortNum - 1)) {
					if (gpowd.getGPOData()) {
						llrpsr.getGpioController().setGPOHight(gpoPortNum - 1);
					} else {
						llrpsr.getGpioController().setGPOLow(gpoPortNum - 1);
					}

				} else {
					logger.error("Inexed out of bounds when "
							+ "setting gpo information");
					status.setErrorCode((short) 100);
					status.setErrorDescription("Tried to set gpo "
							+ "information for "
							+ "a gpo port that does not exist.  "
							+ "Number of GPO ports = " + props.numGPOs());
				}

			}

			logger.debug("set gpo spec");
		}

		// gpi spec
		if (src.getNumGPIPortCurrentStateParams() > 0
				&& status.getErrorCode() == 0) {
			for (int i = 0; i < src.getNumGPIPortCurrentStateParams(); i++) {
				GPIPortCurrentState gpics = src.getGPIPortCurrentStateParam(i);
				GPISettings gpi = props.getGPISettingAt(gpics.getGPIPortNum());
				if (gpi != null) {
					gpi.setGPIConfig(gpics.getGPIConfig());
				} else {
					logger.error("Inexed out of bounds when setting "
							+ "gpi information");
					status.setErrorCode((short) 100);
					status.setErrorDescription("Tried to set gpi "
							+ "information for "
							+ "a gpi port that does not exist.  "
							+ "Number of GPI ports = " + props.numGPIs());
				}

			}

			logger.debug("set gpi spec");
		}

		if (src.getEventsAndReportsParam() != null
				&& status.getErrorCode() == 0) {
			props.holdEventsAndReportsUponReconnected = src
					.getEventsAndReportsParam()
					.getHoldEventsAndReportsUponReconnect();

			logger.debug("set events and reports");
		}

		if (status.getErrorCode() == 0) {
			// Change ConfigurationStateVariable

			llrpsr.getProperties().LLRPConfiguraitonStateVariable++;
		}

		srcr.setMessageID(src.getMessageID());
		srcr.setLLRPStatusParam(status);
		arg.getReturnValue().add(srcr);
		return arg;
	}

	private AntennaProperties getAntennaProp(short antennaID, Properties props) {
		AntennaProperties ap = new AntennaProperties();
		AntennaSettings as = props.antennaList.getAntenna(antennaID);

		ap.setAntennaConnected(as.isAntennaConnected());

		ap.setAntennaGain(as.getAntennaGain());

		ap.setAntennaId(antennaID);
		return ap;
	}

	private AntennaConfiguration getAntennaConfig(short antennaID,
			Properties props) {
		AntennaConfiguration ac = new AntennaConfiguration();
		AntennaSettings as = props.antennaList.getAntenna(antennaID);

		ac.setAntennaId(antennaID);

		RFReceiver rfr = new RFReceiver();
		rfr.setReceiverSensitivity(as.getRecieverSensitivityTableIndex());
		ac.setRFReceiverParam(rfr);

		RFTransmitter rft = new RFTransmitter();
		rft.setTransmitPower(as.getTransmitPowerTableIndex());
		rft.setHopTableId(as.getHopTableID());
		rft.setChannelIndex(as.getChannelIndex());
		ac.setRFTransmitterParam(rft);

		logger.debug("Set rfreciever and rftransmitter props");

		for (int i = 0; i < as.getAirProtoSettingsList().size(); i++) {
			logger.debug("here");
			if (as.getAirProtoSettingsList().get(i) instanceof C1G2InventoryCommand) {

				logger.debug("Doing a c1g2 IC");

				edu.uark.csce.llrp.C1G2InventoryCommand c1g2ic = new edu.uark.csce.llrp.C1G2InventoryCommand();
				C1G2InventoryCommand _c1g2ic = (C1G2InventoryCommand) as
						.getAirProtoSettingsList().get(i);

				c1g2ic
						.setTagInventoryStateAware(_c1g2ic.tagInventoryStateAware);

				// Add Filters
				for (int j = 0; j < _c1g2ic.getC1G2FilterListSize(); j++) {

					logger.debug("doing a filter...");

					C1G2Filter _c1g2f = _c1g2ic.getC1G2FilterAt(j);
					edu.uark.csce.llrp.C1G2Filter c1g2f = new edu.uark.csce.llrp.C1G2Filter();

					c1g2f.setT(_c1g2f.T);
					if (_c1g2ic.tagInventoryStateAware) {
						edu.uark.csce.llrp.C1G2TagInventoryStateAwareFilterAction c1g2tisafa = new edu.uark.csce.llrp.C1G2TagInventoryStateAwareFilterAction();
						c1g2tisafa
								.setTarget((byte) _c1g2f.tagInventoryStateAware.target);
						c1g2tisafa
								.setAction((byte) _c1g2f.tagInventoryStateAware.action);
						c1g2f
								.setC1G2TagInventoryStateAwareFilterActionParam(c1g2tisafa);

					} else {
						edu.uark.csce.llrp.C1G2TagInventoryStateUnawareFilterAction c1g2atisufa = new edu.uark.csce.llrp.C1G2TagInventoryStateUnawareFilterAction();
						c1g2atisufa
								.setAction((byte) _c1g2f.tagInventoryStateUnaware.action);
						c1g2f
								.setC1G2TagInventoryStateUnawareFilterActionParam(c1g2atisufa);
					}

					C1G2TagInventoryMask c1g2tim = new C1G2TagInventoryMask();
					c1g2tim.setMB(_c1g2f.c1g2IM.Mb);
					c1g2tim.setPointer(_c1g2f.c1g2IM.pointer);
					c1g2tim.setMask(_c1g2f.c1g2IM.tagMask);
					c1g2f.setC1G2TagInventoryMaskParam(c1g2tim);
					c1g2ic.addC1G2FilterParam(c1g2f);
				}

				edu.uark.csce.llrp.C1G2RFControl c1g2rf = new edu.uark.csce.llrp.C1G2RFControl();
				c1g2rf.setModeIndex((short) _c1g2ic.c1g2RFC.modeIndex);
				c1g2rf.setTari((short) _c1g2ic.c1g2RFC.tari);
				c1g2ic.setC1G2RFControlParam(c1g2rf);

				edu.uark.csce.llrp.C1G2SingulationControl c1g2singCon = new edu.uark.csce.llrp.C1G2SingulationControl();
				c1g2singCon.setS(_c1g2ic.c1g2SingCon.session);
				c1g2singCon.setTagPopulation(_c1g2ic.c1g2SingCon.tagPopulation);
				c1g2singCon
						.setTagTransitTime(_c1g2ic.c1g2SingCon.tagTransitTime);

				if (props.canDoTagInventoryStateAwareSingulation) {
					edu.uark.csce.llrp.C1G2TagInventoryStateAwareSingulationAction c1g2isasa = new edu.uark.csce.llrp.C1G2TagInventoryStateAwareSingulationAction();
					c1g2isasa.setI(false);
					c1g2isasa.setS(false);
					c1g2singCon
							.setC1G2TagInventoryStateAwareSingulationActionParam(c1g2isasa);
				}
				c1g2ic.setC1G2SingulationControlParam(c1g2singCon);

				ac.addAirProtocolInventoryCommandSettingsParam(c1g2ic);
			}
		}

		return ac;

	}

	private GPIPortCurrentState getGPIPortCurrentState(short GPIPort,
			Properties props) {
		GPIPortCurrentState gpipcs = new GPIPortCurrentState();
		GPISettings gpisettings = props.getGPISettingAt(GPIPort);
		gpipcs.setGPIPortNum(GPIPort);

		gpipcs.setGPIConfig(gpisettings.getGPIConfig());
		gpipcs.setGPIState((byte) gpisettings.getGPIState());
		return gpipcs;
	}

	private GPOWriteData getGPOWriteData(short GPOPort, Properties props) {
		GPOWriteData gpowd = new GPOWriteData();
		GPOSettings gposettings = props.getGPOSettingAt(GPOPort);
		gpowd.setGPOPortNumber(GPOPort);

		gpowd.setGPOData(gposettings.getGPOData());
		return gpowd;
	}
}
