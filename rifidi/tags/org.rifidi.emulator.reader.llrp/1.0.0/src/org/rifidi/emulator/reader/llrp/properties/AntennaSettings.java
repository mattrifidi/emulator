package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolInventoryCommandSettings;

/**
 * This class holds all the information for a single antenna (capabilities,
 * properties, and configuration).
 * 
 * @author kyle
 * 
 */
public class AntennaSettings {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AntennaSettings.class);

	/* CAPABILITIES */
	/**
	 * Antenna ID. Is a capability and cannot be set by the client. O is not
	 * legal; Indexing starts at 1
	 */
	private short AntennaID;

	/**
	 * The min index into the receive sensitivity table index. Is a capability
	 * and cannot be set by the client
	 */
	private int receiveSensitivityTableIndexMin;

	/**
	 * The max index into the receive sensitivity table index. Is a capability
	 * and cannot be set by the client
	 */
	private int receiveSensitivityTableIndexMax;

	/**
	 * A list of air protocols supported for this antenna Is a capability and
	 * cannot be set by the client
	 */
	private ArrayList<AirProtocolEnums> airProtocolsSupported;

	/* PROPERTIES */

	/**
	 * Gain of the antenna in dBI*100
	 */
	private short antennaGain;

	/**
	 * false is not connected, true is connected
	 */
	private boolean antennaConnected;

	/* CONFIGURATION */

	/**
	 * Index into the receiver sensitiviy table
	 */
	private short recieverSensitivityTableIndex;

	/**
	 * Index into the TransmitPowerTable
	 */
	private short transmitPowerTableIndex;

	/**
	 * Hop Table to use
	 */
	private short HopTableID;

	/**
	 * Index into the fixed frequency table
	 */
	private short channelIndex;

	/**
	 * List of air protocol specific Inventory command settings
	 */
	private ArrayList<AirProtocolInventoryCommandSettings> airProtoSettingsList;

	/**
	 * The constructor for this class
	 * 
	 * @param receiveSensitivityTableIndexMin
	 *            The minimum index into the recieve sensitivity table for this
	 *            antenna
	 * @param receiveSensitivityTableIndexMax
	 *            The maximum index into the recieve sensitivity table for this
	 *            antenna
	 * @param airProtocolsSupported
	 *            A list of air protocols supported by this antenna. See 7.1.4
	 */
	public AntennaSettings(int receiveSensitivityTableIndexMin,
			int receiveSensitivityTableIndexMax,
			ArrayList<AirProtocolEnums> airProtocolsSupported) {
		this.receiveSensitivityTableIndexMin = receiveSensitivityTableIndexMin;
		this.receiveSensitivityTableIndexMax = receiveSensitivityTableIndexMax;
		this.airProtocolsSupported = airProtocolsSupported;
		this.airProtoSettingsList = new ArrayList<AirProtocolInventoryCommandSettings>();
	}

	protected void setAntennaID(short antennaID) {
		if (antennaID <= 0) {
			logger.error("Invalid AntennaID.  Antenna ID must be > 0");
		} else {
			this.AntennaID = antennaID;
		}
	}

	/**
	 * @return the airProtocolsSupported
	 */
	public byte getAirProtocolsSupportedAt(int i) {
		AirProtocolEnums ap = this.airProtocolsSupported.get(i);
		if(ap == AirProtocolEnums.C1G2){
			return (byte)1;
		}else return (byte)0;
	}
	
	public int sizeOfAirProtoList(){
		return this.airProtocolsSupported.size();
	}

	/**
	 * @return the antennaID
	 */
	public short getAntennaID() {
		return AntennaID;
	}

	/**
	 * @return the receiveSensitivityTableIndexMax
	 */
	public int getReceiveSensitivityTableIndexMax() {
		return receiveSensitivityTableIndexMax;
	}

	/**
	 * @return the receiveSensitivityTableIndexMin
	 */
	public int getReceiveSensitivityTableIndexMin() {
		return receiveSensitivityTableIndexMin;
	}

	/**
	 * @return the antennaConnected
	 */
	public boolean isAntennaConnected() {
		return antennaConnected;
	}

	/**
	 * @param antennaConnected
	 *            the antennaConnected to set
	 */
	public void setAntennaConnected(boolean antennaConnected) {
		this.antennaConnected = antennaConnected;
	}

	/**
	 * @return the antennaGain
	 */
	public short getAntennaGain() {
		return antennaGain;
	}

	/**
	 * @param antennaGain
	 *            the antennaGain to set
	 */
	public void setAntennaGain(short antennaGain) {
		this.antennaGain = antennaGain;
	}

	/**
	 * @return the channelIndex
	 */
	public short getChannelIndex() {
		return channelIndex;
	}

	/**
	 * @param channelIndex
	 *            the channelIndex to set
	 */
	public void setChannelIndex(short channelIndex) {
		this.channelIndex = channelIndex;
	}

	/**
	 * @return the hopTableID
	 */
	public short getHopTableID() {
		return HopTableID;
	}

	/**
	 * @param hopTableID
	 *            the hopTableID to set
	 */
	public void setHopTableID(short hopTableID) {
		HopTableID = hopTableID;
	}

	/**
	 * @return the recieverSensitivityTableIndex
	 */
	public short getRecieverSensitivityTableIndex() {
		return recieverSensitivityTableIndex;
	}

	/**
	 * @param recieverSensitivityTableIndex
	 *            the recieverSensitivityTableIndex to set
	 */
	public void setRecieverSensitivityTableIndex(
			short recieverSensitivityTableIndex) {
		this.recieverSensitivityTableIndex = recieverSensitivityTableIndex;
	}

	/**
	 * @return the transmitPowerTableIndex
	 */
	public short getTransmitPowerTableIndex() {
		return transmitPowerTableIndex;
	}

	/**
	 * @param transmitPowerTableIndex
	 *            the transmitPowerTableIndex to set
	 */
	public void setTransmitPowerTableIndex(short transmitPowerTableIndex) {
		this.transmitPowerTableIndex = transmitPowerTableIndex;
	}

	/**
	 * @return the airProtoSettingsList
	 */
	public ArrayList<AirProtocolInventoryCommandSettings> getAirProtoSettingsList() {
		return airProtoSettingsList;
	}
	
	/**
	 * Clears the air protocol Settings list and Air protocols supported list
	 *
	 */
	public void clearAirProtoSettingList(){
		airProtoSettingsList.clear();
		airProtocolsSupported.clear();
	}
	
	/**
	 * Adds a single air protocol to the list of bytes
	 * @param airProto to add
	 */
	public void addAirProtocolSupported(AirProtocolEnums airProto){
		this.airProtocolsSupported.add(airProto);
	}

	public void addAirProtocolInventoryCommandSetting(
			AirProtocolInventoryCommandSettings apics) {
		this.airProtoSettingsList.add(apics);
	}

}
