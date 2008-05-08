package org.rifidi.utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.jmonkey.SWTKeyInput;
import org.rifidi.jmonkey.SWTMouseInput;
import org.rifidi.utilities.jmecontext.JmeContext;

import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

public class BaseSWTGame {
	private static Log logger = LogFactory.getLog(BaseSWTGame.class);
	private SWTDisplaySystem displaySys;
	private JmeContext context;
	private GLCanvas canvas;
	private Display display;
	private Shell shell;

	public BaseSWTGame() {
		display = new Display();
		shell = new Shell(display);
//		shell.setSize(800,600);
		shell.setLayout(new GridLayout());

		// set up gl data
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.depthSize = 24;

		// create the gl canvas
		canvas = new GLCanvas(shell, SWT.BORDER, data);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.forceFocus();

		// bind inputs
		MouseInput.setProvider(SWTMouseInput.class.getCanonicalName());
		KeyInput.setProvider(SWTKeyInput.class.getCanonicalName());

		try { // set up the display system
			displaySys = (SWTDisplaySystem) DisplaySystem
					.getDisplaySystem("SWTDISPLAYSYS");
			displaySys.setCurrentGLCanvas(canvas);
			displaySys.createCanvasRenderer(1024, 768, ColorRGBA.lightGray);
			displaySys.switchContext(canvas);
		} catch (Error err) {
			logger.fatal("Error: Probably chosen wrong display system. " + err);
			throw new RuntimeException("Failed to instantiate SWTDisplaySystem");
		}

		// Add a resize listener to the canvas
		canvas.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point canvasSize = canvas.getSize();
				logger.info("Resizing the renderer to: " + canvasSize.x + "x"
						+ canvasSize.y);
				displaySys.getRenderer().reinit(canvasSize.x, canvasSize.y);
			}
		});
	}

	public GLCanvas getCanvas() {
		return canvas;
	}

	public void setJmeContext( JmeContext context ) {
		this.context = context;
	}

	public void start() {
		context.start();
		context.activate();


		shell.pack();
		shell.open();
		shell.setSize(1024,768);
		while ( !shell.isDisposed() ) {
			if ( !display.readAndDispatch() )
				display.sleep();
		}
		context.deactivate();
		context.terminate();
		display.dispose();
	}
}