/*
 *  FloorElement.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.resource.ImageDescriptor;

import com.jme.scene.Node;
import com.jme.util.export.binary.BinaryImporter;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Jul 1, 2008
 * 
 */
public class FloorElement {
	/**
	 * Logger for this class.
	 */
	private Log logger = LogFactory.getLog(FloorElement.class);
	/**
	 * The Library this reference is pointing to.
	 */
	@SuppressWarnings("unchecked")
	private Class library;
	/**
	 * Human readable name of the floorplan.
	 */
	private String name;
	/**
	 * Global unique id of the floorplan.
	 */
	private String id;
	/**
	 * The image that should be displayed next to the reference in the
	 * libraryview
	 */
	private ImageDescriptor imageDescriptor;

	private String path;

	private Node model = null;

	/**
	 * @return the library
	 */
	public Class getLibrary() {
		return this.library;
	}

	/**
	 * @param library
	 *            the library to set
	 */
	public void setLibrary(Class library) {
		this.library = library;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the imageDescriptor
	 */
	public ImageDescriptor getImageDescriptor() {
		return this.imageDescriptor;
	}

	/**
	 * @param imageDescriptor
	 *            the imageDescriptor to set
	 */
	public void setImageDescriptor(ImageDescriptor imageDescriptor) {
		this.imageDescriptor = imageDescriptor;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return this.path;
	}

	/**
	 * @param path
	 *            the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Get the floorplan model.
	 * 
	 * @return
	 */
	public Node getNode() {
		URI modelpath = null;
		try {
			modelpath = getClass().getClassLoader().getResource(path).toURI();
		} catch (URISyntaxException e) {
			logger.debug(e);
		}
		try {
			model = (Node) BinaryImporter.getInstance().load(modelpath.toURL());
		} catch (MalformedURLException e) {
			logger.debug(e);
		} catch (IOException e) {
			logger.debug(e);
		}
		return model;
	}
}
