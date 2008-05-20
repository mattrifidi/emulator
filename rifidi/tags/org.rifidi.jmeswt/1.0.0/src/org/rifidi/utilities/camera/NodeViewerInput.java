/**
 * 
 */
package org.rifidi.utilities.camera;

import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.opengl.GLCanvas;
import org.rifidi.utilities.camera.OffsetRepositionAction.RelativeDirection;

import com.jme.input.InputHandler;
import com.jme.input.action.InputActionEvent;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.util.GameTaskQueueManager;

/**
 * This is an input handler for looking at a given spatial and rotating the
 * camera around it, following from keyboard and mouse look actions. In order
 * for this to work with mouse move, it must be registered as a mouseListener
 * 
 * @author dan
 */
public class NodeViewerInput extends InputHandler implements MouseMoveListener, MouseListener {
	private Log logger = LogFactory.getLog(NodeViewerInput.class);

	public static final String STRAFE_LEFT_TRIGGER = "offsetLeft";
	public static final String STRAFE_RIGHT_TRIGGER = "offsetRight";
	public static final String STRAFE_UP_TRIGGER = "offsetUp";
	public static final String STRAFE_DOWN_TRIGGER = "offsetDown";
	public static final String STRAFE_IN_TRIGGER = "offsetIn";
	public static final String STRAFE_OUT_TRIGGER = "offsetOut";

	public static final String ROTATE_LEFT_TRIGGER = "strafeLeft";
	public static final String ROTATE_RIGHT_TRIGGER = "strafeRight";
	public static final String ROTATE_UP_TRIGGER = "strafeUp";
	public static final String ROTATE_DOWN_TRIGGER = "strafeDown";

	public static final String ZOOM_IN_TRIGGER = "zoomIn";
	public static final String ZOOM_OUT_TRIGGER = "zoomOut";

	private NodeViewerInput myself = this;

	private Vector3f offset = new Vector3f(0,0,0);
	private float theta = 0;
	private float phi = 0.0f;
	private float r = 15;
	private float minR = 1;
	private Camera camera;
	private Spatial object;
	private float keyspeed = 1;
	private float mousespeed = 1;
	private boolean dirty = true;

	private GLCanvas canvas = null;
	private Point lastMousePos = null;
	private boolean mouseLooky = false;
	private boolean invert_y = true;
	private boolean invert_x = true;

	/**
	 * This instantiates a new NodeViewerInput for looking at an object with the given camera
	 * @param obj	the object to look at
	 * @param cam	the camera to use
	 * @param canvas	the canvas in which the looking is taking place (for mouselook stuff
	 * @param keyspeed	the speed used when keys are pressed
	 * @param mousespeed the speed used when the mouse is used
	 */
	public NodeViewerInput( Spatial obj, Camera cam, GLCanvas canvas, float keyspeed, float mousespeed ) {
		this.mousespeed = mousespeed;
		this.keyspeed = keyspeed;
		this.canvas = canvas;
		object = obj;
		camera = cam;

		logger.debug("original camloc:  "+cam.getLocation());
		logger.debug("spatial location: "+obj.getLocalTranslation());

		// take camera's positioning and rotation
		Vector3f vector = obj.getLocalTranslation().subtract(cam.getLocation());
		r = vector.length();
		phi = (float) Math.asin( vector.y / r );
		theta = (float) Math.atan2(vector.z , vector.x);
		offset = Vector3f.ZERO;

		logger.debug("vector:"+vector);
		logger.debug("R:     "+r);
		logger.debug("phi:   "+phi);
		logger.debug("theta: "+theta);

		updateCamera();
		setupActions();
	}

	/**
	 * This sets up the keyboard and mouse look actions
	 */
	private void setupActions() {
		// rotate actions
		addAction( new CameraOrbitHorizAction(this,-keyspeed), ROTATE_RIGHT_TRIGGER, true );
		addAction( new CameraOrbitHorizAction(this,keyspeed), ROTATE_LEFT_TRIGGER, true );
		addAction( new CameraOrbitVertAction(this,-keyspeed), ROTATE_DOWN_TRIGGER, true );
		addAction( new CameraOrbitVertAction(this,keyspeed), ROTATE_UP_TRIGGER, true );
		addAction( new CameraZoomAction(this,-keyspeed*.01f), ZOOM_IN_TRIGGER, true );
		addAction( new CameraZoomAction(this,keyspeed*.01f), ZOOM_OUT_TRIGGER, true );

		// strafe actions
		addAction( new OffsetRepositionAction(this,RelativeDirection.LEFT_RIGHT,-keyspeed*.01f), STRAFE_LEFT_TRIGGER, true );
		addAction( new OffsetRepositionAction(this,RelativeDirection.LEFT_RIGHT, keyspeed*.01f), STRAFE_RIGHT_TRIGGER, true );
		addAction( new OffsetRepositionAction(this,RelativeDirection.UP_DOWN,keyspeed*.01f), STRAFE_UP_TRIGGER, true );
		addAction( new OffsetRepositionAction(this,RelativeDirection.UP_DOWN,-keyspeed*.01f), STRAFE_DOWN_TRIGGER, true );
		addAction( new OffsetRepositionAction(this,RelativeDirection.IN_OUT,keyspeed*.01f), STRAFE_IN_TRIGGER, true );
		addAction( new OffsetRepositionAction(this,RelativeDirection.IN_OUT,-keyspeed*.01f), STRAFE_OUT_TRIGGER, true );
	}

	/**
	 * This updates the camera's position and view direction using r, phi, & theta
	 */
	public void updateCamera() {
		if ( dirty ) {
			Vector3f left = new Vector3f( (float)(-r*Math.sin(theta)), 0, (float)(r*Math.cos(theta)) ).normalize().negate();
			Vector3f offset = new Vector3f((float)(r*Math.cos(theta)*Math.cos(phi)),(float)(r*Math.sin(phi)),(float)(r*Math.sin(theta)*Math.cos(phi)));
			Vector3f loc = this.offset.add(object.getLocalTranslation().subtract(offset));
			Vector3f up = offset.negate().cross(left);
			if ( up.y < 0 )		up = up.negate();

//			logger.debug("location:  "+loc);
//			logger.debug("direction: "+offset);
//			logger.debug("left:      "+left);
//			logger.debug("up:        "+up);

			camera.setLocation( loc );
			camera.setDirection(offset.normalize());
			camera.setLeft(left.normalize());
			camera.setUp(up.normalize());
		}

		camera.update();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseMoveListener#mouseMove(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseMove(MouseEvent e) {
		if ( lastMousePos == null ) {
			lastMousePos = new Point(e.x,e.y);
		} else {
			int dx = e.x - lastMousePos.x;
			int dy = e.y - lastMousePos.y;
			if ( invert_y )		dy = -dy;
			if ( invert_x )		dx = -dx;
			lastMousePos = new Point(e.x,e.y);

			if ( dx != 0 ) {
				InputActionEvent evt = new InputActionEvent();
				evt.setTime(dx);
				new CameraOrbitHorizAction(this,mousespeed).performAction(evt);
			}

			if ( dy != 0 ) {
				InputActionEvent evt = new InputActionEvent();
				evt.setTime(dy);
				new CameraOrbitVertAction(this,mousespeed).performAction(evt);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDoubleClick(MouseEvent e) {
		if ( e.button == 1 ) {
			if ( mouseLooky )
				canvas.removeMouseMoveListener(this);
			else
				canvas.addMouseMoveListener(this);

			mouseLooky = !mouseLooky;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseDown(MouseEvent e) {
		if ( e.button == 1 ) {
			canvas.addMouseMoveListener(this);
			mouseLooky = true;
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
	 */
	public void mouseUp(MouseEvent e) {
		if ( e.button == 1 ) {
			canvas.removeMouseMoveListener(this);
			lastMousePos = null;
			mouseLooky = false;
		}
	}

	/**
	 * @return the keyspeed
	 */
	public float getKeyspeed() {
		return keyspeed;
	}

	/**
	 * @param keyspeed the keyspeed to set
	 */
	public void setKeyspeed(float keyspeed) {
		this.keyspeed = keyspeed;
	}

	/**
	 * @return the phi
	 */
	public float getPhi() {
		return phi;
	}

	/**
	 * @param phi the phi to set
	 */
	public void setPhi(float phi) {
		this.phi = phi;
	}

	/**
	 * @return the r
	 */
	public float getR() {
		return r;
	}

	/**
	 * @param r the r to set
	 */
	public void setR(float r) {
		this.r = r;
	}

	/**
	 * @return the theta
	 */
	public float getTheta() {
		return theta;
	}

	/**
	 * @param theta the theta to set
	 */
	public void setTheta(float theta) {
		this.theta = theta;
	}

	/**
	 * @return the minR
	 */
	public float getMinR() {
		return minR;
	}

	/**
	 * @param minR the minR to set
	 */
	public void setMinR(float minR) {
		this.minR = minR;
	}

	/**
	 * @return the offset
	 */
	public Vector3f getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset( Vector3f offset ) {
		this.offset = offset;
	}

	/**
	 * @return the camera
	 */
	public Camera getCamera() {
		return camera;
	}

	/**
	 * @param camera the camera to set
	 */
	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	/**
	 * @param model
	 */
	public void setSpatial(Node model) {
		object = model;
	}

	/**
	 * @return the object
	 */
	public Spatial getSpatial() {
		return object;
	}

	/**
	 * @return the dirty
	 */
	public boolean isDirty() {
		return dirty;
	}

	/**
	 * @param dirty the dirty to set
	 */
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	/**
	 * @return the inverted
	 */
	public boolean isInvertedY() {
		return invert_y;
	}

	/**
	 * @param inverted the inverted to set
	 */
	public void setInvertY(boolean inverted) {
		this.invert_y = inverted;
	}

	/**
	 * @return the inverted
	 */
	public boolean isInvertedX() {
		return invert_x;
	}

	/**
	 * @param inverted the inverted to set
	 */
	public void setInvertX(boolean inverted) {
		this.invert_x = inverted;
	}
}
