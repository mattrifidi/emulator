/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.rifidi.prototyper.mapeditor.Activator;
import org.rifidi.prototyper.mapeditor.model.FloorplanElement;
import org.rifidi.prototyper.mapeditor.model.MapModel;
import org.rifidi.prototyper.mapeditor.view.wizards.PrototyperCreationWizard.ProjectType;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class PrototyperCreationWizardPage3 extends WizardNewFileCreationPage
		implements SelectionListener {

	private IWorkbench workbench;
	private static int exampleCount = 1;

	/**
	 * @param pageName
	 * @param selection
	 */
	public PrototyperCreationWizardPage3(IWorkbench workbench,
			IStructuredSelection selection) {
		super("Prototyper", selection);
		this.setTitle("New Prototype");
		this.setDescription("Create a new RFID prototype");
		this.workbench = workbench;
		setAllowExistingResources(true);
		setContainerFullPath(Platform.getLocation());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.WizardNewFileCreationPage#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		this.setFileName("prototype" + exampleCount + ".rifidi"); //$NON-NLS-2$//$NON-NLS-1$

		Composite composite = (Composite) getControl();

		setPageComplete(validatePage());
	}

	protected InputStream getInitialContents() {
		PrototyperCreationWizard wizard = (PrototyperCreationWizard) getWizard();

		MapModel model = new MapModel();
		FloorplanElement floorplan;
		model.setName(wizard.projectName);
		if (wizard.custom) {
			ImageLoader loader = new ImageLoader();
			ImageData[] data = loader.load(wizard.pathToFile);
			Float pixPerFeet = 30 / wizard.feetPer30Px;
			floorplan = new FloorplanElement(data[0], pixPerFeet);
		} else {
			if (wizard.exampleProject.equals(ProjectType.HOSPITAL)) {
				ImageDescriptor desc = Activator
						.getImageDescriptor("IMG/hospital.gif");
				Image image = desc.createImage();
				floorplan = new FloorplanElement(image.getImageData(),
						new Float(60));
				image.dispose();
			} else {
				ImageDescriptor desc = Activator
						.getImageDescriptor("IMG/blueprint.jpg");
				Image image = desc.createImage();
				floorplan = new FloorplanElement(image.getImageData(),
						new Float(60));
				image.dispose();
			}
		}
		model.setFloorplan(floorplan);

		ByteArrayInputStream bais = null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(model);
			oos.flush();
			oos.close();
			baos.close();
			bais = new ByteArrayInputStream(baos.toByteArray());
			bais.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bais;
	}

	public boolean finish() {
		IFile newFile = createNewFile();
		if (newFile == null)
			return false; // ie.- creation was unsuccessful

		// Since the file resource was created fine, open it for editing
		// iff requested by the user
		try {
			IWorkbenchWindow dwindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = dwindow.getActivePage();
			if (page != null) {
				page.closeAllEditors(true);
				IDE.openEditor(page, newFile, true);
			}
		} catch (org.eclipse.ui.PartInitException e) {
			e.printStackTrace();
			return false;
		}
		exampleCount++;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse
	 * .swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
	 */
	public void widgetSelected(SelectionEvent e) {

	}

}
