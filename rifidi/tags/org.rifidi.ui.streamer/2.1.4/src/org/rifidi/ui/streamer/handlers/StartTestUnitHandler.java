package org.rifidi.ui.streamer.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.executeDialog.ExecuteDialog;
import org.rifidi.ui.streamer.executeDialog.TestUnitSelectionDialog;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class StartTestUnitHandler implements IHandler {

	private Log logger = LogFactory.getLog(LoadTestSuiteHandler.class);

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		logger.debug("Start TestUnit command called");
		// IStructuredSelection selection = (IStructuredSelection) window
		// .getActivePage().getSelection();
		TestSuiteView view = (TestSuiteView) window.getActivePage().findView(
				TestSuiteView.ID);
		InputObjectRegistry inputObjectRegistry = ((TestSuiteView) window
				.getActivePage().findView(TestSuiteView.ID))
				.getInputObjectRegistry();
		IStructuredSelection selection = (IStructuredSelection) view
				.getTreeView().getSelection();

		TestUnit selectedTestUnit = null;
		if (inputObjectRegistry != null) {
			if (!selection.isEmpty()) {
				Object o = (selection).getFirstElement();
				if (o instanceof TestUnit) {
					selectedTestUnit = (TestUnit) o;
				}
			}
			if (selectedTestUnit == null) {
				if(inputObjectRegistry.getTestUnitSuite() != null)
				{
				TestUnitSelectionDialog testUnitSelectionDialog = new TestUnitSelectionDialog(
						window.getShell(), "Select a TestUnit to run",
						inputObjectRegistry.getTestUnitSuite().getTestUnits());
				selectedTestUnit = testUnitSelectionDialog.open();
				}
			}
		}
		if (selectedTestUnit != null) {
			logger.info("Executing TestUnit " + selectedTestUnit.getID());
			ExecuteDialog executeDialog = new ExecuteDialog(window.getShell(),
					selectedTestUnit, inputObjectRegistry);
			executeDialog.open();
		} else {
			MessageDialog
					.openInformation(window.getShell(),
							"No TestUnits available",
							"There are no TestUnits available. Please create some TestUnits and try again.");
		}
		return null;
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
	}

}
