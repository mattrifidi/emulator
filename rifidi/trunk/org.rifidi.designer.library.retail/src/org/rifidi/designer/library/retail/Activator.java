package org.rifidi.designer.library.retail;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rifidi.designer.library.retail.clothing.Clothing;
import org.rifidi.designer.library.retail.clothingrack.ClothingRack;
import org.rifidi.designer.library.retail.retailbox.RetailBox;
import org.rifidi.designer.library.retail.shelf.Shelf;

import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.rifidi.designer.library.retail";

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
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		URI dirpath = null;
		try {
			dirpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/retail/textures/").toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		MultiFormatResourceLocator loc2 = new MultiFormatResourceLocator(
				dirpath, ".jpg", ".png", ".tga");
		ResourceLocatorTool.addResourceLocator(
				ResourceLocatorTool.TYPE_TEXTURE, loc2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
	 */
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		super.initializeImageRegistry(reg);
		String iconPath = "/icons/";
		reg.put(ClothingRack.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "rack.png"));
		reg.put(Clothing.class.getName(), imageDescriptorFromPlugin(PLUGIN_ID,
				iconPath + "clothing.png"));
		reg.put(Shelf.class.getName(), imageDescriptorFromPlugin(PLUGIN_ID,
				iconPath + "shelf.png"));
		reg.put(RetailBox.class.getName(), imageDescriptorFromPlugin(PLUGIN_ID,
				iconPath + "box.png"));
		reg.put(RetailLibrary.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "rifidi.png"));
	}
}
