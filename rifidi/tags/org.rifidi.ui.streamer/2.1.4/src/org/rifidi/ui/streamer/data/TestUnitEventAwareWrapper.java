package org.rifidi.ui.streamer.data;

import java.util.ArrayList;
import java.util.List;

import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.common.registry.RegistryChangeListener;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TestUnitEventAwareWrapper implements EventAwareWrapper {
	private TestUnit testUnit;
	private List<Action> actionList;
	private ArrayList<RegistryChangeListener> listeners = new ArrayList<RegistryChangeListener>();

	public TestUnitEventAwareWrapper(TestUnit testUnit) {
		this.testUnit = testUnit;
		actionList = testUnit.getActions();
		if (actionList == null)
			actionList = new ArrayList<Action>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.views.testUnit.provider.EventAwareWrapper#getActionList()
	 */
	public List<Action> getList() {
		return actionList;
	}

	/**
	 * @return
	 */
	public TestUnit getTestUnit() {
		return testUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.views.testUnit.provider.EventAwareWrapper#addAction(org.rifidi.streamer.xml.actions.Action)
	 */
	public void add(Object action) {
		actionList.add((Action) action);
		addEvent(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.views.testUnit.provider.EventAwareWrapper#removeAction(org.rifidi.streamer.xml.actions.Action)
	 */
	public void remove(Object action) {
		actionList.remove(action);
		removeEvent(action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.views.testUnit.provider.EventAwareWrapper#addListener(org.rifidi.ui.common.registry.RegistryChangeListener)
	 */
	public void addListener(RegistryChangeListener listener) {
		listeners.add(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.ui.streamer.views.testUnit.provider.EventAwareWrapper#removeListener(org.rifidi.ui.common.registry.RegistryChangeListener)
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
