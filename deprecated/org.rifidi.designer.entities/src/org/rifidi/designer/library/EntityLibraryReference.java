/*
 *  EntityLibraryReference.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * This is a lightweight object that should represent an Entity in a Library.
 * 
 * @author Jochen Mader Oct 4, 2007
 * 
 */
public class EntityLibraryReference {
	/**
	 * The class of the referenced entity. This is required for the loading
	 * mechanism.
	 */
	@SuppressWarnings("unchecked")
	private Class entityClass;
	/**
	 * The Library this reference is pointing to.
	 */
	@SuppressWarnings("unchecked")
	private Class library;
	/**
	 * Human readable name of the entity.
	 */
	private String name;
	/**
	 * Global unique id of the entity.
	 */
	private String id;
	/**
	 * A jface wizard for creating the entity.
	 */
	@SuppressWarnings("unchecked")
	private Class wizard;
	/**
	 * The image that should be displayed next to the reference in the
	 * libraryview
	 */
	private ImageDescriptor imageDescriptor;
	/**
	 * Responsible for displaying the reference in the library view.
	 */
	private boolean hidden;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the library
	 */
	@SuppressWarnings("unchecked")
	public Class getLibrary() {
		return library;
	}

	/**
	 * @param library
	 *            the library to set
	 */
	@SuppressWarnings("unchecked")
	public void setLibrary(Class library) {
		this.library = library;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the wizard
	 */
	@SuppressWarnings("unchecked")
	public Class getWizard() {
		return wizard;
	}

	/**
	 * @param wizard
	 *            the wizard to set
	 */
	@SuppressWarnings("unchecked")
	public void setWizard(Class wizard) {
		this.wizard = wizard;
	}

	/**
	 * @return the imageDescriptor
	 */
	public ImageDescriptor getImageDescriptor() {
		return imageDescriptor;
	}

	/**
	 * @param imageDescriptor
	 *            the imageDescriptor to set
	 */
	public void setImageDescriptor(ImageDescriptor imageDescriptor) {
		this.imageDescriptor = imageDescriptor;
	}

	/**
	 * @return the entityClass
	 */
	@SuppressWarnings("unchecked")
	public Class getEntityClass() {
		return entityClass;
	}

	/**
	 * @param entityClass
	 *            the entityClass to set
	 */
	@SuppressWarnings("unchecked")
	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * @return the hidden
	 */
	public boolean isHidden() {
		return this.hidden;
	}

	/**
	 * @param hidden the hidden to set
	 */
	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

}
