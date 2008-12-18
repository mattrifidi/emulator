package org.rifidi.ui.streamer.data;

import java.util.ArrayList;
import java.util.List;

import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.ui.common.registry.RegistryChangeListener;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class BatchEventAwareWrapper implements EventAwareWrapper {

	private Batch batch;
	private List<Action> actionList;
	private ArrayList<RegistryChangeListener> listeners = new ArrayList<RegistryChangeListener>();

	public BatchEventAwareWrapper(Batch batch) {
		this.batch = batch;
		actionList = batch.getActions();
		if (actionList == null)
			actionList = new ArrayList<Action>();
	}

	/**
	 * @return the actionList
	 */
	public List<Action> getList() {
		return actionList;
	}

	/**
	 * @return the batch
	 */
	public Batch getBatch() {
		return batch;
	}

	/**
	 * @param action
	 */
	public void add(Object action) {
		actionList.add((Action) action);
		addEvent(action);
	}

	/**
	 * @param action
	 */
	public void remove(Object action) {
		actionList.remove(action);
		removeEvent(action);
	}

	/**
	 * @param listener
	 */
	public void addListener(RegistryChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeListener(RegistryChangeListener listener) {
		listeners.remove(listener);
	}

	private void addEvent(Object event) {
		for (RegistryChangeListener listener : listeners) {
			listener.add(event);
		}
	}

	private void removeEvent(Object event) {
		for (RegistryChangeListener listener : listeners) {
			listener.remove(event);
		}
	}

}
