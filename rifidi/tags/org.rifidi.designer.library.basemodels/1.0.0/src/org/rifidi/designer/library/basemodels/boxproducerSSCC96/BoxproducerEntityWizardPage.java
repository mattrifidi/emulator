/*
 *  PusharmEntityWizardPage.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.boxproducerSSCC96;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page for the boxproducerwizard.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class BoxproducerEntityWizardPage extends WizardPage {
	/**
	 * Container for the page.
	 */
	private Composite pageComposite;
	/**
	 * Widget for the producer name.
	 */
	private Text nameText;
	/**
	 * Widget for the production speed.
	 */
	private Text speedText;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 */
	public BoxproducerEntityWizardPage(String pageName) {
		super(pageName);
		setTitle("Box Producer Wizard");
		setMessage("Please give a name and the production speed for the box producer.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		parent.setLayout(new FillLayout());
		pageComposite = new Composite(parent, SWT.NONE);
		GridLayout pageLayout = new GridLayout();
		pageLayout.numColumns = 2;
		pageComposite.setLayout(pageLayout);
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.widthHint = 100;

		GridData grabTwo = new GridData();
		grabTwo.horizontalSpan = 2;
		grabTwo.grabExcessHorizontalSpace = true;
		grabTwo.widthHint = 200;

		Label label = new Label(pageComposite, SWT.None);
		label.setText("Name");
		nameText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(layoutData);

		label = new Label(pageComposite, SWT.CENTER);
		label.setText("Speed\nsec/box");
		speedText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		speedText.setLayoutData(layoutData);

		setControl(pageComposite);
		setPageComplete(false);
		initListeners();
	}

	/**
	 * Initialze the change listeners.
	 */
	private void initListeners() {
		nameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				if (nameText.getText().length() > 0
						&& speedText.getText().length() > 0)
					setPageComplete(true);
			}
		});

		speedText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event event) {
				String text = event.text;
				char[] chars = new char[text.length()];
				text.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!(('0' <= chars[i] && chars[i] <= '9')
							|| chars[i] == '.' || chars[i] == '-')) {
						event.doit = false;
						return;
					}
				}
				setPageComplete(true);
			}
		});
	}

	/**
	 * Get the name of the new producer.
	 * 
	 * @return the name
	 */
	public String getName() {
		return nameText.getText();
	}

	/**
	 * Get the speed of the new producer.
	 * 
	 * @return the speed in seconds
	 */
	public Float getSpeed() {
		return Float.valueOf(speedText.getText());
	}
}
