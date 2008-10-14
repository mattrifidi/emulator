package org.rifidi.ui.ide.workbench;

import java.io.IOException;
import java.net.UnknownHostException;
import java.rmi.ConnectException;
import java.rmi.RemoteException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.rifidi.emulator.rmi.server.RifidiManager;
import org.rifidi.ui.common.registry.ReaderRegistry;

/**
 * Eclipse generated file, defining the Workbench Window. Additional added
 * function - starting the RMI Service for the RifidiEmulator - starting the
 * ViewManager to keep track of the views associated to the readers
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	/**
	 * The log4j logger for this project
	 */
	private static final Log logger = LogFactory
			.getLog(ApplicationWorkbenchWindowAdvisor.class);

	@SuppressWarnings("unused")
	private ViewManager viewManager;
	private IWorkbenchWindowConfigurer configurer;

	/**
	 * @param configurer
	 */
	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#createActionBarAdvisor(org.eclipse.ui.application.IActionBarConfigurer)
	 */
	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#preWindowOpen()
	 */
	public void preWindowOpen() {
		configurer = getWindowConfigurer();
		configurer.setInitialSize(new Point(1024, 768));
		configurer.setShowMenuBar(true);
		configurer.setShowCoolBar(true);
		configurer.setShowStatusLine(true);

		configurer.setTitle("Rifidi Emulator");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.application.WorkbenchWindowAdvisor#postWindowOpen()
	 */
	@Override
	public void postWindowOpen() {

		logger.debug("Log4J system initialized and started");

		// TODO logic for RifidiManager startUP
		try {
			RifidiManager.startManager("127.0.0.1", 1198);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			logger.debug(e.getMessage());
			MessageBox messageBox = new MessageBox(configurer.getWindow()
					.getShell());
			messageBox.setMessage("Could not start RMI Server because "
					+ "the socket is already in use!");
			messageBox.open();
			ActionFactory.QUIT.create(configurer.getWindow()).run();
			return;
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.debug("Starting up ViewManager ");
		// Get the View Manager responsible for showing Views like (AntennaView
		// and ConsoleView)
		viewManager = new ViewManager(configurer.getWindow());

		// Starting the RMI Service to provide the Emulator functionality
		try {
			ReaderRegistry.getInstance().connect("127.0.0.1", 1198);
		} catch (ConnectException e) {
			// Handle this Exception better
			e.printStackTrace();
		}
		getWindowConfigurer().getActionBarConfigurer().getStatusLineManager()
				.setMessage("RMI - Emulator connected to 127.0.0.1:1198");
		super.postWindowOpen();
	}
}
