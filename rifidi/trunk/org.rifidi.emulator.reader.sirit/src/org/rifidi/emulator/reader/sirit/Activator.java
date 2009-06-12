package org.rifidi.emulator.reader.sirit;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.reader.sirit.module.SiritReaderModule;
import org.rifidi.emulator.reader.module.ReaderModule;

public class Activator implements BundleActivator {

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	@SuppressWarnings("unchecked")
	public void start(BundleContext context) throws Exception {
		System.out.println("Registered org.rifidi.emulator.reader.sirit");
		context.registerService(new String[] { ReaderModule.class.getName() },
				new SiritReaderModule(), new Hashtable());

	}

	/*
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Shutting down org.rifidi.emulator.reader.sirit");
	}

}
