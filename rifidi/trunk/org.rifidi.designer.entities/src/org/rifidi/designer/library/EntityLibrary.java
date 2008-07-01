/*
 *  EntityLibrary.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * This interface should be used to create new libraries.
 * 
 * @author Jochen Mader Oct 4, 2007
 * 
 */
public interface EntityLibrary {
	/**
	 * This method is used by the UI to get a list of available entities from
	 * the library.
	 * 
	 * @return list of entites
	 */
	List<EntityLibraryReference> getLibraryReferences();

	/**
	 * Get the list of floorplans this library provides.
	 */
	List<FloorElement> getFloorElements();
	/**
	 * Returns a human readable name for this library.
	 * 
	 * @return library name
	 */
	String getName();
	
	/**
	 * Get an image for the library.
	 * @return
	 */
	ImageDescriptor getImageDescriptor();
}
