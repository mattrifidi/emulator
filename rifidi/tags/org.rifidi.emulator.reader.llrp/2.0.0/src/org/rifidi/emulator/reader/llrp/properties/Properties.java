package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;

import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolLLRPCapabilities;
import org.rifidi.emulator.reader.llrp.airprotocol.UHF_RFModeTable;
import org.rifidi.emulator.reader.llrp.report.ROReportFormat;

public class Properties {

	/* CAPABILITY PROPERTIES */

	/* Antenna Properties & Configuration */
	public AntennaList antennaList;

	/* general capabilities */
	public int deviceManufacturername;

	public int modelName;

	public String firmwareVersion;

	public short maxAntennasSupported() {
		return (short) antennaList.getNumberAntennas();
	}

	public ReceiveSensitivityTable receiveSensitiviyTable;

	public boolean canSetAntennaProps;

	public short numGPIs() {
		return (short) gpiSettingsList.size();
	}

	public short numGPOs() {
		return (short) gpoSettingsList.size();
	}

	public boolean hasUTCClockCapabilities;

	/* llrp capabilities */

	public boolean canDoRFSurvey;

	public boolean canDoTagInventoryStateAwareSingulation;

	public boolean canReportBufferFilledWarnings;

	public int maxNumROSpecs;

	public int maxNumSpecsPerROSpec;

	public int maxNumInventoryParameterSpecsPerAISpec;

	public int maxPriorityLevelSupported;

	public int maxNumAccessSpecs;

	public int maxNumOpSpecsPerAccessSpec;

	public short clientOpSpecTimeout;

	public boolean supportsClientRequestOpSpec;

	public boolean supportsEventAndReportHolding;

	/* Air Protocol LLRP Capabilities */

	private ArrayList<AirProtocolLLRPCapabilities> apllrpCap;

	public void addAirProtocolLLRPCapability(
			AirProtocolLLRPCapabilities apllrpCap) {
		this.apllrpCap.add(apllrpCap);
	}

	public AirProtocolLLRPCapabilities getAirProtocolLLRPCapabilityAt(int index) {
		return apllrpCap.get(index);
	}

	public int getSizeOfAirProtocolLLRPCapabilityList() {
		return apllrpCap.size();
	}

	public AirProtocolLLRPCapabilities getAirProtocolLLRPCapability(
			AirProtocolEnums protocolType) {
		for (AirProtocolLLRPCapabilities apc : this.apllrpCap) {
			if (protocolType == apc.getAirProtocolType()) {
				return apc;
			}
		}
		return null;
	}

	/* Regulatory Capabilities */

	public short countryCode;

	public short communicationStandard;

	public boolean hopping;

	public TransmitPowerTable transmitPowerTable;

	public FrequencyHopTable frequencyHopTable;

	public FixedFrequencyTable fixedFrequencyTable;

	public ArrayList<UHF_RFModeTable> listOfUHFTable;

	/* CONFIGURATION PROPERTIES */

	/* Identification Parameter */
	public int IDType;

	public byte[] readerID;

	/* reader event notification */
	public EventNotificationTable eventNotificaionTable;

	/* RO Report Spec */
	public ROReportFormat roReportFormat_Global;

	/* Access Report Spec */
	public int accessReportTrigger;

	/* Keepalive Spec */
	public int keepAliveTriggerType;

	/**
	 * In miliseconds
	 */
	public int keepAlivePeriodicTriggerValue;

	/* Events and Reports */

	public boolean holdEventsAndReportsUponReconnected;

	/* LLRPConfiguration Variable */
	public int LLRPConfiguraitonStateVariable;

	/* GPIPortCurrentState */
	private ArrayList<GPISettings> gpiSettingsList;

	public GPISettings getGPISettingAt(int index) {
		if (index <= 0) {
			return null;
		} else if (index > numGPIs()) {
			return null;
		} else
			return gpiSettingsList.get(index - 1);
	}

	public void addGPISetting(GPISettings settings) {
		gpiSettingsList.add(settings);
	}

	/* GPOWriteData */
	private ArrayList<GPOSettings> gpoSettingsList;

	public GPOSettings getGPOSettingAt(int index) {
		if (index <= 0) {
			return null;
		} else if (index > numGPIs()) {
			return null;
		} else
			return gpoSettingsList.get(index - 1);
	}

	public void addGPOSetting(GPOSettings settings) {
		gpoSettingsList.add(settings);
	}

	public Properties() {
		apllrpCap = new ArrayList<AirProtocolLLRPCapabilities>();
		transmitPowerTable = new TransmitPowerTable();
		frequencyHopTable = new FrequencyHopTable();
		fixedFrequencyTable = new FixedFrequencyTable();
		listOfUHFTable = new ArrayList<UHF_RFModeTable>();
		eventNotificaionTable = new EventNotificationTable();
		antennaList = new AntennaList();
		roReportFormat_Global = new ROReportFormat();
		gpiSettingsList = new ArrayList<GPISettings>();
		gpoSettingsList = new ArrayList<GPOSettings>();
		receiveSensitiviyTable = new ReceiveSensitivityTable();
	}

	public static boolean shortToBool(short in) {
		if (in <= 0) {
			return false;
		} else
			return true;
	}

	public static int booleanToInt(boolean in) {
		if (in) {
			return 1;
		} else
			return 0;
	}

}
