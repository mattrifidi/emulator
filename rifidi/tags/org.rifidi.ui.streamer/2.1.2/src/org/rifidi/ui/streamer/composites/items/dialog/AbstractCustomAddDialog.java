/**
 * 
 */
package org.rifidi.ui.streamer.composites.items.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.ui.streamer.data.EventAwareWrapper;

/**
 * This is a custom Dialog for presenting information
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public abstract class AbstractCustomAddDialog {

	private Shell shell;
	private Composite composite;
	private Button buttonCancel;
	private Button buttonOk;

	protected EventAwareWrapper input;

	/**
	 * Constructor for CustomDialog
	 * 
	 * @param parent
	 *            the parent Shell of this Dialog
	 * @param title
	 *            the Title for the Dialog
	 * @param input
	 *            the EventAwareWrapper for the inputdata of the Dialog
	 */
	public AbstractCustomAddDialog(Shell parent, String title,
			EventAwareWrapper input) {
		this.input = input;
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		shell.setText(title);
		shell.setLayout(new FillLayout());
	}

	/**
	 * Opens and displays the Dialog
	 */
	public void open() {
		// composite container for all widgets
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
		locationX = (parentSize.width - mySize.width)/2+parentSize.x;
		locationY = (parentSize.height - mySize.height)/2+parentSize.y;
		shell.setLocation(new Point(locationX, locationY));
	}

	/**
	 * Adds a Button "Cancel" and "OK" to the Dialog
	 * 
	 * @param parent
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
				close();
			}
		});
		buttonOk.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				saveChanges();
				close();
			}
		});
	}

	/**
	 * Create the Controls (Widgets on the Dialog) This method will be called
	 * when the dialog will be opened
	 * 
	 * @param parent
	 *            the container for the widgets
	 */
	public abstract void createControls(Composite parent);

	/**
	 * Save the changes of the Widgets to the input object
	 */
	public abstract void saveChanges();

	/**
	 * this closes the Dialog
	 */
	public void close() {
		shell.dispose();
	}
}
