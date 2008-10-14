/**
 * 
 */
package org.rifidi.ui.ide.workbench.actions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.rifidi.ui.ide.workbench.wizards.ConnectionsSettingsWizard;

/**
 * This action is invoked if the menuButton for changeing the RMI Settings is
 * pressed. It's starting the ConnectionSettingsWizard for gathering the
 * information necessary to connect to the new host.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ConnectionSettingsAction extends Action {
	private static Log logger = LogFactory
			.getLog(ConnectionSettingsAction.class);

	public ConnectionSettingsAction(String text, ImageDescriptor image) {
		super(text, image);
		setId(ConnectionSettingsAction.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	@Override
	public void run() {
		logger.debug("create new Connection to Emulator");
		ConnectionsSettingsWizard wizard = new ConnectionsSettingsWizard();
		WizardDialog dialog = new WizardDialog(Display.getCurrent()
				.getActiveShell(), wizard);
		dialog.open();
	}

}
