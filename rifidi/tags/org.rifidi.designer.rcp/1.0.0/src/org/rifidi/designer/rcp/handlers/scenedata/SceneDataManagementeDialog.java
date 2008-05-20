/*
 *  SceneDataManagementeDialog.java
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
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.rifidi.designer.rcp.Activator;
import org.rifidi.designer.rcp.views.view3d.View3D;

/**
 * This is the dialog used to manage layouts.
 * 
 * @author Jochen Mader Feb 1, 2008
 * @tags
 * 
 */
public class SceneDataManagementeDialog {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(SceneDataManagementeDialog.class);
	/**
	 * Viewer for the files.
	 */
	private TableViewer viewer;
	/**
	 * The target folder.
	 */
	private IFolder folder;
	/**
	 * The file to save to.
	 */
	private IFile file;
	/**
	 * Button for opening the selected file.
	 */
	private Button open;
	/**
	 * Button for closing the window.
	 */
	private Button cancel;
	/**
	 * Button for deleting the selected file.
	 */
	private Button delete;

	/**
	 * Constructor
	 * 
	 * @param shell
	 *            the shell for the dialog
	 */
	public SceneDataManagementeDialog(final Shell shell) {
		folder = Activator.getDefault().folder;

		GridLayout layout = new GridLayout();
		layout.numColumns = 1;

		shell.setLayout(layout);

		Composite viewerComp = new Composite(shell, SWT.NONE);
		GridData data1 = new GridData(GridData.FILL_BOTH);
		data1.grabExcessHorizontalSpace = true;
		data1.grabExcessVerticalSpace = true;
		viewerComp.setLayoutData(data1);
		FillLayout filler = new FillLayout();
		filler.marginHeight = 5;
		filler.marginWidth = 5;
		viewerComp.setLayout(filler);

		Composite buttonComp = new Composite(shell, SWT.NONE);
		RowLayout rowy = new RowLayout();
		rowy.marginLeft = 5;
		buttonComp.setLayout(rowy);

		viewer = new TableViewer(viewerComp, SWT.NONE);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setLabelProvider(new PrimitivLabelProvider());
		try {
			viewer.setInput(folder.members());
		} catch (CoreException e1) {
			logger.debug(e1.toString());
		}
		cancel = new Button(buttonComp, SWT.CANCEL);
		cancel.setText("CANCEL");
		cancel.addSelectionListener(new SelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}

		});
		delete = new Button(buttonComp, SWT.CANCEL);
		delete.setText("DELETE");
		delete.setEnabled(false);
		delete.addSelectionListener(new SelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					MessageBox mb = new MessageBox(shell, SWT.OK | SWT.CANCEL);
					mb.setMessage("Really delete " + file.getName() + "?");
					int result = mb.open();
					if (result == SWT.OK) {
						file.delete(true, null);
					}
				} catch (CoreException e1) {
					MessageBox mb = new MessageBox(shell, SWT.OK);
					mb.setMessage(e.toString());
					mb.open();
					logger.warn(e1.toString());
				}
				try {
					viewer.setInput(folder.members());
				} catch (CoreException e1) {
					logger.warn(e1.toString());
				}
			}

		});
		open = new Button(buttonComp, SWT.CANCEL);
		open.setText("OPEN");
		open.setEnabled(false);
		open.addSelectionListener(new SelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				logger.info("Loading " + file.getName());
				shell.close();
				((View3D) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(View3D.ID)).loadScene(file);
			}

		});
		viewer.getTable().addMouseListener(new MouseAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.MouseAdapter#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
			 */
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				logger.info("Loading " + file.getName());
				shell.close();
				((View3D) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage().findView(View3D.ID)).loadScene(file);
			}

		});
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				file = (IFile) ((IStructuredSelection) event.getSelection())
						.getFirstElement();
				delete.setEnabled(true);
				open.setEnabled(true);
			}
		});
	}

	/**
	 * Just a little lable provider to be used in the viewer.
	 * 
	 * 
	 * @author Jochen Mader Feb 1, 2008
	 * @tags
	 * 
	 */
	private class PrimitivLabelProvider extends LabelProvider implements
			ITableLabelProvider {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
		 *      int)
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
		 *      int)
		 */
		public String getColumnText(Object element, int columnIndex) {
			return ((IResource) element).getName();
		}

	}

}
