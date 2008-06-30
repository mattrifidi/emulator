package org.rifidi.ui.ide.workbench.wizards;



import java.rmi.ConnectException;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.rifidi.ui.common.registry.ReaderRegistry;

/**
 * The wizard for gathering information about the RMI Server to connect to. It's
 * defining the basic wizard and the containing pages.
 * 
 * See also ConnectionsSettingsWizardPage.java
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ConnectionsSettingsWizard extends Wizard {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	private ConnectionSettingsWizardPage connectionSettingsWizardPage;

	@Override
	public void addPages() {
		connectionSettingsWizardPage = new ConnectionSettingsWizardPage();
		addPage(connectionSettingsWizardPage);
		super.addPages();
	}

	@Override
	public boolean performFinish() {
		int port = connectionSettingsWizardPage.getPort();
		String ipAddress = connectionSettingsWizardPage.getIpaddress();
		if (port != 0 && ipAddress.length() != 0)
			try {
				ReaderRegistry.getInstance().clean();
				ReaderRegistry.getInstance().connect(ipAddress, port);
			} catch (ConnectException e) {
				MessageBox messageBox = new MessageBox(this.getShell(),SWT.ICON_ERROR);
				messageBox.setText("Error while trying to reach RMI Server");
				messageBox.setMessage("Could not connect to remote RMI Server " + ipAddress + ":" + port);
				messageBox.open();
				return false;
			}
		return true;
	}
}
