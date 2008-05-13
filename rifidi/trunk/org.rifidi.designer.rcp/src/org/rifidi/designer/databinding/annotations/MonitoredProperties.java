/*
 *  MonitoredProperties.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.databinding.annotations;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for telling databinding which properties to monitor for changes.
 * 
 * @author Jochen Mader Jan 31, 2008
 * @tags
 * 
 */
@Target( { TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface MonitoredProperties {
	/**
	 * The names of the properties to monitor.
	 * 
	 * @return
	 */
	String[] names();
}
