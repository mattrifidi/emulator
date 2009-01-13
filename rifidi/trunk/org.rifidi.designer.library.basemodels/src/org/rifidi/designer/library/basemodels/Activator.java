package org.rifidi.designer.library.basemodels;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.rifidi.designer.library.basemodels.boxproducer.BoxproducerEntity;
import org.rifidi.designer.library.basemodels.boxproducercont.BoxproducerContinuousEntity;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.library.basemodels.conveyor.ConveyorEntity;
import org.rifidi.designer.library.basemodels.destroyer.DestroyerEntity;
import org.rifidi.designer.library.basemodels.gate.GateEntity;
import org.rifidi.designer.library.basemodels.infrared.InfraredEntity;
import org.rifidi.designer.library.basemodels.pusharm.PusharmEntity;

import com.jme.util.resource.MultiFormatResourceLocator;
import com.jme.util.resource.ResourceLocatorTool;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.rifidi.designer.library.basemodels";

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		// tell the importer what directory to use for textures
		URI dirpath = null;
		try {
			dirpath = getClass().getClassLoader().getResource(
					"org/rifidi/designer/library/basemodels/textures/").toURI();
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
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
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
		reg.put(ConveyorEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "conveyor.png"));
		reg.put(BoxproducerEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "boxproducer.png"));
		reg.put(BoxproducerContinuousEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "boxproducer.png"));
		reg.put(PusharmEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "pusher.png"));
		reg.put(GateEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "gateway.png"));
		reg.put(CardboxEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "cardboard.png"));
		reg.put(InfraredEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "infrared.png"));
		reg.put(BasemodelsLibrary.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "rifidi.png"));
		reg.put(DestroyerEntity.class.getName(), imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "destroyer.png"));
		reg.put("map_1", imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "map_1.png"));
		reg.put("map_2", imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "map_2.png"));
		reg.put("blank", imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "blank.png"));
		reg.put("4walls", imageDescriptorFromPlugin(
				PLUGIN_ID, iconPath + "4walls.png"));
	}
}
