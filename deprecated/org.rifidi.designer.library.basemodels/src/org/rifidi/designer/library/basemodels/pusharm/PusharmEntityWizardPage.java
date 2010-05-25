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
package org.rifidi.designer.library.basemodels.pusharm;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Wizard page for the {@link PusharmEntityWizardPage}
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class PusharmEntityWizardPage extends WizardPage {
	/**
	 * Container for the page.
	 */
	private Composite pageComposite;
	/**
	 * Widget for the pusharm name.
	 */
	private Text nameText;
	/**
	 * Widget for the pusharm speed.
	 */
	private Text speedText;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 *            the page title
	 */
	public PusharmEntityWizardPage(String pageName) {
		super(pageName);
		setTitle("Pusharm Wizard");
		setMessage("Please give a name and the speed for the pusharm.");
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
		label.setText("Speed\nsec/push");
		speedText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		speedText.setLayoutData(layoutData);

		setControl(pageComposite);
	}

	/**
	 * Get the name of the new entity.
	 * 
	 * @return the name
	 */
	public String getName() {
		return nameText.getText();
	}

	/**
	 * Get the speed of the new entity.
	 * 
	 * @return the speed.
	 */
	public Float getSpeed() {
		return Float.valueOf(speedText.getText());
	}
}
