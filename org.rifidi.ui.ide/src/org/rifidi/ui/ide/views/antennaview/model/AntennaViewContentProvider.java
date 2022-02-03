/**
 * 
 */
package org.rifidi.ui.ide.views.antennaview.model;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.ui.common.reader.UIAntenna;

/**
 * This is the Contentprovider for the AntennaView describing the structure of
 * the objects to display in the AntennaView.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class AntennaViewContentProvider implements IStructuredContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		// If input is a UIAntenna get the list of tags out of it
		if (inputElement instanceof UIAntenna) {
			UIAntenna antenna = (UIAntenna )inputElement;
			List<RifidiTag> tags = antenna.getTagList();
			if(tags!=null){
				return tags.toArray();
			}
		}
		return null;
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
	}

}
