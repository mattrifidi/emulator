/**
 * 
 */
package org.rifidi.ui.common.testers;

import org.eclipse.core.expressions.PropertyTester;
import org.rifidi.ui.common.reader.UIReader;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ReaderStatePropertyTester extends PropertyTester {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.expressions.IPropertyTester#test(java.lang.Object,
	 * java.lang.String, java.lang.Object[], java.lang.Object)
	 */
	@Override
	public boolean test(Object receiver, String property, Object[] args,
			Object expectedValue) {
		if (receiver instanceof UIReader) {
			String expectedString = (String) expectedValue;
			return ((UIReader) receiver).getReaderState().equalsIgnoreCase(
					expectedString);
		}
		return false;
	}

}
