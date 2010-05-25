/*
 *  MiniMapView.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project 
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.views.minimapview;

import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.designer.rcp.game.DesignerGame;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

import com.jme.math.Vector2f;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;

/**
 * Minimap that displays an eagle eye rendering of the map currently displayed.
 * 
 * @author Jochen Mader Nov 4, 2007
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class MiniMapView extends ViewPart {
	/** Eclipse ID */
	public static String ID = "org.rifidi.designer.rcp.views.MiniMapView";
	/** Camera used to render the map. */
	private Camera mapCamera;
	/** Map image data. */
	private ImageData imageData;
	/** Reusable image object. */
	private Image image;
	/** Image for the canvas background. */
	private Image bgImage;
	/** Slider to control zoom. */
	private Scale scale;
	/** Used for dragging. */
	private boolean mousedown = false;
	/** true if the view is disposed. */
	private boolean disposed = false;
	/** Minimap Canvas. */
	private Canvas canvas;
	/** Implementationof the designer functionality. */
	private DesignerGame implementor;

	private WaitingCallable waitingCallable;

	/**
	 * Constructor.
	 */
	public MiniMapView() {
		ServiceRegistry.getInstance().service(this);
		waitingCallable = new WaitingCallable();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());
		Composite comp = new Composite(parent, SWT.NONE);
		GridLayout griddy = new GridLayout();
		griddy.numColumns = 1;
		comp.setLayout(griddy);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		// label = new Label(comp, SWT.CENTER|SWT.BORDER);
		// label.setLayoutData(gridData);
		canvas = new Canvas(comp, SWT.BORDER);
		canvas.setLayoutData(gridData);
		canvas.setForeground(new Color(null, 255, 0, 0));
		image = new Image(Display.getCurrent(), 16, 16);
		bgImage = new Image(Display.getCurrent(), 16, 16);
		canvas.addControlListener(new ControlListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse
			 * .swt.events.ControlEvent)
			 */
			@Override
			public void controlMoved(ControlEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse
			 * .swt.events.ControlEvent)
			 */
			@Override
			public void controlResized(ControlEvent e) {
				drawImage();
			}

		});
		canvas.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
				centerOn(e.x - 15, e.y - 15);
				mousedown = true;
			}

			public void mouseUp(MouseEvent e) {
				mousedown = false;
			}
		});

		canvas.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (mousedown)
					centerOn(e.x - 15, e.y - 15);
			}
		});
	}

	/**
	 * Centers the camera on the minimap coordinates given.
	 * 
	 * @param x
	 *            minimap x coordinate to center on
	 * @param y
	 *            minimap y coordinate to center on
	 */
	private void centerOn(int x, int y) {
		if (imageData != null
				&& waitingCallable.running.compareAndSet(false, true)) {
			int deltaX = (canvas.getSize().x - imageData.width) / 2;
			int deltaY = (canvas.getSize().y - imageData.height) / 2;

			Vector3f coords0 = mapCamera.getWorldCoordinates(new Vector2f(x
					- deltaX, 200 - y + deltaY), 0);
			Vector3f coords1 = mapCamera.getWorldCoordinates(new Vector2f(x
					- deltaX, 200 - y + deltaY), 1);

			Vector3f direction = coords0.subtract(coords1).normalizeLocal();

			Vector3f ground1 = coords0.subtract(direction.mult(coords0.y
					/ direction.y));

			coords0 = implementor.getCamera().getWorldCoordinates(
					new Vector2f(implementor.getCanvas().getSize().x / 2,
							implementor.getCanvas().getSize().y / 2), 0);
			coords1 = implementor.getCamera().getWorldCoordinates(
					new Vector2f(implementor.getCanvas().getSize().y / 2,
							implementor.getCanvas().getSize().y / 2), 1);
			direction = coords0.subtract(coords1).normalizeLocal();

			Vector3f ground2 = coords0.subtract(direction.mult(coords0.y
					/ direction.y));

			// scale the direction out to the ground plane
			waitingCallable.pos = ground1.subtract(ground2);
			implementor.render(waitingCallable);
		}
	}

	/**
	 * Sets the image/imagedata for the minimap and updates the frame.
	 * 
	 * @param img
	 *            the minimap image data
	 */
	public void setImage(ImageData img, int size) {
		imageData = img;
		drawImage();
	}

	protected void drawImage() {
		if (imageData != null) {
			int deltaX = (canvas.getSize().x - imageData.width) / 2;
			int deltaY = (canvas.getSize().y - imageData.height) / 2;
			image.dispose();
			image = new Image(Display.getCurrent(), imageData);
			bgImage.dispose();
			bgImage = new Image(Display.getCurrent(), canvas.getSize().x,
					canvas.getSize().y);
			GC gc = new GC(bgImage);
			gc.drawImage(image, deltaX, deltaY);
			gc.setLineWidth(3);
			gc
					.setForeground(Display.getCurrent().getSystemColor(
							SWT.COLOR_RED));

			Vector3f coords0 = implementor.getCamera().getWorldCoordinates(
					new Vector2f(implementor.getCanvas().getSize().x / 2,
							implementor.getCanvas().getSize().y / 2), 0);
			Vector3f coords1 = implementor.getCamera().getWorldCoordinates(
					new Vector2f(implementor.getCanvas().getSize().x / 2,
							implementor.getCanvas().getSize().y / 2), 1);
			Vector3f direction = coords0.subtract(coords1).normalizeLocal();
			coords0.subtractLocal(direction.mult(coords0.y / direction.y));
			Vector3f coords = mapCamera.getScreenCoordinates(coords0);
			gc.drawRectangle(deltaX + (int) coords.x - 25, 200 + deltaY
					- (int) coords.y - 25, 50, 50);

			canvas.setBackgroundImage(bgImage);
			gc.dispose();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// label.setFocus();
	}

	/**
	 * @param mapCamera
	 *            the mapCamera to set
	 */
	public void setMapCamera(Camera mapCamera) {
		this.mapCamera = mapCamera;
	}

	/**
	 * Get the current zoom value.
	 * 
	 * @return
	 */
	public int getZoom() {
		return scale.getSelection();
	}

	/**
	 * @return the disposed
	 */
	public boolean isDisposed() {
		return this.disposed;
	}

	/**
	 * @param implementor
	 *            the implementor to set
	 */
	@Inject
	public void setImplementor(DesignerGame implementor) {
		this.implementor = implementor;
	}

	/**
	 * Callable for moving the camera.
	 * 
	 * 
	 * @author Jochen Mader - jochen@pramari.com - Jan 12, 2009
	 * 
	 */
	private class WaitingCallable implements Callable<Object> {
		/** Prevent the callabel from being called more than once. */
		public AtomicBoolean running = new AtomicBoolean(false);
		/** Vector that should be added to the camera position */
		public Vector3f pos;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Object call() throws Exception {
			implementor.getRenderer().getCamera().getLocation().addLocal(pos);
			running.compareAndSet(true, false);
			return null;
		}

	}

}