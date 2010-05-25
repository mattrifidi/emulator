/*
 *  ConverterImplementor.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter.editor.implementor;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.monklypse.core.SWTDefaultImplementor;

import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;

/**
 * Game for the Collada editor.
 * 
 * @author Jochen Mader - jochen@pramari.com - May 27, 2008
 * 
 */
public class ConverterImplementor extends SWTDefaultImplementor implements KeyListener {

	private boolean increaseZoom = false;
	private boolean decreaseZoom = false;
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private Node centerNode;
	private CameraNode camNode;
	private LightState lightState;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param width
	 * @param height
	 */
	public ConverterImplementor(String name, int width, int height) {
		super(width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.SWTDefaultImplementor#simpleRender()
	 */
	float y = 0;
	float x = 0;
	float rotval = 0.05f;

	@Override
	public void simpleRender() {
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.SWTDefaultImplementor#simpleSetup()
	 */
	@Override
	public void simpleSetup() {
		getCanvas().addKeyListener(this);
		centerNode = new Node();
		camNode = new CameraNode();
		camNode.setCamera(getRenderer().getCamera());
		camNode.setLocalTranslation(new Vector3f(0, 0, -25f));
		centerNode.attachChild(camNode);
		getRootNode().attachChild(centerNode);
		/** Set up a basic, default light. */
		PointLight light = new PointLight();
		light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLocation(new Vector3f(0, 0, 100));
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		lightState = getRenderer().createLightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		getRootNode().setRenderState(lightState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.SWTDefaultImplementor#simpleUpdate()
	 */
	@Override
	public void simpleUpdate() {
		getRootNode().updateGeometricState(tpf, true);
		getRootNode().updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.
	 * KeyEvent)
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
	 * @see
	 * org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events
	 * .KeyEvent)
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.monklypse.core.JMECanvasImplementor2#resizeCanvas(int, int)
	 */
	@Override
	public void resizeCanvas(int width, int height) {
		if (width > height) {
			getRenderer().reinit(width, (int) (width * .8));
			return;
		}
		getRenderer().reinit((int) (height * .8), height);
	}

}
