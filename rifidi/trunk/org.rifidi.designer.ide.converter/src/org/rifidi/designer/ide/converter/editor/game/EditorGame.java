/*
 *  EditorGame.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.editor.game;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.rifidi.jmeswt.SWTBaseGame;

import com.jme.math.Vector3f;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;

/**
 * Game for the Collada editor.
 * 
 * @author Jochen Mader - jochen@pramari.com - May 27, 2008
 * 
 */
public class EditorGame extends SWTBaseGame implements KeyListener {

	private boolean increaseZoom = false;
	private boolean decreaseZoom = false;
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private Node centerNode;
	private CameraNode camNode;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param width
	 * @param height
	 */
	public EditorGame(String name, int width, int height, Composite parent,
			int updateResolution, int renderResolution) {
		super(name, updateResolution, renderResolution, width, height, parent,
				false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#simpleInitGame()
	 */
	@Override
	protected void simpleInitGame() {
		getGlCanvas().addKeyListener(this);
		centerNode = new Node();
		camNode = new CameraNode();
		camNode.setCamera(getDisplaySys().getRenderer().getCamera());
		camNode.setLocalTranslation(new Vector3f(0, 0, -25f));
		centerNode.attachChild(camNode);
		getRootNode().attachChild(centerNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#update(float)
	 */
	@Override
	protected void update(float interpolation) {
		super.update(interpolation);
		getRootNode().updateGeometricState(interpolation, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#render(float)
	 */
	float y = 0;
	float x = 0;
	float rotval = 0.05f;

	@Override
	protected void render(float interpolation) {
		super.render(interpolation);
		if (increaseZoom) {
			camNode.getLocalTranslation().addLocal(0, 0, 1);
		} else if (decreaseZoom) {
			camNode.getLocalTranslation().addLocal(0, 0, -1);
		}
		if (left) {
			y += rotval;
			if (y > 360) {
				y -= 360;
			}
			centerNode.getLocalRotation().fromAngles(new float[] { x, y, 0 });

		} else if (right) {
			y -= rotval;
			if (y < 0) {
				y = y + 360;
			}
			centerNode.getLocalRotation().fromAngles(new float[] { x, y, 0 });
		}
		if (up) {
			x += rotval;
			if (x > 360) {
				x -= 360;
			}
			centerNode.getLocalRotation().fromAngles(new float[] { x, y, 0 });

		} else if (down) {
			x -= rotval;
			if (x < 0) {
				x = x + 360;
			}
			centerNode.getLocalRotation().fromAngles(new float[] { x, y, 0 });
		}
		getDisplaySys().getRenderer().cleanup();
		getDisplaySys().getRenderer().clearBuffers();
		getDisplaySys().getRenderer().clearColorBuffer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.character == 'q') {
			decreaseZoom = true;
			increaseZoom = false;
		} else if (e.character == 'e') {
			decreaseZoom = false;
			increaseZoom = true;
		} else if (e.character == 'a') {
			left = true;
			right = false;
		} else if (e.character == 'd') {
			left = false;
			right = true;
		} else if (e.character == 'w') {
			up = false;
			down = true;
		} else if (e.character == 's') {
			up = true;
			down = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.character == 'q') {
			decreaseZoom = false;
		} else if (e.character == 'e') {
			increaseZoom = false;
		} else if (e.character == 'a') {
			left = false;
		} else if (e.character == 'd') {
			right = false;
		} else if (e.character == 'w') {
			down = false;
		} else if (e.character == 's') {
			up = false;
		}
	}

}
