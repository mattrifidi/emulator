package org.rifidi.ui.ide.views.tagview.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.rifidi.ui.common.wizards.tag.MultipleNewTagsWizard;
import org.rifidi.ui.ide.views.tagview.TagView;

/**
 * FIXME: Header.  
 * 
 * This it the Action for creating and adding multiple tags. It's using the
 * MultipleTagWizard to create the tags. It's contributed to the RCP Application
 * by defining it in the plugin.xml
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class AddMultipleTagsActionDelegate implements IViewActionDelegate, IWorkbenchWindowActionDelegate {

	private static IViewPart view = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		AddMultipleTagsActionDelegate.view = view;
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
		//List<RifidiTag> tagList = new ArrayList<RifidiTag>();
		MultipleNewTagsWizard wizard = new MultipleNewTagsWizard();
		WizardDialog wizardDialog = new WizardDialog(view.getSite().getShell(),
				wizard);
		wizardDialog.open();
		((TagView) view).refresh();
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
