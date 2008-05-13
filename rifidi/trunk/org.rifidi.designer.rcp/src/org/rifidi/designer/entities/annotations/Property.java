/*
 *  Property.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for properties.
 * 
 * @author Jochen Mader
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Property {
	/**
	 * name of the property as it should show up in the UI.
	 * 
	 * @return
	 */
	String displayName();

	/**
	 * Description of the property which is displayed in the UI.
	 * 
	 * @return
	 */
	String description();

	/**
	 * false+readonly true=writeable.
	 * 
	 * @return
	 */
	boolean readonly();

	/**
	 * A textual representation of the unit for this property.
	 */
	String unit();
}
