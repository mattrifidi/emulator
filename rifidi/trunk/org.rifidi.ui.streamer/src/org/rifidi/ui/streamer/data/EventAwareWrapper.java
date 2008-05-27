package org.rifidi.ui.streamer.data;

import java.util.List;

import org.rifidi.ui.common.registry.RegistryChangeListener;

public interface EventAwareWrapper {

	/**
	 * @return the actionList
	 */
	public abstract List<?> getList();

	/**
	 * @param action
	 */
	public abstract void remove(Object item);

	/**
	 * @param item
	 */
	public abstract void add(Object item);

	/**
	 * @param listener
	 */
	public abstract void addListener(RegistryChangeListener listener);

	/**
	 * @param listener
	 */
	public abstract void removeListener(RegistryChangeListener listener);

}