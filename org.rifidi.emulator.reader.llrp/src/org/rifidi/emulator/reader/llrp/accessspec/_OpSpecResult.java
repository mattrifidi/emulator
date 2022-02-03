package org.rifidi.emulator.reader.llrp.accessspec;

import edu.uark.csce.llrp.C1G2OpSpecResult;

public interface _OpSpecResult {

	/**
	 * This gets an integer telling whether or not the operation was successful.
	 * If it was successful, it returns a 0, otherwise it returns a positive
	 * value.
	 * 
	 * @return
	 */
	public int getResult();

	/**
	 * This gets the opspec result specified by the llrp-tk so that it can be
	 * added to the higher level TagReportData param. It will need to change to
	 * AccessCommandOpSpecResult when the new toolkit is integreated.
	 * 
	 * @return
	 */
	public C1G2OpSpecResult getLLRPTKResult();

}
