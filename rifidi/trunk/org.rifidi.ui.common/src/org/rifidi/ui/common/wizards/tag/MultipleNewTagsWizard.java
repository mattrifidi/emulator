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
package org.rifidi.ui.common.wizards.tag;

import org.eclipse.jface.wizard.Wizard;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.ui.common.wizards.tag.pages.MultipleNewTagsWizardPage;

/**
 * A wizard that allows the creation of an arbitrary number of tags.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class MultipleNewTagsWizard extends Wizard {

	private MultipleNewTagsWizardPage multipleNewTagsWizardPage;
	
	private ITagRegistry tagRegistry;

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
		if ( getPage("wizardPage").isPageComplete() ) {
			multipleNewTagsWizardPage.createTags(tagRegistry);
			return true;
		}
		return false;
	}
	
	@Inject
	public void setTagRegistryService(ITagRegistry tagRegisrty)
	{
		this.tagRegistry = tagRegisrty;
		
	}
}