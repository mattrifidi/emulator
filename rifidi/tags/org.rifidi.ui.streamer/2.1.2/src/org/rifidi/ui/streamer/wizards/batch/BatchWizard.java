package org.rifidi.ui.streamer.wizards.batch;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class BatchWizard extends Wizard implements INewWizard {

	// private Log logger = LogFactory.getLog(BatchWizard.class);
	private BatchWizardPage batchWizardPage;
	private Batch batch;
	private IWorkbench workbench;

	public BatchWizard() {
		super();
		init();
	}

	public BatchWizard(Batch batch) {
		super();
		this.batch = batch;
		init();

	}

	private void init() {
		if (batch == null) {
			batch = new Batch();
			batch.setActions(new ArrayList<Action>());
		}
		setWindowTitle("New Batch Wizard");
		batchWizardPage = new BatchWizardPage("Batch Settings", batch);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(batchWizardPage);
	}

	@Override
	public boolean performFinish() {
		if (batch != null && batch.getID() >= 0) {
			TestSuiteView testSuiteView = (TestSuiteView) workbench
					.getActiveWorkbenchWindow().getActivePage().findView(
							TestSuiteView.ID);
			InputObjectRegistry inputObjectRegistry = testSuiteView
					.getInputObjectRegistry();
			try {
				inputObjectRegistry.registerBatch(batch);
			} catch (DublicateObjectException e) {
				MessageDialog
						.openError(
								workbench.getActiveWorkbenchWindow().getShell(),
								"Error while creating Batch!",
								"Couldn't create Batch with ID "
										+ batch.getID()
										+ " because there is already a Batch with this ID.");
				return false;
			}
		}

		return batchWizardPage.isPageComplete();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}

}
