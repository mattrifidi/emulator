/*
 *  NewReaderWizard.java
 *
 *  Created:	Feb 28, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.wizards.reader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * A wizard to create a new reader.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class NewReaderWizard extends Wizard {

	private static Log logger = LogFactory.getLog(NewReaderWizard.class);

	private NewReaderWizardSelectionPage newReaderWizardSelectionPage;

	private NewReaderDynamicWizardPage newReaderDynamicWizardPage;

	private NewReaderGPIOWizardPage newReaderGPIOWizardPage;

	private ReaderWizardData data = new ReaderWizardData();

	private ReaderRegistryService readerRegistry;

	public NewReaderWizard() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * @param readerRegistry
	 *            the readerRegistry to set
	 */
	@Inject
	public void setReaderRegistry(ReaderRegistryService readerRegistry) {
		this.readerRegistry = readerRegistry;
	}

	@Override
	public void addPages() {
		newReaderWizardSelectionPage = new NewReaderWizardSelectionPage(
				"readerSelectionPage", data, readerRegistry
						.getReaderBlueprints());
		addPage(newReaderWizardSelectionPage);

		newReaderDynamicWizardPage = new NewReaderDynamicWizardPage(
				"readerCreationPage", data, readerRegistry
						.getReaderBlueprints());
		addPage(newReaderDynamicWizardPage);

		newReaderGPIOWizardPage = new NewReaderGPIOWizardPage("gpioPage", data,
				readerRegistry.getReaderBlueprints());
		addPage(newReaderGPIOWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer) {
		newReaderWizardSelectionPage.createControl(pageContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// check if the page is ready to be processed
		if (newReaderDynamicWizardPage.isPageComplete()) {
			logger.debug("Wizard finished. Reader name " + data.readerType);
			return true;
		}
		return false;
	}

	@Override
	public boolean canFinish() {
		if (!newReaderDynamicWizardPage.enableGPIO()
				&& newReaderDynamicWizardPage.isPageComplete())
			return true;
		if (newReaderDynamicWizardPage.isPageComplete()
				&& newReaderGPIOWizardPage.isPageComplete())
			return true;
		return false;
	}

	public ReaderWizardData getReaderWizardData() {
		return data;
	}
}
