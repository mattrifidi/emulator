package org.rifidi.utilities.editor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.rifidi.jmonkey.SWTDisplaySystem;
import org.rifidi.jmonkey.SWTKeyInput;
import org.rifidi.jmonkey.SWTMouseInput;
import org.rifidi.utilities.text.TextOverlay;

import com.jme.input.KeyInput;
import com.jme.input.MouseInput;
import com.jme.renderer.ColorRGBA;
import com.jme.system.DisplaySystem;

public abstract class JmeEditor extends EditorPart {
	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(JmeEditor.class);
	/**
	 * Flag indicating whether a JmeEditor has been initialized.
	 */
	private static boolean initialized = false;
	/**
	 * Listing of all the JmeEditors created.
	 */
	private static List<JmeEditor> editors = new ArrayList<JmeEditor>();
	/**
	 * The active editor.
	 */
	private static JmeEditor activeEditor;
	/**
	 * The JmeEditorContext for this editor.
	 */
	private JmeEditorContext jec;
	/**
	 * Indicates whether this editor is active.
	 */
	private boolean active;
	/**
	 * The display system for this editor.
	 */
	protected SWTDisplaySystem displaySys;
	/**
	 * The canvas this editor will be using to render stuff.
	 */
	protected GLCanvas canvas;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite, org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		setInput(input);
		setSite(site);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		// create the glcanvas for rendering
		Composite composite = new Composite(parent, SWT.None);
		composite.setLayout(new FillLayout());
		GLData data = new GLData();
		data.doubleBuffer = true;
		data.depthSize = 24;
		canvas = new GLCanvas(composite, SWT.NONE, data);
		canvas.setLayoutData( new GridData(SWT.FILL,SWT.FILL,true,true) );

		// Bind inputs
		if ( !initialized ) {
			MouseInput.setProvider( SWTMouseInput.class.getCanonicalName() );
			KeyInput.setProvider( SWTKeyInput.class.getCanonicalName() );

			initialized = true;
		}

		try {			// set up the display system
			displaySys = (SWTDisplaySystem) DisplaySystem.getDisplaySystem("SWTDISPLAYSYS");
			displaySys.setCurrentGLCanvas(canvas);
//			displaySys.createCanvasRenderer(1024, 768, ColorRGBA.lightGray);

		} catch (Error err) {
			logger.fatal("Error: Probably chosen wrong display system. " + err);
			throw new RuntimeException("Failed to instantiate SWTDisplaySystem");
		}

		// Add a resize listener to the canvas
		canvas.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Point canvasSize = canvas.getSize();
				logger.info("Resizing the renderer to: "+canvasSize.x+"x"+canvasSize.y);
				displaySys.getRenderer().reinit(canvasSize.x, canvasSize.y);
			}
		});

		editors.add(this);
		active = false;
		init();
	}

	public abstract void init();
	public abstract void activateMore();
	public abstract void deactivateMore();
	public abstract void disposeMore();

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		if ( !active ) {
			for ( JmeEditor e : editors ) {
				if ( e.isActive() )
					e.deactivate();
			}
			activate();
		}
	}

	/**
	 * @return true if the editor is currently active, false otherwise
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * Activates this editor
	 */
	public void activate() {
		logger.debug("activating JmeEditor with input "+this.getEditorInput());

		canvas.addMouseListener((SWTMouseInput) MouseInput.get());
		canvas.addMouseMoveListener((SWTMouseInput) MouseInput.get());
		canvas.addKeyListener((SWTKeyInput) KeyInput.get());

		jec.activate();
		activateMore();
		active = true;
		activeEditor = this;
	}

	/**
	 * Deactivates the editor
	 */
	public void deactivate() {
		logger.debug("deactivating JmeEditor with input "+this.getEditorInput());

		canvas.removeMouseListener((SWTMouseInput) MouseInput.get());
		canvas.removeMouseMoveListener((SWTMouseInput) MouseInput.get());
		canvas.removeKeyListener((SWTKeyInput) KeyInput.get());

		jec.deactivate();
		deactivateMore();
		active = false;
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		System.out.println("disposing");
		disposeMore();
		editors.remove(this);
	}

	/**
	 * @return the jmeEditorContext
	 */
	public JmeEditorContext getJmeEditorContext() {
		return jec;
	}

	/**
	 * @param jec the jec to set
	 */
	public void setJmeEditorContext(JmeEditorContext jec) {
		this.jec = jec;
	}

	/**
	 * Returns the active jmeeditor.
	 * @return the active jmeeditor
	 */
	public static JmeEditor getActiveEditor() {
		return activeEditor;
	}
}