package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.llrp.airprotocol.UHF_RFModeTable;

public class _UHFC1G2RFModeTable implements UHF_RFModeTable {

	private class UHFC1G2RFModeTableEntry {
		/**
		 * This is a Reader defined identifier that the client may use to set
		 * the Gen2 operating parameters.
		 */
		int modeIdentifier;

		/**
		 * Divide Value.
		 * 
		 * Possible Values:
		 * 
		 * 0 -> Divide Ratio of 8
		 * 
		 * 1 -> Divide Ration of 64/3
		 */
		int DRValue;

		/**
		 * Back Scatter Data in bps. Possible Values: 40000 – 640000 bps
		 */
		int BDRValue;

		/**
		 * Modulation
		 * 
		 * Possible Values:
		 * 
		 * 0 --> Mod value of FM0
		 * 
		 * 1 --> Mod value of 2
		 * 
		 * 2 --> Mod value of 4
		 * 
		 * 3 --> Mod value of 8
		 */
		int MValue;

		/**
		 * Possible Values:
		 * 
		 * 0 --> PR-ASK
		 * 
		 * 1 --> SSB-ASK
		 * 
		 * 2 --> DSB-ASK
		 */
		int ForwardLinkModulation;

		/**
		 * One thousand times the ratio of data-0 symbol length and data-1
		 * symbol length in pulse-interval encoding. The C1G2 spec specifies a
		 * ratio range of 1.5 – 2.0. (see section 6.3.1.2.4 in [C1G2]).
		 * 
		 * Possible Values: 1500-2000
		 * 
		 */
		int PIEValue;

		/**
		 * Minimum Tari time in nanoseconds (see section 6.3.1.2.4 in [C1G2])
		 * Possible Values: 6250-25000
		 * 
		 */
		int MinTariValue;

		/**
		 * Maximum Tari time in nanoseconds. (see section 6.3.1.2.4 in [C1G2]).
		 * Possible Values: 6250-25000
		 * 
		 */
		int MaxTariValue;

		/**
		 * Tari Step size in nanoseconds.(see section 6.3.1.2.4 in [C1G2])
		 * Possible Values: 0 – 18750 nsec
		 * 
		 */
		int StepTariValue;

		/**
		 * Spectral mask characteristics of the mode.
		 * 
		 * Possible values:
		 * 
		 * 0 --> Unknown
		 * 
		 * 1 --> SI – Meets [C1G2] Single-Interrogator Mode Mask
		 * 
		 * 2 --> MI – Meets [C1G2] Multi-InterrogatorMode Mask
		 * 
		 * 3 --> DI - Meets [C1G2] Dense-Interrogator Mode Mask
		 * 
		 */
		int SpectralMaskIndicator;

		/**
		 * This flag indicates if the Reader vendor has received the
		 * certification for the parameter sets specified in this mode.
		 * 
		 */
		boolean EPCHagTCConformance;

		public UHFC1G2RFModeTableEntry(int modeIdentifier, int DRValue,
				int BDRValue, int MValue, int ForwardLinkModulation,
				int PIEValue, int MinTariValue, int MaxTariValue,
				int StepTariValue, int SpectralMaskIndicator,
				boolean EPCHagTCConformance) {
			this.modeIdentifier = modeIdentifier;
			this.DRValue = DRValue;
			this.BDRValue = BDRValue;
			this.MValue = MValue;
			this.ForwardLinkModulation = ForwardLinkModulation;
			this.PIEValue = PIEValue;
			this.MinTariValue = MinTariValue;
			this.MaxTariValue = MaxTariValue;
			this.StepTariValue = StepTariValue;
			this.SpectralMaskIndicator = SpectralMaskIndicator;
			this.EPCHagTCConformance = EPCHagTCConformance;

		}

	}
	
	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(_UHFC1G2RFModeTable.class);

	private ArrayList<UHFC1G2RFModeTableEntry> table;

	public _UHFC1G2RFModeTable() {
		this.table = new ArrayList<UHFC1G2RFModeTableEntry>();
	}

	/**
	 * Adds a new entry to the UHF_C1G2_RFMode_Table. See 15.2.1.1.2.1
	 * 
	 * @param modeIdentifier
	 * @param DRValue
	 *            Divide Ratio
	 * @param BDRValue
	 *            Backscatter data rate in bps
	 * @param MValue
	 *            Modulation
	 * @param ForwardLinkModulation
	 * @param PIEValue
	 *            One thousand times the ratio of data-0 symbol length and
	 *            data-1 symbol length in pulse-interval encoding
	 * 
	 * @param MinTariValue
	 *            Minimum Tari time in nanoseconds
	 * @param MaxTariValue
	 *            Maximum Tari time in nanoseconds
	 * @param StepTariValue
	 *            Tari Step size in nanoseconds.
	 * @param SpectralMaskIndicator
	 * @param EPCHagTCConformance
	 */
	public void addTableEntry(int modeIdentifier, int DRValue, int BDRValue,
			int MValue, int ForwardLinkModulation, int PIEValue,
			int MinTariValue, int MaxTariValue, int StepTariValue,
			int SpectralMaskIndicator, boolean EPCHagTCConformance) {

		UHFC1G2RFModeTableEntry entry = new UHFC1G2RFModeTableEntry(
				modeIdentifier, DRValue, BDRValue, MValue,
				ForwardLinkModulation, PIEValue, MinTariValue, MaxTariValue,
				StepTariValue, SpectralMaskIndicator, EPCHagTCConformance);
		this.table.add(entry);

	}
	
	public int getTableSize(){
		return this.table.size();
	}
	
	public int getModeIdentifier(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.modeIdentifier;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getDRValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.DRValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getBDRValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.BDRValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getMValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.MValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getForwardLinkModulation(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.ForwardLinkModulation;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getPIEValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.PIEValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getMinTariValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.MinTariValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getMaxTariValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.MaxTariValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getStepTariValue(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.StepTariValue;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public int getSpectralMaskIndicator(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.SpectralMaskIndicator;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return 0;
	}
	
	public boolean getEPCHagTCConformance(int index){
		try {
			UHFC1G2RFModeTableEntry entry = ((UHFC1G2RFModeTableEntry) table
					.get(index));
			return entry.EPCHagTCConformance;
		} catch (IndexOutOfBoundsException e) {
			logger
					.error("Index out of Bounds Exception.  UHF C1G2 RFMode Table Entry "
							+ index
							+ " does not exist.  Highest index is "
							+ (table.size() - 1));
		}
		return false;
	}


}
