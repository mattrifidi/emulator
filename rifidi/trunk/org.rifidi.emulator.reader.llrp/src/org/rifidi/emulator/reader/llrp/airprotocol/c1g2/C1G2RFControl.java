package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

public class C1G2RFControl {
	
	/**
	 * Used as an index into the UHFC1G2RFModeTable
	 */
	public int modeIndex;
	
	/**
	 * Value of Tari to use for this mode specified in nsec. possible values: 0 or 6250-25000 nsec
	 */
	public int tari;
	
	public C1G2RFControl(){
		
	}
	
	/**
	 * A convenience constructor for this class
	 * @param modeIndex Used as index into the UFC1G2RFModeTable
	 * @param tari - Value of tari to use for this mode specified in nsecs.
	 */
	public C1G2RFControl(int modeIndex, int tari){
		this.modeIndex = modeIndex;
		this.tari = tari;
	}

}
