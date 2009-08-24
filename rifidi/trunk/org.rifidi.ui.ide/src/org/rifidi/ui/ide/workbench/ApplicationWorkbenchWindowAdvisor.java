package org.rifidi.ui.ide.workbench;

import java.rmi.ConnectException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;

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
	private ReaderRegistryService readerRegistry;

	/**
	 * @param configurer
	 */
	public ApplicationWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		super(configurer);
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * @param readerRegistry the readerRegistry to set
	 */
	@Inject
	public void setReaderRegistry(ReaderRegistryService readerRegistry) {
		this.readerRegistry = readerRegistry;
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

		logger.debug("Starting up ViewManager ");
		// Get the View Manager responsible for showing Views like (AntennaView
		// and ConsoleView)
		viewManager = new ViewManager(configurer.getWindow());

		getWindowConfigurer().getActionBarConfigurer().getStatusLineManager()
				.setMessage("ReaderManagerService is available");
		super.postWindowOpen();
	}
}
