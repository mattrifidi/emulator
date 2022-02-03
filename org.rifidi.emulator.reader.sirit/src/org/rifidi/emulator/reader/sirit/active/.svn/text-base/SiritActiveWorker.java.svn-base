/*
 *  SiritActiveWorker.java
 *
 *  Created:	05.08.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.active;

import org.rifidi.emulator.reader.sharedrc.radio.generic.GenericRadio;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 *
 */
public class SiritActiveWorker implements Runnable {

	
	private GenericRadio radio;
	private TagMemory tagMem;
	
	public SiritActiveWorker(GenericRadio radio, TagMemory tagMem) {
		this.radio = radio;
		this.tagMem = tagMem;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			while(true) {
				radio.scan(null, this.tagMem);
				Thread.sleep(500);
			}
		}
		catch (InterruptedException iex) {
			Thread.currentThread().interrupt();
		}
		
	}

}
