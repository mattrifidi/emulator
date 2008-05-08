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
package org.rifidi.designer.library.basemodels.cardbox;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * The wizard page for the CardboxEntityWizard.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class CardboxEntityWizardPage extends WizardPage {
	/**
	 * The container for the page.
	 */
	private Composite pageComposite;
	/**
	 * Widget for the name of the box.
	 */
	private Text nameText;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 * @param title
	 * @param titleImage
	 */
	public CardboxEntityWizardPage(String pageName, String title,
			ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 */
	public CardboxEntityWizardPage(String pageName) {
		super(pageName);
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

		setControl(pageComposite);
	}

	/**
	 * Get the name for the new box.
	 * 
	 * @return the name
	 */
	public String getName() {
		return nameText.getText();
	}
}
