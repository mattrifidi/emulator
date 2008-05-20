/**
 * 
 */
package org.rifidi.utilities.camera;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class CameraOrbitVertAction implements InputActionInterface {
	private NodeViewerInput input;
	private float speed;

	public CameraOrbitVertAction( NodeViewerInput input, float speed ) {
		this.input = input;
		this.speed = speed;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {
		float phi = input.getPhi();
		phi += speed*evt.getTime()*.05f;
		if ( phi >  .5f*Math.PI )	phi = (float) ( .5f*Math.PI);
		if ( phi < -.5f*Math.PI )	phi = (float) (-.5f*Math.PI);
		input.setPhi(phi);
		input.setDirty(true);
		input.updateCamera();
	}
}

