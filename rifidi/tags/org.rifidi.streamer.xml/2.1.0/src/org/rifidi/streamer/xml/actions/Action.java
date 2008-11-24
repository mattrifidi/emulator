/*
 *  Action.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.actions;

import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This is the abstract common class for all Actions in Streamer. It's necessary
 * for saving all of the different Actions in Streamer with JAXB into a XML File
 * and load it again.
 * 
 * @XMLSeeAlso statement to refer to the implementing classes
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@XmlSeeAlso( { GPIAction.class, TagAction.class, WaitAction.class,
		BatchAction.class })
public abstract class Action {

}
