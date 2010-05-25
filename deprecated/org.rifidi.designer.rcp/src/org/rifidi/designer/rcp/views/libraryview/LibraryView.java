/*
 *  LibraryView.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.libraryview;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.designer.library.EntityLibraryRegistry;

/**
 * View that shows all libraries in a treeviewer
 * 
 * @author Jochen Mader Sep 29, 2007
 * 
 */
public class LibraryView extends ViewPart {
	/**
	 * Eclipse ID
	 */
	public static String ID = "org.rifidi.designer.rcp.views.LibraryView";
	/**
	 * Container for the view.
	 */
	private Composite composite;
	/**
	 * Treeviewer for the libraries.
	 */
	private TreeViewer libraryTreeViewer;

	/**
	 * Constructor.
	 */
	public LibraryView() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		libraryTreeViewer = new TreeViewer(composite);
		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		libraryTreeViewer.addDragSupport(ops, transfers,
				new LibraryDragSourceListener(libraryTreeViewer));
		libraryTreeViewer.setContentProvider(new LibraryTreeContentProvider());
		libraryTreeViewer.setLabelProvider(new LibraryLabelProvider());
		libraryTreeViewer.setInput(EntityLibraryRegistry.getInstance()
				.getLibraries());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
	}
}
