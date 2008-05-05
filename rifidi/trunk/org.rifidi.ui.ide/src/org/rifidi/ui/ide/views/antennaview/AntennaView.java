/*
 *  AntennaView.java
 *
 *  Created:	Oct 26, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.ide.views.antennaview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.ui.common.reader.UIAntenna;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;

/**
 * View used to display antennas and the GPIO's associated with a this reader.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
public class AntennaView extends ViewPart implements ISelectionProvider {

	//private static Log logger = LogFactory.getLog(AntennaView.class);
	
	public static final String ID = "org.rifidi.ui.ide.views.antennaview.AntennaView";

	// the main composite that holds the composites for the individual antennas
	private ScrolledComposite sc;

	// composite inside the scrolled composite
	private Composite antennaChild;
	private Composite gpioChild;
	private GPIOPGroup gpioView;

	// List containing all the composites for the antennas
	private List<TagPGroup> antennaComposites = new ArrayList<TagPGroup>();
	private UIReader uiReader;

	// just a flag that is used to check if the view has already been filled
	// with content
	private boolean empty = true;

	private List<ISelectionChangedListener> selectionListeners = new ArrayList<ISelectionChangedListener>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		this.setPartName(this.getViewSite().getSecondaryId());
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		// Create a child composite to hold the controls
		antennaChild = new Composite(sc, SWT.NONE);
		antennaChild.setLayout(new GridLayout(2, true));
		antennaChild
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sc.setContent(antennaChild);

		uiReader = ReaderRegistry.getInstance().getReaderByName(
				this.getViewSite().getSecondaryId());

		createAntennas(uiReader.getAntennas());

		gpioChild = new Composite(sc, SWT.NONE);
		gpioChild.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		gpioChild.setLayout(new GridLayout());
		gpioView = new GPIOPGroup(gpioChild, SWT.SMOOTH, uiReader);
		gpioView.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		getSite().setSelectionProvider(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	public void toggle() {
		if (uiReader.getNumGPIs() > 0 || uiReader.getNumGPOs() > 0) {
			if (sc.getContent().equals(gpioChild)) {
				sc.setContent(antennaChild);
			} else {
				sc.setContent(gpioChild);
			}
			sc.update();
		}
	}

	/**
	 * Create the antennas
	 * 
	 * @param antennas
	 */
	public void createAntennas(Map<Integer, UIAntenna> antennas) {
		GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);

		// only create composites if none were created so far
		if (empty == true) {
			for (UIAntenna antenna : antennas.values()) {
				TagPGroup tagcomposite = new TagPGroup(antennaChild,
						SWT.SMOOTH, antenna);
				tagcomposite.setLayoutData(gridData);
				antennaComposites.add(tagcomposite);
			}
		}
		empty = false;
		sc
				.setMinSize(600, 400 * (int) Math
						.ceil(((float) antennas.size() / 2)));
		this.antennaChild.pack();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		// dispose all of the pgroups
		for (TagPGroup comp : antennaComposites) {
			comp.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		selectionListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#getSelection()
	 */
	public ISelection getSelection() {
		if (uiReader.getNumGPIs() > 0 || uiReader.getNumGPOs() > 0) {
			return (new StructuredSelection(new Object[] { uiReader }));
		}
		return new StructuredSelection();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#removeSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
	 */
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		selectionListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ISelectionProvider#setSelection(org.eclipse.jface.viewers.ISelection)
	 */
	public void setSelection(ISelection selection) {
	}

}
