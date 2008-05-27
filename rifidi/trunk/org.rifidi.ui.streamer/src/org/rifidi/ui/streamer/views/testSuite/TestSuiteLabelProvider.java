/**
 * 
 */
package org.rifidi.ui.streamer.views.testSuite;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.rifidi.streamer.xml.BatchSuite;
import org.rifidi.streamer.xml.ComponentSuite;
import org.rifidi.streamer.xml.ScenarioSuite;
import org.rifidi.streamer.xml.TestUnitSuite;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.streamer.xml.scenario.Scenario;
import org.rifidi.streamer.xml.testSuite.TestUnit;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TestSuiteLabelProvider implements ILabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		// if (element instanceof ReaderComponent)
		// return Activator.getImageDescriptor(
		// "icons/reader.png").createImage();
		// if (element instanceof Batch)
		// return Activator.getImageDescriptor(
		// "icons/taglist.png").createImage();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof BatchSuite) {
			return "Batches";
		}
		if (element instanceof ComponentSuite) {
			return "Components";
		}
		if (element instanceof ScenarioSuite) {
			return "Scenarios";
		}
		if (element instanceof TestUnitSuite) {
			return "TestUnits";
		}
		if (element instanceof Batch) {
			return "Batch " + ((Batch) element).getID();
		}
		if (element instanceof Scenario) {
			return "Scenario " + ((Scenario) element).getID();
		}
		if (element instanceof ReaderComponent) {
			return "Component " + ((ReaderComponent) element).getID();
		}
		if (element instanceof TestUnit) {
			return "TestUnit " + ((TestUnit) element).getID();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
