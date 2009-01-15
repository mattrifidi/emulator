/*
 *  BooleanCellEditor.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * Cell editor for boolean values.
 * 
 * @author Jochen Mader - jochen@pramari.com - Jan 15, 2009
 * 
 */
public class BooleanCellEditor extends CheckboxCellEditor {
	/** Logger for this class */
	private static Log logger = LogFactory.getLog(CheckboxCellEditor.class);

	/**
	 * 
	 */
	public BooleanCellEditor() {
		super();
	}

	/**
	 * @param parent
	 * @param style
	 */
	public BooleanCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * @param parent
	 */
	public BooleanCellEditor(Composite parent) {
		super(parent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CheckboxCellEditor#doSetValue(java.lang.Object)
	 */
	@Override
	protected void doSetValue(Object value) {
		try {
			super.doSetValue((Boolean) value);
		} catch (NumberFormatException nfe) {
			logger.debug("Got " + value + " " + nfe);
			// Just to make sure, TextCellEditor uses assert to refuse wrong
			// types
			assert (false);
		}
	}

}
