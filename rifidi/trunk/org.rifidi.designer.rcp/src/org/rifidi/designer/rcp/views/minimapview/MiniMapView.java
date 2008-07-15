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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.designer.rcp.GlobalProperties;
import org.rifidi.designer.rcp.views.view3d.listeners.ZoomMouseWheelListener;
import org.rifidi.designer.services.core.camera.CameraService;
import org.rifidi.designer.services.core.camera.CameraServiceImpl;
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
	/**
	 * Eclipse ID
	 */
	public static String ID = "org.rifidi.designer.rcp.views.MiniMapView";
	/**
	 * Logger
	 */
	private static Log logger = LogFactory.getLog(MiniMapView.class);
	/**
	 * Camera used to render the map.
	 */
	private Camera mapCamera;
	/**
	 * Label used to display the map on.
	 */
	private Label label;
	/**
	 * Map image.
	 */
	private ImageData imageData;
	private Image image;
	/**
	 * Slider to control zoom.
	 */
	private Scale scale;
	/**
	 * Used for dragging.
	 */
	private boolean mousedown = false;
	/**
	 * Graphics context for manipulating the map image
	 */
	private GC graphicsContext;
	/**
	 * Reference to the camera service
	 */
	private CameraService cameraService;
	/**
	 * true if the view is disposed.
	 */
	private boolean disposed = false;

	private Updater updater;

	private int size = 0;

	/**
	 * Constructor.
	 */
	public MiniMapView() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
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
		GridData gridData2 = new GridData(GridData.FILL_HORIZONTAL);
		gridData2.grabExcessHorizontalSpace = true;
		label = new Label(comp, SWT.CENTER);
		label.setLayoutData(gridData);

		label.addMouseListener(new MouseListener() {
			public void mouseDoubleClick(MouseEvent e) {
			}

			public void mouseDown(MouseEvent e) {
				centerOn(e.x, e.y);
				mousedown = true;
			}

			public void mouseUp(MouseEvent e) {
				mousedown = false;
			}
		});

		label.addMouseMoveListener(new MouseMoveListener() {
			public void mouseMove(MouseEvent e) {
				if (mousedown)
					centerOn(e.x, e.y);
			}
		});
		label.addMouseWheelListener(new ZoomMouseWheelListener(cameraService));
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
		if (label.getImage() != null) {
			// determine where the user clicked on the map
			int width = label.getImage().getBounds().width;
			int height = label.getImage().getBounds().height;
			float X = x - ((label.getSize().x - width) * .5f);
			float Y = y - ((label.getSize().y - height) * .5f);
			Y = height - Y;

			// determine the direction to the point that was clicked
			Vector3f one = mapCamera.getWorldCoordinates(new Vector2f(X, Y), 1);
			Vector3f two = mapCamera.getWorldCoordinates(new Vector2f(X, Y), 2);
			Vector3f dir = one.subtract(two);

			// scale the direction out to the ground plane
			float scale = Math.abs(one.y / dir.y);
			dir.multLocal(scale);
			Vector3f pos = one.add(dir);
			cameraService.positionCamera(pos);
		}
	}

	/**
	 * Draw a frame on the minimap.
	 * 
	 * @param topleft
	 *            the coordinate of the top-left corner of the frame
	 * @param topright
	 *            the coordinate of the top-right corner of the frame
	 * @param bottomleft
	 *            the coordinate of the bottom-left corner of the frame
	 * @param bottomright
	 *            the coordinate of the bottom-right corner of the frame
	 */
	private void drawFrame() {
		Vector3f location = cameraService.getMainCamera().getLocation().clone();
		location=mapCamera.getScreenCoordinates(location);
		location.y=200-location.y;
		int delta = 20;
		Vector3f topleft = location.add(-3 + delta, -3 + delta, 0);
		Vector3f topright = location.add(3 + delta, -3 + delta, 0);
		Vector3f bottomright = location.add(3 + delta, 3 + delta, 0);
		Vector3f bottomleft = location.add(-3 + delta, 3 + delta, 0);
		
		int[] corners = new int[] { (int) topleft.x - 20, (int) topleft.y - 20,
				(int) topright.x + 20, (int) topright.y - 20,
				(int) topright.x + 20, (int) topright.y - 20,
				(int) bottomright.x + 20, (int) bottomright.y + 20,
				(int) bottomright.x + 20, (int) bottomright.y + 20,
				(int) bottomleft.x - 20, (int) bottomleft.y + 20,
				(int) bottomleft.x - 20, (int) bottomleft.y + 20,
				(int) topleft.x - 20, (int) topleft.y - 20 };
		if (GlobalProperties.windows) {
			graphicsContext.setLineWidth(2);
			graphicsContext.setForeground(new Color(null, 255, 0, 0));
			graphicsContext.setLineStyle(SWT.LINE_SOLID);
			graphicsContext.drawImage(image, 0, 0);
			graphicsContext.drawPolyline(corners);
			label.redraw();
		} else {
			graphicsContext.dispose();
			image.dispose();
			image = new Image(Display.getCurrent(), imageData);
			graphicsContext = new GC(image);
			graphicsContext.drawImage(image, 0, 0);
			graphicsContext.setLineWidth(2);
			graphicsContext.setForeground(new Color(null, 255, 0, 0));
			graphicsContext.setLineStyle(SWT.LINE_SOLID);
			graphicsContext.drawPolyline(corners);
			label.setImage(image);
			label.redraw();
		}
	}

	/**
	 * Sets the image/imagedata for the minimap and updates the frame.
	 * 
	 * @param img
	 *            the minimap image data
	 */
	public void setImage(ImageData img, int size) {
		this.size = size;
		if (imageData == null) {
			imageData = img;
			image = new Image(Display.getCurrent(), img);
			graphicsContext = new GC(image);
			label.setImage(image);
		} else if (!imageData.equals(img)) {
			imageData = img;
			image.dispose();
			graphicsContext.dispose();
			image = new Image(Display.getCurrent(), img);
			graphicsContext = new GC(image);
			label.setImage(image);
		}
		drawFrame();
		if (!GlobalProperties.windows && updater == null) {
			updater = new Updater();
			updater.start();
		}
	}

	/**
	 * Converts the given world coordinates to map relative coordinates.
	 * 
	 * @param worldCoord
	 *            a coordinate in the map's world
	 * @return coordinates of the given point, relative to the map's coordinate
	 *         system
	 */
	public Vector2f convertToMap(Vector3f worldCoord) {
		Vector3f result = mapCamera.getScreenCoordinates(worldCoord);
		return new Vector2f(result.x, image.getBounds().height - result.y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		label.setFocus();
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
	 * @param cameraService
	 *            the cameraService to set
	 */
	public void setCameraService(CameraServiceImpl cameraService) {
		this.cameraService = cameraService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		super.dispose();
		if (updater != null) {
			updater.setKeepRunning(false);
		}
		disposed = true;
	}

	/**
	 * @return the disposed
	 */
	public boolean isDisposed() {
		return this.disposed;
	}

	/**
	 * @param cameraService
	 *            the cameraService to set
	 */
	@Inject
	public void setCameraService(CameraService cameraService) {
		this.cameraService = cameraService;
	}

	private class Updater extends Thread {
		private boolean running = false;
		private boolean keepRunning = true;

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Thread#run()
		 */
		@Override
		public void run() {
			while (keepRunning) {
				getViewSite().getShell().getDisplay().syncExec(new Runnable() {

					/*
					 * (non-Javadoc)
					 * 
					 * @see java.lang.Runnable#run()
					 */
					@Override
					public void run() {
						try {
							drawFrame();
							running = false;
						} catch (SWTException e) {
							// should just occur if the widget is
							// disposed faster than we can shoot
							// down the thread.
							logger.debug(e);
						}
					}
				});
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		/**
		 * @param keepRunning
		 *            the keepRunning to set
		 */
		public void setKeepRunning(boolean keepRunning) {
			this.keepRunning = keepRunning;
		}

	}

}