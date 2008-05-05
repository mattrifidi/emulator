package org.rifidi.ui.ide.views.antennaview.model;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This is the Labelprovider describing the object structures and labeling them
 * 
 * @author Jochen Mader - jochen@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class AntennViewLabelProvider implements ITableLabelProvider {

	// For the checkbox images
	private static ImageRegistry imageRegistry = new ImageRegistry();

	/*
	 * tag status constants
	 */
	public static final String CHECKED_IMAGE = "antennaview-tag-on";
	public static final String UNCHECKED_IMAGE = "antennaview-tag-off";
	public static final String REMOVE_IMAGE = "antennaview-tag-remove";

	static {
		String iconPath = "/icons/";
		imageRegistry.put(CHECKED_IMAGE, org.rifidi.ui.ide.Activator
				.getImageDescriptor(iconPath + CHECKED_IMAGE + ".gif"));
		imageRegistry.put(UNCHECKED_IMAGE, org.rifidi.ui.ide.Activator
				.getImageDescriptor(iconPath + UNCHECKED_IMAGE + ".gif"));
		imageRegistry.put(REMOVE_IMAGE, org.rifidi.ui.ide.Activator
				.getImageDescriptor(iconPath + REMOVE_IMAGE + ".gif"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
		if (columnIndex == 0) {
			String key = ((RifidiTag)element).isVisbile?
					CHECKED_IMAGE:UNCHECKED_IMAGE;
			return imageRegistry.get(key);
		}
		if (columnIndex == 4) {
			return imageRegistry.get(REMOVE_IMAGE);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof RifidiTag) {
			RifidiTag tag = (RifidiTag) element;
			if (columnIndex == 1) {
				// Generation
				TagGen t = tag.getTagType();
				if (t == TagGen.GEN1)
					return "GEN1";
				if (t == TagGen.GEN2)
					return "GEN2";
				return "UNKNOWN";
			}
			if (columnIndex == 2) {
				// DataType
				return tag.getIdFormat();
			}
			if (columnIndex == 3) {
				// TagID
				return tag.toString();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void addListener(ILabelProviderListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse.jface.viewers.ILabelProviderListener)
	 */
	public void removeListener(ILabelProviderListener listener) {
	}

}
