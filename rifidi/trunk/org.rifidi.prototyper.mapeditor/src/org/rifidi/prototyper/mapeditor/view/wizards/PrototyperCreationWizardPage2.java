/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

/**
 * The second page when creating a new prototype. Allows a user to provide the
 * scale of the map.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class PrototyperCreationWizardPage2 extends WizardPage {

	private SWTImageCanvas canvas;
	private Spinner scaleSpinner;

	/**
	 * @param pageName
	 */
	protected PrototyperCreationWizardPage2(String pageName) {
		super(pageName);
		this.setTitle("New Prototype");
		this.setDescription("Provide the scale of the blueprint");
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
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new GridLayout(2, false));

		Label scaleLabel = new Label(composite, SWT.None);
		scaleLabel
				.setText("Approximate distance (feet) between two grid lines: ");
		scaleLabel.setLayoutData(new GridData());

		scaleSpinner = new Spinner(composite, SWT.BORDER);
		scaleSpinner.setDigits(2);
		scaleSpinner.setMaximum(1000 * 100);
		scaleSpinner.setMinimum(1);
		scaleSpinner.setIncrement(100);
		scaleSpinner.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		scaleSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				PrototyperCreationWizard wizard = (PrototyperCreationWizard) getWizard();
				wizard.feetPer30Px = new Float(scaleSpinner.getSelection())
						/ new Float(100);

			}
		});
		scaleSpinner.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				PrototyperCreationWizard wizard = (PrototyperCreationWizard) getWizard();
				wizard.feetPer30Px = new Float(scaleSpinner.getSelection())
						/ new Float(100);

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		canvas = new SWTImageCanvas(composite, SWT.None);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.horizontalSpan = 2;
		canvas.setLayoutData(data);

		setControl(composite);
		setPageComplete(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			PrototyperCreationWizard wizard = (PrototyperCreationWizard) getWizard();
			try {
				canvas.loadImage(wizard.pathToFile);
				setErrorMessage(null);
			} catch (Exception e) {
				setPageComplete(false);
				setErrorMessage("Cannot load image: " + wizard.pathToFile);
			}

			int num = Math.round(wizard.feetPer30Px * 100);
			scaleSpinner.setSelection(num);
		}
		super.setVisible(visible);
	}

}
