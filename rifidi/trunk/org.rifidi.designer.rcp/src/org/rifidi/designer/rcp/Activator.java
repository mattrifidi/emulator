package org.rifidi.designer.rcp;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rifidi.designer.entities.RMIManager;
import org.rifidi.emulator.rmi.server.RifidiManager;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Activator extends AbstractUIPlugin {

	/**
	 * The plug-in ID.
	 */
	public static final String PLUGIN_ID = "org.rifidi.designer.rcp";

	/**
	 * The shared instance.
	 */
	private static Activator plugin;

	/**
	 * The folder used for all scenes.
	 */
	public IFolder folder;

	/**
	 * The rmimanager that handles connections to rifidi.
	 */
	public RMIManager rifidiManager;

	/**
	 * Registry for all actions.
	 */
	private ActionRegistry actionRegistry;

	/**
	 * The constructor.
	 */
	public Activator() {
		actionRegistry = new ActionRegistry();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		// check workspace for required folders
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project = root.getProject("MyProject");
		if (!project.exists()) {
			project.create(null);
		}
		project.open(null);
		folder = project.getFolder("Folder1");
		if (!folder.exists()) {
			folder.create(true, true, null);
		}
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
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
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

	/**
	 * @return the actionRegistry.
	 */
	public ActionRegistry getActionRegistry() {
		return actionRegistry;
	}
}
