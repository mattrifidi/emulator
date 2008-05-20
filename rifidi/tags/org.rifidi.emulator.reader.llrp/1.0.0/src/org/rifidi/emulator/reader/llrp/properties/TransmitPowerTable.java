package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Transmit Power Table that stores the transmit power values. It is a
 * capability and thus cannot be set by the client
 * 
 * @author kyle
 * 
 */
public class TransmitPowerTable {

	/**
	 * Stores the values of the TransmitPowerTable
	 * 
	 * @author kyle
	 * 
	 */
	private class TransmitPowerTableEntry {
		int transmitPowerValue;

		public TransmitPowerTableEntry(int powerValue) {
			this.transmitPowerValue = powerValue;
		}

	}

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(TransmitPowerTable.class);

	private ArrayList<TransmitPowerTableEntry> table;

	public TransmitPowerTable() {
		table = new ArrayList<TransmitPowerTableEntry>();
	}

	/**
	 * 
	 * @return The size of the TransmitPowerTable
	 */
	public int getTableSize() {
		return table.size();
	}

	public void addTableEntry(int transmitPowerValue) {
		table.add(new TransmitPowerTableEntry(transmitPowerValue));
	}

	public int getPowerValue(int index) {
		try {
			TransmitPowerTableEntry tpte = ((TransmitPowerTableEntry) table
					.get(index));
			return tpte.transmitPowerValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  Transmit Power Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;

	}
}
