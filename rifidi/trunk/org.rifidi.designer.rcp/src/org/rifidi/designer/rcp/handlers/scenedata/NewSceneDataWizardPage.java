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

import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.library.EntityLibraryRegistry;
import org.rifidi.designer.library.FloorElement;
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
	 * The new scene data.
	 */
	private SceneData sceneData;
	/**
	 * The folder for the scene data file.
	 */
	private IFolder folder;

	private TableViewer tableViewer;

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
		Label floorTableLabel = new Label(container, SWT.NULL);
		floorTableLabel.setText("Select a floorplan");
		Table floorTable = new Table(container, SWT.BORDER);
		GridData gd2 = new GridData(GridData.FILL_HORIZONTAL
				| GridData.FILL_VERTICAL);
		floorTable.setLayoutData(gd2);
		floorTable.setLinesVisible(true);

		TableColumn tableColumn = new TableColumn(floorTable, SWT.NONE);
		tableColumn.setWidth(300);
		tableColumn.setText("Floorplan");

		tableViewer = new TableViewer(floorTable);
		tableViewer.setContentProvider(new IStructuredContentProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
			 */
			@Override
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof Map) {
					return ((Map<String, FloorElement>) inputElement).values()
							.toArray();
				}
				return null;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
			 */
			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
			 *      java.lang.Object, java.lang.Object)
			 */
			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
			}

		});
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
			 *      int)
			 */
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return ((FloorElement) element).getImageDescriptor()
						.createImage();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
			 *      int)
			 */
			@Override
			public String getColumnText(Object element, int columnIndex) {
				return ((FloorElement) element).getName();
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
			 */
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
			 */
			@Override
			public void dispose() {
				// TODO Auto-generated method stub

			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
			 *      java.lang.String)
			 */
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
			 */
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub

			}

		});
		tableViewer.setInput(EntityLibraryRegistry.getInstance()
				.getFloorReferences());
		floorTable.select(0);
		setControl(container);
	}

	/**
	 * 
	 * @return the new sceneData
	 */
	public SceneData getSceneData() {
		sceneData
				.setFloorId(((FloorElement) ((IStructuredSelection) tableViewer
						.getSelection()).getFirstElement()).getId());
		sceneData.setName(layoutName.getText());
		return sceneData;
	}
}
