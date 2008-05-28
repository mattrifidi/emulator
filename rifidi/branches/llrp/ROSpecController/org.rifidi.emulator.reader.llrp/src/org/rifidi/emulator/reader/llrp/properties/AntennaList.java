package org.rifidi.emulator.reader.llrp.properties;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AntennaList {
	
	private ArrayList<AntennaSettings> antennaList;
	
	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory.getLog(AntennaList.class);
	
	public AntennaList(){
		this.antennaList=new ArrayList<AntennaSettings>();
	}
	
	public void addAntenna(AntennaSettings antenna){
		this.antennaList.add(antenna);
		antenna.setAntennaID((short)antennaList.size());
	}
	
	public AntennaSettings getAntenna(int index){
		if(index<=0){
			logger.error("Invalid Antenna index. Antenna Index starts at 1");
			return null;
		}
		if(index >this.antennaList.size()){
			logger.error("Invalid Antenna inex.  Antenna index out of range");
			return null;
		}
		else return this.antennaList.get(index -1);
	}
	
	public int getNumberAntennas(){
		return this.antennaList.size();
	}

}
