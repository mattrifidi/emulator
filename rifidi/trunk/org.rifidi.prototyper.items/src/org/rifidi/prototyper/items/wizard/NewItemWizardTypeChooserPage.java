/**
 * 
 */
package org.rifidi.prototyper.items.wizard;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.rifidi.prototyper.items.Activator;
import org.rifidi.prototyper.items.model.ItemType;
import org.rifidi.prototyper.items.service.ItemTypeRegistry;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NewItemWizardTypeChooserPage extends WizardPage {

	private ItemTypeRegistry itemTypeRegistry;
	private ItemType selection;

	protected NewItemWizardTypeChooserPage(String pageName) {
		super(pageName);
		ServiceRegistry.getInstance().service(this);
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
		setTitle("New Item Wizard");
		setDescription("Choose the Type of Item to Create");
		Composite composite = new Composite(parent, SWT.None);
		GridLayout gridLayout = new GridLayout(1, true);
		composite.setLayout(gridLayout);

		Tree tree = new Tree(composite, SWT.None);
		GridData treeGridData = new GridData(GridData.FILL_HORIZONTAL);
		treeGridData.heightHint = 200;
		treeGridData.grabExcessHorizontalSpace = true;
		tree.setLayoutData(treeGridData);
		fillTree(tree);
		tree.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.item.getData() instanceof ItemType) {
					selection = (ItemType) e.item.getData();
					validate();
				} else {
					setPageComplete(false);
				}
			}

		});

	}

	/**
	 * Private helper method to fill the tree with possible command choices
	 * 
	 * @param tree
	 */
	private void fillTree(Tree tree) {
		Map<String, TreeItem> categoriesToTreeItem = new HashMap<String, TreeItem>();
		// step through each command.
		for (String category : this.itemTypeRegistry.getItemCategories()) {
			TreeItem categoryTreeItem = categoriesToTreeItem.get(category);
			// create a command category if one does not exist
			if (categoryTreeItem == null) {
				categoryTreeItem = new TreeItem(tree, SWT.None);
				categoryTreeItem.setData(category);
				// ensure first letter is capitalized
				String first = category.substring(0, 1);
				String rest = category.substring(1);
				categoryTreeItem.setText(first.toUpperCase() + rest);
				categoryTreeItem.setImage(Activator.getDefault()
						.getImageRegistry().get(Activator.IMAGE_FOLDER));
				categoriesToTreeItem.put(category, categoryTreeItem);

			}
			for (ItemType itemType : itemTypeRegistry.getItemTypes(category)) {
				TreeItem itemTypeTreeItem = new TreeItem(categoryTreeItem,
						SWT.None);
				itemTypeTreeItem.setText(itemType.getType());
				itemTypeTreeItem.setData(itemType);
				itemTypeTreeItem.setImage(Activator.getDefault()
						.getImageRegistry().get(itemType.getType()));
				categoryTreeItem.setExpanded(true);
			}

		}

	}
	
	/**
	 * Private method called once a command has been chosen
	 */
	private void validate() {
		setPageComplete(true);
	}
	
	protected ItemType getSelectedItemType(){
		return this.selection;
	}

	/**
	 * @param itemTypeRegistry
	 *            the itemTypeRegistry to set
	 */
	@Inject
	public void setItemTypeRegistry(ItemTypeRegistry itemTypeRegistry) {
		this.itemTypeRegistry = itemTypeRegistry;
	}

}
