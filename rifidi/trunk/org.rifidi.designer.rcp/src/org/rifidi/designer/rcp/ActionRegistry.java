/*
 *  ActionRegistry.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.Action;


/**
 * This class is just a helper to store actions in.
 * It's just a helper for easily enabling/disabling actions.
 * 
 * @author Jochen Mader Nov 24, 2007
 *
 */
public class ActionRegistry {
	/**
	 * The registered actions.
	 */
	private Map<String, Action> actionsById;
	
	/**
	 * Constructor.
	 */
	public ActionRegistry(){
		actionsById=new HashMap<String, Action>();
	}
	
	/**
	 * Add a new action.
	 * @param action the action to add
	 */
	public void addAction(final Action action){
		actionsById.put(action.getId(), action);
	}
	
	/**
	 * Find an action by its eclipse id.
	 * @param name the name/id of the action
	 * @return the action or null
	 */
	public Action getActionById(final String name){
		return actionsById.get(name);
	}
}
