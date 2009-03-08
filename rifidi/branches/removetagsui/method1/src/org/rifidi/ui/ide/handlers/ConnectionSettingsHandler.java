/**
 * 
 */
package org.rifidi.ui.ide.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
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
public class ConnectionSettingsHandler extends AbstractHandler {
	public static final String ID= "org.rifidi.ui.ide.handlers.ConnectionSettingsHandler"; 
	
	private static Log logger = LogFactory
			.getLog(ConnectionSettingsHandler.class);

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		logger.debug("create new Connection to Emulator");
		ConnectionsSettingsWizard wizard = new ConnectionsSettingsWizard();
		WizardDialog dialog = new WizardDialog(Display.getCurrent()
				.getActiveShell(), wizard);
		dialog.open();
		return null;
	}

}
