/*
 *  SWTMouseInput.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmeswt.input;

import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.graphics.Point;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.image.Image;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;

/**
 * SWT based mouse input to be used with jmonkey.
 * 
 * @author Jochen Mader - jochen@pramari.com - August 22, 2008
 * 
 */
public class SWTMouseInput extends MouseInput implements MouseListener,
		MouseMoveListener, MouseWheelListener {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger
			.getLogger(SWTDisplaySystem.class.getName());
	/**
	 * Number of availabel buttons.
	 */
	private int buttonCount = 0;
	/**
	 * Last pointer position.
	 */
	private Point lastPos = new Point(0, 0);
	/**
	 * New pointer position.
	 */
	private Point newPos = new Point(0, 0);

	private ArrayList<Integer> allowMoveOn = null;
	/**
	 * State array for the buttons (true=pressed).
	 */
	private boolean[] buttonStates = null;
	/**
	 * The amount the mousewheel has moved.
	 */
	private int mouseWheelDelta = 0;
	/**
	 * Position where the mousewheel was turned.
	 */
	private Point wheelPos = new Point(0, 0);

	/**
	 * Default constructor that creates a mouseinput for 3 buttons.
	 * 
	 */
	public SWTMouseInput() {
		super();
		this.buttonCount = 3;
		allowMoveOn = new ArrayList<Integer>();
		allowMoveOn.add(1);
		buttonStates = new boolean[buttonCount];
		for (int count = 0; count < buttonCount; count++) {
			buttonStates[count] = false;
		}
	}

	public void addAllowMoveButton(Integer b) {
		if (b >= -1 && b < buttonCount)
			if (!allowMoveOn.contains(b))
				allowMoveOn.add(b);
	}

	public void removeAllowMoveButton(Integer b) {
		if (allowMoveOn.contains(b))
			allowMoveOn.remove((Object) b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#destroy()
	 */
	@Override
	protected void destroy() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getButtonCount()
	 */
	@Override
	public int getButtonCount() {
		return buttonCount;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getButtonIndex(java.lang.String)
	 */
	@Override
	public int getButtonIndex(String buttonName) {
		String lowerButtonName = buttonName.toLowerCase();
		if (lowerButtonName.contains("MOUSE")) {
			return Integer.parseInt(lowerButtonName.substring(5));
		}
		logger.warning("Unknown button: " + buttonName);
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getButtonName(int)
	 */
	@Override
	public String getButtonName(int buttonIndex) {
		return "MOUSE" + buttonIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getWheelDelta()
	 */
	@Override
	public int getWheelDelta() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getWheelRotation()
	 */
	@Override
	public int getWheelRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getXAbsolute()
	 */
	@Override
	public int getXAbsolute() {
		return newPos.x;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getXDelta()
	 */
	@Override
	public int getXDelta() {
		int delta = lastPos.x - newPos.x;
		lastPos.x = newPos.x;
		return delta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getYAbsolute()
	 */
	@Override
	public int getYAbsolute() {
		return newPos.y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#getYDelta()
	 */
	@Override
	public int getYDelta() {
		int delta = newPos.y - lastPos.y;
		lastPos.y = newPos.y;
		return delta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#isButtonDown(int)
	 */
	@Override
	public boolean isButtonDown(int buttonCode) {
		return buttonStates[buttonCode];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#isCursorVisible()
	 */
	@Override
	public boolean isCursorVisible() {
		logger.warning("isCursorVisible not implemented");
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#setCursorPosition(int, int)
	 */
	@Override
	public void setCursorPosition(int x, int y) {
		logger
				.warning("Use Display.getCurrent().setCursorLocation() for positioning the mouse!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#setCursorVisible(boolean)
	 */
	@Override
	public void setCursorVisible(boolean v) {
		logger.warning("Use SWTDisplaySystem.hideMouse/showMouse!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#setHardwareCursor(java.net.URL, int, int)
	 */
	@Override
	public void setHardwareCursor(URL file, int xHotspot, int yHotspot) {
		logger.warning("setHardwareCursor not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#setHardwareCursor(java.net.URL)
	 */
	@Override
	public void setHardwareCursor(URL file) {
		logger.warning("setHardwareCursor not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#update()
	 */
	@Override
	public void update() {
		for (MouseInputListener listener : listeners) {
			listener.onMove(newPos.x - lastPos.x, newPos.y - lastPos.y,
					newPos.x, newPos.y);
			for (int count = 1; count < getButtonCount(); count++) {
				listener.onButton(count, buttonStates[count], newPos.x,
						newPos.y);
			}
			if(mouseWheelDelta>0){
				listener.onWheel(mouseWheelDelta, wheelPos.x, wheelPos.y);	
			}
		}
		mouseWheelDelta=0;
	}

	// MouseListener methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
		logger.warning("mouseDoubleClick not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if (buttonStates.length - 1 > e.button) {
			buttonStates[e.button] = true;
		}
		lastPos.x = e.x;
		lastPos.y = e.y;
		newPos.x = lastPos.x;
		newPos.y = lastPos.y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if (buttonStates.length - 1 > e.button) {
			buttonStates[e.button] = false;
		}
		lastPos.x = 0;
		lastPos.y = 0;
		newPos.x = lastPos.x;
		newPos.y = lastPos.y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
		if (buttonStates[1]) {
			if (!(newPos.x == e.x && newPos.y == e.y)) {
				lastPos.x = newPos.x;
				lastPos.y = newPos.y;
				newPos.x = e.x;
				newPos.y = e.y;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.MouseInput#setHardwareCursor(java.net.URL,
	 *      com.jme.image.Image[], int[], int, int)
	 */
	@Override
	public void setHardwareCursor(URL file, Image[] images, int[] delays,
			int hotspot, int hotspot2) {
		logger.warning("setHardwareCursor not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.MouseWheelListener#mouseScrolled(org.eclipse.swt.events.MouseEvent)
	 */
	@Override
	public void mouseScrolled(MouseEvent e) {
		mouseWheelDelta = e.count;
		wheelPos.x = e.x;
		wheelPos.y = e.y;
	}

}
