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
package org.rifidi.initializer;

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
import org.rifidi.initializer.exceptions.InitializationException;

/**
 * 
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
	private Map<Class, IInitializer> initMap = new HashMap<Class, IInitializer>();
	/**
	 * Extension point for initializers.
	 */

	private IExtensionPoint point;

	/**
	 * Constructor.
	 */
	public InitService() {
		logger.debug("InitService created");
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this);
		point = registry.getExtensionPoint("org.rifidi.initializer");
		if (point == null) {
			logger.fatal("Extension point org.rifidi.initializer missing!!");
		}
		for (IExtension extension : point.getExtensions()) {
			getInitializers(extension);
		}
	}

	public void getInitializers(IExtension extension) {
		for (IConfigurationElement configElement : extension
				.getConfigurationElements()) {
			try {
				Class clazz=Class.forName(configElement.getAttribute("class"));
				IInitializer initializer=(IInitializer)clazz.newInstance();
				for (IConfigurationElement child : configElement.getChildren()) {
					if(initMap.containsKey(Class.forName(child.getAttribute("class")))){
						throw new RuntimeException("Tried to register two different initializers for "+child.getAttribute("class"));
					}
					initMap.put(Class.forName(child.getAttribute("class")), initializer);
					System.out.println("added: "+Class.forName(child.getAttribute("class"))+" "+initializer);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.initializer.IInitService#init(java.lang.Object)
	 */
	@Override
	public void init(Object initializee) throws InitializationException {
		if(initMap.containsKey(initializee.getClass())){
			initMap.get(initializee.getClass()).init(initializee);
		}
		for(Class clazz:initializee.getClass().getClasses()){
			if (initMap.containsKey(clazz)){
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
					getInitializers(delta.getExtension());
				}
				continue;
			}
		}
	}

}
