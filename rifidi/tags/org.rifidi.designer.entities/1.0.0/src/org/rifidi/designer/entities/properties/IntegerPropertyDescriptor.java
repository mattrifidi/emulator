package org.rifidi.designer.entities.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * Descriptor for integer properties.
 * 
 * 
 * @author Jochen Mader Feb 1, 2008
 * @tags
 * 
 */
public class IntegerPropertyDescriptor extends PropertyDescriptor {
	/**
	 * Creates an property descriptor with the given id and display name.
	 * 
	 * @param id
	 *            the id of the property
	 * @param displayName
	 *            the name to display for the property
	 */
	public IntegerPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	/**
	 * The <code>TextPropertyDescriptor</code> implementation of this
	 * <code>IPropertyDescriptor</code> method creates and returns a new
	 * <code>TextCellEditor</code>.
	 * <p>
	 * The editor is configured with the current validator if there is one.
	 * </p>
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new IntegerCellEditor(parent);
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		return editor;
	}
}
