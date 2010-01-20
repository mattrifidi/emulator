/**
 * 
 */
package org.rifidi.prototyper.items.wizard;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.rifidi.prototyper.items.model.ItemType;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.id.TagType;

/**
 * This page lets a user fill in the data necessary to create a new Item
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NewItemWizardPage extends WizardPage {

	/** The text control for the Item name */
	private Text nameText;
	private Combo tagTypeCombo;
	private Combo generationCombo;
	private Text tagPrefixText;
	private Spinner numberSpinner;
	private List<TagType> supportedFormats = Arrays.asList(TagType.values());
	private ItemType itemType;
	private Composite composite;

	/**
	 * Constuctor
	 * 
	 * @param pageName
	 */
	protected NewItemWizardPage(String pageName) {
		super(pageName);
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
		composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(2, false));
		setControl(composite);

	}

	private void draw() {
		setTitle("New Item Wizard");
		setDescription("Create a new " + itemType.getType() + " Item");
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
		nameText.setText(itemType.getType());
		Label numberLabel = new Label(composite, SWT.None);
		numberLabel.setText("Number of Items: ");
		numberSpinner = new Spinner(composite, SWT.None);
		numberSpinner.setMinimum(1);
		numberSpinner.setSelection(1);
		numberSpinner.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
				
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});

		Group IDGroup = new Group(composite, SWT.None);
		IDGroup.setLayout(new GridLayout(2, false));
		IDGroup.setText("Item ID");
		GridData idgroupGridData = new GridData(GridData.FILL_HORIZONTAL);
		idgroupGridData.horizontalSpan = 2;
		IDGroup.setLayoutData(idgroupGridData);

		// create label and combo for the generation of the tag
		Label generationLabel = new Label(IDGroup, SWT.NONE);
		generationLabel.setText("Select the tag generation");
		generationCombo = new Combo(IDGroup, SWT.BORDER | SWT.READ_ONLY);
		generationCombo.add("GEN1");
		generationCombo.add("GEN2");
		generationCombo.setText("GEN2");
		generationCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		generationCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});
		generationCombo.setEnabled(true);

		// create the label and combo for the datatype
		Label dataTypeLabel = new Label(IDGroup, SWT.None);
		dataTypeLabel.setText("Select the tag datatype");
		tagTypeCombo = new Combo(IDGroup, SWT.BORDER | SWT.READ_ONLY);
		tagTypeCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		for (TagType value : supportedFormats) {
			tagTypeCombo.add(value.toString());
		}
		tagTypeCombo.setText(TagType.GID96.toString());
		tagTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});

		Label tagPrefixLabel = new Label(IDGroup, SWT.NONE);
		tagPrefixLabel.setText("Custom ECP96 prefix");
		tagPrefixText = new Text(IDGroup, SWT.BORDER | SWT.SINGLE);
		tagPrefixText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tagPrefixText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		tagPrefixText.setEnabled(false);
		validate();
	}

	/**
	 * A method that validates after every key stroke.
	 */
	private void validate() {
		String name = nameText.getText();
		if (name == null || name.equals("")) {
			setPageComplete(false);
			setErrorMessage("Name must not be empty");
			return;
		}
		String tagGen = generationCombo.getItem(generationCombo
				.getSelectionIndex());
		if (tagGen == null || tagGen.equals("")) {
			setPageComplete(false);
			setErrorMessage("Tag Generation must be selected");
			return;
		}
		String tagType = tagTypeCombo.getItem(tagTypeCombo.getSelectionIndex());
		String prefixText;
		if (tagType == null || tagType.equals("")) {
			setPageComplete(false);
			setErrorMessage("Tag Type must be selected");
			return;
		}
		if (TagType.valueOf(tagType).equals(TagType.CustomEPC96)) {
			if (!this.tagPrefixText.isEnabled()) {
				this.tagPrefixText.setEnabled(true);
			}
			prefixText = this.tagPrefixText.getText();
			if (prefixText == null || prefixText.equals("")) {
				setErrorMessage("A valid EPC header must be supplied");
				setPageComplete(false);
				return;
			}
		} else {
			this.tagPrefixText.clearSelection();
			this.tagPrefixText.setEnabled(false);
		}
		setErrorMessage(null);
		setPageComplete(true);
		setData();
	}

	private void setData() {
		((NewItemWizard) this.getWizard()).itemName = nameText.getText();
		((NewItemWizard) this.getWizard()).itemType = itemType;
		((NewItemWizard) this.getWizard()).number = Integer
				.parseInt(numberSpinner.getText());
		((NewItemWizard) this.getWizard()).tagGen = TagGen
				.valueOf(this.generationCombo.getItem(this.generationCombo
						.getSelectionIndex()));

		((NewItemWizard) this.getWizard()).tagType = TagType
				.valueOf(this.tagTypeCombo.getItem(this.tagTypeCombo
						.getSelectionIndex()));

		((NewItemWizard) this.getWizard()).tagPrefix = tagPrefixText.getText();
	}

	/**
	 * @param itemType
	 */
	protected void setItemType(ItemType itemType) {
		this.itemType = itemType;
		for (Control c : composite.getChildren()) {
			c.dispose();
		}
		draw();
		composite.layout();

	}

}
