/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IWorkbench;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class PrototyperOpenWizard extends Wizard {

	private PrototyperOpenWizardPage1 page1;
	private IStructuredSelection selection;
	private IWorkbench workbench;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench, org.eclipse.jface.viewers.IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		page1 = new PrototyperOpenWizardPage1(workbench, "Open Prototype");
		addPage(page1);
		super.addPages();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		return page1.finish();
	}

}
