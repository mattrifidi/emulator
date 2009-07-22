/**
 * 
 */
package org.rifidi.emulator.scripting;

import java.util.HashSet;
import java.util.Set;

import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Matthew Dean - matt@pramari.com
 *
 */
public class ReaderManager {
	
	private Set<ReaderModuleFactory> moduleFactoryList = null;

	/**
	 * Called by spring.  
	 * 
	 * @param moduleFactoryList the moduleFactoryList to set
	 */
	public void setModuleFactoryList(Set<ReaderModuleFactory> moduleFactoryList) {
		this.moduleFactoryList = moduleFactoryList;
	}
	
	public Set<String> getReaderTypes() {
		Set<String> retVal = new HashSet<String>();
		for(ReaderModuleFactory rmf:this.moduleFactoryList) {
			retVal.add(rmf.getReaderType());
		}
		
		return retVal;
	}
	
	
	
}
