/*
 *  NewSceneDataHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.entities;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;
import org.rifidi.designer.entities.placement.BinaryPattern;
import org.rifidi.designer.entities.placement.BitMap;
import org.rifidi.designer.rcp.views.view3d.View3D;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for rotating entities that are currently selected in the selection
 * provider.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class RotateSelectedHandler extends AbstractHandler {
	/**
	 * Reference to the selection service.
	 */
	private SelectionService selectionService;
	/**
	 * Reference to the scene data service.
	 */
	private SceneDataService sceneDataService;

	/**
	 * Constructor.
	 */
	public RotateSelectedHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// get the selected entity and rotate it

		List<Entity> selection = selectionService.getSelectionList();
		if (!View3D.moving) {
			// get the scene information, the entity, and its pattern
			BitMap sceneBitMap = sceneDataService.getCurrentSceneData()
					.getBitMap();
			for (Object obj : selection.toArray()) {
				VisualEntity entity = (VisualEntity) obj;
				// remove initial rotation from the bit map
				sceneBitMap.removePattern(entity.getPositionFromTranslation(),
						entity.getPattern().getPattern());

				// duplicate the pattern for post-rotation checking and find its
				// position
				BinaryPattern clonedPattern = (BinaryPattern) entity
						.getPattern().clone();
				clonedPattern.rotateRight();

				// check if this is a valid configuration
				if (!sceneBitMap.checkCollision(entity
						.getPositionFromTranslation(clonedPattern),
						clonedPattern.getPattern())) {
					entity.rotateRight(); // if no potential collisions,
					// rotate
				} else {
					// otherwise, don't rotate
					// TODO: dan send message
					// view3D.getSceneData().getWorldData().getOverlayManager().postMessage("Invalid
					// rotation!", 2.5f);
				}

				// re-place the entity in the bitmap
				sceneBitMap.addPattern(entity.getPositionFromTranslation(),
						entity.getPattern().getPattern());
			}
			return null;
		}
		for (Object obj : selection.toArray()) {
			VisualEntity entity = (VisualEntity) obj;
			entity.rotateRight();
		}
		return null;
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
	}

	/**
	 * @param selectionService
	 *            the selectionService to set
	 */
	@Inject
	public void setSelectionService(SelectionService selectionService) {
		this.selectionService = selectionService;
	}
}
