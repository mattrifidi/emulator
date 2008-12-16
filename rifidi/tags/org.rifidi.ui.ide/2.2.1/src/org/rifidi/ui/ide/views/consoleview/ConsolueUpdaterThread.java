/*
 *  ConsolueUpdaterThread.java
 *
 *  Created:	Mar 20, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.ide.views.consoleview;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.rifidi.ui.common.registry.ReaderRegistry;

/**
 * This Thread updates the output on the console for the associated Reader. The
 * logs are obtained over RMI getCachedLog()
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ConsolueUpdaterThread extends Thread {

	// private Log logger=LogFactory.getLog(ConsolueUpdaterThread.class);

	private StyledText text;
	private String readerName;
	private boolean stopped = false;

	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return stopped;
	}

	/**
	 * @param stopped
	 *            the stopped to set
	 */
	public void setStopped(boolean stopped) {
		this.stopped = stopped;
	}

	/**
	 * Constructor
	 * 
	 * @param text
	 * @param readerName
	 */
	public ConsolueUpdaterThread(StyledText text, String readerName) {
		this.text = text;
		this.readerName = readerName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		// Runner Thread for communication with EclipseFramework
		Runner runner = new Runner(text);

		// ArrayList for Console output
		ArrayList<String> consoleCache = null;

		ReaderRegistry readerRegistry = ReaderRegistry.getInstance();

		while (stopped == false) {
			// Fetch the ConsoleCache for Reader
			try {
				consoleCache = (ArrayList<String>) readerRegistry
						.getCachedLogs(readerName);

				// if we got something add it to the textfield
				if (consoleCache.size() > 0) {
					runner.setNewText(consoleCache);
					text.getDisplay().syncExec(runner);
				}
			} catch (NullPointerException e) {
				// TODO: we expect this
			}
			// relax a bit
			try {
				sleep(50);
			} catch (InterruptedException ie) {
			}
		}
	}

	/**
	 * Thread that does the actual work.
	 * 
	 * @author Jochen Mader - jochen@pramari.com
	 * @author Andreas Huebner - andreas@pramari.com
	 * 
	 */
	private class Runner implements Runnable {

		private StyledText text;
		private List<String> newText;
		private int count = 0;
		private boolean firstRun = true;

		/**
		 * @param text is the Text to set in the Widget
		 */
		public Runner(StyledText text) {
			this.text = text;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			if (firstRun) {
				firstRun = false;
				text.setText("");
			}
			for (String item : newText) {

				String color = item.substring(0, item.indexOf(">") + 1);
				String message = item.substring(item.indexOf(">") + 1);
				if (color.equals("<INPUT>")) {
					text.append(message);
					text.setLineBackground(count, 1, text.getDisplay()
							.getSystemColor(SWT.COLOR_RED));
				} else if (color.equals("<OUTPUT>")) {
					text.append(message);
					text.setLineBackground(count, 1, text.getDisplay()
							.getSystemColor(SWT.COLOR_GREEN));

				} else {
					text.append(message);
				}
				text.setSelection(text.getCharCount());
				text.showSelection();
				count++;
			}
		}

		/**
		 * @param newText
		 *            the newText to set
		 */
		public void setNewText(List<String> newText) {
			this.newText = newText;
		}
	}
}
