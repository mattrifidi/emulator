/*
 *  LibraryDragSourceListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.libraryview;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.rifidi.designer.library.EntityLibraryReference;

/**
 * DragSourceListener that only allows the dragging of LibraryReferences
 * 
 * @author Jochen Mader Sep 29, 2007
 * 
 */
public class LibraryDragSourceListener implements DragSourceListener {
	/**
	 * Viewer for the libraries.
	 */
	private TreeViewer viewer;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 */
	public LibraryDragSourceListener(TreeViewer viewer) {
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			event.data = ((EntityLibraryReference) selection.getFirstElement())
					.getId();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		if (selection.size() > 1
				|| !(selection.getFirstElement() instanceof EntityLibraryReference)) {
			event.doit = false;
		}
	}

}
