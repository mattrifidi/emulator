package org.rifidi.ui.streamer.views.scenario;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.ui.streamer.composites.ScenarioComposite;
import org.rifidi.ui.streamer.data.ScenarioEventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ScenarioView extends ViewPart {

	public static final String ID = "org.rifidi.ui.streamer.views.scenario.ScenarioView";
	private Scenario scenario;
	private ScenarioComposite scenarioComposite;

	/**
	 * 
	 */
	public ScenarioView() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		scenarioComposite = new ScenarioComposite(parent, SWT.NONE, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setScenario(Scenario scenario) {
		// Update UI elements
		this.scenario = scenario;
		updateUI();
	}

	private void updateUI() {
		if (scenario != null) {
			setPartName("Scenario " + scenario.getID());
			scenarioComposite.setScenario(new ScenarioEventAwareWrapper(
					scenario));
		}
	}

}
