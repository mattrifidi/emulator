/*
 *  Directional.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.interfaces;

/**
 * Interface used by directional entities to show which way they are oriented.
 * 
 * TODO: temporary solution, will be replaced
 * @author Dan West - dan@pramari.com
 */
public interface Directional {

	/**
	 * Makes the entity's directional indicator visible.
	 */
	void showDirectionalIndicator();

	/**
	 * Hides the entity's directional indicator.
	 */
	void hideDirectionalIndicator();
}
