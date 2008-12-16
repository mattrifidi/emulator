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
package org.rifidi.services.tags.impl;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.services.tags.IGen1Tag;
import org.rifidi.services.tags.enums.TagErrors;
import org.rifidi.services.tags.enums.TagGen;
import org.rifidi.services.tags.exceptions.InvalidMemoryAccessException;

/**
 * Implementation of Class 1 Generation 1 (Write Once, Read Many)
 * 
 * @author Jochen Mader
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class C1G1Tag implements IGen1Tag{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private byte[] id;
	
	private boolean writeable=true;
	
	/**
	 * NoArg Constructor for JAXB.
	 */
	public C1G1Tag(){
		
	}
	
	public C1G1Tag(byte[] id) {
		super();
		this.id = id;
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
		if(writeable==true){
			this.id=id;
			writeable=false;
		}
		else throw new InvalidMemoryAccessException(TagErrors.TagIsReadOnly);
	}

	/**
	 * @return the writeable
	 */
	public boolean isWriteable() {
		return writeable;
	}

	/**
	 * @param writeable the writeable to set
	 */
	public void setWriteable(boolean writeable) {
		this.writeable = writeable;
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