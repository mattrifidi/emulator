/**
 * 
 */
package org.rifidi.emulator.reader.llrp.commandhandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.command.CommandObject;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolLLRPCapabilities;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._C1G2LLRPCapabilities;
import org.rifidi.emulator.reader.llrp.airprotocol.c1g2._UHFC1G2RFModeTable;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.properties.AntennaSettings;
import org.rifidi.emulator.reader.llrp.properties.Properties;
import org.rifidi.emulator.reader.module.abstract_.AbstractReaderSharedResources;

import edu.uark.csce.llrp.FixedFrequencyTable;
import edu.uark.csce.llrp.FrequencyHopTable;
import edu.uark.csce.llrp.FrequencyInformation;
import edu.uark.csce.llrp.GPIOCapabilities;
import edu.uark.csce.llrp.GeneralDeviceCapabilities;
import edu.uark.csce.llrp.GetReaderCapabilities;
import edu.uark.csce.llrp.GetReaderCapabilitiesResponse;
import edu.uark.csce.llrp.LLRPCapabilities;
import edu.uark.csce.llrp.LLRPStatus;
import edu.uark.csce.llrp.Message;
import edu.uark.csce.llrp.PerAntennaAirProtocol;
import edu.uark.csce.llrp.ReceiveSensitivityTableEntry;
import edu.uark.csce.llrp.RegulatoryCapabilities;
import edu.uark.csce.llrp.TransmitPowerLevelTableEntry;
import edu.uark.csce.llrp.UHFBandCapabilities;
import edu.uark.csce.llrp.UHFC1G2RFModeTable;

/**
 * @author kyle
 * 
 */
public class LLRPReaderDeviceCapabilities {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory
			.getLog(LLRPReaderDeviceCapabilities.class);

	public CommandObject getReaderCapabilities(CommandObject arg,
			AbstractReaderSharedResources asr) {

		LLRPReaderSharedResources llrpsr = (LLRPReaderSharedResources) asr;
		
		Properties props = llrpsr.getProperties();
		
		LLRPStatus stat = new LLRPStatus();
		stat.setErrorCode((short) 0);
		stat.setErrorDescription("Success");
		
		GetReaderCapabilitiesResponse grcr = new GetReaderCapabilitiesResponse();
		
		//decode the message
		Message m = null;
		byte[] rawMsg = (byte[])arg.getArguments().get(0);
		try{
			m = Message.receive(new ByteArrayInputStream(rawMsg));
		}catch(IOException e){
			stat.setErrorCode((short)101);
			stat.setErrorDescription("Malformed GET_READER_CAPABILITIES Message");
			logger.error("error when deserializing the GET_READER_CAPABILITIES");
			grcr.setLLRPStatusParam(stat);
			arg.getReturnValue().add(grcr);
			return arg;
		}
		
		GetReaderCapabilities grc = (GetReaderCapabilities) m;

		byte req = grc.getRequestedData();

		// CustomExtensions are not supported
		if (grc.getNumCustomParams() > 0 && stat.getErrorCode()==0) {
			stat.setErrorCode((short) 100);
			stat.setErrorDescription("unknown custom extension");
		}

		// General Device Capabilities
		if ((req == 0 || req == 1) && stat.getErrorCode() == 0) {

			GeneralDeviceCapabilities gdc = new GeneralDeviceCapabilities();
			gdc.setDeviceManufacturerName(props.deviceManufacturername);
			gdc.setModelName(props.modelName);
			gdc.setReaderFirmwareVersion(props.firmwareVersion);
			gdc.setMaxNumberOfAntennaSupported(props.maxAntennasSupported());

			for (int i = 0; i < props.receiveSensitiviyTable.getTableSize(); i++) {
				ReceiveSensitivityTableEntry rste = new ReceiveSensitivityTableEntry();
				rste
						.setReceiveSensitivityValue((short) props.receiveSensitiviyTable
								.getReceiveSensitivityValue(i));
				gdc.addReceiveSensitivityTableEntryParam(rste);
			}

			for (int i = 0; i < props.maxAntennasSupported(); i++) {
				PerAntennaAirProtocol paap = new PerAntennaAirProtocol();
				AntennaSettings as = props.antennaList.getAntenna(i + 1);
				paap.setAntennaID(as.getAntennaID());
				for (int j = 0; j < as.sizeOfAirProtoList(); j++) {
					paap.addProtocolElement((as.getAirProtocolsSupportedAt(j)));
				}

				gdc.addPerAntennaAirProtocolParam(paap);
			}

			GPIOCapabilities gpioc = new GPIOCapabilities();
			gpioc.setNumGPIs(props.numGPIs());
			gpioc.setNumGPOs(props.numGPOs());
			gdc.setGPIOCapabilitiesParam(gpioc);

			grcr.setGeneralDeviceCapabilitiesParam(gdc);

		}

		// LLRP Capabilities
		if ((req == 0 || req == 2)&& stat.getErrorCode()==0) {
			LLRPCapabilities llrpc = new LLRPCapabilities();
			llrpc.setCanDoRFSurvey(props.canDoRFSurvey);
			llrpc
					.setCanDoTagInventoryStateAwareSingulation(props.canDoTagInventoryStateAwareSingulation);
			llrpc
					.setCanReportBufferFillWarning(props.canReportBufferFilledWarnings);
			llrpc.setMaxNumROSpecs(props.maxNumROSpecs);
			llrpc.setMaxNumSpecsPerROSpec(props.maxNumSpecsPerROSpec);
			llrpc
					.setMaxPriorityLevelSupported((byte) props.maxPriorityLevelSupported);
			llrpc.setMaxNumAccessSpecs(props.maxNumAccessSpecs);
			llrpc
					.setMaxNumOpSpecsPerAccessSpec(props.maxNumOpSpecsPerAccessSpec);

			llrpc
					.setSupportsClientRequestOpSpec(props.supportsClientRequestOpSpec);
			if (props.supportsClientRequestOpSpec) {
				llrpc.setClientRequestOpSpecTimeout(props.clientOpSpecTimeout);
			}

			llrpc
					.setSupportsEventAndReportHolding(props.supportsEventAndReportHolding);
			grcr.setLLRPCapabilitiesParam(llrpc);
		}
		if ((req == 0 || req == 3)&& stat.getErrorCode()==0) {
			RegulatoryCapabilities rc = new RegulatoryCapabilities();
			rc.setCountryCode(props.countryCode);
			rc.setCommunicationsStandard(props.communicationStandard);
			UHFBandCapabilities uhfbc = new UHFBandCapabilities();

			for (int i = 0; i < props.transmitPowerTable.getTableSize(); i++) {
				TransmitPowerLevelTableEntry tptte = new TransmitPowerLevelTableEntry();
				tptte.setIndex((short) i);
				tptte.setTransmitPowerValue((short) props.transmitPowerTable
						.getPowerValue(0));
				uhfbc.addTransmitPowerLevelTableEntryParam(tptte);
			}
			for (int i = 0; i < props.listOfUHFTable.size(); i++) {
				if (props.listOfUHFTable.get(i) instanceof _UHFC1G2RFModeTable) {
					_UHFC1G2RFModeTable C1G2table = (_UHFC1G2RFModeTable) props.listOfUHFTable
							.get(i);
					for (int j = 0; j < C1G2table.getTableSize(); j++) {
						edu.uark.csce.llrp.UHFC1G2RFModeTableEntry entry = new edu.uark.csce.llrp.UHFC1G2RFModeTableEntry();

						entry.setBDRValue(C1G2table.getBDRValue(j));
						entry.setConformance(C1G2table
								.getEPCHagTCConformance(j));
						/**
						 * TODO: fix once DRValue is changed to int
						 */
						entry.setDRValue(false);
						/**
						 * TODOL fix once FLM is changed to int
						 */
						entry.setFLM((byte) C1G2table
								.getForwardLinkModulation(j));
						entry.setM((byte) C1G2table.getMValue(j));
						entry.setMaxTariValue(C1G2table.getMaxTariValue(j));
						entry.setMinTariValue(C1G2table.getMinTariValue(j));
						entry.setModeID((byte) C1G2table.getModeIdentifier(j));
						entry.setPIEValue(C1G2table.getPIEValue(j));
						entry.setStepTariValue(C1G2table.getStepTariValue(j));
						UHFC1G2RFModeTable mt = new UHFC1G2RFModeTable();
						mt.addUHFC1G2RFModeTableEntryParam(entry);
						uhfbc.setUHFC1G2RFModeTableParam(mt);

					}
				}
			}

			FrequencyInformation fi = new FrequencyInformation();
			fi.setHopping(props.hopping);
			if (props.hopping) {
				for (int i = 0; i < props.frequencyHopTable.getTableSize(); i++) {
					FrequencyHopTable fht = new FrequencyHopTable();
					fht.setHopTableId((byte) props.frequencyHopTable
							.gettableID(i));
					for (Integer j : props.frequencyHopTable
							.getFrequencyList(i)) {
						fht.addHopFrequencyElement(j);
					}
					fi.addFrequencyHopTableParam(fht);
				}

			} else {
				FixedFrequencyTable fft = new FixedFrequencyTable();
				for (int i = 0; i < props.fixedFrequencyTable.getTableSize(); i++) {
					fft.addFrequencyElement(props.fixedFrequencyTable
							.getPowerValue(i));
				}
				fi.setFixedFrequencyTableParam(fft);
			}
			uhfbc.setFrequencyInformationParam(fi);
			rc.setUHFBandCapabilitiesParam(uhfbc);
			grcr.setRegulatoryCapabilitiesParam(rc);
		}
		if ((req == 0 || req == 4)&& stat.getErrorCode()==0) {
			for (int i = 0; i < props.getSizeOfAirProtocolLLRPCapabilityList(); i++) {
				AirProtocolLLRPCapabilities a = props
						.getAirProtocolLLRPCapabilityAt(i);
				if (props.getAirProtocolLLRPCapabilityAt(i) instanceof _C1G2LLRPCapabilities) {
					_C1G2LLRPCapabilities caps = (_C1G2LLRPCapabilities) props
							.getAirProtocolLLRPCapabilityAt(i);
					edu.uark.csce.llrp.C1G2LLRPCapabilities c1g2llrpc = new edu.uark.csce.llrp.C1G2LLRPCapabilities();
					c1g2llrpc
							.setCanSupportBlockErase(caps.canSupportBlockErase);
					c1g2llrpc
							.setCanSupportBlockWrite(caps.canSupportBlockWrite);
					c1g2llrpc
							.setMaxNumSelectFiltersPerQuery(caps.maxNumSelectFiltersPerQuery);
					grcr.setC1G2LLRPCapabilitiesParam(c1g2llrpc);
				}
			}

		}
		// Check the Requested Data Value
		if (req >= 5 || req < 0) // Valid values for this field are 0-4
		{
			stat.setErrorCode((short) 100);
			stat.setErrorDescription("Requested Data is: " + req
					+ ". Valid values for this field are 0-4");
		}

		grcr.setMessageID(grc.getMessageID());

		grcr.setLLRPStatusParam(stat);
		arg.getReturnValue().add(grcr);

		return arg;
	}
}
