/**
 * 
 */
package org.rifidi.prototyper.items.view;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.rifidi.prototyper.items.Activator;
import org.rifidi.prototyper.items.model.ItemType;
import org.rifidi.prototyper.items.model.TaggedItem;

/**
 * The label Provider for the Item View
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemViewLabelProvider implements ILabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof TaggedItem) {
			TaggedItem item = (TaggedItem) element;
			switch (item.getType()) {
			case CARGO:
				return Activator.getDefault().getImageRegistry().get(
						ItemType.CARGO.name());
			case FORKLIFT:
				return Activator.getDefault().getImageRegistry().get(
						ItemType.FORKLIFT.name());
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof TaggedItem) {
			TaggedItem ti = (TaggedItem)element;
			return ti.getName() + " (ID:"+ti.getTag()+")";
		} else {
			return element.toString();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

}
