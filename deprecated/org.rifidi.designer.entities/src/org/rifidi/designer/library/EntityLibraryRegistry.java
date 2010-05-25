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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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

/**
 * Singleton to manage the libraries
 * 
 * @author Jochen Mader Oct 4, 2007
 * 
 * TODO: right now we don't listen for changes like the addition of a new
 * library
 */
public class EntityLibraryRegistry implements IRegistryChangeListener {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(EntityLibraryRegistry.class);
	/**
	 * Singleton pattern.
	 */
	private static EntityLibraryRegistry instance;
	/**
	 * All currently known libraries.
	 */
	private List<EntityLibrary> libraries;
	/**
	 * All known entity library references.
	 */
	private Map<String, EntityLibraryReference> entityReferences;
	/**
	 * All known floor elements by their names.
	 */
	private Map<String, FloorElement> floorReferences;
	/**
	 * List of available entites.
	 */
	private List<Class<?>> entityClasses;
	/**
	 * The extension point.
	 */
	private IExtensionPoint point;
	/**
	 * Extension point for internal entities.
	 */
	private IExtensionPoint internalPoint;

	/**
	 * private constructor Singleton pattern.
	 * 
	 */
	private EntityLibraryRegistry() {
		libraries = Collections
				.synchronizedList(new ArrayList<EntityLibrary>());
		entityReferences = Collections
				.synchronizedMap(new HashMap<String, EntityLibraryReference>());
		floorReferences = Collections
				.synchronizedMap(new HashMap<String, FloorElement>());
		entityClasses = Collections.synchronizedList(new ArrayList<Class<?>>());
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		registry.addRegistryChangeListener(this, "org.rifidi.designer.library");
		point = registry.getExtensionPoint("org.rifidi.designer.library");
		if (point == null) {
			logger
					.fatal("Extension point org.rifidi.designer.library missing!!");
			return;
		}
		for (IExtension extension : point.getExtensions()) {
			fillLibraries(extension, true);
		}
		internalPoint = registry
				.getExtensionPoint("org.rifidi.designer.entities.internal");
		for (IExtension extension : internalPoint.getExtensions()) {
			fillInternalClasses(extension, true);
		}
	}

	/**
	 * Singleton pattern
	 * 
	 * @return the instance
	 */
	public static EntityLibraryRegistry getInstance() {
		if (instance == null) {
			instance = new EntityLibraryRegistry();
		}
		return instance;
	}

	private void fillInternalClasses(IExtension extension, boolean add) {
		for (IConfigurationElement configElement : extension
				.getConfigurationElements()) {
			try {
				Class<?> internalentity = Class.forName(configElement
						.getAttribute("class"));
				if (add) {
					entityClasses.add(internalentity);
				} else {
					entityClasses.remove(internalentity);
				}
			} catch (InvalidRegistryObjectException e) {
				logger.error("Exception while loading "
						+ configElement.getAttribute("class") + "\n" + e);
			} catch (ClassNotFoundException e) {
				logger.error("Exception while loading "
						+ configElement.getAttribute("class") + "\n" + e);
			}
		}
	}

	private void fillLibraries(IExtension extension, boolean add) {
		synchronized (libraries) {
			for (IConfigurationElement configElement : extension
					.getConfigurationElements()) {
				try {
					Class<?> library = Class.forName(configElement
							.getAttribute("class"));
					if (add) {
						EntityLibrary lib = (EntityLibrary) library
								.newInstance();
						libraries.add(lib);
						for (EntityLibraryReference ref : lib
								.getLibraryReferences()) {
							entityReferences.put(ref.getId(), ref);
							entityClasses.add(ref.getEntityClass());
						}
						if(lib.getFloorElements()!=null){
							for (FloorElement element : lib
									.getFloorElements()) {
								floorReferences.put(element.getId(), element);
							}	
						}
					} else {
						EntityLibrary delete = null;
						for (EntityLibrary searchLib : libraries) {

							if (searchLib.getClass().equals(library)) {
								delete = searchLib;
								for (EntityLibraryReference ref : searchLib
										.getLibraryReferences()) {
									entityReferences.remove(ref.getId());
									entityClasses.remove(ref.getEntityClass());
								}
							}
						}
						if (delete != null) {
							libraries.remove(delete);
						} else {
							logger
									.warn("tried to remove a nonexistend library: "
											+ library);
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

	/**
	 * Get all currently available libraries.
	 * 
	 * @return list of libraries
	 */
	public List<EntityLibrary> getLibraries() {
		return libraries;
	}

	/**
	 * Get a list of all available entites classes.
	 * 
	 * @return list of entity classes
	 */
	public List<Class<?>> getEntityClasses() {
		return entityClasses;
	}

	/**
	 * Get the library reference for the specified unique name.
	 * 
	 * @param name
	 * @return
	 */
	public EntityLibraryReference getEntityLibraryReference(String name) {
		return entityReferences.get(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IRegistryChangeListener#registryChanged(org.eclipse.core.runtime.IRegistryChangeEvent)
	 */
	@Override
	public void registryChanged(IRegistryChangeEvent event) {
		for (IExtensionDelta delta : event.getExtensionDeltas()) {
			if (delta.getExtensionPoint().equals(point)) {
				if (delta.getKind() == IExtensionDelta.ADDED) {
					fillInternalClasses(delta.getExtension(), true);
					fillLibraries(delta.getExtension(), true);
				} else {
					fillInternalClasses(delta.getExtension(), true);
					fillLibraries(delta.getExtension(), false);
				}
			}
		}
	}

	/**
	 * @return the floorReferences
	 */
	public Map<String, FloorElement> getFloorReferences() {
		return this.floorReferences;
	}

}
