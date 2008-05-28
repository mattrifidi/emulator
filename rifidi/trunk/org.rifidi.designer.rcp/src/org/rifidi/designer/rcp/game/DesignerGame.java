/*
 *  DesignerGame.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.game;

import org.eclipse.swt.widgets.Composite;
import org.rifidi.jmeswt.SWTBaseGame;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 28, 2008
 * 
 */
public class DesignerGame extends SWTBaseGame {

	/**
	 * @param name
	 * @param updateResolution
	 * @param renderResolution
	 * @param width
	 * @param height
	 * @param parent
	 */
	public DesignerGame(String name, int updateResolution,
			int renderResolution, int width, int height, Composite parent) {
		super(name, updateResolution, renderResolution, width, height, parent);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.rifidi.jmeswt.SWTBaseGame#simpleInitGame()
	 */
	@Override
	protected void simpleInitGame() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.rifidi.jmeswt.SWTBaseGame#render(float)
	 */
	@Override
	protected void render(float interpolation) {
		// TODO Auto-generated method stub
		super.render(interpolation);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.jmeswt.SWTBaseGame#update(float)
	 */
	@Override
	protected void update(float interpolation) {
		// TODO Auto-generated method stub
		super.update(interpolation);
	}

}
