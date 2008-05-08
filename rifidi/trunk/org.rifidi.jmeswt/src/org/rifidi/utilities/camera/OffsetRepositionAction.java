/**
 * 
 */
package org.rifidi.utilities.camera;

import com.jme.input.action.InputActionEvent;
import com.jme.input.action.InputActionInterface;
import com.jme.math.Vector3f;

/**
 * @author dan
 *
 */
public class OffsetRepositionAction implements InputActionInterface {
	private RelativeDirection direction;
	private NodeViewerInput input;
	private float speed;

	public OffsetRepositionAction( NodeViewerInput input, RelativeDirection dir, float speed ) {
		this.input = input;
		this.speed = speed;
		direction = dir;
	}

	/* (non-Javadoc)
	 * @see com.jme.input.action.InputActionInterface#performAction(com.jme.input.action.InputActionEvent)
	 */
	public void performAction(InputActionEvent evt) {
		Vector3f dir = Vector3f.ZERO;

		if ( direction == RelativeDirection.IN_OUT ) {
			dir = input.getCamera().getDirection();
		} else if ( direction == RelativeDirection.UP_DOWN ) {
			dir = input.getCamera().getUp();
		} else if ( direction == RelativeDirection.LEFT_RIGHT ) {
			dir = input.getCamera().getLeft();
		} else {
			System.out.println("unknown direction");
		}
		Vector3f offset = input.getOffset().add(dir.normalize().mult(speed));
		input.setOffset(offset);
		input.setDirty(true);
		input.updateCamera();
	}

	public enum RelativeDirection {
		IN_OUT,
		UP_DOWN,
		LEFT_RIGHT
	}
}
