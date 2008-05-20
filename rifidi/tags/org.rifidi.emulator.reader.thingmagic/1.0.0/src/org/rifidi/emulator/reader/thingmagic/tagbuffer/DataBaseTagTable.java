package org.rifidi.emulator.reader.thingmagic.tagbuffer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.thingmagic.database.*;
import org.rifidi.emulator.reader.thingmagic.database.enums.Etag_id;
import org.rifidi.emulator.reader.thingmagic.formatter.ThingMagicRQLCommandFormatter;
import org.rifidi.emulator.reader.thingmagic.module.*;
import org.rifidi.emulator.reader.thingmagic.tagbuffer.ThingMagicTagMemory;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * @author jmaine
 * This database implementer give access to the tag memory.
 * We do all the filtering here too before we select what needs to be displayed.
 */
public class DataBaseTagTable implements DataBaseTable<Etag_id, Object> {
	private static Log logger = LogFactory.getLog(ThingMagicRQLCommandFormatter.class);
	
	private ThingMagicReaderSharedResources tmsr;
	
	/**
	 * Constructor
	 * @param tmsr ThingMagic Shared Resources 
	 */
	public DataBaseTagTable (ThingMagicReaderSharedResources tmsr){
		this.tmsr = tmsr;
	}
	
	/**
	 * Search the table for tag information.
	 * We filter before we go through and find the information that the caller wants.
	 * @param keyList The list of keys to collect 
	 * @param where Filter clause
	 * @param timeout Timeout in milliseconds
	 */
	@Override
	public List<List<Object>> searchTable(List<Etag_id> keyList, IFilter where, int timeout) {
		logger.debug("searchTable() Called:" + keyList);
		// TODO Auto-generated method stub
		
		/*
		 * grab the tagmemory from the shared resources.
		 */
		ThingMagicTagMemory tagMemory = (ThingMagicTagMemory) this.tmsr.getTagMemory();
		
		List<List<Object>> retVal = new ArrayList<List<Object>>();
		
		/* clear all previously accumulated tags. */
		tagMemory.clear();
		
		/* Force the radio to scan now. */
		tmsr.getRadio().scan(null, tagMemory);
		
		try {
			Thread.sleep(timeout); // let the tags gather for a moment.
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		/* grab the tags out of the tag memory. */
		Collection<RifidiTag> tags = tagMemory.getTagReport();
		
		/* if where is null then there is no filtering to be done
		 * if not then we run the tags through the filter.
		 */
		if (where != null)
			tags = where.filter(tags);
		
		//logger.debug(tags);
		for (RifidiTag tag: tags){
			//if (!tag.isVisbile) continue; 
			List<Object> element = new ArrayList<Object>();
			retVal.add(element);
			
			// TODO Add the rest of the cases.
			/*
			 * Now we go though the tags in the order given.
			 */
			for(Etag_id tag_id: keyList){
				switch(tag_id){
					case ID:
						/* we have remove the spaces in the middle of the hex
						 * string returned.
						 */
						String[] strs = tag.toString().split("\\s");
						String packed = "";
						for (String str: strs)
							packed += str;
						/* now we add */
						element.add("0x" + packed);
					break;
					case ANTENNA_ID:
						element.add(tag.getAntennaLastSeen().toString());
					break;
					case READ_COUNT:
						element.add(tag.getReadCount().toString());
					break;
					/*case TIMESTAMP:
					{
						
					}*/	
					// TODO implement the rest of the cases.
					case PROTOCOL_ID:
						// FIXME send back information correctly
						//element.add(tag.getTag().getTagGeneration().toString());
					//break;
					default:
						element.add("unimplimented=>" + tag_id);
					
				}		
			}
		}
		
		
		//logger.debug(retVal);
		return retVal;
	}

}
