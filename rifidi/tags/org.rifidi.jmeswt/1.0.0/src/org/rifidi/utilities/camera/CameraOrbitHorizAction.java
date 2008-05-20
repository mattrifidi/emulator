/**
 * 
 */
package org.rifidi.utilities.camera;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;

/**
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class CameraOrbitHorizAction implements InputActionInterface {
	private NodeViewerInput input;
	private float speed;

	public CameraOrbitHorizAction( NodeViewerInput input, float speed ) {
		this.input = input;
		this.speed = speed;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {
		float theta = input.getTheta();
		theta += speed*evt.getTime()*.05f;
		while ( theta > 2*Math.PI )	theta -= 2*Math.PI;
		while ( theta < 0 )			theta += 2*Math.PI;
		input.setTheta(theta);
		input.setDirty(true);
		input.updateCamera();
	}
}