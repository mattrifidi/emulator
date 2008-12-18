/**
 * 
 */
package org.rifidi.ui.streamer.wizards.scenario;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.ui.streamer.composites.ScenarioComposite;
import org.rifidi.ui.streamer.data.ScenarioEventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ScenarioWizardPage extends WizardPage {

	private Scenario scenario;

	protected ScenarioWizardPage(String pageName, Scenario scenario) {
		super(pageName);
		this.scenario = scenario;

		setMessage("Create a new Scenario by setting at least the Batch ID");
		setTitle("New Scenario Wizard");
		setPreviousPage(null);

		setPageComplete(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		ScenarioComposite scenarioComposite = new ScenarioComposite(parent,
				SWT.NONE, true);
		scenarioComposite.setScenario(new ScenarioEventAwareWrapper(scenario));

		// This is essential otherwise the Page cannot be shown (Strange
		// Eclipse!!!)
		setControl(parent);
	}
}
