/*
 *  Activator.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rifidi.emulator.rmi.server.RifidiManager;

/**
 * FIXME: Class comment.  
 * 
 * @author Jochen Mader - jochen@pramari.com - May 14, 2008
 * 
 */
public class Activator extends AbstractUIPlugin {
	/**
	 * Reference to the rifidi manager.
	 */
	public RMIManager rifidiManager;

	/**
	 * The shared instance.
	 */
	private static Activator plugin;

	/**
	 * ID of this plugin.
	 */
	public static String PLUGIN_ID = "org.rifidi.designer.entities";

	/**
	 * Constructor.
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		// start rifidi
		try {
			RifidiManager.startManager("127.0.0.1", 1198);
			rifidiManager = RMIManager.getInstance("127.0.0.1");
		} catch (java.rmi.server.ExportException e) {
			MessageBox mb = new MessageBox(new Shell(), SWT.OK);
			mb.setText("Unable to start new instance!");
			mb
					.setMessage("Please close all running instances of Rifidi designer"
							+ "before staring a new one.");
			mb.open();
			return;
		}
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(final String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

}
