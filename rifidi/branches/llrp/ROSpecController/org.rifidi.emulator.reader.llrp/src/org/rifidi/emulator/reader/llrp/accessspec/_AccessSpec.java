/*
 *  _AccessSpec.java
 *
 *  Created:	Oct 9, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 *	Author: 	kyle
 */
package org.rifidi.emulator.reader.llrp.accessspec;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderSharedResources;
import org.rifidi.emulator.reader.llrp.report.LLRPReportControllerFactory;
import org.rifidi.services.tags.impl.RifidiTag;

import edu.uark.csce.llrp.AccessSpec;
import edu.uark.csce.llrp.AccessSpecID;
import edu.uark.csce.llrp.C1G2OpSpecResult;
import edu.uark.csce.llrp.TagReportData;

/**
 * @author kyle
 * 
 */
public class _AccessSpec {

	/**
	 * The ID of the AccesSpec, set by the client
	 */
	private int specID;

	/**
	 * The antenna to perform this operation on. 0 means all antennas
	 */
	private short antennaID;

	/**
	 * Which air protocol to use
	 */
	private AirProtocolEnums protocolID;

	/**
	 * The State of the access spec. if true, the access spec is enable. If
	 * false, it is disabled.
	 */
	public boolean currentState;

	/**
	 * 0 - no stop trigger defined, 1 - operation count
	 */
	private int stopTriggerType;

	/**
	 * if stopTrigger=1, the max number of operations to use before disabling
	 * this accessspec
	 */
	private int operationCount;
	

	/**
	 * The number of operations performed so far. For use with the operation
	 * stop trigger.
	 */
	private int numOfOperationsPerformed;
	
	/**
	 * The rospec that this AccessSpec is associcated with.
	 */
	private int roSpecID;

	/**
	 * Which tags to operate on
	 */
	private _TagSpec tagSpec;

	/**
	 * The operations to perform on the selected tags
	 */
	private ArrayList<_OpSpec> opSpecs;

	/**
	 * When the access report should be sent. If it equals -1, use the one in
	 * the global properties, which is set by a SET_READER_CONFIG message
	 */
	private int accessReportTrigger;

	/**
	 * the incoming accessSpec. Save it to return on a GET_ACESSSPECS_RESPONSE
	 */
	private AccessSpec ltkAccesSpec;

	LLRPReaderSharedResources llrpsr;
	
	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(_AccessSpec.class);


	/**
	 * 
	 * @param specID
	 *            The ID of the AccesSpec, set by the client
	 * @param antennaID
	 *            The antenna to perform this operation on. 0 means all antennas
	 * @param protocolID
	 *            Which air protocol to use
	 * @param stopTriggerType
	 *            0 - no stop trigger defined, 1 - operation count
	 * @param operationCount
	 *            if stopTrigger=1, the max number of operations to use before
	 *            disabling this accessspec.
	 * @param roSpecID the Rospec that this access spec is associated with
	 * @param ts
	 *            Which tags to operate on
	 * @param opSpecs
	 *            The operations to perform on the selected tags
	 * 
	 * @param accessReportTrigger
	 *            when the access Report should be sent
	 * @param llrpsr
	 *            The shared resources
	 * 
	 * @param ltkAccessSpec
	 *            the incoming accessSpec for returning as part of a
	 *            GET_ACCESSSPECS_REPSONSE message
	 */
	public _AccessSpec(int specID, short antennaID,
			AirProtocolEnums protocolID, int stopTriggerType,
			int operationCount, int roSpecID, _TagSpec ts, ArrayList<_OpSpec> opSpecs,
			int accessReportTrigger, LLRPReaderSharedResources llrpsr,
			AccessSpec ltkAccessSpec) {

		this.specID = specID;
		this.antennaID = antennaID;
		this.protocolID = protocolID;
		this.stopTriggerType = stopTriggerType;
		this.operationCount = operationCount;
		this.roSpecID = roSpecID;
		this.tagSpec = ts;
		this.opSpecs = opSpecs;
		this.llrpsr = llrpsr;
		this.ltkAccesSpec = ltkAccessSpec;
		this.accessReportTrigger = accessReportTrigger;
	}

	public boolean shouldPerformOperation(RifidiTag tag) {

		logger.debug("Attempting to match AccessSpec with ID " + this.specID + " to tag");
		// if trigger hasn't fired, and the state true, and the tag matches,
		// return true
		if (this.currentState && tagSpec.matchTag(tag)) {
			logger.debug("AccessSpec found that matches: " + tag);
			return true;
		}

		return false;
	}

	public boolean performOperations(RifidiTag tag, TagReportData trd) {
		logger.debug("checking to see if access operations should be performed...");
		this.numOfOperationsPerformed++;
		// iterate through list of opspecs and perform each operation
		Iterator<_OpSpec> iter = opSpecs.iterator();

		// accumulation of results so that we don't add results to trd if there
		// was an error in a opSpec
		ArrayList<C1G2OpSpecResult> results = new ArrayList<C1G2OpSpecResult>();

		boolean stop = false;
		while (iter.hasNext() && !stop) {
			_OpSpec op = iter.next();
			_OpSpecResult result = op.performOperation(tag);
			results.add(result.getLLRPTKResult());
			if(result.getResult()!=0){
				stop=true;
			}
		}
		
		// add each result to trd
		for (C1G2OpSpecResult r : results) {
			trd.addOpSpecResultParam(r);
			AccessSpecID id = new AccessSpecID();
			id.setAccessSpecID(this.specID);
			trd.setAccessSpecIDParam(id);
		}

		
		//if accessReport trigger is not defined, look up the global value
		if(accessReportTrigger==-1){
			accessReportTrigger = llrpsr.getProperties().accessReportTrigger;
		}
		// send report now if we need to
		if (this.accessReportTrigger == 1) {
			LLRPReportControllerFactory.getInstance().getReportController(
					llrpsr.getReaderName()).sendReport(trd);
			// remove from list so we don't resend it later
			llrpsr.getTagReportDataEntries().remove(trd);
		}

		// if stop trigger has fired, delete this spec from the list
		if (this.stopTriggerType == 1 && this.operationCount != 0) {
			if (this.numOfOperationsPerformed >= this.operationCount) {
				logger.debug("AccessSpec stop trigger has fired, delete access spec");
				llrpsr.accessSpecs.removeAccessSpec(this.specID);
			}
		}

		return true;
	}

	/**
	 * @return the specID
	 */
	public int getSpecID() {
		return specID;
	}

	/**
	 * @return the ltkAccesSpec
	 */
	public AccessSpec getLtkAccesSpec() {
		return ltkAccesSpec;
	}

	/**
	 * @return the accessReportTrigger
	 */
	public int getAccessReportTrigger() {
		return accessReportTrigger;
	}

	/**
	 * @return the roSpecID
	 */
	public int getRoSpecID() {
		return roSpecID;
	}

}
