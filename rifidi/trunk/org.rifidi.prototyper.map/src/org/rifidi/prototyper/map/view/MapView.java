/**
 * 
 */
package org.rifidi.prototyper.map.view;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.ScalableLayeredPane;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.emulator.readerview.support.ReaderDNDSupport;
import org.rifidi.prototyper.items.model.ItemDNDSupport;
import org.rifidi.prototyper.items.model.TaggedItem;
import org.rifidi.prototyper.items.service.ItemService;
import org.rifidi.prototyper.map.collision.CollisionManager;
import org.rifidi.prototyper.map.controller.EditModeListener;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;
import org.rifidi.prototyper.map.view.layers.FloorplanLayer;
import org.rifidi.prototyper.map.view.layers.HotspotLayer;
import org.rifidi.prototyper.map.view.layers.ItemLayer;
import org.rifidi.prototyper.map.view.mousehandler.MapViewMouseHandler;
import org.rifidi.prototyper.model.HotspotViewModel;
import org.rifidi.prototyper.model.ItemViewModel;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * The main view. Displays the map and accepts DND operations.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapView extends ViewPart implements DropTargetListener,
		EditModeListener {

	/** The ID for this view */
	public static final String VIEW_ID = "org.rifidi.prototyper.map.view";
	/** The floorplan layer */
	private FloorplanLayer fplayer = null;
	/** The hotspot layer */
	private HotspotLayer hslayer = null;
	/** The Item layer */
	private ItemLayer ilayer = null;
	/** The canvas that holds the three layers */
	private Canvas canvas;
	/** The main pane */
	private ScalableLayeredPane pane;
	/** An object that keeps track of item-hotspot collisions */
	private CollisionManager collisionManager;
	/** A handler for mouse and keyboard events. */
	private MapViewMouseHandler mouseHandler;
	/** The service that keeps track of which items have been created */
	private ItemService itemService;
	/** The current state of edit mode. */
	private boolean editMode = false;
	/** The logger for this class */
	private final static Log logger = LogFactory.getLog(MapView.class);

	/**
	 * 
	 */
	public MapView() {
		super();
		ServiceRegistry.getInstance().service(this);
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
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());

		canvas = new FigureCanvas(composite, SWT.None);

		LightweightSystem lws = new LightweightSystem(canvas);

		ScalableLayeredPane pane = new ScalableLayeredPane();
		fplayer = new FloorplanLayer(ViewModelSingleton.getInstance()
				.getFloorplan());
		hslayer = new HotspotLayer(ViewModelSingleton.getInstance()
				.getHotspots());
		ilayer = new ItemLayer(ViewModelSingleton.getInstance().getItems());

		pane.add(fplayer, 0);
		pane.add(hslayer, 1);
		pane.add(ilayer, 2);

		collisionManager = new CollisionManager(hslayer);
		mouseHandler = new MapViewMouseHandler(collisionManager, ilayer,
				hslayer);

		lws.setContents(pane);

		DropTarget t = new DropTarget(canvas, DND.DROP_COPY | DND.DROP_DEFAULT);
		t.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		t.addDropListener(this);

		pane.addMouseListener(mouseHandler);
		pane.addMouseMotionListener(mouseHandler);
		pane.addKeyListener(mouseHandler);
		pane.setFocusTraversable(true);
		ViewModelSingleton.getInstance().addListener(this);
		// TODO: hack. Should look up the edit mode state somehow.
		ViewModelSingleton.getInstance().setEditMode(true);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		super.dispose();
		// TODO: destroy layers here
		pane.removeMouseListener(mouseHandler);
		pane.removeMouseMotionListener(mouseHandler);
		ViewModelSingleton.getInstance().removeListener(this);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragEnter(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void dragEnter(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragLeave(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void dragLeave(DropTargetEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dragOperationChanged(org.eclipse
	 * .swt.dnd.DropTargetEvent)
	 */
	@Override
	public void dragOperationChanged(DropTargetEvent event) {
		if (event.detail == DND.DROP_DEFAULT) {
			if ((event.operations & DND.DROP_COPY) != 0) {
				event.detail = DND.DROP_COPY;
			} else {
				event.detail = DND.DROP_NONE;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.dnd.DropTargetListener#dragOver(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void dragOver(DropTargetEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.dnd.DropTargetListener#drop(org.eclipse.swt.dnd.
	 * DropTargetEvent)
	 */
	@Override
	public void drop(DropTargetEvent event) {
		if (!TextTransfer.getInstance().isSupportedType(event.currentDataType)) {
			return;
		}
		String textTransfer = (String) event.data;
		Point p = event.display.map(null, canvas, event.x, event.y);
		if (editMode && ReaderDNDSupport.isReaderDND((String) event.data)) {
			logger.debug("ADD Hotspot: " + textTransfer + p);
			HotspotViewModel model = new HotspotViewModel();
			model.setMinimumHeight(100);
			model.setMinimumWidth(100);
			model.setName(ReaderDNDSupport.getReaderID(textTransfer));
			model.setAntennaID(ReaderDNDSupport.getAntennaID(textTransfer));
			model.setX(p.x);
			model.setY(p.y);
			ViewModelSingleton.getInstance().addHotspot(model);
		} else if (!editMode && ItemDNDSupport.isItem(textTransfer)) {
			TaggedItem ti = itemService.getItem(ItemDNDSupport
					.getItemID(textTransfer));
			if (ti != null) {
				logger.debug("ADD ITEM: " + ti.getName() + " " + p);
				ItemViewModel model = new ItemViewModel();
				model.setX(p.x);
				model.setY(p.y);
				model.setItemID(ti.getTag());
				model.setName(ti.getName());
				model.setImage(ti.getImage());
				ViewModelSingleton.getInstance().addItem(model);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DropTargetListener#dropAccept(org.eclipse.swt.dnd
	 * .DropTargetEvent)
	 */
	@Override
	public void dropAccept(DropTargetEvent event) {

	}

	@Inject
	public void setItemService(ItemService itemService) {
		this.itemService = itemService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.controller.EditModeListener#setEditMode(boolean
	 * )
	 */
	@Override
	public void setEditMode(boolean toggle) {
		this.editMode = toggle;
	}

}
