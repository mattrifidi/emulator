/**
 * 
 */
package org.rifidi.ui.ide.views.readerview.model;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.ide.properties.model.PropertySourceFile;

/**
 * This is The AdapterFactory providing information about the available Adapters
 * for the UIReader Class
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class UIReaderAdapterFactory implements IAdapterFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		// Action Filter
		if (adapterType == IActionFilter.class
				&& adaptableObject instanceof UIReader) {
			return new UIReaderActionFilter();
		}
		// Property View
		if (adapterType == IPropertySource.class
				&& adaptableObject instanceof UIReader) {
			return new PropertySourceFile((UIReader) adaptableObject);
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@SuppressWarnings("unchecked")
	public Class[] getAdapterList() {
		return new Class[] { IActionFilter.class, IPropertySource.class };
	}

}
