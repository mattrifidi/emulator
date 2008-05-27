/*
 *  RenderThread.java
 *
 *  Project:		RiFidi IDE 2.0 - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.view3d.threads;

import java.nio.IntBuffer;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.rcp.GlobalProperties;
import org.rifidi.designer.rcp.views.minimapview.MiniMapView;
import org.rifidi.designer.services.core.messaging.FadingTextNode;
import org.rifidi.jmonkey.SWTDisplaySystem;

import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.scene.Node;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;
import com.jme.util.GameTaskQueueManager;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;

/**
 * This thread is responsible for rendering the scene to the different canvas'.
 * 
 * @author Jochen Mader
 * @author Dan West
 */
public class RenderThread extends Thread {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(RenderThread.class);
	/**
	 * Flag for thread termination.
	 */
	private boolean keepRunning = true;
	/**
	 * the canvas this thread renders to.
	 */
	private GLCanvas glCanvas;
	/**
	 * display to use for submitting callables into eclipse.
	 */
	private Display display;
	/**
	 * lock shared with update thread to avoid collisions.
	 */
	private ReentrantLock lock;
	/**
	 * The runnable used for rendering stuff.
	 */
	private RenderRunnable renderRunnable;
	/**
	 * Currently loaded scene.
	 */
	private SceneData sceneData;
	/**
	 * Is the rendering thread paused?
	 */
	private boolean running = false;

	private FadingTextNode fadingTextNode;
	/**
	 * Constructor.
	 * 
	 * @param locklock
	 *            shared with update thread.
	 * @param display
	 *            default eclipse display
	 * @param glCanvas
	 *            render target
	 * @param sceneData
	 *            currently loaded scene
	 * @param messagingService
	 *            current messaging service
	 */
	public RenderThread(ReentrantLock lock, Display display, GLCanvas glCanvas,
			SceneData sceneData, FadingTextNode fadingTextNode) {
		logger.debug("New RenderThread instantiated.");
		this.fadingTextNode=fadingTextNode;
		this.display = display;
		this.lock = lock;
		this.glCanvas = glCanvas;
		this.sceneData = sceneData;
		this.setName("RenderThread");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if (glCanvas == null) {
			throw new RuntimeException("glCanvas not set in RenderThread!");
		}
		if (sceneData == null) {
			throw new RuntimeException("sceneData not set in RenderThread!");
		}
		try {
			// create the runnable to loop on
			renderRunnable = new RenderRunnable(lock, fadingTextNode);

			// start the main render loop
			logger.debug("RenderThread started!");
			while (keepRunning && !glCanvas.isDisposed()) {
				if (!running) {
					running = true;
					display.asyncExec(renderRunnable);
				}
				try {
					sleep(40);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		} catch (SWTException e) {
			logger.debug(e);
		}
		logger.debug("RenderThread stopped");
	}

	/**
	 * @return the keepRunning
	 */
	public boolean isKeepRunning() {
		return keepRunning;
	}

	/**
	 * @param keepRunning
	 *            the keepRunning to set
	 */
	public void setKeepRunning(boolean keepRunning) {
		this.keepRunning = keepRunning;
	}

	/**
	 * The threaded object that executes the main body of the render thread.
	 * 
	 * @author Jochen Mader
	 * @author dan
	 */
	private class RenderRunnable implements Runnable {
		/**
		 * JMonkey display system.
		 */
		private SWTDisplaySystem displaySys;
		/**
		 * Reference to the minimap.
		 */
		private MiniMapView miniMapView;
		/**
		 * How often do we render the minimap.
		 */
		private int minimapCounter = 10;
		/**
		 * Offscreenrenderer for the minimap.
		 */
		private OffscreenRenderer offy;
		/**
		 * Node that combines the room and the scnee node.
		 */
		private Node compositeNode;
		/**
		 * Set to true after the first run of the runnable.
		 */
		private boolean firstRun = false;
		private FadingTextNode textNode;
		/**
		 * Constructor.
		 * 
		 * @param lock
		 *            shared lock
		 */
		public RenderRunnable(ReentrantLock lock, FadingTextNode fadingTextNode) {
			displaySys = (SWTDisplaySystem) DisplaySystem.getDisplaySystem();

			compositeNode = new Node();
			compositeNode.attachChild(sceneData.getRootNode());
			compositeNode.attachChild(sceneData.getRoomNode());
			this.textNode=fadingTextNode;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			lock.lock();
			if (offy == null) {
				offy = ((SWTDisplaySystem) DisplaySystem.getDisplaySystem())
						.createOffscreenRenderer(200, 200);
				if (offy.isSupported()) {
					offy.setBackgroundColor(new ColorRGBA(.667f, .667f, .851f,
							1f));
					offy.getCamera().setLocation(
							new Vector3f(sceneData.getWidth() / 2, 2, sceneData
									.getWidth() / 2));
					offy.getCamera()
							.setDirection(new Vector3f(0f, -1f, -.001f));
					offy.getCamera().setParallelProjection(true);
					offy.getCamera().setFrustum(-100.0f, 1000.0f,
							-.6f * sceneData.getWidth(),
							.6f * sceneData.getWidth(),
							-.6f * sceneData.getWidth(),
							.6f * sceneData.getWidth());
					EXTFramebufferObject.glBindFramebufferEXT(
							EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
				} else {
					logger.debug("Offscreen rendering is not supported!");
				}
			}
			if (!glCanvas.isDisposed()) {
				try {
					// checkandresize();
					// Execute queued render callables
					GameTaskQueueManager.getManager().getQueue(
							GameTaskQueue.RENDER).execute();

					displaySys.getRenderer().clearBuffers(); // clear screen

					// draw all scene elements
					// sceneData.getRootNode().updateRenderState();
					GameStateManager.getInstance().render(0);
					displaySys.getRenderer().draw(sceneData.getRootNode());

					// draw the room (separate because of renderqeue and
					// transparency)
					// sceneData.getRoomNode().updateRenderState
					displaySys.getRenderer().draw(sceneData.getRoomNode());

					if (GlobalProperties.physicsDebugging) {
						PhysicsDebugger.drawPhysics(
								sceneData.getPhysicsSpace(), displaySys
										.getRenderer());
					}
					if (GlobalProperties.boundingDebugging) {
						Debugger.drawBounds(sceneData.getRoomNode(), displaySys
								.getRenderer());
						Debugger.drawBounds(sceneData.getRootNode(), displaySys
								.getRenderer());
					}
					textNode.update(0);
					textNode.render(displaySys.getRenderer());
					if (firstRun) {
						displaySys.getRenderer().displayBackBuffer();
						glCanvas.swapBuffers();
					}
					if (miniMapView != null && miniMapView.isDisposed()) {
						miniMapView = null;
					}
					if (miniMapView == null) {
						miniMapView = (MiniMapView) PlatformUI.getWorkbench()
								.getActiveWorkbenchWindow().getActivePage()
								.findView(MiniMapView.ID);
						if (miniMapView != null) {

							firstRun = true;
							miniMapView.setMapCamera(offy.getCamera());
						}
					}
					if (offy.isSupported() && miniMapView != null
							&& minimapCounter == 10) {
						minimapCounter = 0;
						renderMinimap();
					}
					if (miniMapView != null) {
						minimapCounter++;
					}
					firstRun = true;
				} catch (SWTException swte) {
					// exception occurs if widget is
					// disposed
					logger.debug(swte);
				}
			}
			lock.unlock();
			running = false;
		}

		/**
		 * Helper method to render the minimap to an image and put it in the
		 * minimap view
		 * 
		 * @param rootNode
		 */
		ImageData imgData;

		private void renderMinimap() {
			offy.render(compositeNode);
			IntBuffer buffer = offy.getImageData();
			if (imgData == null) {
				imgData = new ImageData(200, 200, 32, new PaletteData(0xFF0000,
						0x00FF00, 0x0000FF));
			}
			for (int y = 0; y < 200; y++) {
				for (int x = 0; x < 200; x++) {
					imgData.setPixel(x, y, buffer.get((199 - y) * 200 + x));
				}
			}
			miniMapView.setImage(imgData);
		}
	}

}
