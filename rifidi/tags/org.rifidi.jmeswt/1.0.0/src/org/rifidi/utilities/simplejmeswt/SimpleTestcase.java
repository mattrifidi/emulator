/**
 * 
 */
package org.rifidi.utilities.simplejmeswt;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;

import com.jme.input.FirstPersonHandler;
import com.jme.input.InputHandler;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.Text;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;
import com.jmex.physics.PhysicsSpace;

import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.utilities.ReoccurringCallable;
import org.rifidi.utilities.text.TextOverlayElement;

/**
 * This thread is the superclass for all extensions running inside this environment.
 * It creates the basic scene, and starts the render and update threads for it.
 * @author Dan West - "Phoenix' - dan@pramari.com
 */
public abstract class SimpleTestcase {
	private PhysicsSpace physicsSpace = PhysicsSpace.create();

	/**
	 * The logger for this class
	 */
	private static Log logger = LogFactory.getLog(SimpleTestcase.class);

	/**
	 * Reference to the display system
	 */
	private SWTDisplaySystem displaySys;

	/**
	 * The thread that performs the rendering
	 */
	private RenderThread renderThread;

	/**
	 * The thread that performs updating
	 */
	private UpdateThread updateThread;

	/**
	 * The input handler
	 */
	private InputHandler input;

	/**
	 * The canavas to render to
	 */
	private GLCanvas canvas;

	/**
	 * The camera
	 */
	private Camera camera;

	/**
	 * Root node of the scenegraph
	 */
	private Node rootNode;

	/**
	 * The fps and stats display
	 */
	private TextOverlayElement fpsOverlay;

	/**
	 * Lock used for threadsafe render/update
	 */
	private ReentrantLock lock;

	/**
	 * Instantiate a new testcase and setup render/update threads.
	 * Calls init()
	 * 
	 * @param canvas the canvas to render to
	 */
	public SimpleTestcase( GLCanvas canvas ) {
		displaySys = (SWTDisplaySystem) DisplaySystem.getDisplaySystem();
		this.canvas = canvas;
		init();

		lock = new ReentrantLock();
		renderThread = new RenderThread(lock,Display.getCurrent(),rootNode,canvas);
		updateThread = new UpdateThread(lock,rootNode);

		logger.debug("SimpleJmeThread initialized");
	}

	/**
	 * Set up the basic scenegraph, render states, lights, camera, input handler
	 */
	public void init() {
		rootNode = new Node("root");

		// create a light
		PointLight l = new PointLight();
		LightState ls = displaySys.getRenderer().createLightState();
		l.setLocation(new Vector3f( 5, 5, 5));
		l.setDiffuse(ColorRGBA.white);
		rootNode.setRenderState(ls);
		ls.setEnabled(true);
		l.setEnabled(true);
		ls.attach(l);

		// set up the z buffer state
		ZBufferState buf = displaySys.getRenderer().createZBufferState();
		buf.setFunction(ZBufferState.CF_LEQUAL);
		rootNode.setRenderState(buf);
		buf.setEnabled(true);

		// add backface culling
		CullState cullState = displaySys.getRenderer().createCullState();
		cullState.setCullMode(CullState.CS_BACK);
		rootNode.setRenderState(cullState);

		// create default camera
		int width = displaySys.getRenderer().getWidth();
		int height = displaySys.getRenderer().getHeight();
		camera = displaySys.getRenderer().createCamera(width,height);
		camera.setFrustumPerspective(45.0f, (float)width / (float)height, 0.01f, 1000.0f);
		camera.setParallelProjection(false);
		camera.update();
		camera.setLocation(new Vector3f(0,0,-10));
		camera.setDirection(Vector3f.UNIT_Z.negate());
		camera.setLeft(Vector3f.UNIT_X.negate());
		camera.setUp(Vector3f.UNIT_Y);
		camera.update();
		displaySys.getRenderer().setCamera(camera);
		displaySys.switchContext(canvas);

		input = new FirstPersonHandler(camera,40,.5f);

		Renderer r = DisplaySystem.getDisplaySystem().getRenderer();
		r.enableStatistics(true);
		fpsOverlay = new TextOverlayElement("",-1);
		Text t = (Text) fpsOverlay.getTextGeom();
		t.setLocalTranslation(0,canvas.getSize().y-t.getHeight(),0);

		initMore();

		// allow update to be called on every update call
		GameTaskQueue update = GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE);
		GameTaskQueue render = GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER);

		new ReoccurringCallable( update, new Callable<Object>() {
			public Object call() throws Exception {
				update();
				return null;
			}
		} );

		// allow render to be called on every render call
		new ReoccurringCallable( render, new Callable<Object>() {
			public Object call() throws Exception {
				render();
				return null;
			}
		} );
	}

	/**
	 *  start the rendering and updating
	 */
	public void start() {
		renderThread.start();
		updateThread.start();
	}

	/**
	 * Update the scene
	 */
	private void update() {
		float tpf = Timer.getTimer().getTimePerFrame();
		DisplaySystem.getDisplaySystem().getRenderer().getCamera().update();
		input.update(tpf);
		physicsSpace.update(tpf);

		fpsOverlay.setText(
				"FPS: " + (int)Timer.getTimer().getFrameRate() + " - " +
				displaySys.getRenderer().getStatistics() );

		// allow extending classes to update more stuff
		updateMore();
	}

	/**
	 * Perform rendering operations
	 */
	private void render() {
		fpsOverlay.update(0);
		displaySys.getRenderer().clearStatistics();
		fpsOverlay.render(displaySys.getRenderer());

		// allow extending classes to render more stuff
		renderMore();
	}

	/**
	 * Extending classes can override these for added functionality
	 */
	public abstract void initMore();
	public abstract void updateMore();
	public abstract void renderMore();

	/**
	 * @return the renderThread
	 */
	public RenderThread getRenderThread() {
		return renderThread;
	}

	/**
	 * @return the updateThread
	 */
	public UpdateThread getUpdateThread() {
		return updateThread;
	}

	/**
	 * @return the rootNode
	 */
	public Node getRootNode() {
		return rootNode;
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
	 * @return the input
	 */
	public InputHandler getInputHandler() {
		return input;
	}

	/**
	 * @param inputHandler the input to set
	 */
	public void setInputHandler(InputHandler inputHandler) {
		input = inputHandler;
	}

	public GLCanvas getCanvas() {
		return canvas;
	}

	public PhysicsSpace getPhysicsSpace() {
		return physicsSpace;
	}	
}