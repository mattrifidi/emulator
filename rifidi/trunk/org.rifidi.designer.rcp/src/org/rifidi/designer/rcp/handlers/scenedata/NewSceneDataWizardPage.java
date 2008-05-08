/*
 *  NewSceneDataWizardPage.java
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.rcp.Activator;

/**
 * Wizard page that allows the user to enter a name for his fancy new layout and
 * give it a size.
 * 
 * @author Jochen Mader Nov 20, 2007
 * 
 */
public class NewSceneDataWizardPage extends WizardPage {

	/**
	 * The name of the new scene.
	 */
	private Text layoutName;
	/**
	 * Combobox for selecting the width.
	 */
	private Combo widthCombo;
	/**
	 * The new scene data.
	 */
	private SceneData sceneData;
	/**
	 * The folder for the scene data file.
	 */
	private IFolder folder;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 *            title of the page
	 */
	public NewSceneDataWizardPage(String pageName) {
		super(pageName);
		sceneData = new SceneData();
		folder = Activator.getDefault().folder;
		setMessage("Please enter a name for your layout, select the size and hit finish.");
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
		nameLabel.setText("Give a name for the layout");
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
		Label widthLabel = new Label(container, SWT.NULL);
		widthLabel.setText("&Extent of the room");
		widthCombo = new Combo(container, SWT.NULL);
		widthCombo.setLayoutData(gd);
		widthCombo.add("64x64");
		widthCombo.add("256x256");
		widthCombo.select(0);
		setControl(container);
	}

	/**
	 * 
	 * @return the new sceneData
	 */
	public SceneData getSceneData() {
		sceneData.setHeight(5);
		sceneData.setWidth((int) Math.pow(4,
				(widthCombo.getSelectionIndex() + 3)));
		sceneData.setName(layoutName.getText());
		return sceneData;
	}
}
