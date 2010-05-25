/*
 *  InteractionMode.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.mode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.widgets.Control;

/**
 * This class allows a set of listeners to be associated with a given user
 * interaction mode and provides functionality for (de)activating the mode (all
 * listeners).
 * 
 * @author Dan West - 'Phoenix' - dan@pramari.com - 12/2/2007
 */
public class InteractionMode {
	/**
	 * List of registered mouse listeners.
	 */
	private List<MouseListener> mouseListeners = new ArrayList<MouseListener>();
	/**
	 * List of registered mouse move listeners.
	 */
	private List<MouseMoveListener> mouseMoveListeners = new ArrayList<MouseMoveListener>();
	/**
	 * List of registered mouse wheel listeners.
	 */
	private List<MouseWheelListener> mouseWheelListeners = new ArrayList<MouseWheelListener>();
	/**
	 * List of registered key listeners.
	 */
	private List<KeyListener> keyListeners = new ArrayList<KeyListener>();

	/**
	 * Activates the mode by attaching all the listeners to the provided
	 * control.
	 * 
	 * @param control
	 *            the control to apply the listeners to
	 */
	public void activate(Control control) {
		for (MouseListener l : mouseListeners)
			control.addMouseListener(l);
		for (MouseMoveListener l : mouseMoveListeners)
			control.addMouseMoveListener(l);
		for (MouseWheelListener l : mouseWheelListeners)
			control.addMouseWheelListener(l);
		for (KeyListener l : keyListeners)
			control.addKeyListener(l);
	}

	/**
	 * Deactivates the mode by removing the listeners from the given control.
	 * 
	 * @param control
	 *            the control to remove the listeners from
	 */
	public void deactivate(Control control) {
		for (MouseListener l : mouseListeners)
			control.removeMouseListener(l);
		for (MouseMoveListener l : mouseMoveListeners)
			control.removeMouseMoveListener(l);
		for (MouseWheelListener l : mouseWheelListeners)
			control.removeMouseWheelListener(l);
		for (KeyListener l : keyListeners)
			control.removeKeyListener(l);
	}

	/**
	 * Adds a new mouselistener to this mode.
	 * 
	 * @param mouseListener
	 *            the listener to add
	 */
	public void addMouseListener(MouseListener mouseListener) {
		mouseListeners.add(mouseListener);
	}

	/**
	 * Returns the mouse listeners for this mode.
	 * @return the mouse listeners for this mode
	 */
	public List<MouseListener> getMouseListeners() {
		return Collections.unmodifiableList(mouseListeners);
	}

	/**
	 * Adds a new mousemovelistener to the mode.
	 * 
	 * @param mouseMoveListener
	 *            the listener to add
	 */
	public void addMouseMoveListener(MouseMoveListener mouseMoveListener) {
		mouseMoveListeners.add(mouseMoveListener);
	}

	/**
	 * Returns the mouse move listeners for this mode.
	 * @return the mouse move listeners for this mode
	 */
	public List<MouseMoveListener> getMouseMoveListeners() {
		return Collections.unmodifiableList(mouseMoveListeners);
	}

	/**
	 * Adds a new mousewheellistener to the mode.
	 * 
	 * @param mouseWheelListener
	 *            the listener to add
	 */
	public void addMouseWheelListener(MouseWheelListener mouseWheelListener) {
		mouseWheelListeners.add(mouseWheelListener);
	}

	/**
	 * Returns the mouse wheel listeners for this mode.
	 * @return the mouse wheel listeners for this mode
	 */
	public List<MouseWheelListener> getMouseWheelListeners() {
		return Collections.unmodifiableList(mouseWheelListeners);
	}

	/**
	 * Adds a new keylistener to the mode.
	 * 
	 * @param keyListener
	 *            the listener to add
	 */
	public void addKeyListener(KeyListener keyListener) {
		keyListeners.add(keyListener);
	}

	/**
	 * Returns the key listeners for this mode.
	 * @return the key listeners for this mode
	 */
	public List<KeyListener> getKeyListeners() {
		return Collections.unmodifiableList(keyListeners);
	}
}