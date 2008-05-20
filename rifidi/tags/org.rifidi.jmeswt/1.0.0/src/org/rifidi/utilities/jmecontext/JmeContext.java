package org.rifidi.utilities.jmecontext;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.utilities.ReoccurringCallable;
import org.rifidi.utilities.jmecontext.threads.RenderThread;
import org.rifidi.utilities.jmecontext.threads.UpdateThread;

import com.jme.renderer.Camera;
import com.jme.system.DisplaySystem;
import com.jme.util.GameTaskQueue;

public abstract class JmeContext {
	protected DisplaySystem displaySys;

	protected Camera camera;
	protected GLCanvas canvas;

	private ReentrantLock lock;
	private RenderThread renderThread;
	private UpdateThread updateThread;
	private GameTaskQueue renderQueue;
	private GameTaskQueue updateQueue;

	public JmeContext( GLCanvas canvas ) {
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
		renderThread.setActive(true);
		((SWTDisplaySystem)displaySys).switchContext(canvas);
		((SWTDisplaySystem)displaySys).setCurrentGLCanvas(canvas);
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
}