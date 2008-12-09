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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	/** Logger */
	private static Log logger = LogFactory.getLog(MiniMapView.class);
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
	/** Graphics context for manipulating the map image */
	private GC graphicsContext;
	/** true if the view is disposed. */
	private boolean disposed = false;
	/** Minimap Canvas. */
	private Canvas canvas;

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
			// determine where the user clicked on the map
			int width = bgImage.getBounds().width;
			int height = bgImage.getBounds().height;
			float X = x - ((width - image.getBounds().width) * .5f);
			float Y = y - ((height - image.getBounds().height) * .5f);
			Y = height - Y;

			// determine the direction to the point that was clicked
			Vector3f one = mapCamera.getWorldCoordinates(new Vector2f(X, Y), 1);
			Vector3f two = mapCamera.getWorldCoordinates(new Vector2f(X, Y), 2);
			Vector3f dir = one.subtract(two);

			// scale the direction out to the ground plane
			float scale = Math.abs(one.y / dir.y);
			dir.multLocal(scale);
			waitingCallable.pos = one.add(dir);
			implementor.render(waitingCallable);
		}
	}


	public Vector2f calcPos(Vector2f screenPos) {
		Vector3f coords = implementor.getCamera().getWorldCoordinates(
				new Vector2f(screenPos.x, screenPos.y), 0);
		Vector3f coords2 = implementor.getCamera().getWorldCoordinates(
				new Vector2f(screenPos.x, screenPos.y), 1);
		Vector3f direction = coords.subtract(coords2).normalizeLocal();
		coords.subtractLocal(direction.mult(coords.y / direction.y));
		coords.setY(0);

		Vector3f screencoords = mapCamera.getScreenCoordinates(coords);
		Vector2f ret = new Vector2f(screencoords.x, imageData.height
				- screencoords.y);
		if (ret.x > imageData.width - 1) {
			ret.x = imageData.width - 1;
		}
		if (ret.x < 0) {
			ret.x = 0;
		}
		if (ret.y > imageData.height - 1) {
			ret.y = imageData.height - 1;
		}
		if (ret.y < 0) {
			ret.y = 0;
		}
		return ret;
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
			image.dispose();
			image = new Image(Display.getCurrent(), imageData);
			bgImage.dispose();
			bgImage = new Image(Display.getCurrent(), canvas.getSize().x,
					canvas.getSize().y);
			GC gc = new GC(bgImage);
			gc.drawImage(image, (canvas.getSize().x - imageData.width) / 2,
					(canvas.getSize().y - imageData.height) / 2);
			//draw the frame
			Vector2f wLeftTop = calcPos(new Vector2f(0, implementor.getCanvas()
					.getSize().y));
			Vector2f center = calcPos(new Vector2f(implementor.getCanvas()
					.getSize().x / 2, implementor.getCanvas().getSize().y / 2));
			gc.drawRectangle((int) wLeftTop.x, (int) wLeftTop.y,
					(int) (center.x - wLeftTop.x) * 2,
					(int) (center.y - wLeftTop.y) * 2);
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

	private class WaitingCallable implements Callable<Object> {

		public AtomicBoolean running = new AtomicBoolean(false);
		public Vector3f pos;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.concurrent.Callable#call()
		 */
		@Override
		public Object call() throws Exception {
			implementor.getRenderer().getCamera().setLocation(pos);
			running.compareAndSet(true, false);
			return null;
		}

	}

}