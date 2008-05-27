/*
 *  SWTKeyInput.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmeswt.input;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.lwjgl.input.Keyboard;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;

/**
 * SWT based key input to be used with jmonkey.
 * 
 * @author Jochen Mader - jochen@pramari.com - August 22, 2008
 * 
 */
public class SWTKeyInput extends KeyInput implements KeyListener {
	/**
	 * Stack for key events.
	 */
	private Stack<Object[]> eventStack = new Stack<Object[]>();
	/**
	 * Map for holding infos about what keys are currently pressed.
	 */
	private Map<Integer, Boolean> keyMap = new HashMap<Integer, Boolean>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.KeyInput#destroy()
	 */
	@Override
	protected void destroy() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.KeyInput#getKeyIndex(java.lang.String)
	 */
	@Override
	public int getKeyIndex(String name) {
		return Keyboard.getKeyIndex(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.KeyInput#getKeyName(int)
	 */
	@Override
	public String getKeyName(int key) {
		return Keyboard.getKeyName(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.KeyInput#isKeyDown(int)
	 */
	@Override
	public boolean isKeyDown(int key) {
		if (keyMap.get(key) == null) {
			return false;
		}
		return keyMap.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.input.KeyInput#update()
	 */
	@Override
	public void update() {
		if (listeners != null && listeners.size() > 0) {
			Object[] eventArray = null;
			try {
				while ((eventArray = eventStack.pop()) != null) {
					KeyEvent e = (KeyEvent) eventArray[0];
					Boolean pressed = (Boolean) eventArray[1];
					for (KeyInputListener listener : listeners) {
						System.out.println("listener: "+listener);
						listener.onKey(e.character, Mapping.SWTtoLWJGL
								.get(e.keyCode), pressed);
					}
				}
			} catch (EmptyStackException e) {
				// we expect this to happen
			}
		}
		eventStack.clear();
	}

	// KeyListener methods

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		keyMap.put(Mapping.SWTtoLWJGL.get(e.keyCode), true);
		eventStack.push(new Object[] { e, true });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		keyMap.put(Mapping.SWTtoLWJGL.get(e.keyCode), false);
		eventStack.push(new Object[] { e, false });
	}

}
