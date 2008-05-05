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

import java.util.List;

import org.eclipse.jface.wizard.Wizard;
import org.rifidi.emulator.tags.impl.RifidiTag;
import org.rifidi.ui.common.wizards.tag.pages.MultipleNewTagsWizardPage;

/**
 * A wizard that allows the creation of an arbitrary number of tags.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class MultipleNewTagsWizard extends Wizard {

	private MultipleNewTagsWizardPage multipleNewTagsWizardPage;
	private List<RifidiTag> taglist;

	public MultipleNewTagsWizard( List<RifidiTag> taglist ) {
		this.taglist = taglist;
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
			multipleNewTagsWizardPage.createTags(taglist);
			return true;
		}
		return false;
	}
}