/**
 * 
 */
package org.rifidi.ui.ide.views.readerview.model;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * This is the ContentProvider for the ReaderView it's telling the view about
 * the ReaderRegistry structure
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderViewContentProvider implements ITreeContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof UIReader) {
			UIReader r = ((UIReader) parentElement);
			return new Object[] { "# Antennas: " + r.getNumAntennas(),
					"Type: " + r.getReaderType(), r.getPropertiesMap() };
		}
		if (parentElement instanceof Map) {
			return ((Map) parentElement).entrySet().toArray();
		}
		if (parentElement instanceof Map.Entry) {
			return new Object[] { ((Map.Entry) parentElement).getValue() };
		}
		return null;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent(Object element) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren(Object element) {
		if (element instanceof ReaderRegistry)
			return true;
		if (element instanceof UIReader)
			return true;
		if (element instanceof Map)
			return true;
		if (element instanceof Map.Entry)
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ReaderRegistry) {
			List<UIReader> list = ((ReaderRegistryService) inputElement)
					.getReaderList();
			return (Object[]) list.toArray();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		viewer.refresh();
	}

}
