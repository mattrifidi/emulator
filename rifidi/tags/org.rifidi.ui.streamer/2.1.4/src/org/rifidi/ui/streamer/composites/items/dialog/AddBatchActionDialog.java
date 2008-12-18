/**
 * 
 */
package org.rifidi.ui.streamer.composites.items.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.actions.GPIAction;
import org.rifidi.streamer.xml.actions.TagAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.ui.streamer.data.EventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class AddBatchActionDialog extends AbstractCustomAddDialog {

	private Combo addActionCombo;
	private String[] availableActions;

	/**
	 * Constructor 
	 * 
	 * @param parent
	 * @param title
	 * @param input
	 */
	public AddBatchActionDialog(Shell parent, String title, EventAwareWrapper input) {
		super(parent, title, input);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.composites.items.dialog.CustomDialog#createControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControls(Composite parent) {
		addActionCombo = new Combo(parent, SWT.NONE);
		availableActions = new String[] { "TagAction", "WaitAction",
				"GPIAction" };
		addActionCombo.setItems(availableActions);
		GridData addActionGridData = new GridData(GridData.FILL);
		addActionGridData.horizontalSpan = 2;
		addActionCombo.setLayoutData(addActionGridData);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.composites.items.dialog.CustomDialog#saveChanges()
	 */
	@Override
	public void saveChanges() {
		Action action = null;
		switch (addActionCombo.getSelectionIndex()) {
		case 0:
			action = new TagAction();
			//TODO regenerate is not yet supported
			((TagAction)action).setRegenerate(true);
			break;
		case 1:
			action = new WaitAction();
			break;
		case 2:
			action = new GPIAction();
		}
		if (action != null)
			input.add(action);
	}

}
