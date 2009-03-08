/**
 * 
 */
package org.rifidi.ui.ide.views.readerview.model;

import org.eclipse.ui.IActionFilter;
import org.rifidi.ui.common.reader.UIReader;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class UIReaderActionFilter implements IActionFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionFilter#testAttribute(java.lang.Object,
	 *      java.lang.String, java.lang.String)
	 */
	public boolean testAttribute(Object target, String name, String value) {
		if (target instanceof UIReader && "readerState".equals(name)) {
			return value.equals(((UIReader) target).getReaderState());
		}
		return false;
	}

}
