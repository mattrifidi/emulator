package org.rifidi.ui.ide.views.readerview.model;

import java.util.Map;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.ide.Activator;

/**
 * This is the LabelProvider for the ReaderView
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderViewLabelProvider implements ILabelProvider {

	public Image getImage(Object element) {
		if (element instanceof UIReader)
			return Activator.getImageDescriptor("icons/reader-24x24.png")
					.createImage();
		return null;
	}

	@SuppressWarnings("unchecked")
	public String getText(Object element) {
		if (element instanceof UIReader) {
			String ret = ((UIReader) element).getReaderName() + " ("
					+ ((UIReader) element).getReaderState() + ")";
			return ret;
		}
		 if (element instanceof Map)
			return "Properties:";
		if (element instanceof Map.Entry)
			return (String) ((Map.Entry) element).getKey();
		if (element instanceof String)
			return (String) element;
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
