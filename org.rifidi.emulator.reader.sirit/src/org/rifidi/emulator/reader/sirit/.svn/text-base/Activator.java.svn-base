package org.rifidi.emulator.reader.sirit;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.reader.sirit.module.SiritReaderModuleFactory;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

public class Activator implements BundleActivator {

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		System.out.println("Registered org.rifidi.emulator.reader.sirit local");
		context.registerService(new String[] { ReaderModuleFactory.class
				.getName() }, new SiritReaderModuleFactory(), new Hashtable());

	}

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Shutting down org.rifidi.emulator.reader.sirit");
	}

}
