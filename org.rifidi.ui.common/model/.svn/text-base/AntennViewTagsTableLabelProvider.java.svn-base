/*
 *  AntennViewTagsTableLabelProvider.java
 *
 *  Created:	Feb 27, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ide.views.antennaview.model;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.rifidi.ide.reader.UIAntenna;
import org.rifidi.ide.tag.IdeTag;

/**
 * 
 * Label provider that takes an IdeTag object and extracts all relevant
 * information from it and provides a consumer with the strings.
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class AntennViewTagsTableLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	// Names of images used to represent checkboxes
	public static final String CHECKED_IMAGE 	= "antennaview-tag-on";
	public static final String UNCHECKED_IMAGE  = "antennaview-tag-off";
	public static final String REMOVE_IMAGE  = "antennaview-tag-remove";

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();

	private UIAntenna antenna;

	static {
		String iconPath = "/icons/"; 
		imageRegistry.put(CHECKED_IMAGE, org.rifidi.ide.Activator.getImageDescriptor(
				iconPath + CHECKED_IMAGE + ".gif"
				)
			);
		imageRegistry.put(UNCHECKED_IMAGE, org.rifidi.ide.Activator.getImageDescriptor(
				iconPath + UNCHECKED_IMAGE + ".gif"
				)
			);
		imageRegistry.put(REMOVE_IMAGE, org.rifidi.ide.Activator.getImageDescriptor(
				iconPath + REMOVE_IMAGE + ".gif"
				)
			);	
	}
	
	//the antenna this labelprovider is associated with
	public AntennViewTagsTableLabelProvider(UIAntenna antenna){
		super();
		this.antenna=antenna;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if(columnIndex==0){
			String key = ((IdeTag)element).getAntennas().get(antenna)? CHECKED_IMAGE:UNCHECKED_IMAGE;
			return imageRegistry.get(key);
		}
		if(columnIndex==5){
			return imageRegistry.get(REMOVE_IMAGE);
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return ((IdeTag) element).getTagGen();
		case 2:
			return ((IdeTag) element).getTagFormat();
		case 3:
			return ((IdeTag) element).getTagData();
		case 4:
			return "100%";
		default:
			return "";
		}
	}

	/**
	 * @return the antenna
	 */
	public UIAntenna getAntenna() {
		return antenna;
	}

	/**
	 * @param antenna the antenna to set
	 */
	public void setAntenna(UIAntenna antenna) {
		this.antenna = antenna;
	}

}
