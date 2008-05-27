package org.rifidi.ui.streamer.handlers;

import java.io.File;

import javax.xml.bind.JAXBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.handler.FileLoadHandler;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.MetaFile;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.TestUnitSuite;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class LoadTestSuiteHandler implements IHandler {

	private Log logger = LogFactory.getLog(LoadTestSuiteHandler.class);

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
		// TODO implement in a better way
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		TestSuiteView testSuiteView = (TestSuiteView) window.getActivePage()
				.findView(TestSuiteView.ID);

		FileDialog fileDialog = new FileDialog(window.getShell(), SWT.OPEN);
		String[] filenameExtensions = new String[] { "*.xml", "*" };
		fileDialog.setFilterExtensions(filenameExtensions);
		String filename = fileDialog.open();
		if (filename != null) {
			// Reset the Perspective to close all open views and display the
			// placeholder again
			window.getActivePage().resetPerspective();

			InputObjectRegistry inputObjectRegistry = new InputObjectRegistry();

			MetaFile metaFile = null;

			try {
				String parentPath = (new File(filename)).getParent();
				metaFile = FileLoadHandler.openMetaFile(filename, parentPath);

				ScenarioSuite scenarioSuite = metaFile.getScenarioSuite();
				BatchSuite batchSuite = metaFile.getBatchSuite();
				ComponentSuite componentSuite = metaFile.getComponentSuite();
				TestUnitSuite testUnitSuite = metaFile.getTestUnitSuite();

				if (scenarioSuite != null)
					inputObjectRegistry.registerScenarioSuite(scenarioSuite);
				if (batchSuite != null)
					inputObjectRegistry.registerBatchSuite(batchSuite);
				if (componentSuite != null)
					inputObjectRegistry.registerComponentSuite(componentSuite);
				if (testUnitSuite != null)
					inputObjectRegistry.registerTestUnitSuite(testUnitSuite);

				testSuiteView.setInputObjectRegistry(inputObjectRegistry);
			} catch (JAXBException e) {
				MessageDialog.openError(window.getShell(),
						"Couldn't open file!", "The given file: " + filename
								+ " has not a valid TestSuite.xml-Format.");
				logger.error(filename + " is not a valid XML");
			} catch (DublicateObjectException e) {
				e.printStackTrace();
			}

			// LoadTestSuite loadTestSuite;
			// // TODO following is just for testing
			// try {
			// String parentPath = (new File(filename)).getParent();
			//
			// loadTestSuite = FileLoadHandler.openTestSuite(filename,
			// parentPath);
			//
			// for (FileUnit fileUnit : loadTestSuite.getFileUnits()) {
			// logger.debug("Try to load " + fileUnit.getFileName());
			// if (fileUnit.getFileType() == FileUnit.FileType.BATCH) {
			// BatchSuite batchSuite = FileLoadHandler.openBatchSuite(
			// fileUnit.getFileName(), parentPath);
			// inputObjectRegistry.registerBatchSuite(batchSuite);
			// }
			// if (fileUnit.getFileType() == FileUnit.FileType.SCENARIO) {
			// ScenarioSuite scenarioSuite = FileLoadHandler
			// .openScenarioSuite(fileUnit.getFileName(),
			// parentPath);
			// inputObjectRegistry
			// .registerScenarioSuite(scenarioSuite);
			// }
			// if (fileUnit.getFileType() == FileUnit.FileType.COMPONENT) {
			// ComponentSuite componentSuite = FileLoadHandler
			// .openComponentSuite(fileUnit.getFileName(),
			// parentPath);
			// inputObjectRegistry
			// .registerComponentSuite(componentSuite);
			// }
			// }
			// for (TestUnit testUnit : loadTestSuite.getTestUnits()) {
			// inputObjectRegistry.registerTestUnit(testUnit);
			// }
			// testSuiteView.setInputObjectRegistry(inputObjectRegistry);
			// } catch (JAXBException e1) {
			// MessageDialog.openError(window.getShell(),
			// "Couldn't open file!", "The given file: " + filename
			// + " has not a valid TestSuite.xml-Format.");
			// logger.error(filename + " is not a valid XML");
			// } catch (DublicateObjectException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
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
		// TODO Auto-generated method stub

	}

}
