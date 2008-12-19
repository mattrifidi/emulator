/*
 *  MultipleNewTagsWizardPage.java
 *
 *  Created:	Mar 6, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.wizards.tag.pages;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.factory.TagCreationPattern;
import org.rifidi.tags.id.TagType;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.ui.common.validators.HexValidator;

/**
 * On this page you can select a tagtype, tag generation and a number to create
 * that number of tags.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
@Deprecated
public class MultipleNewTagsWizardPage extends WizardPage {

	private Log logger = LogFactory.getLog(MultipleNewTagsWizardPage.class);

	/**
	 * combobox containing the available tagformats
	 */
	private Combo tagTypeCombo;

	/**
	 * combobox for selecting the generation of the tag
	 */
	private Combo generationCombo;
	/**
	 * input field for the tag data
	 */
	private Spinner tagNrSpinner;

	/**
	 * input field for the Prefix on CustomEPC96 Tags
	 */
	private Text tagPrefixText;

	private HexValidator hexValidator = new HexValidator();

	private List<TagType> supportedFormats = Arrays.asList(TagType.values());

	public MultipleNewTagsWizardPage(String pageName) {
		super(pageName);
		setTitle("Create multiple tags");
		setDescription("Here you can create as many tags of one type as you want.");
	}

	// private HexValidator hexValidator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		// TODO CustomEPC96 Tag
		// hexValidator = new HexValidator();
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		// we want two rows
		layout.numColumns = 2;
		composite.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		Label dataTypeLabel = new Label(composite, SWT.NONE);

		// create the label and combo for the datatype
		dataTypeLabel.setText("Select the tag datatype");
		tagTypeCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		tagTypeCombo.setLayoutData(gridData);
		for (TagType value : supportedFormats) {
			tagTypeCombo.add(value.toString());
		}
		tagTypeCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});

		// create label and combo for the generation of the tag
		Label generationLabel = new Label(composite, SWT.NONE);
		generationLabel.setText("Select the tag generation");
		generationCombo = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		generationCombo.add("GEN1");
		generationCombo.add("GEN2");
		generationCombo.setLayoutData(gridData);
		generationCombo.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {

			}

			public void widgetSelected(SelectionEvent e) {
				dialogChanged();
			}
		});
		generationCombo.setEnabled(false);
		// create label and textfield for the tagdata
		Label tagData = new Label(composite, SWT.NONE);
		tagData.setText("Enter the amount of tags to create");
		tagNrSpinner = new Spinner(composite, SWT.BORDER | SWT.SINGLE);
		tagNrSpinner.setLayoutData(gridData);
		tagNrSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		tagNrSpinner.setEnabled(false);

		Label tagPrefixLabel = new Label(composite, SWT.NONE);
		tagPrefixLabel.setText("Enter tag prefix");
		tagPrefixText = new Text(composite, SWT.BORDER | SWT.SINGLE);
		tagPrefixText.setLayoutData(gridData);
		tagPrefixText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});
		tagPrefixText.setEnabled(false);

		setPageComplete(false);
		setControl(composite);
	}

	/**
	 * called everytime a combo's or textfield's input changes
	 * 
	 */
	private void dialogChanged() {
		if (tagTypeCombo.getSelectionIndex() > -1) {
			generationCombo.setEnabled(true);
			if (generationCombo.getSelectionIndex() > -1) {
				tagNrSpinner.setEnabled(true);
				if (tagNrSpinner.getSelection() > 0) {
					if (tagTypeCombo.getText().equals(TagType.CustomEPC96.toString())) {
						tagPrefixText.setEnabled(true);
						if (tagPrefixText.getText().length() < 24) {
							String errorString = hexValidator
									.isValid(tagPrefixText.getText());
							if (errorString == null) {
								setErrorMessage(null);
								setPageComplete(true);
							} else {
								setErrorMessage(errorString);
								setPageComplete(false);
							}
						}
					}else
					{
						setPageComplete(true);
					}
				}
			}
		}
	}

	public void createTags(ITagRegistry tagRegistry) {
		TagGen tagGen = null;
		String selectedTagGen = generationCombo.getItem(generationCombo
				.getSelectionIndex());
		String selectedTagType = tagTypeCombo.getItem(tagTypeCombo
				.getSelectionIndex());
		String prefix = tagPrefixText.getText();
		int number = tagNrSpinner.getSelection();
		if (selectedTagGen.equals("GEN2"))
			tagGen = TagGen.GEN2;
		if (selectedTagGen.equals("GEN1"))
			tagGen = TagGen.GEN1;
		
		TagCreationPattern pattern = new TagCreationPattern();
		pattern.setNumberOfTags(number);
		pattern.setTagGeneration(tagGen);
		pattern.setTagType(TagType.valueOf(selectedTagType));
		pattern.setPrefix(prefix);
		tagRegistry.createTags(pattern);

		
		logger.debug("Wizard created " + tagRegistry.getTags().size() + " tags.");
	}
}