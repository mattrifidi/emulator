/*
 *  EntityLibraryRegistry.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

/**
 * Singleton to manage the libraries
 * 
 * @author Jochen Mader Oct 4, 2007
 * 
 * TODO: right now we don't listen for changes like the addition of a new
 * library
 */
public class EntityLibraryRegistry {

	private static Log logger = LogFactory.getLog(EntityLibraryRegistry.class);
	/**
	 * Singleton pattern.
	 */
	private static EntityLibraryRegistry instance;
	/**
	 * All currently known libraries TODO: turn this class into a listener for
	 * BundleEvents.
	 */
	private ArrayList<EntityLibrary> libraries;
	/**
	 * All known entity references.
	 */
	private Map<String, EntityLibraryReference> references;

	/**
	 * private constructor Singleton pattern.
	 * 
	 */
	private EntityLibraryRegistry() {
	}

	/**
	 * Singleton pattern
	 * 
	 * @return the instance
	 */
	public static EntityLibraryRegistry getInstance() {
		if (instance == null) {
			instance = new EntityLibraryRegistry();
			instance.getLibraries();
		}
		return instance;
	}

	/**
	 * Get all currently available libraries.
	 * 
	 * @return list of libraries
	 */
	@SuppressWarnings("unchecked")
	public List<EntityLibrary> getLibraries() {
		if (libraries == null) {
			libraries = new ArrayList<EntityLibrary>();
			references = new HashMap<String, EntityLibraryReference>();
			IExtensionRegistry registry = Platform.getExtensionRegistry();
			IExtensionPoint point = registry
					.getExtensionPoint("org.rifidi.designer.library");
			if (point == null) {
				logger
						.fatal("Extension point org.rifidi.designer.library missing!!");
			}
			for (IExtension extension : point.getExtensions()) {
				for (IConfigurationElement configElement : extension
						.getConfigurationElements()) {
					try {
						Class library = Class.forName(configElement
								.getAttribute("class"));
						EntityLibrary lib = (EntityLibrary) library
								.newInstance();
						libraries.add(lib);
						for (EntityLibraryReference ref : lib
								.getLibraryReferences()) {
							references.put(ref.getId(), ref);
						}
					} catch (InvalidRegistryObjectException e) {
						logger.error("Exception while loading "
								+ configElement.getAttribute("class") + "\n"
								+ e);
					} catch (ClassNotFoundException e) {
						logger.error("Exception while loading "
								+ configElement.getAttribute("class") + "\n"
								+ e);
					} catch (InstantiationException e) {
						logger.error("Exception while loading "
								+ configElement.getAttribute("class") + "\n"
								+ e);
					} catch (IllegalAccessException e) {
						logger.error("Exception while loading "
								+ configElement.getAttribute("class") + "\n"
								+ e);
					}
				}
			}
		}

		return libraries;
	}

	/**
	 * Get a list of all available entites classes.
	 * 
	 * @return list of entity classes
	 */
	@SuppressWarnings("unchecked")
	public List<Class> getEntityClasses() {
		ArrayList<Class> classes = new ArrayList<Class>();
		for (EntityLibraryReference ref : references.values()) {
			classes.add(ref.getEntityClass());
		}
		return classes;
	}

	/**
	 * Get the library reference for the specified unique name.
	 * 
	 * @param name
	 * @return
	 */
	public EntityLibraryReference getEntityLibraryReference(String name) {
		return references.get(name);
	}
}
