package org.rifidi.jmonkey;

import java.util.ArrayList;
import java.util.List;

import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.renderer.lwjgl.LWJGLRenderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.Timer;

/**
 * This abstract class should be implemented by applications utilizing jMonkey
 * in eclipse. It takes care of initialization of the engine.
 * 
 * @author Jochen Mader
 * 
 */
public abstract class AbstractSwtGame {

	protected Timer timer;

	protected List<Canvas3d> canvasList = new ArrayList<Canvas3d>();

	protected Node rootNode;

	protected Renderer renderer;

	protected int width, height;

	private SWTDisplaySystem displaySys;
	
	private long startTime = 0;
	
	protected float tpf;

	private long fps = 0;
	/**
	 * Constructor
	 * 
	 * @param width
	 *            renderer width
	 * @param height
	 *            renderer height
	 * @param canvasList
	 *            List of precreated canvas'
	 */
	public AbstractSwtGame(int width, int height, List<Canvas3d> canvasList) {
		// get the right diaply system
		try {
			displaySys = (SWTDisplaySystem) DisplaySystem
					.getDisplaySystem("SWTDISPLAYSYS");
		} catch (Error err) {
			System.out.println("Error: Probably chosen wrong display system. "
					+ err);
		}
		this.width = width;
		this.height = height;
		this.canvasList = canvasList;
	}

	/**
	 * Init the whole system. Should be called AFTER everything is set up.
	 * 
	 */
	public void init() {
		doSystemSetup();
		doSceneGraphSetup();
		initCameras();
		startTime = System.currentTimeMillis() + 5000;
	}

	/**
	 * Setup the basic system. Create states ...
	 * 
	 */
	public void doSystemSetup() {

		displaySys.setCurrentGLCanvas(canvasList.get(0).canvas);
		renderer = new LWJGLRenderer(width, height);
		renderer.setHeadless(true);
		renderer.setBackgroundColor(ColorRGBA.black);
		displaySys.setRenderer(renderer);
		displaySys.updateStates(renderer);

		for (Canvas3d canvas : canvasList) {
			displaySys.switchContext(canvas.canvas);
		}
		timer = Timer.getTimer();
	}

	/**
	 * Implement this method to create the cameras for the separate displays.
	 * Each GLCanvas requires ONE camera!!
	 * 
	 */
	// TODO: add support for more than one camera
	public abstract void initCameras();

	/**
	 * Call this method to actually do the drawing. It loops through the list of
	 * displays and renders everytime the whole scenegraph (starting at the
	 * rootNode)
	 * 
	 */
	public void paint() {
		timer.update();
		/** Update tpf to time per frame according to the Timer. */
		tpf = timer.getTimePerFrame();
		System.out.println(timer.getResolution());
		
		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.UPDATE)
				.execute();

		GameTaskQueueManager.getManager().getQueue(GameTaskQueue.RENDER)
				.execute();

		for (Canvas3d canvas : canvasList) {
			displaySys.switchContext(canvas.canvas);
			displaySys.setCurrentGLCanvas(canvas.canvas);

			canvas.cameras.get(0).update();
			displaySys.getRenderer().setCamera(canvas.cameras.get(0));
			displaySys.getRenderer().clearBuffers();
			displaySys.getRenderer().draw(rootNode);
			displaySys.getRenderer().displayBackBuffer();
			canvas.canvas.swapBuffers();
		}
		
		if (startTime > System.currentTimeMillis()) {
			fps++;
		} else {
			long timeUsed = 5000 + (startTime - System.currentTimeMillis());
			startTime = System.currentTimeMillis() + 5000;
			System.out.println(fps + " frames in " + (timeUsed / 1000f)
					+ " seconds = " + (fps / (timeUsed / 1000f))
					+ " FPS (average)");
			fps = 0;
		}
	}

	/**
	 * Setup the scenegraph.
	 * Create a root node and add children.
	 *
	 */
	public abstract void doSceneGraphSetup();

	public abstract void doUpdate();
}
