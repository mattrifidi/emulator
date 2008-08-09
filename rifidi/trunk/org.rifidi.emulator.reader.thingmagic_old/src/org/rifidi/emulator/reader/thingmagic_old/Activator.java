package org.rifidi.emulator.reader.thingmagic_old;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.thingmagic_old.module.ThingMagicReaderModule;



/**
 * The activator class controls the plug-in life cycle
 */
public class Activator implements BundleActivator  {


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Registered org.rifidi.emulator.reader.thingmagic_old");
		context.registerService(new String[]{ReaderModule.class.getName()}, new ThingMagicReaderModule(), new Hashtable());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("Shutting down org.rifidi.emulator.reader.thingmagic");
	}


}
