/* 
 * NewReaderHandler.java
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;
import org.rifidi.ui.common.wizards.reader.NewReaderWizard;
import org.rifidi.ui.common.wizards.reader.ReaderWizardData;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;

/**
 * @author Matthew Dean - matt@pramari.com
 * 
 */
public class NewReaderHandler extends AbstractHandler {

	/** The reader regsitry */
	private ReaderRegistryService readerRegistry;

	/**
	 * Constructor.  
	 */
	public NewReaderHandler() {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart wbpart = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getActivePart();

		NewReaderWizard wizard = new NewReaderWizard();
		WizardDialog wizardDialog = new WizardDialog(wbpart.getSite()
				.getShell(), wizard);
		if (wizardDialog.open() == Window.OK) {
			try {
				ReaderWizardData data = wizard.getReaderWizardData();
				readerRegistry.create(data.getGeneralReaderHolder());
			} catch (DuplicateReaderException e) {
				// ignore this one.. we already care about that
				e.printStackTrace();
			}
		}

		return null;
	}
}
