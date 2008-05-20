/*
 *  InitService.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.services.initializer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionDelta;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IRegistryChangeEvent;
import org.eclipse.core.runtime.IRegistryChangeListener;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.rifidi.services.initializer.exceptions.InitializationException;

/**
 * Threadsafe implementation of the
 * 
 * @see {@link IInitService}
 * 
 * @author Jochen Mader - jochen@pramari.com - May 14, 2008
 * 
 */
public class InitService implements IInitService, IRegistryChangeListener {

	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(InitService.class);
	/**
	 * Map for class to initializer mapping.
	 */
	private Map<Class, IInitializer> initMap;
	/**
	 * Extension point for initializers.
	 */

	private IExtensionPoint point;

	/**
	 * Constructor.
	 */
	public InitService() {
		logger.debug("InitService created");
		initMap = Collections
				.synchronizedMap(new HashMap<Class, IInitializer>());
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this);
		point = registry.getExtensionPoint("org.rifidi.services.initializer");
		if (point == null) {
			logger
					.fatal("Extension point org.rifidi.services.initializer missing!!");
		}
		for (IExtension extension : point.getExtensions()) {
			getInitializers(extension, true);
		}
	}

	/**
	 * Helper method for parsing extension points.
	 * 
	 * @param extension
	 *            the extension point to parse
	 * @param add
	 *            true if the found extensions should be added to the list,
	 *            false if they should be removed.
	 */
	private void getInitializers(IExtension extension, boolean add) {
		synchronized (initMap) {
			for (IConfigurationElement configElement : extension
					.getConfigurationElements()) {
				try {
					Class clazz = Class.forName(configElement
							.getAttribute("class"));
					IInitializer initializer = (IInitializer) clazz
							.newInstance();
					for (IConfigurationElement child : configElement
							.getChildren()) {
						if (initMap.containsKey(Class.forName(child
								.getAttribute("class")))) {
							logger.warn("Initializer "
									+ initMap.get(child.getAttribute("class"))
									+ " for " + child.getAttribute("class")
									+ " got replaced by "
									+ initializer.getClass().getName());
						}
						if (add) {
							initMap.put(Class.forName(child
									.getAttribute("class")), initializer);
							logger.debug("added: "
									+ Class
											.forName(child
													.getAttribute("class"))
									+ " " + initializer);
						} else {
							initMap.remove(Class.forName(child
									.getAttribute("class")));
							logger.debug("removed: "
									+ Class
											.forName(child
													.getAttribute("class"))
									+ " " + initializer);
						}

					}
				} catch (InvalidRegistryObjectException e) {
					logger.error("Exception while loading "
							+ configElement.getAttribute("class") + "\n" + e);
				} catch (ClassNotFoundException e) {
					logger.error("Exception while loading "
							+ configElement.getAttribute("class") + "\n" + e);
				} catch (InstantiationException e) {
					logger.error("Exception while loading "
							+ configElement.getAttribute("class") + "\n" + e);
				} catch (IllegalAccessException e) {
					logger.error("Exception while loading "
							+ configElement.getAttribute("class") + "\n" + e);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.services.initializer.IInitService#init(java.lang.Object)
	 */
	@Override
	public void init(Object initializee) throws InitializationException {
		if (initMap.containsKey(initializee.getClass())) {
			initMap.get(initializee.getClass()).init(initializee);
		}
		for (Class clazz : initializee.getClass().getClasses()) {
			if (initMap.containsKey(clazz)) {
				initMap.get(clazz).init(initializee);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IRegistryChangeListener#registryChanged(org.eclipse.core.runtime.IRegistryChangeEvent)
	 */
	@Override
	public void registryChanged(IRegistryChangeEvent event) {
		for (IExtensionDelta delta : event.getExtensionDeltas()) {
			if (delta.getKind() == delta.ADDED) {
				if (delta.getExtensionPoint().equals(point)) {
					getInitializers(delta.getExtension(), true);
				}
				continue;
			}
			if (delta.getKind() == delta.REMOVED) {
				if (delta.getExtensionPoint().equals(point)) {
					getInitializers(delta.getExtension(), false);
				}
				continue;
			}
		}
	}

}
