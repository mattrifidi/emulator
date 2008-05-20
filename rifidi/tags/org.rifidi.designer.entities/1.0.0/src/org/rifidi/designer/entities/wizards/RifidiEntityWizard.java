/*
 *  RifidiEntityWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.wizards;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

/**
 * Superclass for all wizards used to create entities.
 * 
 * @author Dan West - 'Phoenix' - dan@pramari.com
 * @author Jochen Mader
 */
public abstract class RifidiEntityWizard extends Wizard {
	/**
	 * Failure message.
	 */
	protected final String ENTITY_NAME_TAKEN_MESSAGE = "An entity with the given name already exists!";
	/**
	 * List of already taken entity names.
	 */
	protected List<String> takenNamesList;

	/**
	 * Sets the list of entity names that are already taken.
	 * @param takenNames the list of names that are taken already
	 */
	public void setTakenNamesList(final List<String> takenNamesList) {
		this.takenNamesList = takenNamesList;
	}
}