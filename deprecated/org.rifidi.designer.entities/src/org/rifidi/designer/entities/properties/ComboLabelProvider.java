/*
 *  ComboLabelProvider.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.properties;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Label provider for the properties view that is used to display the groups
 * combobox.
 * 
 * @author Jochen Mader
 * 
 */
public class ComboLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if ((Integer) element == 3) {
			return "NONE";
		}
		return "GROUP " + ((Integer) element + 1);
	}

}
