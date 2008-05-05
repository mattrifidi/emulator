package org.rifidi.ui.ide.workbench.wizards;

import org.eclipse.jface.wizard.Wizard;

/**
 * The wizard for gathering information about the RMI Server to connect to. It's
 * defining the basic wizard and the containing pages.
 * 
 * See also ConnectionsSettingsWizardPage.java
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ConnectionsSettingsWizard extends Wizard {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(new ConnectionSettingsWizardPage());
		super.addPages();
	}

	@Override
	public boolean performFinish() {
		// TODO Auto-generated method stub
		return true;
	}

}
