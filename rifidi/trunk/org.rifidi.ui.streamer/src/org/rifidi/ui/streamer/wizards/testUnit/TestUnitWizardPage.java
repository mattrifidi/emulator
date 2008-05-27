package org.rifidi.ui.streamer.wizards.testUnit;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.composites.TestUnitComposite;
import org.rifidi.ui.streamer.data.TestUnitEventAwareWrapper;

public class TestUnitWizardPage extends WizardPage {

	private TestUnit testUnit;
	private TestUnitComposite testUnitComposite;

	protected TestUnitWizardPage(String pageName, TestUnit testUnit) {
		super(pageName);
		this.testUnit = testUnit;
		setMessage("Create a new TestUnit by setting at least the TestUnit ID.\n This ID needs to be unique.");
		setTitle("New Batch Wizard");
		setPreviousPage(null);

		// Page can only be completed if the id is insert
		setPageComplete(true);
	}

	@Override
	public void createControl(Composite parent) {
		testUnitComposite = new TestUnitComposite(parent,
				SWT.NONE, true);
		testUnitComposite.setTestUnit(new TestUnitEventAwareWrapper(
				testUnit));

		// This is essential otherwise the Page cannot be shown (Strange
		// Eclipse!!!)
		setControl(parent);

	}
}
