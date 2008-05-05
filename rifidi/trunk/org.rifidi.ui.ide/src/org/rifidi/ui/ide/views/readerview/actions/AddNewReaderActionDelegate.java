package org.rifidi.ui.ide.views.readerview.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.wizards.reader.NewReaderWizard;
import org.rifidi.ui.common.wizards.reader.exceptions.DuplicateReaderException;

/**
 * This is the Action which gets invoked everytime the Button for creating a new
 * reader is pressed. It's contributed to the RCP Application by defining it in
 * the plugin.xml.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class AddNewReaderActionDelegate implements IViewActionDelegate, IWorkbenchWindowActionDelegate {

	// private Log logger = LogFactory.getLog(AddNewReaderActionDelegate.class);

	private static IViewPart view = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		AddNewReaderActionDelegate.view = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
	}
}
