package org.rifidi.utilities.editor;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.utilities.ReoccurringCallable;
import org.rifidi.utilities.editor.threads.RenderThread;
import org.rifidi.utilities.editor.threads.UpdateThread;

import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;

public abstract class JmeEditorContext {
	/**
	 * The display system for this context.
	 */
	protected DisplaySystem displaySys;
	/**
	 * The renderer this context uses.
	 */
	protected Renderer renderer;
	/**
	 * The camera this context uses.
	 */
	protected Camera camera;
	/**
	 * The canvas this context renders to.
	 */
	protected GLCanvas canvas;
	/**
	 * Lock for render/update threads.
	 */
	private ReentrantLock lock;
	/**
	 * The rendering thread.
	 */
	private RenderThread renderThread;
	/**
	 * The updating thread.
	 */
	private UpdateThread updateThread;
	/**
	 * Queue for rendering events.
	 */
	private GameTaskQueue renderQueue;
	/**
	 * Queue for updating events.
	 */
	private GameTaskQueue updateQueue;

	/**
	 * Main constructor.
	 * @param canvas the canvas this context will render to
	 */
	public JmeEditorContext( GLCanvas canvas ) {
		this.canvas = canvas;

		renderQueue = new GameTaskQueue();
		updateQueue = new GameTaskQueue();
		lock = new ReentrantLock();
		renderThread = new RenderThread(lock,canvas,renderQueue,Display.getCurrent());
		updateThread = new UpdateThread(lock,canvas,updateQueue);
		displaySys = DisplaySystem.getDisplaySystem();

		// allow update to be called every update call
		new ReoccurringCallable( updateQueue, new Callable<Object>() {
			public Object call() throws Exception {
				update();
				return null;
			}
		} );

		// allow render to be called on every render call
		new ReoccurringCallable( renderQueue, new Callable<Object>() {
			public Object call() throws Exception {
				render();
				return null;
			}
		} );
	}

	public abstract void init();
	public abstract void update();
	public abstract void render();
	public abstract void activateMore();
	public abstract void deactivateMore();

	public void start() {
		init();
		updateThread.start();
		renderThread.start();
	}

	public void terminate() /*throws InterruptedException*/ {
		renderThread.terminate();
//			renderThread.join();
		updateThread.terminate();
//			updateThread.join();
	}

	/**
	 * Activates this jmeContext
	 */
	public void activate() {
		lock.lock();
		canvas.forceFocus();
		if ( !displaySys.getRenderer().equals(renderer) )
			displaySys.setRenderer(renderer);
		((SWTDisplaySystem)displaySys).switchContext(canvas);
		((SWTDisplaySystem)displaySys).setCurrentGLCanvas(canvas);
		renderThread.setActive(true);
		activateMore();
		lock.unlock();
	}

	/**
	 * Deactivates this jmeContext
	 */
	public void deactivate() {
		lock.lock();
		renderThread.setActive(false);
		deactivateMore();
		lock.unlock();
	}

	/**
	 * @return the renderQueue
	 */
	public GameTaskQueue getRenderQueue() {
		return renderQueue;
	}

	/**
	 * @return the updateQueue
	 */
	public GameTaskQueue getUpdateQueue() {
		return updateQueue;
	}

	/**
	 * Sets the renderer to use for this context.
	 * @param r the renderer to use
	 */
	public void setRenderer( Renderer r ) {
		renderer = r;
	}
}