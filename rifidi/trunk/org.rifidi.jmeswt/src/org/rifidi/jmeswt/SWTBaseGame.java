/*
 *  SWTBaseGame.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jmeswt;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.rifidi.jmeswt.input.SWTKeyInput;
import org.rifidi.jmeswt.input.SWTMouseInput;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.app.AbstractGame;
import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.light.PointLight;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.ColorRGBA;
import com.jme.scene.Node;
import com.jme.scene.state.LightState;
import com.jme.scene.state.WireframeState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.system.JmeException;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;

/**
 * Base for SWT-JME games.
 * 
 * @author Jochen Mader - jochen@pramari.com - May 26, 2008
 * 
 */
public abstract class SWTBaseGame extends AbstractGame {
	/**
	 * Logger for this class.
	 */
	private static final Logger logger = Logger.getLogger(SWTBaseGame.class
			.getName());
	/**
	 * Canvas for this game.
	 */
	private GLCanvas glCanvas;
	/**
	 * SWT parent composite for the canvas.
	 */
	private Composite parent;
	/**
	 * Name of the game.
	 */
	private String name;
	/**
	 * Initial size of the canvas.
	 */
	private int width, height;
	/**
	 * The camera.
	 */
	private Camera cam;
	/**
	 * Root of the scene graph.
	 */
	private Node rootNode;
	/**
	 * State for wireframe display.
	 */
	private WireframeState wireState;
	/**
	 * State for the light.
	 */
	private LightState lightState;
	/**
	 * Queue for rendering.
	 */
	private GameTaskQueue renderQueue;
	/**
	 * Queue for updating.
	 */
	private GameTaskQueue updateQueue;
	/**
	 * The thread for rendering the scene.
	 */
	private RenderThread renderThread;
	/**
	 * The thread for updating the scene.
	 */
	private UpdateThread updateThread;
	/**
	 * Semaphore for update and render threads.
	 */
	private Semaphore gameSemaphore = new Semaphore(1);
	/**
	 * Invisible cursor.
	 */
	private Cursor invisibleCursor;
	/**
	 * Visible cursor.
	 */
	private Cursor defaultCursor;
	/**
	 * Resolution for the update thread.
	 */
	private int updateResolution;
	/**
	 * Resolution for the render thread.
	 */
	private int renderResolution;

	/**
	 * Constructor. Note: parent can be null as long as setParent() is called
	 * before the start() method is hit.
	 * 
	 * @param updateResolution
	 *            millisecs between two update runs
	 * @param renderResolution
	 *            millisecs between two render runs
	 * @param name
	 *            a name for the game
	 * @param width
	 *            initial width
	 * @param height
	 *            initial height
	 * @param parent
	 *            the parent of the glCanvas that will be created
	 * @param single
	 *            if set to true the game assumes that no other SWTgames are
	 *            running and that it can use the default queues.
	 */
	public SWTBaseGame(String name, int updateResolution, int renderResolution,
			int width, int height, Composite parent, boolean single) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
		this.parent = parent;
		// add our taskqueues for rendering and updating
		if (single) {
			renderQueue = GameTaskQueueManager.getManager().getQueue(
					GameTaskQueue.RENDER);
			updateQueue = GameTaskQueueManager.getManager().getQueue(
					GameTaskQueue.UPDATE);
		} else {
			renderQueue = new GameTaskQueue();
			updateQueue = new GameTaskQueue();
			GameTaskQueueManager.getManager().addQueue(name + ".render",
					renderQueue);
			GameTaskQueueManager.getManager().addQueue(name + ".update",
					updateQueue);
		}
		this.renderResolution = renderResolution;
		this.updateResolution = updateResolution;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#cleanup()
	 */
	@Override
	protected void cleanup() {
		renderThread.setKeepRunning(false);
		updateThread.setKeepRunning(false);
		try {
			renderThread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		try {
			updateThread.join();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#initGame()
	 */
	@Override
	protected void initGame() {
		/** Create rootNode */
		rootNode = new Node("rootNode");

		/**
		 * Create a wirestate to toggle on and off. Starts disabled with default
		 * width of 1 pixel.
		 */
		wireState = display.getRenderer().createWireframeState();
		wireState.setEnabled(false);
		rootNode.setRenderState(wireState);

		/**
		 * Create a ZBuffer to display pixels closest to the camera above
		 * farther ones.
		 */
		ZBufferState buf = display.getRenderer().createZBufferState();
		buf.setEnabled(true);
		buf.setFunction(ZBufferState.CF_LEQUAL);
		rootNode.setRenderState(buf);

		/** Set up a basic, default light. */
		PointLight light = new PointLight();
		light.setDiffuse(new ColorRGBA(0.75f, 0.75f, 0.75f, 0.75f));
		light.setAmbient(new ColorRGBA(0.5f, 0.5f, 0.5f, 1.0f));
		light.setLocation(new Vector3f(100, 100, 100));
		light.setEnabled(true);

		/** Attach the light to a lightState and the lightState to rootNode. */
		lightState = display.getRenderer().createLightState();
		lightState.setEnabled(true);
		lightState.attach(light);
		rootNode.setRenderState(lightState);

		/** Let derived classes initialize. */
		simpleInitGame();

		rootNode.updateGeometricState(0.0f, true);
		rootNode.updateRenderState();
	}

	/**
	 * Called near end of initGame(). Must be defined by derived classes.
	 */
	protected abstract void simpleInitGame();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#initSystem()
	 */
	@Override
	protected void initSystem() {
		display = (SWTDisplaySystem) DisplaySystem
				.getDisplaySystem("SWTDISPLAYSYS");
		glCanvas = ((SWTDisplaySystem) display).createGLCanvas(width, height,
				parent);
		/** Set a black background. */
		display.getRenderer().setBackgroundColor(ColorRGBA.black.clone());
		cam = display.getRenderer().createCamera(width, height);
		/** Set up how our camera sees. */
		Vector3f loc = new Vector3f(0.0f, 0.0f, 25.0f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0f, -1.0f);
		/** Move our camera to a correct place and orientation. */
		cam.setFrame(loc, left, up, dir);
		cameraPerspective();
		/** Signal that we've changed our camera's location/frustum. */
		cam.update();
		/** Assign the camera to this renderer. */
		display.getRenderer().setCamera(cam);
		if (!MouseInput.isInited()) {
			MouseInput.setProvider(SWTMouseInput.class.getCanonicalName());
		}
		if (!KeyInput.isInited()) {
			KeyInput.setProvider(SWTKeyInput.class.getCanonicalName());
		}
		// create an invisible cursor and store a reference to the default
		// cursor
		Color white = Display.getCurrent().getSystemColor(SWT.COLOR_WHITE);
		Color black = Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		PaletteData palette = new PaletteData(new RGB[] { white.getRGB(),
				black.getRGB() });
		ImageData sourceData = new ImageData(16, 16, 1, palette);
		sourceData.transparentPixel = 0;
		invisibleCursor = new Cursor(Display.getCurrent(), sourceData, 0, 0);
		defaultCursor = glCanvas.getCursor();
	}

	protected void cameraPerspective() {
		cam.setFrustumPerspective(45.0f, (float) display.getWidth()
				/ (float) display.getHeight(), 1, 1000);
		cam.setParallelProjection(false);
		cam.update();
	}

	protected void cameraParallel() {
		cam.setParallelProjection(true);
		float aspect = (float) display.getWidth() / display.getHeight();
		cam.setFrustum(-100, 1000, -50 * aspect, 50 * aspect, -50, 50);
		cam.update();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#quit()
	 */
	@Override
	protected void quit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#reinit()
	 */
	@Override
	protected void reinit() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#render(float)
	 */
	@Override
	protected void render(float interpolation) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#start()
	 */
	@Override
	public void start() {
		initSystem();
		assertDisplayCreated();
		initGame();
		updateThread = new UpdateThread(this, gameSemaphore, updateResolution);
		updateThread.start();
		renderThread = new RenderThread(this, gameSemaphore, renderResolution);
		renderThread.start();
	}

	/**
	 * Use this to stop rendering this game. This WON'T stop the update thread.
	 */
	public void stopRendering() {
		renderThread.setKeepRunning(false);
		renderThread = null;
	}

	/**
	 * Resume the rendering of this game.
	 */
	public void resumeRendering() {
		renderThread = new RenderThread(this, gameSemaphore, renderResolution);
		renderThread.start();
	}

	/**
	 * Resize the game. This method uses the actual values of the canvas to
	 * adjust the size of the renderer and the camera.
	 */
	public void resize() {
		display.setWidth(glCanvas.getSize().x);
		display.setHeight(glCanvas.getSize().y);
		display.getRenderer()
				.reinit(glCanvas.getSize().x, glCanvas.getSize().y);
		display.getRenderer().getCamera().setFrustumPerspective(
				45.0f,
				(float) DisplaySystem.getDisplaySystem().getRenderer()
						.getWidth()
						/ (float) DisplaySystem.getDisplaySystem()
								.getRenderer().getHeight(), 1, 1000);
		display.getRenderer().getCamera().update();
	}

	/**
	 * Make sure everything is created.
	 */
	protected void assertDisplayCreated() throws JmeException {
		if (display == null) {
			logger.severe("Display system is null.");

			throw new JmeException("Window must be created during"
					+ " initialization.");
		}
		if (!display.isCreated()) {
			logger.severe("Display system not initialized.");

			throw new JmeException("Window must be created during"
					+ " initialization.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme.app.AbstractGame#update(float)
	 */
	@Override
	protected void update(float interpolation) {
	}

	public void hideMouse() {
		glCanvas.setCursor(invisibleCursor);
	}

	public void showMouse() {
		glCanvas.setCursor(defaultCursor);
	}

	/**
	 * @return the rootNode
	 */
	public Node getRootNode() {
		return this.rootNode;
	}

	/**
	 * @return the glCanvas
	 */
	public GLCanvas getGlCanvas() {
		return this.glCanvas;
	}

	/**
	 * @return the displaySys
	 */
	public SWTDisplaySystem getDisplaySys() {
		return (SWTDisplaySystem) display;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the renderQueue
	 */
	public GameTaskQueue getRenderQueue() {
		return this.renderQueue;
	}

	/**
	 * @return the updateQueue
	 */
	public GameTaskQueue getUpdateQueue() {
		return this.updateQueue;
	}

	/**
	 * @return the parent
	 */
	public Composite getParent() {
		return this.parent;
	}

	/**
	 * @param parent
	 *            the parent to set
	 */
	public void setParent(Composite parent) {
		this.parent = parent;
	}

}
