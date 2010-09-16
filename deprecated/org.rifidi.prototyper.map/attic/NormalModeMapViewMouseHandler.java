/**
 * 
 */
package org.rifidi.prototyper.map.view.mousehandler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.KeyEvent;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.rifidi.prototyper.map.collision.CollisionManager;
import org.rifidi.prototyper.map.view.figures.ItemFigure;
import org.rifidi.prototyper.map.view.layers.ItemLayer;

/**
 * A class that uses the CTL key to form groups of Items
 * 
 * @author kyle
 * 
 */
public class NormalModeMapViewMouseHandler extends
		AbstractMapViewMouseHandlerState {

	private CollisionManager collistionManager;
	private ItemLayer itemLayer;
	private final Set<ItemFigure> selectedItems;
	private final Map<ItemFigure, Dimension> selectedItemOffsets;
	private boolean multiSelect = false;
	private ItemFigure currentItem;
	private ItemFigure hoverFigure;
	private boolean mouseButtonPushed = false;

	public NormalModeMapViewMouseHandler(CollisionManager collistionManager,
			ItemLayer itemLayer) {
		super();
		this.collistionManager = collistionManager;
		this.itemLayer = itemLayer;
		selectedItems = new HashSet<ItemFigure>();
		selectedItemOffsets = new HashMap<ItemFigure, Dimension>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mousePressed(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent me) {
		mouseButtonPushed = true;
		if (hoverFigure != null) {
			hoverFigure.hideHoverText();
		}

		ItemFigure item = getFigureAt(me.getLocation());

		if (item != null) {
			if (!multiSelect) {
				if (!selectedItems.contains(item)) {
					deselect(new HashSet<ItemFigure>(selectedItems));
				}
				select(item, me.getLocation());
			} else {
				select(item, me.getLocation());
			}
			currentItem = item;

		} else {
			deselect(new HashSet<ItemFigure>(selectedItems));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent me) {
		mouseButtonPushed = false;
	}

	private void deselect(ItemFigure itemFigure) {
		itemFigure.deselect();
		this.selectedItems.remove(itemFigure);
		this.selectedItemOffsets.remove(itemFigure);
	}

	private void deselect(Set<ItemFigure> itemFigures) {
		for (ItemFigure itemFigure : itemFigures) {
			deselect(itemFigure);
		}
	}

	private void select(ItemFigure itemFigure, Point location) {
		selectedItems.add(itemFigure);
		for (ItemFigure figure : selectedItems) {
			selectedItemOffsets.put(figure, figure.getLocation().getDifference(
					location));
		}
		itemFigure.select();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mouseDragged(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent me) {
		if (hoverFigure != null) {
			hoverFigure.hideHoverText();
		}

		for (ItemFigure selectedItem : selectedItems) {
			if (selectedItem != null) {
				selectedItem.move(me.getLocation().getTranslated(
						selectedItemOffsets.get(selectedItem)));
				collistionManager.itemMoved(selectedItem);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #keyPressed(org.eclipse.draw2d.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.keycode == KeyEvent.CONTROL) {
			this.multiSelect = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #keyReleased(org.eclipse.draw2d.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent ke) {
		if (ke.keycode == KeyEvent.CONTROL) {
			this.multiSelect = false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mouseHover(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseHover(MouseEvent me) {
		if (!mouseButtonPushed) {
			ItemFigure fig = getFigureAt(me.getLocation());
			fig.showHoverText();
			this.hoverFigure = fig;
		}
	}

	private ItemFigure getFigureAt(Point point) {
		IFigure figure = itemLayer.findFigureAt(point);
		ItemFigure item = null;
		while (figure != null && item == null) {
			if (figure instanceof ItemFigure) {
				item = (ItemFigure) figure;
			} else {
				figure = figure.getParent();
			}
		}
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.prototyper.map.view.mousehandler.AbstractMapViewMouseHandlerState
	 * #mouseMoved(org.eclipse.draw2d.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent me) {
		if (hoverFigure != null) {
			ItemFigure fig = getFigureAt(me.getLocation());
			if (fig != hoverFigure) {
				hoverFigure.hideHoverText();
			}
		}
	}

}
