/*
 *  GlobalProperties.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp;

/**
 * A class holding some global properties. Will be replaced by something else
 * soon.
 * 
 * TODO: get rid of this
 * 
 * @author Jochen Mader Nov 21, 2007
 * 
 */
public class GlobalProperties {
	/**
	 * enable/disable physicsdebugging.
	 */
	public static boolean physicsDebugging = false;
	/**
	 * enable/disable boundingdebugging.
	 */
	public static boolean boundingDebugging = false;
	/**
	 * enable/disable minimap.
	 */
	public static boolean minimap = true;
	/**
	 * Basevalue for frustum calculation
	 */
	public static float basefrustumvalue = 80;
	/**
	 * Enable/disable developer mode (shows different set of actions.)
	 */
	public static boolean developerMode = true;
	/**
	 * TODO: Stupid thing, label drawing on windows!=linux
	 */
	public static boolean windows=false;
}
