package org.rifidi.emulator.reader.llrp.report;

/**
 * A collection of variables that tell what information a ROReport should
 * contain.
 * 
 * @author kyle
 * 
 */
public class ROReportFormat {

	/**
	 * 0 - No reports are generated automatically. Have to use the GET_REPORT
	 * message from the client to get reports 1 - Upon N TagReportData params or
	 * End of AISpec 2 - Upon N TagReportData params or End of ROSpec
	 */
	public int reportTrigger=0;

	/**
	 * Number of TagReportDataParams used in ROReportTrigger=1 and 2. If N=0,
	 * there is no limit
	 */
	public short N = 0;

	public boolean enableROSpecID = false;

	public boolean enableSpecIndex = false;

	public boolean enableInventoryParamaterSpecID = false;

	public boolean enableAntennaID = false;

	public boolean enableChannelIndex = false;

	public boolean enablePeakRSSI = false;

	public boolean enableFirstSeenTimestamp = false;

	public boolean enableLastSeenTimestamp = false;

	public boolean enableTagSeenCount = false;

	public boolean enableAccessSpecID = false;

	/**
	 * TODO: These fields are C1G2 specific and should eventually move into
	 * thier own Class that is referenced here by an interface
	 */

	public boolean enablePC = false;

	public boolean enableCRC = false;

}
