/*
 *  GateEntityWizard.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.gate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.RMIManager;
import org.rifidi.designer.entities.wizards.RifidiEntityWizard;
import org.rifidi.designer.library.EntityWizardRifidiIface;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.blueprints.DigestReaders;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;
import org.rifidi.ui.common.wizards.reader.pages.NewReaderDynamicWizardPage;
import org.rifidi.ui.common.wizards.reader.pages.NewReaderGPIOWizardPage;
import org.xml.sax.SAXException;

/**
 * Wizard for the creation of a DestroyerEntity.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class GateEntityWizard extends RifidiEntityWizard implements
		EntityWizardRifidiIface {
	/**
	 * Logger for this entity.
	 */
	private static Log logger = LogFactory.getLog(GateEntityWizard.class);
	/**
	 * Wizard page for this entity.
	 */
	private GateEntityWizardPage gateEntityWizardPage;
	/**
	 * The new entity.
	 */
	private GateEntity entity = null;
	/**
	 * Map containing the readernames as keys and the blueprints as values.
	 */
	private static HashMap<String, ReaderBlueprint> readerBlueprints = null;
	/**
	 * The reader instance.
	 */
	private UIReader reader;
	/**
	 * Dynamic wizard page for the different readers.
	 */
	private NewReaderDynamicWizardPage newReaderDynamicWizardPage;
	/**
	 * GPIO wizard page.
	 */
	public NewReaderGPIOWizardPage newReaderGPIOpage;

	/**
	 * Constructor.
	 */
	public GateEntityWizard() {
		super();
		reader = new UIReader();
	}

	/**
	 * Set the RMIManager to create/manage the readers.
	 * 
	 * @param rmiManager
	 *            the RMIManager instance
	 */
	public void setRMIManager(RMIManager rmiManager) {

		if (readerBlueprints == null) {
			readerBlueprints = new HashMap<String, ReaderBlueprint>();
			List<String> supportedReaders = rmiManager
					.getSupportedReaderTypes();
			for (String currReader : supportedReaders) {
				DigestReaders diggy = new DigestReaders();
				try {
					byte currentXMLBytes[] = rmiManager
							.getReaderXMLDescription(currReader).getBytes();

					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
							currentXMLBytes);
					ReaderBlueprint rbp = diggy.digest(byteArrayInputStream);
					readerBlueprints.put(currReader, rbp);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (SAXException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		super.addPages();

		gateEntityWizardPage = new GateEntityWizardPage("PusharmEntityWizard",
				readerBlueprints, reader);
		addPage(gateEntityWizardPage);

		newReaderDynamicWizardPage = new NewReaderDynamicWizardPage("GateWizardPage",
				reader, readerBlueprints);
		addPage(newReaderDynamicWizardPage);

		newReaderGPIOpage = new NewReaderGPIOWizardPage("GPIO info page",
				reader, readerBlueprints);
		addPage(newReaderGPIOpage);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		// check if the page is ready to be processed
		if (newReaderDynamicWizardPage.isPageComplete()) {
			if (takenNamesList.contains(gateEntityWizardPage.getName())) {
				newReaderDynamicWizardPage
						.setErrorMessage(ENTITY_NAME_TAKEN_MESSAGE);
				return false;
			}

			logger.debug("Reader Wizard finished. Reader '"
					+ reader.getReaderName() + "' will be created.");
			entity = new GateEntity();
			entity.setName(gateEntityWizardPage.getName());
			entity.setReader(reader);
			// entity.setName(gateEntityWizardPage.getName());
			// entity.setReaderName(reader.getReaderName());
			// entity.setReaderClassName(reader.getReaderClassName());
			// entity.setNumAntennas(reader.getNumAntennas());
			// entity.setNumGPIs(reader.getNumGPIs());
			// entity.setNumGPOs(reader.getNumGPOs());
			// entity.setPropertiesMap(reader.getPropertiesMap());

			return true;

		}
		return false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#createPageControls(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPageControls(Composite pageContainer) {
		gateEntityWizardPage.createControl(pageContainer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.library.EntityWizardIface#getEntity()
	 */
	public Entity getEntity() {
		return entity;
	}

	@Override
	public boolean canFinish() {
		if (!newReaderDynamicWizardPage.enableGPIO()
				&& newReaderDynamicWizardPage.isPageComplete())
			return true;
		if (newReaderDynamicWizardPage.isPageComplete()
				&& newReaderGPIOpage.isPageComplete())
			return true;
		return false;
	}
}
