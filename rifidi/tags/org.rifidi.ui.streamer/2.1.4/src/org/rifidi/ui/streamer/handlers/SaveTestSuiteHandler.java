package org.rifidi.ui.streamer.handlers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.streamer.handler.FileLoadHandler;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.MetaFile;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.TestUnitSuite;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class SaveTestSuiteHandler implements IHandler {

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
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);

		TestSuiteView testSuiteView = (TestSuiteView) window.getActivePage()
				.findView(TestSuiteView.ID);

		InputObjectRegistry inputObjectRegistry = testSuiteView
				.getInputObjectRegistry();

		ScenarioSuite scenarioSuite = inputObjectRegistry.getScenarioSuite();
		BatchSuite batchSuite = inputObjectRegistry.getBatchSuite();
		ComponentSuite componentSuite = inputObjectRegistry.getComponentSuite();
		TestUnitSuite testUnitSuite = inputObjectRegistry.getTestUnitSuite();

		// TODO later change to MetaFile
		MetaFile metaFile = new MetaFile();
		metaFile.setBatchSuite(batchSuite);
		metaFile.setComponentSuite(componentSuite);
		metaFile.setScenarioSuite(scenarioSuite);
		metaFile.setTestUnitSuite(testUnitSuite);

		FileDialog fileDialog = new FileDialog(window.getShell(), SWT.SAVE);
		String[] filenameExtensions = new String[] { "*.xml", "*" };
		fileDialog.setFilterExtensions(filenameExtensions);
		String filename = fileDialog.open();

		if (filename != null) {
			int index = filename.lastIndexOf(".");
			if (index <= 0) {
				filename = filename + ".xml";
			}
			logger.debug("Would save to: " + filename);
			String ret = FileLoadHandler.save(metaFile);
			try {
				File file = new File(filename);
				if (file.exists()) {
					MessageBox mb = new MessageBox(window.getShell(),
							SWT.ICON_WARNING | SWT.YES | SWT.NO);
					mb.setText("File already exists");
					mb.setMessage("The file " + filename
							+ " already exists.\n Override it?");
					if (mb.open() == SWT.YES) {
						FileWriter fileWriter = new FileWriter(file);
						fileWriter.write(ret);
						fileWriter.close();
					}
				} else {

					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(ret);
					fileWriter.close();
				}

			} catch (IOException e) {
				e.printStackTrace();
			}

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
