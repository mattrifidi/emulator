/*
 *  Activator.java
 *
 *  Created:	May 5, 2008
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.thingmagic;

import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.thingmagic.module.ThingMagicReaderModule;



/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Jerry Maine - jerry@pramari.com
 *
 */
public class Activator implements BundleActivator  {


	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		System.out.println("Registered org.rifidi.emulator.reader.thingmagic");
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
