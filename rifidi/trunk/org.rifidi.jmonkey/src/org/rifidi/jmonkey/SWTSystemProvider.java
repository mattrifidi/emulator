package org.rifidi.jmonkey;


import com.jme.system.DisplaySystem;
import com.jme.system.lwjgl.LWJGLSystemProvider;

public class SWTSystemProvider extends LWJGLSystemProvider {

	private final static String PROVIDER_IDENTIFIER = "SWTDISPLAYSYS";
	
	private final static DisplaySystem displaySystem = new SWTDisplaySystem();
	
	public SWTSystemProvider(){
		super();
	}
	
	public String getProviderIdentifier() {
		return PROVIDER_IDENTIFIER;
	}

	public DisplaySystem getDisplaySystem() {
		
		return displaySystem;
	}

}