package org.rifidi.ui.streamer.executeDialog;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.streamer.xml.testSuite.TestUnit;

/**
 * Dialog to select one of the TestUnits
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TestUnitSelectionDialog {

	/**
	 * Enum for the selected Button
	 */
	private enum ButtonSelected {
		OK, CANCEL
	}

	private Shell shell;
	private Composite composite;
	private Button buttonCancel;
	private Button buttonOk;

	private List<TestUnit> testUnits;
	private Combo testUnitCombo;
	private ButtonSelected buttonSelected;
	private int index = -1;

	/**
	 * Creates a new TestUnitSelectionDialog
	 * 
	 * @param parent
	 *            the parent shell of this dialog
	 * @param title
	 *            the title of the Dialog
	 * @param testUnits
	 *            the TestUnits available
	 */
	public TestUnitSelectionDialog(Shell parent, String title,
			List<TestUnit> testUnits) {
		this.testUnits = testUnits;
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		shell.setLayout(new FillLayout());
	}

	/**
	 * opens the Dialog and blocks until the dialog disposes
	 * 
	 * @return the selected TestUnit or null
	 */
	public TestUnit open() {
		if(testUnits == null )
			return null;
		
		composite = new Composite(shell, SWT.NONE);
		// Create a 2 row layout and add it to the main composite
		GridLayout layout = new GridLayout(2, true);
		composite.setLayout(layout);
		// add the custom Widgets
		createControls(composite);
		// add OK and Cancel Button
		addButtons(composite);
		// Set size of Composite and open shell
		shell.setMinimumSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		shell.pack();
		shell.open();

		// Move the Dialog in the center of the parent shell
		Rectangle parentSize = shell.getParent().getBounds();
		Rectangle mySize = shell.getBounds();
		int locationX, locationY;
		locationX = (parentSize.width - mySize.width) / 2 + parentSize.x;
		locationY = (parentSize.height - mySize.height) / 2 + parentSize.y;
		shell.setLocation(new Point(locationX, locationY));
		shell.open();

		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}

		if (buttonSelected == ButtonSelected.OK)
			if (index > -1)
				return testUnits.get(index);
		return null;
	}

	/**
	 * Layout the Controls on the given Composite
	 * 
	 * @param parent
	 *            the Composite on which the Controls appear
	 */
	private void createControls(Composite parent) {
		GridData testUnitComboGridData = new GridData();
		testUnitComboGridData.horizontalSpan = 2;
		testUnitCombo = new Combo(parent, SWT.READ_ONLY);
		testUnitCombo.setLayoutData(testUnitComboGridData);
		String[] availableTestUnits = new String[testUnits.size()];
		for (int i = 0; i < testUnits.size(); i++) {
			availableTestUnits[i] = "TestUnit " + testUnits.get(i).getID();
		}
		testUnitCombo.setItems(availableTestUnits);
	}

	/**
	 * Add the OK and Cancel Button
	 * 
	 * @param parent
	 *            the Composite on which the Controls appear
	 */
	private void addButtons(Composite parent) {
		buttonCancel = new Button(parent, SWT.PUSH);
		buttonCancel.setText("Cancel");
		buttonCancel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttonOk = new Button(parent, SWT.PUSH);
		buttonOk.setText("Ok");
		buttonOk.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		addButtonSelectionListeners();
	}

	/**
	 * Register the saveChanges and dispose Method to the Buttons
	 */
	private void addButtonSelectionListeners() {
		buttonCancel.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				buttonSelected = ButtonSelected.CANCEL;
				shell.close();
			}
		});
		buttonOk.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				buttonSelected = ButtonSelected.OK;
				index = testUnitCombo.getSelectionIndex();
				shell.close();
			}
		});
	}
}
