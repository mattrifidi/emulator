/*
 *  ConsoleView.java
 *
 *  Created:	Nov 3, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */

package org.rifidi.ui.ide.views.consoleview;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

/**
 * This is a view that will show the console output of selected readers.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ConsoleView extends ViewPart {

	public static final String ID = "org.rifidi.ui.ide.views.consoleview.ConsoleView";

	private Log logger = LogFactory.getLog(ConsoleView.class);

	private Composite top = null;

	private Composite composite = null;

	private StyledText text = null;

	private String readerName = "";

	private ConsolueUpdaterThread updaterThread = null;

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		top = new Composite(parent, SWT.NONE);
		top.setLayout(new FillLayout());
		createComposite();
	}

	/**
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	/**
	 * This method initializes composite
	 * 
	 */
	private void createComposite() {
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.verticalAlignment = org.eclipse.swt.layout.GridData.FILL;
		gridData.grabExcessVerticalSpace = true;
		composite = new Composite(top, SWT.NONE);
		composite.setLayout(new GridLayout());
		text = new StyledText(composite, SWT.BORDER | SWT.V_SCROLL | SWT.WRAP
				| SWT.H_SCROLL);
		text.setText("");
		text.setLayoutData(gridData);

		readerName = getViewSite().getSecondaryId();
		setReaderName(readerName);
		logger.debug("Consoleview with name " + readerName);
	}

	/**
	 * @return the readerName
	 */
	public String getReaderName() {
		return readerName;
	}

	/**
	 * @return the current console log
	 */
	// added getText for ConsoleSaveAction
	public String getText() {
		return text.getText();
	}

	/**
	 * this
	 * 
	 * @param readerName
	 *            the readerName to set
	 */
	public void setReaderName(String readerName) {
		this.readerName = readerName;
		if (!(updaterThread == null)) {
			updaterThread.setStopped(true);
		}
		setPartName(readerName);
		updaterThread = new ConsolueUpdaterThread(text, readerName);
		updaterThread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		// get rid of the thread that updates this view
		updaterThread.setStopped(true);
		super.dispose();
	}

	public void stopConsoleUpdaterThread() {
		updaterThread.setStopped(true);
	}
}
