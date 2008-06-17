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
package org.rifidi.ui.ide.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.wizards.reader.NewReaderWizard;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;
import org.rifidi.ui.ide.views.readerview.ReaderView;

/**
 * @author Matthew Dean - matt@pramari.com
 *
 */
public class NewReaderHandler implements IHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#addHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#dispose()
	 */
	@Override
	public void dispose() {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IViewPart view = (IViewPart) HandlerUtil
		.getActiveWorkbenchWindow(event).getActivePage().findView(
				ReaderView.ID);
		
		UIReader reader = new UIReader();
		NewReaderWizard wizard = new NewReaderWizard(reader);
		WizardDialog wizardDialog = new WizardDialog(view.getSite().getShell(),
				wizard);
		if (wizardDialog.open() == Window.OK) {
			try {
				ReaderRegistry.getInstance().create(reader);
			} catch (DuplicateReaderException e) {
				// ignore this one.. we already care about that
				e.printStackTrace();
			}
		}
		
		
		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#isHandled()
	 */
	@Override
	public boolean isHandled() {
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#removeHandlerListener(org.eclipse.core.commands.IHandlerListener)
	 */
	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}

}
