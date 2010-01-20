/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.ide.IDE;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class PrototyperOpenWizardPage1 extends WizardPage implements Listener {

	private IFile file;
	private IWorkbench workbench;

	/**
	 * @param pageName
	 */
	public PrototyperOpenWizardPage1(IWorkbench workbench, String pageName) {
		super(pageName);
		this.workbench = workbench;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		setTitle("Open an Existing Prototype");
		setDescription("Choose a Rifidi Prototype to Open");
		FileChooserGroup group = new FileChooserGroup(parent, this);
		setControl(group);
		validate();
	}

	protected boolean finish() {
		try {
			IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = dwindow.getActivePage();
			if (page != null){
				page.closeAllEditors(true);
				IDE.openEditor(page, file, true);
			}
		} catch (org.eclipse.ui.PartInitException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.
	 * Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if (event.data != null && event.data instanceof IFile) {
			file = (IFile) event.data;
		} else {
			file = null;
		}
		validate();
	}

	private void validate() {
		if (file != null) {
			setPageComplete(true);
		} else {
			setPageComplete(false);
		}
	}

}
