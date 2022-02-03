package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This class represents the Recieve Sensitivity Table for an LLRP reader. It is
 * only set when an LLRP Reader is created becuase it is a capability and not a
 * configuration (i.e. a client cannot set these values)
 * 
 * @author kyle
 * 
 */
public class ReceiveSensitivityTable {

	/**
	 * Stores the values of the RecieveSensitivityTable
	 * 
	 * @author kyle
	 * 
	 */
	private class ReceiveSensitivityTableEntry implements
			Comparable<ReceiveSensitivityTableEntry> {

		/**
		 * Valid values are 0 to -128
		 */
		int recieveSensitvityValue;

		public ReceiveSensitivityTableEntry(int sensitivityValue) {
			if (sensitivityValue >= -128 && sensitivityValue <= 0) {
				this.recieveSensitvityValue = sensitivityValue;
			} else {
				logger
						.error("Invalid value for Recieve Sensitivity Value.  Value is "
								+ sensitivityValue
								+ " but it must be between -128 and 0.  Setting to 0");
				this.recieveSensitvityValue = 0;
			}
		}

		public int compareTo(ReceiveSensitivityTableEntry o) {
			if (this.recieveSensitvityValue < o.recieveSensitvityValue) {
				return -1;
			} else if (this.recieveSensitvityValue == o.recieveSensitvityValue) {
				return 0;
			} else {
				return 1;
			}
		}

	}

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(TransmitPowerTable.class);

	private ArrayList<ReceiveSensitivityTableEntry> table;

	public ReceiveSensitivityTable() {
		table = new ArrayList<ReceiveSensitivityTableEntry>();
	}

	/**
	 * 
	 * @return The size of the TransmitPowerTable
	 */
	public int getTableSize() {
		return table.size();
	}

	/**
	 * This method adds a new sensitivity value to the table. If the value
	 * already exists, it ignores the add request, becuase only one entry may be
	 * made per value
	 * 
	 * @param sensitivityValue
	 */
	public void addTableEntry(int sensitivityValue) {

		/*
		 * Although this method may not be extremely efficient, it is ok because
		 * it is not expected to have many values
		 */
		ReceiveSensitivityTableEntry newEntry = new ReceiveSensitivityTableEntry(
				sensitivityValue);
		boolean alreadyExists = false;

		for (ReceiveSensitivityTableEntry curEntry : table) {
			if (curEntry == newEntry) {
				alreadyExists = true;
			}
		}
		if (!alreadyExists) {
			table.add(new ReceiveSensitivityTableEntry(sensitivityValue));
			Collections.sort(table);
		}
	}

	public int getReceiveSensitivityValue(int index) {
		try {
			ReceiveSensitivityTableEntry tpte = ((ReceiveSensitivityTableEntry) table
					.get(index));
			return tpte.recieveSensitvityValue;
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
