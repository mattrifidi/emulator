/**
 * 
 */
package org.rifidi.emulator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.log.ReaderLogService;
import org.rifidi.emulator.log.ReaderLogServiceImpl;

/**
 * @author Kyle
 * 
 */
public class Activator implements BundleActivator {

	public static BundleContext context;
	public static Activator instance;
	private ReaderLogService readerLogService = new ReaderLogServiceImpl();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext arg0) throws Exception {
		Activator.context = arg0;
		instance = this;
		arg0.registerService(ReaderLogService.class.getCanonicalName(),
				readerLogService, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext arg0) throws Exception {
		instance = null;
	}

	public ReaderLogService getReaderLogService() {
		return readerLogService;
	}

	public static Activator getInstance() {
		return instance;
	}

}
