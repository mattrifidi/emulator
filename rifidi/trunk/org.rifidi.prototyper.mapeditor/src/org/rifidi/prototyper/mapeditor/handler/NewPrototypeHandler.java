package org.rifidi.prototyper.mapeditor.handler;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.prototyper.mapeditor.view.wizards.PrototyperCreationWizard;

public class NewPrototypeHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart wbpart = HandlerUtil.getActiveWorkbenchWindow(event)
				.getActivePage().getActivePart();

		PrototyperCreationWizard wizard = new PrototyperCreationWizard();
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection) {
			wizard.init(PlatformUI.getWorkbench(), (IStructuredSelection) sel);
		} else {
			wizard.init(PlatformUI.getWorkbench(), null);
		}
		WizardDialog wizardDialog = new WizardDialog(wbpart.getSite()
				.getShell(), wizard);
		wizardDialog.open();
		return null;
	}

}
