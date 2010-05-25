/*
 *  Editor3DDropTargetListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.listeners;

import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.rifidi.designer.library.EntityLibraryRegistry;
import org.rifidi.designer.rcp.views.view3d.View3D;

/**
 * This DropListener accepts EntityLibraryReferences dropped on it.
 * 
 * @author Jochen Mader Sep 29, 2007
 * 
 */
public class Editor3DDropTargetListener implements DropTargetListener {

	/*
	 * Reference to the editor this listener is associated with
	 */
	private View3D view3D;

	/**
	 * Constructor.
	 * 
	 * @param editor
	 */
	public Editor3DDropTargetListener(View3D view3D) {
		this.view3D = view3D;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	public void dragEnter(DropTargetEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	public void dragLeave(DropTargetEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse
	 * .swt.dnd.DropTargetEvent)
	 */
	public void dragOperationChanged(DropTargetEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	public void dragOver(DropTargetEvent event) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	public void drop(DropTargetEvent event) {
		Point pos = getPosition(view3D.getGlCanvas());
		pos.x = event.x - pos.x;
		pos.y = view3D.getGlCanvas().getSize().y - (event.y - pos.y);
		if (TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			// get the EntityLibraryReference and hand it to the editor to
			// create an Entity
			view3D.createNewEntity(EntityLibraryRegistry.getInstance()
					.getEntityLibraryReference((String) event.data), pos);
		}
	}

	private Point getPosition(Control control) {
		if (control.getParent() == null) {
			return control.getLocation();
		}
		Point ret = getPosition(control.getParent());
		ret.x += control.getLocation().x;
		ret.y += control.getLocation().y;
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd
	 * .DropTargetEvent)
	 */
	public void dropAccept(DropTargetEvent event) {
	}

}
