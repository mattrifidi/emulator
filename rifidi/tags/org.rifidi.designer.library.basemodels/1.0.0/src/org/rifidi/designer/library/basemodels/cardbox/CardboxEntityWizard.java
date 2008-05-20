/*
 *  CardboxEntityWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.cardbox;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityWizardIface;

/**
 * Wizard for the creation of a CardboxEntity.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class CardboxEntityWizard extends RifidiEntityWizard implements EntityWizardIface {
	/**
	 * Wizard page for this wizard.
	 */
	private CardboxEntityWizardPage cardboxEntityWizardPage;
	/**
	 * The new cardbox.
	 */
	private CardboxEntity entity = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		cardboxEntityWizardPage = new CardboxEntityWizardPage(
				"PusharmEntityWizard");
		addPage(cardboxEntityWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if ( takenNamesList.contains(cardboxEntityWizardPage.getName()) ) {
			cardboxEntityWizardPage.setErrorMessage(ENTITY_NAME_TAKEN_MESSAGE);
			return false;
		}

		entity = new CardboxEntity();
		entity.setName(cardboxEntityWizardPage.getName());
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
