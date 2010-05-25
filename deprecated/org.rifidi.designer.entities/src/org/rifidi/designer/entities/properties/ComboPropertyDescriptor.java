/*
 *  ComboPropertyDescriptor.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.properties;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;
/**
 * 
 * 
 * Property descriptor for the groups combo box.
 * 
 * @author Jochen Mader Feb 1, 2008
 * @tags
 *
 */
public class ComboPropertyDescriptor extends PropertyDescriptor {
    /**
     * Creates an property descriptor with the given id and display name.
     * 
     * @param id the id of the property
     * @param displayName the name to display for the property
     */
    public ComboPropertyDescriptor(Object id, String displayName) {
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
    	String[] items=new String[]{"GROUP 1","GROUP 2","GROUP 3","NONE"};
        CellEditor editor = new ComboBoxCellEditor(parent,items);
        if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
        return editor;
    }
}
