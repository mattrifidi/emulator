package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;


public class C1G2Filter {
	
	public C1G2InventoryMask c1g2IM;
	
	/**
	 * Truncate.  
	 * 0 - Unspecified.  The reader decides whether to truncated
	 * 1 - Do not Truncate
	 * 2 - Truncate
	 */
	public int T;
	
	public C1G2TagInventoryStateAwareFilterAction tagInventoryStateAware;
	
	public C1G2TagInventoryStateUnawareFilterAction tagInventoryStateUnaware;
	

}
