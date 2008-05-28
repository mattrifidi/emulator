package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

public class C1G2InventoryMask {

	/**
	 * C1G2 Tag Memory Bank to use.
	 * 
	 * Possible values:
	 * 
	 * 0 - Mask used for C1G2 select command applies to tag's Reserved Mem
	 * 
	 * 1 - Mask used for C1G2 select command applies to tag's EPC memory
	 * 
	 * 2 - Mask used for C1G2 select command applies to tag's TID memory
	 * 
	 * 3 - Mask used for C1G2 select command applies to tag's user memory
	 */
	public int Mb;

	/**
	 * the first (msb) bit location of the specified memory bank against which
	 * to comapre the Tag Mask
	 */
	public short pointer;
	
	/**
	 * Number of bits to compare against the TagMask
	 */
	public short length;
	
	/**
	 * The pattern against which to compare
	 */
	public byte[] tagMask; 
}
