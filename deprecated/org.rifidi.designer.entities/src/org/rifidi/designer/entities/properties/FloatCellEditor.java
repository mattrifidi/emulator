/*
 *  FloatCellEditor.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Cell editor for float values.
 * 
 * @author Jochen Mader
 * 
 */
public class FloatCellEditor extends TextCellEditor {
	/** Logger for this class */
	private static Log logger = LogFactory.getLog(FloatCellEditor.class);

	/**
	 * Constructor.
	 */
	public FloatCellEditor() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            parent composite
	 * @param style
	 *            swt style
	 */
	public FloatCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Constructor.
	 * 
	 * @param parent
	 *            parent composite
	 */
	public FloatCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.TextCellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		try {
			super.doSetValue(((Float) value).toString());
		} catch (NumberFormatException nfe) {
			logger.debug("Got " + value + " " + nfe);
			// Just to make sure, TextCellEditor uses assert to refuse wrong
			// types
			assert (false);
		}
	}

}
