package org.rifidi.ui.streamer.composites.items;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.actions.BatchAction;

/**
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
public class BatchActionComposite extends Composite {

	private BatchAction batchAction;
	private Spinner batchIDSpinner;
	private Spinner scenarioIDSpinner;

	public BatchActionComposite(Composite parent, int style,
			BatchAction batchAction) {
		super(parent, style);

		this.batchAction = batchAction;

		setLayout(new GridLayout(2, false));

		Label batchIDLabel = new Label(this, SWT.NONE);
		batchIDLabel.setText("Batch ID:");
		batchIDSpinner = new Spinner(this, SWT.BORDER);
		batchIDSpinner.setMaximum(1000);

		Label scenarioIDLabel = new Label(this, SWT.NONE);
		scenarioIDLabel.setText("Scenario ID:");
		scenarioIDSpinner = new Spinner(this, SWT.BORDER);
		scenarioIDSpinner.setMaximum(1000);

		Button saveButton = new Button(this, SWT.PUSH);
		saveButton.setText("Save");
		saveButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				saveChanges();
			}
		});

		updateWidgets();
	}

	private void updateWidgets() {
		batchIDSpinner.setSelection(batchAction.getBatchID());
		scenarioIDSpinner.setSelection(batchAction.getScenarioID());
	}

	private void saveChanges() {
		batchAction.setBatchID(batchIDSpinner.getSelection());
		batchAction.setScenarioID(scenarioIDSpinner.getSelection());
	}
}
