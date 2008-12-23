/*
 *  MultipleNewTagsWizard.java
 *
 *  Created:	Mar 6, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.views.tags.wizard;

import org.eclipse.jface.wizard.Wizard;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.tags.factory.TagCreationPattern;

/**
 * A wizard that allows the creation of an arbitrary number of tags.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class MultipleNewTagsWizard extends Wizard {

	private MultipleNewTagsWizardPage multipleNewTagsWizardPage;

	private TagCreationPattern pattern;

	public MultipleNewTagsWizard() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		multipleNewTagsWizardPage = new MultipleNewTagsWizardPage("wizardPage");
		addPage(multipleNewTagsWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		if (getPage("wizardPage").isPageComplete()) {
			pattern = multipleNewTagsWizardPage.getPattern();
			return true;
		}
		return false;
	}

	/**
	 * @return the pattern
	 */
	public TagCreationPattern getPattern() {
		return this.pattern;
	}
}