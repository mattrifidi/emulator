/*
 *  PusharmEntityWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.pusharm;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityWizardIface;

import com.jme.scene.Node;

/**
 * Wizard for the creation of a PusharmEntity.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class PusharmEntityWizard extends RifidiEntityWizard implements
		EntityWizardIface {
	/**
	 * Wizard page for the wizard.
	 */
	private PusharmEntityWizardPage pusharmEntityWizardPage;
	/**
	 * The newly created entity.
	 */
	private PusharmEntity entity = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();
		pusharmEntityWizardPage = new PusharmEntityWizardPage(
				"PusharmEntityWizard");
		addPage(pusharmEntityWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (takenNamesList.contains(pusharmEntityWizardPage.getName())) {
			pusharmEntityWizardPage.setErrorMessage(ENTITY_NAME_TAKEN_MESSAGE);
			return false;
		}

		entity = new PusharmEntity();
		entity.setName(pusharmEntityWizardPage.getName());
		entity.setSpeed(pusharmEntityWizardPage.getSpeed());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityWizardIface#setRootNode(com.jme.scene.Node)
	 */
	public void setRootNode(Node node) {
	}

}
