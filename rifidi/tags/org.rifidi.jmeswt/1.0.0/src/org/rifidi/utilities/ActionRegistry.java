/**
 * 
 */
package org.rifidi.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.action.IAction;

/**
 * As eclipse doesn't provide a consistent way to enable/disbale actions we are
 * doing it on our own. Since multiple actions may have the same id, a list is used
 * to store collisions.
 * 
 * @author Jochen Mader
 * @author Dan West
 */
public class ActionRegistry {
	private static Log logger = LogFactory.getLog(ActionRegistry.class);
	private static ActionRegistry instance;
	private Map<String,List<IAction>> actions;

	/**
	 * Constructor: initialize storage for actions
	 */
	private ActionRegistry(){
		actions = new HashMap<String, List<IAction>>();
		logger.info("Action registry has been initialized");
	}

	/**
	 * Get the singleton instance of the action registry
	 * @return the singleton instance of the action registry
	 */
	public static ActionRegistry getInstance(){
		if( instance == null )
			instance = new ActionRegistry();
		return instance;
	}

	/**
	 * Add the provided action to the registry
	 * @param action the action to add
	 */
	public void addIAction(IAction action){
		if ( actions.get(action.getId()) == null ) {
			actions.put(action.getId(), new ArrayList<IAction>());
			addIAction(action);
		} else {
			actions.get(action.getId()).add(action);
			logger.info("Action ("+action.getId()+") added to the action registry.");
		}
	}

	/**
	 * Remove the provided action from the registry
	 * @param action the action to remove
	 */
	public void removeIAction(IAction action) {
		List<IAction> idlist = actions.get(action.getId());
		if ( idlist != null ) {
			idlist.remove(action);
			if ( idlist.size() == 0 )
				actions.remove(action.getId());
		}
	}

	/**
	 * Set the enabled state for the given actionId to the desired value
	 * @param actionId the id of the action whose state to change
	 * @param value the new enablement value for the given state
	 */
	public void setEnablement(String actionId, boolean value){
		if ( actions.get(actionId) != null )
			for ( IAction a : actions.get(actionId) )
				a.setEnabled(value);
		else
			logger.warn("Enablement not updated for action id: "+actionId+". The action wasn't found in the registry.");
	}

	/**
	 * Get the enablement state for the requested action
	 * @param actionId the action whose state to return
	 * @return the state of the given actionid
	 */
	public Boolean getEnablement(String actionId) {
		if ( actions.get(actionId) != null && actions.get(actionId).size() > 0 )
			return actions.get(actionId).get(0).isEnabled();
		return null;
	}

	/**
	 * Get an unmodifiable copy of the action hash
	 * @return a copy of the map of all actions
	 */
	public Map<String,List<IAction>> getActions(){
		return Collections.unmodifiableMap(actions);
	}
}
