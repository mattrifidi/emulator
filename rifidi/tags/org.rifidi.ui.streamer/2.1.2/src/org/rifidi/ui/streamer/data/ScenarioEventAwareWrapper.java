package org.rifidi.ui.streamer.data;

import java.util.ArrayList;
import java.util.List;

import org.rifidi.streamer.xml.scenario.PathItem;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.ui.common.registry.RegistryChangeListener;

public class ScenarioEventAwareWrapper implements EventAwareWrapper {

	private Scenario scenario;
	private List<PathItem> pathItemList;
	private ArrayList<RegistryChangeListener> listeners = new ArrayList<RegistryChangeListener>();

	public ScenarioEventAwareWrapper(Scenario scenario) {
		this.scenario = scenario;
		pathItemList = scenario.getPathItems();
		if (pathItemList == null)
			pathItemList = new ArrayList<PathItem>();
	}

	public List<PathItem> getList() {
		return pathItemList;
	}

	/**
	 * @return the batch
	 */
	public Scenario getScenario() {
		return scenario;
	}

	/**
	 * @param action
	 */
	public void add(Object pathItem) {
		pathItemList.add((PathItem) pathItem);
		addEvent(pathItem);
	}

	/**
	 * @param action
	 */
	public void remove(Object pathItem) {
		pathItemList.remove(pathItem);
		removeEvent(pathItem);
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
