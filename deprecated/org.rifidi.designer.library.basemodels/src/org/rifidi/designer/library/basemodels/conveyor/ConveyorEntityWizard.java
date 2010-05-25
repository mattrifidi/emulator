/*
 *  ConveyorEntityWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.conveyor;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityWizardIface;

/**
 * Wizard for the creation of a ConveyorEntity.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class ConveyorEntityWizard extends RifidiEntityWizard implements
		EntityWizardIface {
	/**
	 * Wizardpage for this wizard.
	 */
	private ConveyorEntityWizardPage conveyorEntityWizardPage;
	/**
	 * The newly created entity.
	 */
	private ConveyorEntity entity = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		conveyorEntityWizardPage = new ConveyorEntityWizardPage(
				"PusharmEntityWizard");
		addPage(conveyorEntityWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (takenNamesList.contains(conveyorEntityWizardPage.getName())) {
			conveyorEntityWizardPage.setErrorMessage(ENTITY_NAME_TAKEN_MESSAGE);
			return false;
		}

		entity = new ConveyorEntity();
		entity.setName(conveyorEntityWizardPage.getName());
		entity.setSpeed(conveyorEntityWizardPage.getSpeed());
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityWizardIface#getEntity()
	 */
	public Entity getEntity() {
		return entity;
	}

}
