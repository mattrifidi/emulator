/*
 *  GPIOPlugin.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.gpioeditor;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * Plugin for the gpio editor.
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Mar 13, 2008
 * 
 */
public class GPIOPlugin extends AbstractUIPlugin {

	/**
	 * Singleton pattern.
	 */
	private static GPIOPlugin instance;

	/**
	 * Singleton pattern.
	 */
	public static GPIOPlugin getDefault() {
		return instance;
	}

	/**
	 * Constructor.
	 */
	public GPIOPlugin() {
		if (instance == null) {
			instance = this;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

}