/*
 *  EntityDragSourceListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.entityview;

import java.util.Iterator;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.rifidi.designer.entities.Entity;

/**
 * DragSourceListener that only allows the dragging of entities between groups.
 * 
 * @author Jochen Mader Sep 29, 2007
 * 
 */
public class EntityDragSourceListener implements DragSourceListener {
	/**
	 * The drag source.
	 */
	private TreeViewer viewer;

	/**
	 * Constructor.
	 * 
	 * @param viewer
	 */
	public EntityDragSourceListener(TreeViewer viewer) {
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
	@SuppressWarnings("unchecked")
	public void dragSetData(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		StringBuffer buf = new StringBuffer();
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			Iterator iterator = selection.iterator();
			while (iterator.hasNext()) {
				Object ob = iterator.next();
				if (ob instanceof Entity) {
					buf.append(((Entity) ob).getEntityId() + "\n");
				}
			}
			event.data = buf.toString();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.DragSourceEvent)
	 */
	@SuppressWarnings("unchecked")
	public void dragStart(DragSourceEvent event) {
		IStructuredSelection selection = (IStructuredSelection) viewer
				.getSelection();
		Iterator iterator = selection.iterator();
		int type = 0;
		while (iterator.hasNext()) {
			Object ob = iterator.next();
			if (type == 0) {
				if (ob instanceof Entity) {
					type = 1;
				} else {
					event.doit = false;
					return;
				}
			} else if (!(ob instanceof Entity)) {
				event.doit = false;
				return;
			} else if (type == 1 && !(ob instanceof Entity)) {
				event.doit = false;
				return;
			} else if (type == 2) {
				event.doit = false;
				return;
			}
		}
	}

}
