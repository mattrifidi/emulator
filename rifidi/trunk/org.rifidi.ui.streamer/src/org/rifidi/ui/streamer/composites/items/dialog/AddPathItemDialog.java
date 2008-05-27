package org.rifidi.ui.streamer.composites.items.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.scenario.PathItem;
import org.rifidi.ui.streamer.data.EventAwareWrapper;

public class AddPathItemDialog extends AbstractCustomAddDialog {

	private Spinner travelTimeSpinner;
	private Spinner readerIDSpinner;
	private Spinner antennaNumSpinner;

	/**
	 * Constructor for AddPathItemDialog
	 * 
	 * @param parent
	 *            the parent Shell
	 * @param title
	 *            the title of the Dialog
	 * @param input
	 *            the ScenarioEventAwareWrapper
	 */
	public AddPathItemDialog(Shell parent, String title, EventAwareWrapper input) {
		super(parent, title, input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.composites.items.dialog.AbstractCustomDialog#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControls(Composite parent) {
		Label readerIDLabel = new Label(parent, SWT.NONE);
		readerIDLabel.setText("Reader ID:");
		readerIDSpinner = new Spinner(parent, SWT.BORDER);
		readerIDSpinner.setMaximum(10000);

		Label antennaNumLabel = new Label(parent, SWT.NONE);
		antennaNumLabel.setText("Antenna Number:");
		antennaNumSpinner = new Spinner(parent, SWT.BORDER);
		antennaNumSpinner.setMaximum(10);
		
		Label travelTimeLabel = new Label(parent, SWT.NONE);
		travelTimeLabel.setText("Travel time (ms):");
		travelTimeSpinner = new Spinner(parent, SWT.BORDER);
		travelTimeSpinner.setMaximum(1000000000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.composites.items.dialog.AbstractCustomDialog#saveChanges()
	 */
	@Override
	public void saveChanges() {
		PathItem pathItem = new PathItem();
		pathItem.setReaderID(readerIDSpinner.getSelection());
		pathItem.setTravelTime((long) travelTimeSpinner.getSelection());
		pathItem.setAntennaNum(antennaNumSpinner.getSelection());
		input.add(pathItem);
	}

}
