/**
 * 
 */
package org.rifidi.utilities.camera;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class CameraZoomAction implements InputActionInterface {
	private NodeViewerInput input;
	private float speed;

	public CameraZoomAction( NodeViewerInput input, float speed ) {
		this.input = input;
		this.speed = speed;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {
		float r = input.getR();
		r += speed;
		if ( r <= 0 )	r = input.getMinR();
		input.setR(r);
		input.setDirty(true);
		input.updateCamera();
	}
}
