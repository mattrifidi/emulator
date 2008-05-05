/**
 * 
 */
package org.rifidi.ui.common.wizards.reader.pages;

import java.util.Map;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class NewReaderGPIOWizardPage extends WizardPage {

	private UIReader reader;

	private Spinner numGPISpinner;
	private Spinner numGPOSpinner;
	int maxGPI;
	int maxGPO;
	private Map<String, ReaderBlueprint> availableReaders;

	public NewReaderGPIOWizardPage(String pageName, UIReader reader,
			Map<String, ReaderBlueprint> availableReaders) {
		super(pageName);
		this.reader = reader;

		setTitle("GPIO Settings Page");
		setDescription("Fill out all fields and hit finish to add a reader");

		setPageComplete(false);
		this.availableReaders = availableReaders;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {

		maxGPI = availableReaders.get(reader.getReaderType()).getMaxgpis();
		maxGPO = availableReaders.get(reader.getReaderType()).getMaxgpos();

		// Get composite
		Composite composite = new Composite(parent, SWT.NONE);

		// Set composite and layout
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		// Label and Textfield for GPI
		Label gpiLabel = new Label(composite, SWT.NONE);
		gpiLabel.setText("Please enter number of GPI's");

		numGPISpinner = new Spinner(composite, SWT.BORDER);
		numGPISpinner.setLayoutData(gridData);
		numGPISpinner.setMaximum(maxGPI);
		numGPISpinner.setSelection(maxGPI);

		numGPISpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		// Label and Textfield for GPO
		Label gpoLabel = new Label(composite, SWT.NONE);
		gpoLabel.setText("Please enter number of GPO's");

		numGPOSpinner = new Spinner(composite, SWT.BORDER);
		numGPOSpinner.setLayoutData(gridData);
		numGPOSpinner.setMaximum(maxGPO);
		numGPOSpinner.setSelection(maxGPO);

		numGPOSpinner.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		// page controls
		dialogChanged();
		setPageComplete(true);
		setControl(composite);
		composite.layout();
	}

	private void dialogChanged() {
		if (numGPOSpinner.getSelection() > 0
				|| numGPISpinner.getSelection() > 0) {
			reader.setNumGPIs(numGPISpinner.getSelection());
			reader.setNumGPOs(numGPOSpinner.getSelection());
			setPageComplete(true);
		} else
			setPageComplete(false);

	}

}
