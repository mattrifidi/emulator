package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolEnums;
import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolLLRPCapabilities;

public class _C1G2LLRPCapabilities implements AirProtocolLLRPCapabilities {
	
	public boolean canSupportBlockErase;
	
	public boolean canSupportBlockWrite;
	
	public short maxNumSelectFiltersPerQuery;

	public AirProtocolEnums getAirProtocolType() {
		return AirProtocolEnums.C1G2;
	} 

}
