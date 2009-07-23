/**
 * 
 */
package org.rifidi.emulator.scripting;

import java.util.HashSet;
import java.util.Set;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;
import org.rifidi.tags.impl.RifidiTag;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class ReaderManagerImpl implements ReaderManager {

	private Set<ReaderModuleFactory> moduleFactoryList = null;

	/**
	 * Called by spring.
	 * 
	 * @param moduleFactoryList
	 *            the moduleFactoryList to set
	 */
	public void setModuleFactoryList(Set<ReaderModuleFactory> moduleFactoryList) {
		this.moduleFactoryList = moduleFactoryList;
	}

	public Set<String> getReaderTypes() {
		Set<String> retVal = new HashSet<String>();
		for (ReaderModuleFactory rmf : this.moduleFactoryList) {
			retVal.add(rmf.getReaderType());
			
		}

		return retVal;
	}

	@Override
	public void addTags(String readerID, int antenna, Set<RifidiTag> tagList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RifidiTag createGen1Tag(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RifidiTag createGen2Tag(String data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String createReader(GeneralReaderPropertyHolder grph) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteReader(String readerID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GeneralReaderPropertyHolder getDefault(String readerType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeTags(String readerID, int antenna, Set<RifidiTag> tagList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGPIPortHigh(String readerID, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setGPIPortLow(String readerID, int port) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start(String readerID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stop(String readerID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean testGPOPort(String readerID, int port) {
		// TODO Auto-generated method stub
		return false;
	}
}
