/**
 * 
 */
package org.rifidi.prototyper.items.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.prototyper.items.wizard.NewItemWizard;

/**
 * This class is a handler for the add Item command.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AddItemHandler extends AbstractHandler implements IHandler {

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
		NewItemWizard wizard = new NewItemWizard();
		WizardDialog wd = new WizardDialog(window.getShell(), wizard);
		wd.open();
		return null;
	}

}
