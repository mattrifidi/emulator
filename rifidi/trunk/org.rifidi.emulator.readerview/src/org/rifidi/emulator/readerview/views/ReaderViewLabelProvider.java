/**
 * 
 */
package org.rifidi.emulator.readerview.views;

import java.util.Map;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.rifidi.emulator.readerview.Activator;
import org.rifidi.ui.common.reader.UIReader;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ReaderViewLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof UIReader) {
			return Activator.getDefault().getImageRegistry().get(
					Activator.IMG_READER);
		}
		return super.getImage(element);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
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
		return super.getText(element);
	}

}
