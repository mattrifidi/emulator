package org.rifidi.emulator.reader.llrp.airprotocol.c1g2;

import java.util.ArrayList;

import org.rifidi.emulator.reader.llrp.airprotocol.AirProtocolInventoryCommandSettings;

public class C1G2InventoryCommand implements
		AirProtocolInventoryCommandSettings {

	public boolean tagInventoryStateAware;
	
	private ArrayList<C1G2Filter> c1g2Filter = new ArrayList<C1G2Filter>();

	public C1G2RFControl c1g2RFC = new C1G2RFControl();

	public C1G2SingulationControl c1g2SingCon = new C1G2SingulationControl();
	
	public int getC1G2FilterListSize(){
		return c1g2Filter.size();
	}
	
	public void addC1G2Filter(C1G2Filter newFilter){
		this.c1g2Filter.add(newFilter);
	}
	
	public C1G2Filter getC1G2FilterAt(int index){
		return this.c1g2Filter.get(index);
	}
	


}
