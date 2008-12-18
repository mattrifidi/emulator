package org.rifidi.ui.streamer.wizards.testUnit;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class TestUnitWizard extends Wizard implements INewWizard {

	private TestUnit testUnit;
	private IWorkbench workbench;
	private TestUnitWizardPage testUnitWizardPage;

	public TestUnitWizard() {
		super();
		init();
	}

	public TestUnitWizard(TestUnit testUnit) {
		super();
		this.testUnit = testUnit;
		init();
	}

	private void init() {
		if (testUnit == null) {
			testUnit = new TestUnit();
			testUnit.setActions(new ArrayList<Action>());
			testUnit.setIterations(-1);
		}
		setWindowTitle("New TestUnit Wizard");
		testUnitWizardPage = new TestUnitWizardPage("TestUnit Settings",
				testUnit);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(testUnitWizardPage);
	}

	@Override
	public boolean performFinish() {
		if (testUnit != null && testUnit.getID() >= 0) {
			TestSuiteView testSuiteView = (TestSuiteView) workbench
					.getActiveWorkbenchWindow().getActivePage().findView(
							TestSuiteView.ID);
			InputObjectRegistry inputObjectRegistry = testSuiteView
					.getInputObjectRegistry();
			try {
				inputObjectRegistry.registerTestUnit(testUnit);
			} catch (DublicateObjectException e) {
				MessageDialog
						.openError(
								workbench.getActiveWorkbenchWindow().getShell(),
								"Error while creating TestUnit!",
								"Couldn't create TestUnit with ID "
										+ testUnit.getID()
										+ " because there is already a TestUnit with this ID.");
				return false;
			}
		}

		return testUnitWizardPage.isPageComplete();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
	}

}
