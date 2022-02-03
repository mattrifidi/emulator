/*
 *  AntennaViewContentProvider.java
 *
 *  Created:	Feb 28, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ide.views.antennaview.model;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.ide.reader.UIAntenna;
import org.rifidi.ide.registry.TagRegistry;

/**
 * A simple content provider which is able to take a list of objects and provide
 * it to consumers.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class AntennaViewContentProvider implements IStructuredContentProvider{

	@SuppressWarnings("unused")
	private static Log logger=LogFactory.getLog(AntennaViewContentProvider.class);
	
	//the viewer this content provider is associated with
	private TableViewer viewer;
	
	//the antenna this contenprovider is associated with
	private UIAntenna antenna;
	
	/**
	 * Constructor
	 * @param antenna the antenna this conten provider should provide conten for
	 */
	public AntennaViewContentProvider(UIAntenna antenna){
		//logger.debug("Antenna conten provider created");
		this.antenna=antenna;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		return TagRegistry.getInstance().getIdeTagsByUIAntenna(antenna).toArray();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer=(TableViewer)viewer;
		viewer.refresh();
	}

	/**
	 * @return the viewer
	 */
	public TableViewer getViewer() {
		return viewer;
	}

	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

}
