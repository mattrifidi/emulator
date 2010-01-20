package org.rifidi.prototyper.items;

import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rifidi.prototyper.items.service.ItemService;
import org.rifidi.prototyper.items.service.ItemTypeRegistry;
import org.rifidi.prototyper.items.view.ItemModelProviderSingleton;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.rifidi.prototyper.items";
	public static final String IMAGE_FOLDER = "folder";
	

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
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
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ItemTypeRegistry reg = new ItemTypeRegistry();
		context.registerService(ItemService.class.getName(),
				ItemModelProviderSingleton.getModelProvider(), new Hashtable());
		context.registerService(ItemTypeRegistry.class.getName(), reg,
				new Hashtable());

		Enumeration e = plugin.getBundle().findEntries("/", "*ItemType.xml",
				true);
		final Set<String> initialItemTypes = new HashSet<String>();
		while (e.hasMoreElements()) {
			URL url = (URL) e.nextElement();
			initialItemTypes.add(url.getFile());
		}
		final ItemTypeRegistry thisReg = reg;
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				thisReg.loadItemTypes(initialItemTypes);
			}
		});
		

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse
	 * .jface.resource.ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		reg.put(IMAGE_FOLDER, getImageDescriptor("icons/folder.png"));

	}

}
