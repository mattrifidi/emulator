package org.rifidi.ui.streamer.composites.items.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.actions.BatchAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.ui.streamer.data.EventAwareWrapper;

public class AddTestUnitActionDialog extends AbstractCustomAddDialog {

	private Combo addActionCombo;
	private String[] availableActions;

	/**
	 * Constructor for AddTestUnitActionDialog
	 * @param parent the parent Shell
	 * @param title the title of the Dialog
	 * @param input the TestUnitEventAwareWrapper
	 */
	public AddTestUnitActionDialog(Shell parent, String title,
			EventAwareWrapper input) {
		super(parent, title, input);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.ui.streamer.composites.items.dialog.AbstractCustomDialog#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControls(Composite parent) {
		addActionCombo = new Combo(parent, SWT.NONE);
		availableActions = new String[] { "BatchAction", "WaitAction" };
		addActionCombo.setItems(availableActions);
		GridData addActionGridData = new GridData(GridData.FILL);
		addActionGridData.horizontalSpan = 2;
		addActionCombo.setLayoutData(addActionGridData);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.ui.streamer.composites.items.dialog.AbstractCustomDialog#saveChanges()
	 */
	@Override
	public void saveChanges() {
		Action action = null;
		switch (addActionCombo.getSelectionIndex()) {
		case 0:
			action = new BatchAction();
			break;
		case 1:
			action = new WaitAction();
			break;
		}
		input.add(action);
	}

}
