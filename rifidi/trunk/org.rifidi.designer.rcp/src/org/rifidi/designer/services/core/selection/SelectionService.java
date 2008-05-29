/*
 *  SelectionService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.selection;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionProvider;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.VisualEntity;

/**
 * This service is responsible for handling the selection of entities throughout
 * the application. It is also a selectionprovider to allow easy cahining of
 * views.
 * 
 * @author Jochen Mader Jan 24, 2008
 * @tags
 * 
 */
public interface SelectionService extends ISelectionProvider {

	/**
	 * Select the given entity.
	 * 
	 * @param ent
	 *            the selected visual entity
	 * @param multiple
	 *            select multiple entities
	 * @param informlisteners
	 *            inform the listeners about this selection
	 */
	void select(VisualEntity ent, boolean multiple, boolean informlisteners);

	/**
	 * Select the given list of entity.
	 * 
	 * @param entities
	 *            the list of selected visual entity
	 * @param informlisteners
	 *            inform the listeners about this selection
	 * @param source
	 *            the source of the event
	 */
	void select(List<VisualEntity> entities, boolean informlisteners,
			Object source);

	/**
	 * Clear selection.
	 */
	void clearSelection();

	/**
	 * @return the selection
	 */
	List<Entity> getSelectionList();

}
