/*
 *  SWTSystemProvider.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmonkey;

import com.jme.system.DisplaySystem;
import com.jme.system.lwjgl.LWJGLSystemProvider;

/**
 * System provider to register {@link SWTDisplaySystem} to jmonkey.
 * 
 * @author Jochen Mader - jochen@pramari.com - August 22, 2008
 * 
 */
public class SWTSystemProvider extends LWJGLSystemProvider {

	private final static String PROVIDER_IDENTIFIER = "SWTDISPLAYSYS";

	private final static DisplaySystem displaySystem = new SWTDisplaySystem();

	public SWTSystemProvider() {
		super();
	}

	public String getProviderIdentifier() {
		return PROVIDER_IDENTIFIER;
	}

	public DisplaySystem getDisplaySystem() {

		return displaySystem;
	}

}