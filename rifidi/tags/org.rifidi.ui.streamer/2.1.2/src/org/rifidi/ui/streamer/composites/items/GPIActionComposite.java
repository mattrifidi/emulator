package org.rifidi.ui.streamer.composites.items;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.actions.GPIAction;

/**
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
public class GPIActionComposite extends Composite {

	private GPIAction gpiAction;
	private Spinner portSpinner;
	private Button signalCheckbox;

	public GPIActionComposite(Composite parent, int style, GPIAction gpiAction) {
		super(parent, style);

		this.gpiAction = gpiAction;

		setLayout(new GridLayout(2, false));

		Label portLabel = new Label(this, SWT.NONE);
		portLabel.setText("GPI Port:");
		portSpinner = new Spinner(this, SWT.BORDER);
		portSpinner.setMaximum(50);

		Label signalLabel = new Label(this, SWT.NONE);
		signalLabel.setText("Signal on/off");
		signalCheckbox = new Button(this, SWT.CHECK);

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
		portSpinner.setSelection(gpiAction.getPort());
		signalCheckbox.setSelection(gpiAction.isSignal());
	}

	private void saveChanges() {
		gpiAction.setPort(portSpinner.getSelection());
		gpiAction.setSignal(signalCheckbox.getSelection());
	}
}
