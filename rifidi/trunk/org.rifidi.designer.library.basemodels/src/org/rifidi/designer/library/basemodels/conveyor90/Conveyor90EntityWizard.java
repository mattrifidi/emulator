package org.rifidi.designer.library.basemodels.conveyor90;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityWizardIface;

public class Conveyor90EntityWizard extends RifidiEntityWizard implements EntityWizardIface {
	private Conveyor90EntityWizardPage wizPage = null;
	
	private Conveyor90Entity entity = null; 
	
	

	@Override
	public void addPages() {
		super.addPages();
		wizPage = new Conveyor90EntityWizardPage("Conveyor Entity Wizard");
		addPage(wizPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if ( takenNamesList.contains(wizPage.getName()) ) {
			wizPage.setErrorMessage(ENTITY_NAME_TAKEN_MESSAGE);
			return false;
		}

		entity = new Conveyor90Entity();
		entity.setName(wizPage.getName());
		entity.setSpeed(wizPage.getSpeed());
		return true;
	}

	public Entity getEntity() {
		return entity;
	}

}
