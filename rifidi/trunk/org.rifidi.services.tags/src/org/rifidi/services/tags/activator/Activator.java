package org.rifidi.services.tags.activator;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.services.tags.registry.impl.TagRegistryImpl;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.rifidi.emulator.tags";

	// The shared instance
	private static Activator plugin;
	/**
	 * The service registration for removing the service on stop.
	 */
	private ServiceRegistration serviceRegistration;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		serviceRegistration=context.registerService(ITagRegistry.class.getName(), new TagRegistryImpl(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		context.ungetService(serviceRegistration.getReference());
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
