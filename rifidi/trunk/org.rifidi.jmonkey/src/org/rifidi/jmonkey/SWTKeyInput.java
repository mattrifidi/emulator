package org.rifidi.jmonkey;


import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.lwjgl.input.Keyboard;

import com.jme.input.KeyInput;
import com.jme.input.KeyInputListener;

public class SWTKeyInput extends KeyInput implements KeyListener{

	private Stack<Object[]> eventStack=new Stack<Object[]>();
	private Map<Integer,Boolean> keyMap= new HashMap<Integer, Boolean>();
	
	/* (non-Javadoc)
	 * @see com.jme.input.KeyInput#destroy()
	 */
	@Override
	protected void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.jme.input.KeyInput#getKeyIndex(java.lang.String)
	 */
	@Override
	public int getKeyIndex(String name) {
		return Keyboard.getKeyIndex(name);
	}

	/* (non-Javadoc)
	 * @see com.jme.input.KeyInput#getKeyName(int)
	 */
	@Override
	public String getKeyName(int key) {
		return Keyboard.getKeyName(key);
	}

	/* (non-Javadoc)
	 * @see com.jme.input.KeyInput#isKeyDown(int)
	 */
	@Override
	public boolean isKeyDown(int key) {
		if(keyMap.get(key)==null){
			return false;
		}
		return keyMap.get(key);
	}

	/* (non-Javadoc)
	 * @see com.jme.input.KeyInput#update()
	 */
	@Override
	public void update() {
		if(listeners!=null && listeners.size()>0){
			Object[] eventArray=null;
			try{
				while((eventArray=eventStack.pop())!=null){					
					KeyEvent e=(KeyEvent)eventArray[0];
					Boolean pressed=(Boolean)eventArray[1];
					for(KeyInputListener listener:listeners){
						listener.onKey(e.character, Mapping.SWTtoLWJGL.get(e.keyCode), pressed);
					}
				}
			}
			catch(EmptyStackException e){
				//we expect this to happen
			}
		}
		eventStack.clear();
	}

	
	//KeyListener methods
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		keyMap.put(Mapping.SWTtoLWJGL.get(e.keyCode),true);
		eventStack.push(new Object[]{e,true});
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
		keyMap.put(Mapping.SWTtoLWJGL.get(e.keyCode),false);
		eventStack.push(new Object[]{e,false});
	}
	
}
