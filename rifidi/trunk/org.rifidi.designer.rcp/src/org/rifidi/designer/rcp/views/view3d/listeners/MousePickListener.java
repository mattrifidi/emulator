/*
 *  MousePickListener.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.listeners;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.intersection.BoundingPickResults;
import com.jme.intersection.PickResults;
import com.jme.math.Ray;
import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;

/**
 * A listener that listenes for mouseclicks on the glcanvas and finds the picks.
 * 
 * @author Jochen Mader Oct 30, 2007
 */
public class MousePickListener implements MouseListener, KeyListener {

	/**
	 * Are we selecting more than one entitiy?
	 */
	private boolean multiselect = false;
	/**
	 * The currently picked entity.
	 */
	private VisualEntity pickedEntity = null;
	/**
	 * Reference to the selection service.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the current scene data service.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Reference to the current finder service.
	 */
	private FinderService finderService;
	/**
	 * Reference to the 3d view.
	 */
	private View3D view3D;

	/**
	 * Constructor.
	 * 
	 * @param view3D
	 *            the 3d view
	 * @param selectionService
	 *            the current selection service.
	 */
	public MousePickListener(View3D view3D, SelectionService selectionService,
			SceneDataService sceneDataService, FinderService finderService) {
		this.view3D = view3D;
		this.selectionService = selectionService;
		this.sceneDataService = sceneDataService;
		this.finderService = finderService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if (e.button == 1 || e.button == 3) {
			pickedEntity = null;
			Camera cam = DisplaySystem.getDisplaySystem().getRenderer()
					.getCamera();
			// create ray
			int canvasY = ((SWTDisplaySystem) DisplaySystem.getDisplaySystem())
					.getCurrentGLCanvas().getSize().y;
			Vector3f coord = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 0);
			Vector3f coord2 = cam.getWorldCoordinates(new Vector2f(e.x, canvasY
					- e.y), 1);
			Ray ray = new Ray(coord, coord2.subtractLocal(coord)
					.normalizeLocal());
			PickResults pickResults = new BoundingPickResults();
			// shoot
			sceneDataService.getCurrentSceneData().getRootNode().findPick(ray,
					pickResults);
			Node node = null;
			// loop[ through the results to find an entity
			// this has to be done as for some reasons a bounding box appears
			// around the room and is hit first, darn
			float distance = -1;
			for (int count = 0; count < pickResults.getNumber(); count++) {
				// find the one closest to the camera
				if (distance == -1
						|| distance > pickResults.getPickData(count)
								.getDistance()) {

					node = pickResults.getPickData(count).getTargetMesh()
							.getParentGeom().getParent();
					VisualEntity _pickedEntity = finderService
							.getVisualEntityByNode(node);
					
					if (_pickedEntity != null) {
						pickedEntity = _pickedEntity;
						distance = pickResults.getPickData(count).getDistance();
					}
				}
			}

			if (pickedEntity == null) {
				selectionService.clearSelection();
			}

			// if we found something tell the editor to do it's magic
			if (pickedEntity != null) {
				if (!multiselect) {
					selectionService.select(pickedEntity, false, true);
				} else {
					selectionService.select(pickedEntity, true, true);
				}
				if (e.button == 1) {
					view3D.switchMode(View3D.Mode.PlacementMode);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		if (e.keyCode == SWT.SHIFT) {
			multiselect = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		if (e.keyCode == SWT.SHIFT) {
			multiselect = false;
		}
	}
}