/*
 *  BoxproducerEntityWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.boxproducerSGTIN96;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityWizardIface;

/**
 * Wizard for the creation of a BoxproducerEntityGID96.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class BoxproducerEntityWizard extends RifidiEntityWizard implements EntityWizardIface {
	/**
	 * Wizard page for this wizard.
	 */
	private BoxproducerEntityWizardPage boxproducerEntityWizardPage;
	/**
	 * The new boxproducer.
	 */
	private BoxproducerEntitySGTIN96 entity = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		boxproducerEntityWizardPage = new BoxproducerEntityWizardPage(
				"PusharmEntityWizard");
		addPage(boxproducerEntityWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if ( takenNamesList.contains(boxproducerEntityWizardPage.getName()) ) {
			boxproducerEntityWizardPage.setErrorMessage(ENTITY_NAME_TAKEN_MESSAGE);
			return false;
		}

		entity = new BoxproducerEntitySGTIN96();
		entity.setName(boxproducerEntityWizardPage.getName());
		entity.setSpeed(boxproducerEntityWizardPage.getSpeed());
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
