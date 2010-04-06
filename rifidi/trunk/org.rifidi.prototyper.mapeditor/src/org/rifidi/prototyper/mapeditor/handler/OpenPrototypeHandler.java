/**
 * 
 */
package org.rifidi.prototyper.mapeditor.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.prototyper.mapeditor.view.wizards.PrototyperOpenWizard;

/**
 * This is a handler class for opening an existing prototype
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class OpenPrototypeHandler extends AbstractHandler implements IHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		PrototyperOpenWizard wizard = new PrototyperOpenWizard();
		wizard.init(PlatformUI.getWorkbench(), null);
		WizardDialog wizardDialog = new WizardDialog(window.getShell(), wizard);
		wizardDialog.open();
		return null;
	}

}
