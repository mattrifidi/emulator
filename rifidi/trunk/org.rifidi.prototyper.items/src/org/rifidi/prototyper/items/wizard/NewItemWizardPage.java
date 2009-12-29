/**
 * 
 */
package org.rifidi.prototyper.items.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.prototyper.items.service.ItemTypeRegistry;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * This page lets a user fill in the data necessary to create a new Item
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NewItemWizardPage extends WizardPage {

	/** The text control for the Item name */
	private Text nameText;
	/** The text control for the Item ID */
	private Text IDText;
	/** Combo type chooser */
	private Combo combo;
	private ItemTypeRegistry itemTypeRegistry;

	/**
	 * Constuctor
	 * 
	 * @param pageName
	 */
	protected NewItemWizardPage(String pageName) {
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
		super.setDescription("Create a new Item");
		super.setTitle("New Item");
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(2, false));

		Label itemType = new Label(composite, SWT.NONE);
		itemType.setText("Item Type");
		combo = new Combo(composite, SWT.READ_ONLY);
		for (String s : itemTypeRegistry.getAllItemTypes()) {
			combo.add(s);
		}
		combo.select(0);
		combo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		Label nameLabel = new Label(composite, SWT.None);
		nameLabel.setText("Item Name");
		nameText = new Text(composite, SWT.NONE);
		nameText.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				validate();

			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		nameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label IDLabel = new Label(composite, SWT.None);
		IDLabel.setText("Item ID");
		IDText = new Text(composite, SWT.NONE);
		IDText.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				validate();
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		IDText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		setControl(composite);
		setPageComplete(true);

	}

	/**
	 * A method that validates after every key stroke.
	 */
	private void validate() {
		if (nameText.getText() == null) {
			setPageComplete(false);
			return;
		}
		if (nameText.getText().equals("")) {
			setPageComplete(false);
			return;
		}
		if (IDText.getText() == null) {
			setPageComplete(false);
			return;
		}
		if (IDText.getText().equals("")) {
			setPageComplete(false);
			return;
		}
		((NewItemWizard) this.getWizard()).itemName = nameText.getText();
		((NewItemWizard) this.getWizard()).itemID = IDText.getText();
		String itemType = combo.getItem(combo.getSelectionIndex());
		((NewItemWizard) this.getWizard()).itemType = itemType;
		setPageComplete(true);

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
