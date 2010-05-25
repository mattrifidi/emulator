/*
 *  SaveAsWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.scenedata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.rifidi.designer.rcp.Activator;

/**
 * A wizard that allows a user to save a layout under a different name.
 * 
 * @author Jochen Mader Nov 20, 2007
 * 
 */
public class SaveAsWizard extends Wizard {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(SaveAsWizard.class);
	/**
	 * Wizard page for this wzard.
	 */
	private SaveAsWizardPage saveAsWizardPage;
	/**
	 * The target file.
	 */
	private IFile newLayout;
	/**
	 * The new name for the layout.
	 */
	private String name = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		saveAsWizardPage = new SaveAsWizardPage("New Layout");
		addPage(saveAsWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		newLayout = Activator.getDefault().folder.getFile(saveAsWizardPage
				.getName());
		name = saveAsWizardPage.getName();
		try {
			newLayout.create(null, true, null);
		} catch (CoreException e) {
			logger.error("Error while saving: " + e);
			MessageBox mb = new MessageBox(getShell(), SWT.OK);
			mb.setText("Error while saving file:");
			mb.setMessage(e.toString());
			mb.open();
		}
		return true;
	}

	/**
	 * @return the newLayout
	 */
	public IFile getNewLayout() {
		return newLayout;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
}
