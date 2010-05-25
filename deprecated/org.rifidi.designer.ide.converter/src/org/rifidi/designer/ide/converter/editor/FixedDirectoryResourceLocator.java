/*
 *  FixedDirectoryResourceLocator.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.editor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.Path;

import com.jme.util.resource.ResourceLocator;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Aug 5, 2008
 * 
 */
public class FixedDirectoryResourceLocator implements ResourceLocator {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger
			.getLogger(FixedDirectoryResourceLocator.class.getName());
	/**
	 * Root for the search.
	 */
	private IContainer root;

	/**
	 * @param root
	 */
	public FixedDirectoryResourceLocator(IContainer root) {
		this.root = root;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.util.resource.ResourceLocator#locateResource(java.lang.String)
	 */
	@Override
	public URL locateResource(String resourceName) {
		resourceName = resourceName
				.substring(resourceName.lastIndexOf("/") + 1);
		IFile file = root.getFile(new Path(resourceName));
		try {
			return file.getLocationURI().toURL();
		} catch (MalformedURLException e) {
			logger.warning(e.toString());
		}
		return null;
	}

}
