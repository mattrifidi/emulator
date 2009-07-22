package org.rifidi.emulator.reader.awid;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.reader.awid.module.AwidReaderModuleFactory;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

public class Activator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		System.out.println("Registered org.rifidi.emulator.reader.awid");
		context.registerService(new String[] { ReaderModuleFactory.class
				.getName() }, new AwidReaderModuleFactory(), new Hashtable());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Shutting down org.rifidi.emulator.reader.awid");
	}

}
