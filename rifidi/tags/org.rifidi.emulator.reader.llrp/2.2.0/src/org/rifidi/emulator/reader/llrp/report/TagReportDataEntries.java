/**
 * 
 */
package org.rifidi.emulator.reader.llrp.report;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.uark.csce.llrp.EPC96;
import edu.uark.csce.llrp.EPCData;
import edu.uark.csce.llrp.Parameter;
import edu.uark.csce.llrp.TagReportData;

/**
 * 
 * This data structure keeps track of Tag Report Data Entries. It does not allow
 * a single tag to be in the list twice. It can be accessed by multiple threads,
 * so it needs to be thread safe
 * 
 * @author kyle
 * 
 */
public class TagReportDataEntries {

	ArrayList<TagReportData> list = new ArrayList<TagReportData>();

	/**
	 * This method adds the ReportData if it is not already in the list
	 */
	public void add(TagReportData trd) {

		boolean alreadyMember = false;
		for (TagReportData currentTrd : list) {
			if (DataEntriesEqual(currentTrd, trd)) {
				alreadyMember = true;
				break;
			}
		}

		if (!alreadyMember) {
			list.add(trd);
		}

	}

	/**
	 * This method returns a list of all Tag Report Data paramaters and deletes
	 * and clears the list.
	 * 
	 * @return
	 */
	public ArrayList<TagReportData> getAllDataEntries() {
		ArrayList<TagReportData> retVal;
		synchronized (this) {
			retVal = new ArrayList<TagReportData>(list);
			list.clear();
		}
		return retVal;
	}

	/**
	 * Remove a single Tag report data parameter
	 * @param trd
	 */
	public void removeDataEntry(TagReportData trd) {
		TagReportData dataToRemove = null;
		synchronized (this) {
			for (TagReportData reportData : list) {
				if (DataEntriesEqual(trd, reportData)) {
					dataToRemove = reportData;
				}
			}
			if(dataToRemove!=null){
				list.remove(dataToRemove);
			}
		}
	}

	/**
	 * Returns the size of the list
	 * 
	 * @return
	 */
	public int getNumDataEntries() {
		return list.size();
	}

	/**
	 * Test if the two Tag Report Data parameters have equal IDs
	 * 
	 * @param trd1
	 * @param trd2
	 * @return
	 */
	private boolean DataEntriesEqual(TagReportData trd1, TagReportData trd2) {
		return Arrays.equals(getID(trd1), getID(trd2));
	}

	/**
	 * Extracts the IDs from the TagReportData parameters
	 * 
	 * @param trd
	 * @return
	 */
	private byte[] getID(TagReportData trd) {
		Parameter data = trd.getEPCDataParam();
		byte[] id = null;
		if (data instanceof EPC96) {
			id = ((EPC96) data).getData();
		} else if (data instanceof EPCData) {
			id = ((EPCData) data).getData();
		}
		return id;
	}

}
