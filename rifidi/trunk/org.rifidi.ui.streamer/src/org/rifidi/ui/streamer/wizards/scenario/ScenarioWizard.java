package org.rifidi.ui.streamer.wizards.scenario;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.rifidi.streamer.exceptions.DublicateObjectException;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.scenario.PathItem;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;

public class ScenarioWizard extends Wizard implements INewWizard {

	private Scenario scenario;
	private ScenarioWizardPage scenarioWizardPage;
	private IWorkbench workbench;

	public ScenarioWizard() {
		super();
		init();
	}

	public ScenarioWizard(Scenario scenario) {
		super();
		this.scenario = scenario;
		init();
	}

	public void init() {
		if (scenario == null) {
			scenario = new Scenario();
			scenario.setPathItems(new ArrayList<PathItem>());
		}
		setWindowTitle("New Scenario Wizard");
		scenarioWizardPage = new ScenarioWizardPage("Scenario Settings",
				scenario);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.wizard.Wizard#addPages()
	 */
	@Override
	public void addPages() {
		addPage(scenarioWizardPage);
	}

	@Override
	public boolean performFinish() {
		if (scenario != null && scenario.getID() >= 0) {
			TestSuiteView testSuiteView = (TestSuiteView) workbench
					.getActiveWorkbenchWindow().getActivePage().findView(
							TestSuiteView.ID);
			InputObjectRegistry inputObjectRegistry = testSuiteView
					.getInputObjectRegistry();
			try {
				inputObjectRegistry.registerScenario(scenario);
			} catch (DublicateObjectException e) {
				MessageDialog
						.openError(
								workbench.getActiveWorkbenchWindow().getShell(),
								"Error while creating Scenario!",
								"Couldn't create Scenario with ID "
										+ scenario.getID()
										+ " because there is already a Scenario with this ID.");
				return false;
			}
		}
		return scenarioWizardPage.isPageComplete();
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;

	}

}
