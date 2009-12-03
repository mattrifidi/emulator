/**
 * 
 */
package org.rifidi.prototyper.map.collision;

import java.util.HashMap;

import org.rifidi.prototyper.map.view.figures.HotspotFigure;
import org.rifidi.prototyper.map.view.figures.ItemFigure;
import org.rifidi.prototyper.map.view.layers.HotspotLayer;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.console.service.ConsoleService;

/**
 * The CollisionManager calculates and keeps track of which ItemFigures have
 * intersected with which Hotspots.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class CollisionManager {

	/** The hotspot layer */
	private final HotspotLayer hsLayer;
	/** A service to log output to the console */
	private ConsoleService service;
	/** map that keeps track of which hotspots figures are at */
	private final HashMap<ItemFigure, HotspotFigure> itemsToFigures;
	/** The name of the console to display collisions at */
	private final String CONSOLE_NAME = "Collisions";

	/**
	 * Constructor
	 * 
	 * @param hsLayer
	 *            The hotspot layer to watch
	 */
	public CollisionManager(HotspotLayer hsLayer) {
		super();
		this.hsLayer = hsLayer;
		itemsToFigures = new HashMap<ItemFigure, HotspotFigure>();
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * This method should be called whenever an item is moved. The collision
	 * manager will figure out if the given item has collided with a hotspot
	 * 
	 * The current implementation of this method uses a brute force method. It
	 * looks at all hotspots within the hotspot layer and asks each one if there
	 * is a collision. This is ok for a small number of hotspots, but it
	 * probably not sufficient for a large number.
	 * 
	 * @param item
	 *            The item to check. Should not be null.
	 */
	public void itemMoved(ItemFigure item) {
		if (item == null)
			return;
		for (Object o : hsLayer.getChildren()) {
			if (o instanceof HotspotFigure) {
				HotspotFigure newHS = (HotspotFigure) o;
				if (newHS.intersects(item.getBounds())) {

					HotspotFigure curHS = itemsToFigures.get(item);

					if (curHS == newHS) {
						return;
					} else if (curHS == null) {
						handleCollision(item, newHS);
						return;
					} else if (curHS != newHS) {
						removeCollision(item);
						handleCollision(item, newHS);
						return;
					}

				}
			}
		}
		removeCollision(item);
	}

	/**
	 * A helper method that does the work of handling a collision.
	 * 
	 * @param item
	 *            The item that moved to a hotspot
	 * @param hotspot
	 *            The hotspot into which an item moved.
	 */
	private void handleCollision(ItemFigure item, HotspotFigure hotspot) {
		itemsToFigures.put(item, hotspot);
		String s = item + " Arrived at " + hotspot + "\n";
		service.write(CONSOLE_NAME, s);
		hotspot.itemArrived(item);
	}

	/**
	 * A helper method that does the work of handling the event when an items
	 * moves from the bounds of a hotspot
	 * 
	 * @param item
	 */
	private void removeCollision(ItemFigure item) {
		HotspotFigure hotspot = itemsToFigures.remove(item);
		if (hotspot != null) {
			String s = item + " Departed from " + hotspot + "\n";
			service.write(CONSOLE_NAME, s);
			hotspot.itemDeparted(item);
		}
	}

	/**
	 * Used to inject the console service
	 */
	@Inject
	public void setConsoleService(ConsoleService service) {
		this.service = service;
		this.service.createConsole(CONSOLE_NAME);
	}

}
