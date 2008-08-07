/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic_old.tagbuffer;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * @author jmaine
 *
 */
public class ThingMagicTagMemory implements TagMemory {
	
	/**
	 * TagMemory constructor.
	 */
	public ThingMagicTagMemory(){
		super();
		clear();
	}
	
	
	private static Log logger = LogFactory.getLog(ThingMagicTagMemory.class);
	
	Collection<RifidiTag> tags;

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory#clear()
	 */
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		logger.debug("ThingMagic.clear() called.");
		tags = new ArrayList<RifidiTag>();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory#getTagReport()
	 */
	@Override
	public Collection<RifidiTag> getTagReport() {
		// TODO Auto-generated method stub
		logger.debug("getTagReport() called.");
		return tags;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory#resume()
	 */
	@Override
	public void resume() {
		// TODO Auto-generated method stub
		logger.debug("resume() called.");
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory#suspend()
	 */
	@Override
	public void suspend() {
		// TODO Auto-generated method stub
		logger.debug("suspend() called.");

	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory#updateMemory(java.util.Collection)
	 */
	@Override
	public void updateMemory(Collection<RifidiTag> tagsToAdd) {
		// TODO Auto-generated method stub
		logger.debug("updateMemory() called.");
		
		for( RifidiTag t : tagsToAdd){
			if(tags.contains(t)){ 
				t.incrementReadCount();
			}else{
				t.incrementReadCount();
				tags.add(t);
			}
			
		}
	}

}
