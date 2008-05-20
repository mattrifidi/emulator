/*
 *  C0G1Tag.java
 *
 *  Created:	Sep 24, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.tags.impl;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.tags.Gen1Tag;
import org.rifidi.emulator.tags.enums.TagConstants;
import org.rifidi.emulator.tags.enums.TagErrors;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.exceptions.InvalidMemoryAccessException;

/**
 * Implementation of Class 0 Generation 1 (Read Only Tag)
 * 
 * @author Jochen Mader
 *
 */
/**
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
@XmlRootElement
public class C0G1Tag implements Gen1Tag{

	private static Log logger=LogFactory.getLog(C0G1Tag.class);
		
	private byte[] id = null;
	
	/**
	 * NoArg Constructor for JAXB.
	 */
	public C0G1Tag(){
		
	}
	
	public C0G1Tag(BigInteger id) {
		super();
		this.id = id.toByteArray();
	}
	/* (non-Javadoc)
	 * @see org.rifidi.tags.Gen1Tag#getTagGeneration()
	 */
	public TagGen getTagGeneration() {
		return TagGen.GEN1;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.tags.Gen1Tag#getId()
	 */
	public byte[] readId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.tags.Gen1Tag#writeId(byte[])
	 */
	public void writeId(byte[] id) throws InvalidMemoryAccessException{
		logger.info("C0G1 doesn't support writeId: "+id.toString());
		throw new InvalidMemoryAccessException(TagErrors.TagIsReadOnly);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.Gen1Tag#getId()
	 */
	public byte[] getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.tags.Gen1Tag#setId(byte[])
	 */
	public void setId(byte[] id) throws InvalidMemoryAccessException {
		if ( id != null )
			throw new InvalidMemoryAccessException(TagErrors.TagIsReadOnly);
		else
			this.id = id;
	}
}