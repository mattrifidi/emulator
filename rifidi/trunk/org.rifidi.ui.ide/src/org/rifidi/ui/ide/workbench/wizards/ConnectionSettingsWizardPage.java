/**
 * 
 */
package org.rifidi.ui.ide.workbench.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.rifidi.ui.common.validators.IpAddressValidator;

/**
 * Wizard to gather the information about the RMI Server to connect to.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
/**
 * @author andreas
 *
 */
public class ConnectionSettingsWizardPage extends WizardPage {

	// private Log logger =
	// LogFactory.getLog(ConnectionSettingsWizardPage.class);

	private Text ipAddressText;
	private Text portNumberText;
	private int port;
	private String ipaddress;


	/**
	 * Default Constructor
	 */
	public ConnectionSettingsWizardPage() {
		super("Emulator Connection Settings");
		setTitle("Emulator Connection Settings");
		setDescription("Please fill out the following fields to etablish a connection \n"
				+ "to the RMI Service providing the reader simulation");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NULL);

		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		Label IPlabel = new Label(composite, SWT.CENTER);
		IPlabel.setText("enter ip address");

		ipAddressText = new Text(composite, SWT.FILL | SWT.BORDER);
		ipAddressText.setLayoutData(gridData);
		ipAddressText.setTextLimit(15);
		ipAddressText.addListener(SWT.Verify, new Listener() {

			public void handleEvent(Event event) {
				String text = event.text;
				char[] chars = new char[text.length()];
				text.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (('0' <= chars[i] && chars[i] <= '9')
							|| ('.' == chars[i])) {
						event.doit = true;
						return;
					}
					event.doit = false;
				}

			}

		});
		ipAddressText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateInput();
			}

		});

		Label portNumberLabel = new Label(composite, SWT.CENTER);
		portNumberLabel.setText("enter port number");

		portNumberText = new Text(composite, SWT.FILL | SWT.BORDER);
		portNumberText.setLayoutData(gridData);
		portNumberText.setTextLimit(5);
		portNumberText.addListener(SWT.Verify, new Listener() {

			public void handleEvent(Event event) {
				String text = event.text;
				char[] chars = new char[text.length()];
				text.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if ('0' <= chars[i] && chars[i] <= '9') {
						event.doit = true;
						return;
					}
					event.doit = false;
				}

			}

		});
		portNumberText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				updateInput();
			}

		});

		setPageComplete(false);
		setControl(composite);
	}

	/**
	 * This method is invoked every time information entered in the wizard is changed
	 */
	protected void updateInput() {
		IpAddressValidator ipAddressValidator = new IpAddressValidator();
		String portString = portNumberText.getText();
		String ipString = ipAddressText.getText();
		if (ipString != null && portString != null) {
			String error = ipAddressValidator.isValidIP(ipString);
			port = Integer.parseInt(portString);
			if (error == null && port >= 0 && port <= 65535) {
				ipaddress = ipString;
				setPageComplete(true);
			}
		}
	}

	public int getPort() {
		return port;
	}

	public String getIpaddress() {
		return ipaddress;
	}
	
	
	

}
