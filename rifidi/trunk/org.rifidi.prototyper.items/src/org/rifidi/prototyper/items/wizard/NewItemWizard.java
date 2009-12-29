/**
 * 
 */
package org.rifidi.prototyper.items.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.view.ItemModelProviderSingleton;

/**
 * A wizard that creates a new item.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NewItemWizard extends Wizard implements INewWizard {

	/** The name of the item */
	protected String itemName;
	/** The ID of the item */
	protected String itemID;
	/** The type of item */
	protected String itemType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		ItemModel i = new ItemModel();
		i.setName(itemName);
		i.setTag(itemID);
		i.setType(itemType);
		ItemModelProviderSingleton.getModelProvider().addItem(i);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(new NewItemWizardTypeChooserPage("Type Chooser"));
		addPage(new NewItemWizardPage("New Item"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#canFinish()
	 */
	@Override
	public boolean canFinish() {
		return (itemName != null) && (itemID != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
	 * org.eclipse.jface.viewers.IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		// TODO Auto-generated method stub

	}

}
