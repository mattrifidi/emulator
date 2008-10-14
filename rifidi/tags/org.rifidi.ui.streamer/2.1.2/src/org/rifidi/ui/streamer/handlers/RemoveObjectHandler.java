package org.rifidi.ui.streamer.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.views.batch.BatchView;
import org.rifidi.ui.streamer.views.components.ComponentView;
import org.rifidi.ui.streamer.views.scenario.ScenarioView;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;
import org.rifidi.ui.streamer.views.testUnit.TestUnitView;

public class RemoveObjectHandler implements IHandler {

	private Log logger = LogFactory.getLog(LoadTestSuiteHandler.class);
	private IWorkbenchWindow window;

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		logger.error(event.getCommand().getId() + " got invoked");
		window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

		// Object selection = ((IStructuredSelection) window.getActivePage()
		// .getSelection()).getFirstElement();

		TestSuiteView testSuiteView = (TestSuiteView) window.getActivePage()
				.findView(TestSuiteView.ID);
		Object selection = testSuiteView.getSelectedObject();

		InputObjectRegistry inputObjectRegistry = testSuiteView
				.getInputObjectRegistry();

		if (selection instanceof Batch) {
			Batch batch = (Batch) selection;
			if (showWarning("Batch " + batch.getID())) {
				logger.debug("Delete Batch " + batch.getID());
				inputObjectRegistry.removeBatch(batch);
				IViewReference view = window.getActivePage().findViewReference(
						BatchView.ID, "Batch" + batch.getID());
				if (view != null)
					window.getActivePage().hideView(view);
			}
		}
		if (selection instanceof Scenario) {
			Scenario scenario = (Scenario) selection;
			if (showWarning("Scenario " + scenario.getID())) {
				logger.debug("Delete Scenario " + scenario.getID());
				inputObjectRegistry.removeScenario(scenario);
				IViewReference view = window.getActivePage().findViewReference(
						ScenarioView.ID, "Scenario" + scenario.getID());
				if (view != null)
					window.getActivePage().hideView(view);
			}
		}
		if (selection instanceof ReaderComponent) {
			ReaderComponent readerComponent = (ReaderComponent) selection;
			if (showWarning("ReaderComponent " + readerComponent.getID())) {
				logger.debug("Delete ReaderComponent "
						+ readerComponent.getID());
				inputObjectRegistry.removeComponent(readerComponent);
				IViewReference view = window.getActivePage().findViewReference(
						ComponentView.ID, "Component" + readerComponent.getID());
				if (view != null)
					window.getActivePage().hideView(view);
			}
		}
		if (selection instanceof TestUnit) {
			TestUnit testUnit = (TestUnit) selection;
			if (showWarning("TestUnit " + testUnit.getID())) {
				logger.debug("Delete TestUnit " + testUnit.getID());
				inputObjectRegistry.removeTestUnit(testUnit);
				IViewReference view = window.getActivePage().findViewReference(
						TestUnitView.ID, "TestUnit" + testUnit.getID());
				if (view != null)
					window.getActivePage().hideView(view);
			}
		}

		return null;
	}

	private boolean showWarning(String msg) {
		MessageBox messageBox = new MessageBox(window.getShell(),
				SWT.ICON_WARNING | SWT.YES | SWT.NO);
		messageBox.setText("Warning: Removing Item");
		messageBox.setMessage("Do you really want to delete this item?" + "\n"
				+ msg);
		if (messageBox.open() == SWT.YES) {
			return true;
		} else
			return false;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
