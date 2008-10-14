package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The Frequency Hop table is a table of tables. The FreqencyHoTableEntry are
 * tables that store an ID and a list of frequencies. The Frequency Hop table is
 * a capability and cannot be set by the client
 * 
 * @author kyle
 * 
 */
public class FrequencyHopTable {

	/**
	 * Stores the values of the Frequecy Hop Table.
	 * 
	 * @author kyle
	 * 
	 */
	private class FrequencyHopTableEntry {

		/**
		 * The ID for this hop table
		 */
		int hopTableID;

		ArrayList<Integer> frequencyList;

		public FrequencyHopTableEntry(int hopTableID, ArrayList<Integer>frequencyList) {
			this.hopTableID = hopTableID;
			this.frequencyList=frequencyList;
		}

	}

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(FrequencyHopTable.class);


	private ArrayList<FrequencyHopTableEntry> table;

	public FrequencyHopTable() {
		table = new ArrayList<FrequencyHopTableEntry>();
	}

	
	/**
	 * 
	 * @return The size of the TransmitPowerTable
	 */
	public int getTableSize() {
		return table.size();
	}

	public void addTableEntry(int transmitPowerValue,
			ArrayList<Integer> frequencyList) {
		table
				.add(new FrequencyHopTableEntry(transmitPowerValue,
						frequencyList));

	}

	/**
	 * Gets the Frequency List for a specific frequency hop table
	 * @param index The index of the frequency entry to get
	 * @return An array of integers that are the frequency information
	 */
	public ArrayList<Integer> getFrequencyList(int index) {
		try {
			FrequencyHopTableEntry fhte = ((FrequencyHopTableEntry) table
					.get(index));
			return fhte.frequencyList;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  Frequency Hop Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return new ArrayList<Integer>();

	}
	
	/**
	 * Gets the ID for a specific frequecny hop table
	 * @param index the index of the entry to get
	 * @return The ID of the frequency hop entry table
	 */
	public int gettableID(int index){
		try {
			FrequencyHopTableEntry fhte = ((FrequencyHopTableEntry) table
					.get(index));
			return fhte.hopTableID;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  Frequency Hop Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}

}
