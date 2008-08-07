/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic_old.database.enums;


import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.services.tags.impl.RifidiTag;

/**
 * @author jmaine
 * This enum lists all the rows for tag_id table.
 */
public enum Etag_id implements GenericTableRow<Object>{
	PROTOCOL_ID(true, false), ANTENNA_ID(true, false),
	READ_COUNT(true, false), ID(true, true), KILLED(false, true), 
	PASSWROD(false, true), LOCKED(true, false), FREQENCY(true, false),
	DSPMICROS(true, false), TIMESTAMP(true, false);
	
	private static Log logger = LogFactory.getLog(Etag_id.class);
	
	boolean read;
	boolean write;
	
	/**
	 * Enum constructor.
	 * @param read If the the row is readable.
	 * @param write If the row is writable.
	 */
	Etag_id (boolean read, boolean write){
		this.read = read;
		this.write = write;
		
	}

	/**
	 * Tell if the table row is readable
	 */
	@Override
	public boolean isReadable() {
		// TODO Auto-generated method stub
		return this.read;
	}

	/**
	 * Tell if the table row is writable. 
	 */
	@Override
	public boolean isWriteable() {
		// TODO Auto-generated method stub
		return this.write;
	}

	/**
	 * Comparator for RifidiTags.
	 * We intentionally <i>bend</i> the Comparator contract 
	 * to allow the second argument
	 * to be of type String.
	 * @param o1 Object of RifidiTag type.
	 * @param o2 Object of RifidiTag or String type.
	 */
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		RifidiTag r1 = null;
		RifidiTag r2 = null;
		String s2 = null;
		
		/* we intentionally /bend/ the Comparator contract here
		 * by letting the second arg be a string!
		 * Then we compare the String version of the first /part/
		 * with the string of the second.
		 */ 
		
		/* better be safe than sorry! */
		if (!(o1 instanceof RifidiTag))  
			throw new ClassCastException("First argument is not of type RifidiTag.");
		r1 = (RifidiTag) o1; // cast.
		
		/* now some checking on the second argument */
		if (o2 instanceof RifidiTag){
			r2 = (RifidiTag) o2;
		} else if (o2 instanceof String) {
			s2 = (String) o2;
		} else throw new ClassCastException(
							"Second argument is not of type RifidiTag nor of type String."
						 );
		
		logger.debug("Object 1:" + o1);
		
		logger.debug("Object 2:" + o2);
		
		/* if it is not read able why go any further? */
		if (!isReadable())
			throw new IllegalArgumentException(this.toString() + " is not a readable type!");
		
		/* now we get to the dirty work*/
		int retVal = 0;
		
		switch (this){
			case ID:
				// I love the trinary statement.
				BigInteger big1 = new BigInteger(r1.toByte());
				BigInteger big2 = ((r2 != null) ? 
									   new BigInteger(r2.toByte()) :
									   new BigInteger(s2, 16));
				
				retVal = big1.compareTo(big2);
			break;
			case ANTENNA_ID:
				retVal = r1.getAntennaLastSeen() - ((r2 != null) ?
														r2.getAntennaLastSeen().intValue() :
														Integer.parseInt(s2));	
			break;
			case READ_COUNT:
				retVal = r1.getAntennaLastSeen() - ((r2 != null) ?
														r2.getReadCount().intValue() :
														Integer.parseInt(s2));
			break;
			/* TODO need to make this implementation actually match to what the thing magic
			 * reader would return.
			 */
			case PROTOCOL_ID:
				logger.debug(r1.getTag().getTagGeneration().toString());
				
				
				if ( s2 != null ){
					s2 = s2.trim();
				
					if ( ( s2.indexOf("'") != s2.lastIndexOf("'")) && (s2.indexOf("'") >= 0)	)
						s2 = s2.substring(s2.indexOf("'") + 1, s2.lastIndexOf("'"));
					else if ( ( s2.indexOf("’") != s2.lastIndexOf("’")) && (s2.indexOf("’") >= 0)	)
						s2 = s2.substring(s2.indexOf("’") + 1, s2.lastIndexOf("’"));
					
					logger.debug(s2);
				}
				
				retVal = r1.getTag().getTagGeneration().toString().compareToIgnoreCase(
													((r2 != null) ?
														r2.getTag().getTagGeneration().toString() :
														s2
														
													));
				
				logger.debug(retVal);
			break;
			default:
				throw new IllegalArgumentException("Comparator for: " + this + " not implimented!");
		
		}
		
		return retVal;
	}
	
}	