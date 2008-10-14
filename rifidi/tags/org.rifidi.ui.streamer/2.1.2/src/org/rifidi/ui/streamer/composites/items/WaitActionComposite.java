/**
 * 
 */
package org.rifidi.ui.streamer.composites.items;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.actions.WaitAction;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class WaitActionComposite extends Composite {

	private Spinner maxWaitTimeSpinner;
	private Spinner minWaitTimeSpinner;
	private Button isRandomCheckBox;

	private WaitAction waitAction;

	public WaitActionComposite(Composite parent, int style,
			WaitAction waitAction) {
		super(parent, style);

		this.waitAction = waitAction;
		setLayout(new GridLayout(2, false));

		Label isRandomLabel = new Label(this, SWT.NONE);
		isRandomLabel.setText("Is random intervall");
		isRandomCheckBox = new Button(this, SWT.CHECK);
		isRandomCheckBox.setAlignment(SWT.LEFT);
		isRandomCheckBox.setText("");

		Label minWaitTimeLabel = new Label(this, SWT.NONE);
		minWaitTimeLabel.setText("min. wait time (ms):");
		minWaitTimeSpinner = new Spinner(this, SWT.BORDER);
		minWaitTimeSpinner.setMaximum(1000000000);

		Label maxWaitTimeLabel = new Label(this, SWT.NONE);
		maxWaitTimeLabel.setText("max. wait time (ms):");
		maxWaitTimeSpinner = new Spinner(this, SWT.BORDER);
		maxWaitTimeSpinner.setMaximum(1000000000);
		maxWaitTimeSpinner.setEnabled(false);

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

		minWaitTimeSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				maxWaitTimeSpinner
						.setMinimum(minWaitTimeSpinner.getSelection());
			}

		});

		isRandomCheckBox.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				maxWaitTimeSpinner.setEnabled(isRandomCheckBox.getSelection());
			}

		});

		updateWidgets();
	}

	private void updateWidgets() {
		maxWaitTimeSpinner.setSelection((int) waitAction.getMaxWaitTime());
		minWaitTimeSpinner.setSelection((int) waitAction.getMinWaitTime());
		isRandomCheckBox.setSelection(waitAction.isRandom());
		if (waitAction.isRandom())
			maxWaitTimeSpinner.setEnabled(true);
	}

	public void saveChanges() {
		long maxTime = (long) maxWaitTimeSpinner.getSelection();
		long minTime = (long) minWaitTimeSpinner.getSelection();
		if (minTime > maxTime) {
			MessageDialog.openError(this.getShell(), "Values incorrect",
					"Minimum time is bigger than maximum time.");
		} else {
			waitAction.setMaxWaitTime(maxTime);
			waitAction.setMinWaitTime(minTime);
			waitAction.setRandom(isRandomCheckBox.getSelection());
		}
	}
}
