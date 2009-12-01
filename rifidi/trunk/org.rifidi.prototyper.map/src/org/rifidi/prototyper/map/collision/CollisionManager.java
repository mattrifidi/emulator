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
	/***/
	private ConsoleService service;
	private final HashMap<ItemFigure, HotspotFigure> items;
	private final String CONSOLE_NAME = "Collisions";

	public CollisionManager(HotspotLayer hsLayer) {
		super();
		this.hsLayer = hsLayer;
		items = new HashMap<ItemFigure, HotspotFigure>();
		ServiceRegistry.getInstance().service(this);
	}

	public void itemMoved(ItemFigure item) {
		if (item == null)
			return;
		for (Object o : hsLayer.getChildren()) {
			if (o instanceof HotspotFigure) {
				HotspotFigure newHS = (HotspotFigure) o;
				if (newHS.intersects(item.getBounds())) {

					HotspotFigure curHS = items.get(item);

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

	private void handleCollision(ItemFigure item, HotspotFigure hotspot) {
		items.put(item, hotspot);
		String s = item + " Arrived at " + hotspot + "\n";
		service.write(CONSOLE_NAME, s);
		hotspot.itemArrived(item);
	}

	private void removeCollision(ItemFigure item) {
		HotspotFigure hotspot = items.remove(item);
		if (hotspot != null) {
			String s = item + " Departed from " + hotspot + "\n";
			service.write(CONSOLE_NAME, s);
			hotspot.itemDeparted(item);
		}
	}

	@Inject
	public void setConsoleService(ConsoleService service) {
		this.service = service;
		this.service.createConsole(CONSOLE_NAME);
	}

}
