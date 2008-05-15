package org.rifidi.jmonkey;

import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;

import com.jme.image.Image;
import com.jme.input.MouseInput;
import com.jme.input.MouseInputListener;

public class SWTMouseInput extends MouseInput implements MouseListener, MouseMoveListener {
	private static Log logger = LogFactory.getLog(SWTMouseInput.class);
	private int buttonCount = 0;

	private Point lastPos = new Point(0,0);
	private Point newPos = new Point(0,0);

	private ArrayList<Integer> allowMoveOn = null;
	private boolean[] buttonStates = null; 
	
	/**
	 * Default constructor that creates a mouseinput for 3 buttons.
	 *
	 */
	public SWTMouseInput() {
		super();
		this.buttonCount=3;
		allowMoveOn = new ArrayList<Integer>();
		allowMoveOn.add(1);
		buttonStates=new boolean[buttonCount];
		for(int count=0;count<buttonCount;count++){
			buttonStates[count]=false;
		}		
	}

	public void addAllowMoveButton( Integer b ) {
		if ( b >= -1 && b < buttonCount )
			if ( !allowMoveOn.contains(b) )
				allowMoveOn.add(b);
	}

	public void removeAllowMoveButton( Integer b ) {
		if ( allowMoveOn.contains(b) )
			allowMoveOn.remove((Object)b);
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#destroy()
	 */
	@Override
	protected void destroy() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getButtonCount()
	 */
	@Override
	public int getButtonCount() {
		return buttonCount;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getButtonIndex(java.lang.String)
	 */
	@Override
	public int getButtonIndex(String buttonName) {
		String lowerButtonName=buttonName.toLowerCase();
		if(lowerButtonName.contains("MOUSE")){
			//TODO: try catch or not try catch that is the question here
			return Integer.parseInt(lowerButtonName.substring(5));
		}
		logger.fatal("Unknown button: "+buttonName);
		return -1;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getButtonName(int)
	 */
	@Override
	public String getButtonName(int buttonIndex) {
		return "MOUSE"+buttonIndex;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getWheelDelta()
	 */
	@Override
	public int getWheelDelta() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getWheelRotation()
	 */
	@Override
	public int getWheelRotation() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getXAbsolute()
	 */
	@Override
	public int getXAbsolute() {
		return newPos.x;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getXDelta()
	 */
	@Override
	public int getXDelta() {
		int delta=lastPos.x-newPos.x;
		lastPos.x=newPos.x;
		return delta;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getYAbsolute()
	 */
	@Override
	public int getYAbsolute() {
		return newPos.y;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#getYDelta()
	 */
	@Override
	public int getYDelta() {
		int delta=newPos.y-lastPos.y;
		lastPos.y=newPos.y;
		return delta;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#isButtonDown(int)
	 */
	@Override
	public boolean isButtonDown(int buttonCode) {
		return buttonStates[buttonCode];
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#isCursorVisible()
	 */
	@Override
	public boolean isCursorVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#setCursorPosition(int, int)
	 */
	@Override
	public void setCursorPosition(int x, int y) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#setCursorVisible(boolean)
	 */
	@Override
	public void setCursorVisible(boolean v) {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#setHardwareCursor(java.net.URL, int, int)
	 */
	@Override
	public void setHardwareCursor(URL file, int xHotspot, int yHotspot) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#setHardwareCursor(java.net.URL)
	 */
	@Override
	public void setHardwareCursor(URL file) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#update()
	 */
	@Override
	public void update() {
        for (MouseInputListener listener: listeners) {
        	listener.onMove(newPos.x-lastPos.x, newPos.y-lastPos.y, newPos.x, newPos.y);
        	for(int count=1;count<getButtonCount();count++){
        		listener.onButton(count, buttonStates[count], newPos.x, newPos.y);
        	}
        }
	}
	
	
	//MouseListener methods
	
	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if(!(buttonStates.length-1>e.button)){
			buttonStates[e.button]=true;	
		}
		lastPos.x=e.x;
		lastPos.y=e.y;
		newPos.x=lastPos.x;
		newPos.y=lastPos.y;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if(!(buttonStates.length-1>e.button)){
			buttonStates[e.button]=false;	
		}
		lastPos.x=0;
		lastPos.y=0;
		newPos.x=lastPos.x;
		newPos.y=lastPos.y;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
		if( buttonStates[1] ) {
			lastPos.x=newPos.x;
			lastPos.y=newPos.y;
			newPos.x=e.x;
			newPos.y=e.y;
		}
	}

	/* (non-Javadoc)
	 * @see com.jme.input.MouseInput#setHardwareCursor(java.net.URL, com.jme.image.Image[], int[], int, int)
	 */
	@Override
	public void setHardwareCursor(URL file, Image[] images, int[] delays,
			int hotspot, int hotspot2) {
		// TODO Auto-generated method stub
		
	}	
	
}

