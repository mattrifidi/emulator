/*
 *  DesignerGame.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.game;

import java.nio.IntBuffer;
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.rifidi.designer.entities.SceneData;
import org.rifidi.designer.entities.SceneData.Direction;
import org.rifidi.designer.rcp.GlobalProperties;
import org.rifidi.designer.rcp.views.minimapview.MiniMapView;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.entities.SceneDataChangedListener;
import org.rifidi.designer.services.core.entities.SceneDataService;
import org.rifidi.jmeswt.SWTBaseGame;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.light.DirectionalLight;
import com.jme.light.LightNode;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.OffscreenRenderer;
import com.jme.renderer.Renderer;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.geom.Debugger;
import com.jmex.game.state.GameStateManager;
import com.jmex.physics.PhysicsDebugger;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 28, 2008
 * 
 */
public class DesignerGame extends SWTBaseGame implements
		SceneDataChangedListener {
	private static final Log logger = LogFactory.getLog(DesignerGame.class);
	/**
	 * Wall transparency. TODO redo trans stuff
	 */
	private AlphaState wallAlpha;
	/**
	 * Zbuffer for the rootnode.
	 */
	private ZBufferState zbufferState;
	/**
	 * Cullstate for the rootNode.
	 */
	private CullState cullState;
	/**
	 * The primary lightstate.
	 */
	private LightState ls;
	/**
	 * Currently used scenedata.
	 */
	private SceneData sceneData;
	/**
	 * Reference to the scenedataservice.
	 */
	private SceneDataService sceneDataService;
	/**
	 * Reference to the camera service.
	 */
	private CameraService cameraService;
	/**
	 * Offscreenrenderer for the minimap.
	 */
	private OffscreenRenderer offy;
	/**
	 * Reference to the minimap.
	 */
	private MiniMapView miniMapView;
	/**
	 * Display minimap every nth frame.
	 */
	private int minimapCounter = 10;
	/**
	 * ImageData for the minimap.
	 */
	private ImageData imgData;

	/**
	 * @param name
	 * @param updateResolution
	 * @param renderResolution
	 * @param width
	 * @param height
	 * @param parent
	 */
	public DesignerGame(String name, int updateResolution,
			int renderResolution, int width, int height, Composite parent) {
		super(name, updateResolution, renderResolution, width, height, parent,
				true);
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#simpleInitGame()
	 */
	@Override
	protected void simpleInitGame() {
		offy = ((SWTDisplaySystem) display).createOffscreenRenderer(200, 200);
		if (offy.isSupported()) {
			EXTFramebufferObject.glBindFramebufferEXT(
					EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		} else {
			logger.debug("Offscreen rendering is not supported!");
		}

		GameStateManager.create();
		// alphastate for transparent walls
		wallAlpha = DisplaySystem.getDisplaySystem().getRenderer()
				.createAlphaState();
		wallAlpha.setBlendEnabled(true);
		wallAlpha.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		wallAlpha.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		wallAlpha.setEnabled(true);
		// create ZBuffer stuff
		zbufferState = DisplaySystem.getDisplaySystem().getRenderer()
				.createZBufferState();
		zbufferState.setFunction(ZBufferState.CF_LEQUAL);
		zbufferState.setEnabled(true);

		// create cullstate for backface culling
		CullState cullState = DisplaySystem.getDisplaySystem().getRenderer()
				.createCullState();
		cullState.setCullMode(CullState.CS_BACK);

		// create a default light
		ls = DisplaySystem.getDisplaySystem().getRenderer().createLightState();
		LightNode lightNode = new LightNode("Head light", ls);
		DirectionalLight dl = new DirectionalLight();
		dl.setDiffuse(new ColorRGBA(1, 1, 1, 1));
		dl.setAmbient(new ColorRGBA(0.4f, 0.4f, 0.4f, 1));
		dl.setDirection(new Vector3f(0.1f, -1, 0.1f));
		dl.setEnabled(true);
		lightNode.setLight(dl);

		getRootNode().setRenderState(zbufferState);
		getRootNode().setRenderState(cullState);
		getRootNode().setRenderState(ls);
		getRootNode().updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#render(float)
	 */
	@Override
	protected void render(float interpolation) {
		super.render(interpolation);
		GameStateManager.getInstance().render(0);
		display.getRenderer().draw(sceneData.getRoomNode());
		if (GlobalProperties.physicsDebugging) {
			PhysicsDebugger.drawPhysics(sceneData.getPhysicsSpace(), display
					.getRenderer());
		}
		if (GlobalProperties.boundingDebugging) {
			Debugger.drawBounds(sceneData.getRoomNode(), display.getRenderer());
			Debugger.drawBounds(sceneData.getRootNode(), display.getRenderer());
		}
		if (miniMapView == null) {
			miniMapView = (MiniMapView) PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage().findView(
							MiniMapView.ID);
			miniMapView.setMapCamera(offy.getCamera());
		}
		if (offy.isSupported() && miniMapView != null && minimapCounter == 10) {
			minimapCounter = 0;
			offy.render(getRootNode());
			offy.render(sceneData.getRoomNode(),false);
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.jmeswt.SWTBaseGame#update(float)
	 */
	@Override
	protected void update(float interpolation) {
		// TODO Auto-generated method stub
		super.update(interpolation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.SceneDataChangedListener#destroySceneData(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void destroySceneData(SceneData sceneData) {
		getUpdateQueue().enqueue(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getRootNode().detachAllChildren();
				return null;
			}

		});
		// if (updateThread != null) {
		// stop();
		// updateThread.setKeepRunning(false);
		// try {
		// updateThread.join();
		// } catch (InterruptedException e) {
		// logger.debug("Got interrupted while joining on UpdateThread: "
		// + e);
		// }
		//
		// renderThread.setKeepRunning(false);
		// try {
		// renderThread.join();
		// } catch (InterruptedException e) {
		// logger.debug("Got interrupted while joining on RenderThread: "
		// + e);
		// }
		// GameStateManager.getInstance().cleanup();
		// }
	}

	/**
	 * Make walls (in)visible according to their position relative to the
	 * camera.
	 */
	public void showHideWalls() {
		// show all walls
		showWall(Direction.NORTH);
		showWall(Direction.SOUTH);
		showWall(Direction.EAST);
		showWall(Direction.WEST);
		// hide east/west wall
		if (cameraService.getMainCamera().getDirection().x > 0) {
			hideWall(Direction.WEST);
		} else if (cameraService.getMainCamera().getDirection().x < 0) {
			hideWall(Direction.EAST);
		}

		// hide north/south wall
		if (cameraService.getMainCamera().getDirection().z < 0) {
			hideWall(Direction.SOUTH);
		} else if (cameraService.getMainCamera().getDirection().z > 0) {
			hideWall(Direction.NORTH);
		}
	}

	/**
	 * @param dir
	 *            direction specifying which wall to hide
	 */
	public void hideWall(final Direction dir) {
		sceneDataService.getWalls().get(dir).setRenderState(wallAlpha);
		sceneDataService.getWalls().get(dir).setRenderQueueMode(
				Renderer.QUEUE_OPAQUE);
		sceneDataService.getWalls().get(dir).updateRenderState();
	}

	/**
	 * @param dir
	 *            direction specifying which wall to show
	 */
	public void showWall(Direction dir) {
		sceneDataService.getWalls().get(dir).clearRenderState(
				RenderState.RS_ALPHA);
		sceneDataService.getWalls().get(dir).setRenderQueueMode(
				Renderer.QUEUE_SKIP);
		sceneDataService.getWalls().get(dir).updateRenderState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.services.core.entities.SceneDataChangedListener#sceneDataChanged(org.rifidi.designer.entities.SceneData)
	 */
	@Override
	public void sceneDataChanged(SceneData sceneDataNew) {
		this.sceneData = sceneDataNew;
		getUpdateQueue().enqueue(new Callable<Object>() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see java.util.concurrent.Callable#call()
			 */
			@Override
			public Object call() throws Exception {
				getRootNode().attachChild(sceneData.getRootNode());
				getRootNode().updateRenderState();
				sceneData.getRoomNode().setRenderState(zbufferState);
				sceneData.getRoomNode().setRenderState(cullState);
				sceneData.getRoomNode().setRenderState(ls);
				// showHideWalls();
				hideWall(Direction.SOUTH);
				hideWall(Direction.EAST);
				sceneData.getRoomNode().updateRenderState();
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
				}
				return null;
			}

		});
	}

	/**
	 * @param sceneDataService
	 *            the sceneDataService to set
	 */
	@Inject
	public void setSceneDataService(SceneDataService sceneDataService) {
		this.sceneDataService = sceneDataService;
		sceneDataService.addSceneDataChangedListener(this);
	}

	/**
	 * @param cameraService
	 *            the cameraService to set
	 */
	@Inject
	public void setCameraService(CameraService cameraService) {
		this.cameraService = cameraService;
	}

}
