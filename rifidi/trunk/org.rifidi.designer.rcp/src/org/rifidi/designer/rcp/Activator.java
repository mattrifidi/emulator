package org.rifidi.designer.rcp;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.monklypse.core.SWTDefaultImplementor;
import org.osgi.framework.BundleContext;
import org.rifidi.designer.entities.RMIManager;
import org.rifidi.designer.entities.internal.RifidiTagWithParentWorkbenchAdapter;
import org.rifidi.designer.rcp.game.DesignerGame;
import org.rifidi.designer.services.core.collision.FieldService;
import org.rifidi.designer.services.core.collision.FieldServiceImpl;
import org.rifidi.designer.services.core.entities.EntitiesService;
import org.rifidi.designer.services.core.entities.EntitiesServiceImpl;
import org.rifidi.designer.services.core.entities.FinderService;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.designer.services.core.events.EventsService;
import org.rifidi.designer.services.core.events.EventsServiceImpl;
import org.rifidi.designer.services.core.highlighting.HighlightingService;
import org.rifidi.designer.services.core.selection.SelectionService;
import org.rifidi.designer.services.core.selection.SelectionServiceImpl;
import org.rifidi.designer.services.core.world.CommandStateService;
import org.rifidi.designer.services.core.world.WorldService;

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
	 * Reference to the current display.
	 */
	public static Display display;

	/**
	 * The constructor.
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
		context.registerService(FieldService.class.getName(),
				new FieldServiceImpl(), null);
		context.registerService(
				new String[] { EntitiesService.class.getName(),
						FinderService.class.getName(),
						SceneDataService.class.getName() },
				new EntitiesServiceImpl(), null);
		context.registerService(EventsService.class.getName(),
				new EventsServiceImpl(), null);
		context.registerService(SelectionService.class.getName(),
				new SelectionServiceImpl(), null);

		context.registerService(new String[] { WorldService.class.getName(),
				CommandStateService.class.getName(),
				HighlightingService.class.getName(),
				DesignerGame.class.getName(),
				SWTDefaultImplementor.class.getName() }, new DesignerGame(
				"designer", 754, 584), null);
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
		String iconPath = "/icons/";
		reg.put(RifidiTagWithParentWorkbenchAdapter.class.getName(),
				imageDescriptorFromPlugin(PLUGIN_ID, iconPath + "tag.png"));
	}
}
