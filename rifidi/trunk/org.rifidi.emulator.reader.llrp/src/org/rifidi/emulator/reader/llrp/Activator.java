package org.rifidi.emulator.reader.llrp;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.reader.llrp.module.LLRPReaderModule;
import org.rifidi.emulator.reader.module.ReaderModule;

public class Activator implements BundleActivator {
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Registered org.rifidi.emulator.reader.llrp");
		context.registerService(new String[]{ReaderModule.class.getName()}, new LLRPReaderModule(), new Hashtable());
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Shutting down org.rifidi.emulator.reader.llrp");
	}

}
