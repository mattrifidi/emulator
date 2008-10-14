/**
 * 
 */
package org.rifidi.ui.streamer.views.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.ui.streamer.composites.ReaderComposite;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ComponentView extends ViewPart {

	public static final String ID = "org.rifidi.ui.streamer.views.components.ComponentView";
	private ReaderComposite readerComposite;

	/**
	 * 
	 */
	public ComponentView() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		readerComposite = new ReaderComposite(parent, SWT.NONE, false);
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

	public void setComponent(ReaderComponent component) {
		if (component != null) {
			readerComposite.setComponent(component);
			setPartName("Component " + component.getID());
		}
	}

}
