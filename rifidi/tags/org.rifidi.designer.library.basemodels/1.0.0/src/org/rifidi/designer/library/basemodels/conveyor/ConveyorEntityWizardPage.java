/*
 *  ConveyorEntityWizardPage.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.conveyor;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page for the ConveyorEntityWizard.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class ConveyorEntityWizardPage extends WizardPage {
	/**
	 * Container for the poage.
	 */
	private Composite pageComposite;
	/**
	 * Widget for the conveyor name.
	 */
	private Text nameText;
	/**
	 * Widget for the speed.
	 */
	private Text speedText;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 *            title of the page
	 */
	public ConveyorEntityWizardPage(String pageName) {
		super(pageName);
		setTitle("Conveyor Wizard");
		setMessage("Please give a name and the speed for the conveyor.");
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
		label.setText("Speed\nft/sec");
		speedText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		speedText.setLayoutData(layoutData);
		speedText.addListener(SWT.Verify, new Listener() {
			public void handleEvent(Event event) {
				String text = event.text;
				char[] chars = new char[text.length()];
				text.getChars(0, chars.length, chars, 0);
				for (int i = 0; i < chars.length; i++) {
					if (!('0' <= chars[i] && chars[i] <= '9')
							&& chars[i] != '-' && chars[i] != '.') {
						event.doit = false;
						return;
					}
				}
			}
		});

		setControl(pageComposite);
	}

	/**
	 * Get the name of the new conveyor.
	 * 
	 * @return the name
	 */
	public String getName() {
		return nameText.getText();
	}

	/**
	 * Get the speed of the conveyor.
	 * 
	 * @return the speed.
	 */
	public Float getSpeed() {
		return Float.valueOf(speedText.getText());
	}
}
