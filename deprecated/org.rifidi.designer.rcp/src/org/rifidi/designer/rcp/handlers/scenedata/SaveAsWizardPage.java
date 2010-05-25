/*
 *  SaveAsWizardPage.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.scenedata;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.designer.rcp.Activator;

/**
 * Wizard page that allows the user to enter a name for his fancy new layout and
 * give it a size.
 * 
 * @author Jochen Mader Nov 20, 2007
 * 
 */
public class SaveAsWizardPage extends WizardPage {
	/**
	 * Widget for entering the new name.
	 */
	private Text layoutName;
	/**
	 * THe new name of the layout.
	 */
	private String name = "";
	/**
	 * The folder to store the layout in.
	 */
	private IFolder folder;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 *            title of the page
	 */
	public SaveAsWizardPage(String pageName) {
		super(pageName);
		folder = Activator.getDefault().folder;
		setMessage("Please enter a name for your layout, slect the size and hit finish.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		Label nameLabel = new Label(container, SWT.NULL);
		nameLabel.setText("Give a new name for the layout");
		layoutName = new Text(container, SWT.BORDER);
		layoutName.setLayoutData(gd);

		setPageComplete(false);
		layoutName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					if (layoutName.getText().length() > 0) {
						for (IResource member : folder.members()) {
							if (member.getName().equals(layoutName.getText())) {
								setErrorMessage("Please choose another name, the given one is already taken.");
								setPageComplete(false);
								return;
							}
						}
						name = layoutName.getText();
						setErrorMessage(null);
						setPageComplete(true);
						return;
					}
					setPageComplete(false);
				} catch (CoreException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		});
		setControl(container);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
