/*
 *  NewSceneDataWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.scenedata;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.wizard.Wizard;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.library.EntityLibraryRegistry;
import org.rifidi.designer.rcp.Activator;

/**
 * This wizard enables a user to create a new layout.
 * 
 * @author Jochen Mader Nov 20, 2007
 * 
 */
public class NewSceneDataWizard extends Wizard {
	/**
	 * The wizard page for this wizard
	 */
	private NewSceneDataWizardPage newSceneDataWizardPage;
	/**
	 * The newly created layout file.
	 */
	private IFile newLayout;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		newSceneDataWizardPage = new NewSceneDataWizardPage("New Layout");
		addPage(newSceneDataWizardPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		SceneData sceneData = newSceneDataWizardPage.getSceneData();
		newLayout = Activator.getDefault().folder.getFile(sceneData.getName());
		try {
			newLayout.create(new ByteArrayInputStream(toByteArray(sceneData)),
					true, null);
			newLayout.setContents(new ByteArrayInputStream(
					toByteArray(sceneData)), true, false, null);
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Helper method to convert the scene into a byte array. TODO:
	 * REDUNDANT!!!!!!!
	 * 
	 * @param sceneData
	 * @return
	 */
	private byte[] toByteArray(SceneData sceneData) {
		ByteArrayOutputStream fileOutput = new ByteArrayOutputStream();
		try {
			EntityLibraryRegistry.getInstance().getLibraries();
			List<Class<?>> classes = EntityLibraryRegistry.getInstance()
					.getEntityClasses();
			classes.add(org.rifidi.designer.entities.SceneData.class);
			classes.add(org.rifidi.designer.entities.VisualEntity.class);
			JAXBContext context = JAXBContext.newInstance(classes
					.toArray(new Class[0]));
			Marshaller marshaller = context.createMarshaller();
			marshaller.marshal(sceneData, fileOutput);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileOutput.toByteArray();
	}

	/**
	 * @return the newLayout
	 */
	public IFile getNewLayout() {
		return newLayout;
	}
}
