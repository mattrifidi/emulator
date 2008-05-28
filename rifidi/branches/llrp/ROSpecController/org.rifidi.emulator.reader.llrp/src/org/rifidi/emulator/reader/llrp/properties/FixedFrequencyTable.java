package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The FixedFrequency Table stores frequency values. It is a capability and
 * cannot be set by the client
 * 
 * @author kyle
 * 
 */
public class FixedFrequencyTable {

	/**
	 * Stores the values of the TransmitPowerTable
	 * 
	 * @author kyle
	 * 
	 */
	private class FixedFrequencyTableEntry {
		/**
		 * Frequency in Khz
		 */
		int frequency;

		public FixedFrequencyTableEntry(int frequency) {
			this.frequency = frequency;
		}

	}

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(TransmitPowerTable.class);


	private ArrayList<FixedFrequencyTableEntry> table;

	public FixedFrequencyTable() {
		table = new ArrayList<FixedFrequencyTableEntry>();
	}


	/**
	 * 
	 * @return The size of the FixedFrequencyTable
	 */
	public int getTableSize() {
		return table.size();
	}

	public void addTableEntry(int frequency) {
		table.add(new FixedFrequencyTableEntry(frequency));
	}

	/**
	 * This method get a frequency at a certain channelIndex
	 * 
	 * @param channelIndex
	 *            The ChannelIndex is a frequency's postion in this list
	 * @return a frequency at a certain channel index
	 */
	public int getPowerValue(int channelIndex) {
		try {
			FixedFrequencyTableEntry ffte = ((FixedFrequencyTableEntry) table
					.get(channelIndex));
			return ffte.frequency;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  Fixed Frequency Table Entry "
							+ channelIndex
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;

	}

}
