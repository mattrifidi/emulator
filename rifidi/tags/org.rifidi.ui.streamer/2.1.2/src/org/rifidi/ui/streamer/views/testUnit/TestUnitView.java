/**
 * 
 */
package org.rifidi.ui.streamer.views.testUnit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.streamer.xml.testSuite.TestUnit;
import org.rifidi.ui.streamer.composites.TestUnitComposite;
import org.rifidi.ui.streamer.data.TestUnitEventAwareWrapper;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TestUnitView extends ViewPart {

	public static final String ID = "org.rifidi.ui.streamer.views.testUnit.TestUnitView";
	private TestUnitEventAwareWrapper testUnit;
	private TestUnitComposite testUnitComposite;

	/**
	 * 
	 */
	public TestUnitView() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		testUnitComposite = new TestUnitComposite(parent,
				SWT.NONE, false);
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

	public void setTestUnit(TestUnit testUnit) {
		// Update UI elements
		this.testUnit = new TestUnitEventAwareWrapper(testUnit);
		updateUI();
	}

	public void updateUI() {
		if (testUnit != null) {
			setPartName("TestUnit " + testUnit.getTestUnit().getID());
			testUnitComposite.setTestUnit(testUnit);
		}
	}

}
