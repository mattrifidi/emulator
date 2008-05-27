package org.rifidi.ui.streamer.executeDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.streamer.executers.ScenarioExecuter;
import org.rifidi.streamer.executers.TestUnitExecuter;
import org.rifidi.streamer.executers.listener.ScenarioStateListener;

public class ExecuteDialogContentProvider implements
		IStructuredContentProvider, ScenarioStateListener {

	private Log logger = LogFactory.getLog(ExecuteDialogContentProvider.class);
	private TableViewer viewer;

	// private TableViewer viewer;

	@Override
	public Object[] getElements(Object inputElement) {
		logger.debug(inputElement.getClass().getSimpleName());
		if (inputElement instanceof TestUnitExecuter) {
			return ((TestUnitExecuter) inputElement).getScenarioExecuters()
					.toArray();
		}
		return null;
	}

	@Override
	public void dispose() {
		TestUnitExecuter testUnitExecuter = (TestUnitExecuter) viewer
				.getInput();
		for (ScenarioExecuter scenario : testUnitExecuter
				.getScenarioExecuters()) {
			scenario.removeStateChangeListener(this);
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;
		if (newInput != null) {
			if (newInput instanceof TestUnitExecuter) {
				TestUnitExecuter testUnitExecuter = (TestUnitExecuter) newInput;
				for (ScenarioExecuter scenario : testUnitExecuter
						.getScenarioExecuters()) {
					scenario.addStateChangeListener(this);
				}
			}
		}
		if (oldInput != null) {
			if (newInput instanceof TestUnitExecuter) {
				TestUnitExecuter testUnitExecuter = (TestUnitExecuter) newInput;
				for (ScenarioExecuter scenario : testUnitExecuter
						.getScenarioExecuters()) {
					scenario.removeStateChangeListener(this);
				}
			}
		}
	}

	@Override
	public void stateChangeEvent(final Object event) {
		viewer.getControl().getDisplay().asyncExec(new Runnable() {
			public void run() {
				if (!viewer.getTable().isDisposed())
					viewer.update(event, null);
			}
		});

	}

}
