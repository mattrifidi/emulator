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
import org.eclipse.swt.SWT;
import org.rifidi.prototyper.map.collision.CollisionManager;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;
import org.rifidi.prototyper.map.view.figures.ItemFigure;
import org.rifidi.prototyper.map.view.layers.ItemLayer;

/**
 * The class that handles logic for selecting and moving Items around the map.
 * It uses a right-click action to group items
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class NormalModeMapViewMouseHandler extends
		AbstractMapViewMouseHandlerState {
	private CollisionManager collistionManager;
	private ItemLayer itemLayer;
	private final Set<ItemFigure> selectedItems;
	private final Map<ItemFigure, Dimension> selectedItemOffsets;
	private ItemFigure hoverFigure;
	private boolean mouseButtonPushed = false;
	private boolean shouldMove = false;

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
			hoverFigure = null;
		}

		ItemFigure item = getFigureAt(me.getLocation());

		shouldMove = true;

		// if left click
		if (me.button == 1) {
			// if there is exactly one item already selected
			if (selectedItems.size() == 1) {
				// if clicked on nothing, deselect the item
				if (item == null) {
					deselect(new HashSet<ItemFigure>(selectedItems));
				}
				// if clicked on an item
				else {
					// if the item is already selected, update the offset
					if (selectedItems.contains(item)) {
						updateOffsets(me.getLocation());
					}
					// otherwise deselect the old item and select the new one
					else {
						deselect(new HashSet<ItemFigure>(selectedItems));
						select(item, me.getLocation());
					}
				}
			}
			// if there are 0 or many items selected
			else {
				// if clicked on nothing, disable moving
				if (item == null) {
					shouldMove = false;
				}
				// if clicked on an item
				else {
					// if the item was already selected, update the offset
					if (selectedItems.contains(item)) {
						updateOffsets(me.getLocation());
					}
					// otherwise add it to selected set
					else {
						select(item, me.getLocation());
					}
				}
			}

		}
		// if right mouse button
		else if (me.button == 3) {
			// if clicked on an item
			if (item != null) {
				// if item was selected, deslect it
				if (selectedItems.contains(item)) {
					deselect(item);
				}
				// if item was not selected, select it
				else {
					select(item, me.getLocation());
				}
			}
			// never move on a right mouse click
			shouldMove = false;
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
			hoverFigure = null;
		}

		if (shouldMove) {
			for (ItemFigure selectedItem : selectedItems) {
				if (selectedItem != null) {
					selectedItem.move(me.getLocation().getTranslated(
							selectedItemOffsets.get(selectedItem)));
					collistionManager.itemMoved(selectedItem);
				}
			}
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
			if (fig != null) {
				fig.showHoverText();
				this.hoverFigure = fig;
			} else {
				this.hoverFigure = null;
			}
		}
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
				hoverFigure = null;
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
		if (hoverFigure != null) {
			hoverFigure.hideHoverText();
			hoverFigure = null;
		}
		if (ke.character == SWT.DEL) {
			Set<ItemFigure> itemsToDelete = new HashSet<ItemFigure>(
					selectedItems);
			deselect(itemsToDelete);
			for (ItemFigure fig : itemsToDelete) {
				ViewModelSingleton.getInstance().removeItem(fig.getID());
			}
		}
	}

	/**
	 * A private helper method to deselect an item
	 * 
	 * @param itemFigure
	 */
	private void deselect(ItemFigure itemFigure) {
		itemFigure.deselect();
		this.selectedItems.remove(itemFigure);
		this.selectedItemOffsets.remove(itemFigure);
	}

	/**
	 * A private helper method to deslect a group of items
	 * 
	 * @param itemFigures
	 */
	private void deselect(Set<ItemFigure> itemFigures) {
		for (ItemFigure itemFigure : itemFigures) {
			deselect(itemFigure);
		}
	}

	/**
	 * A private helper method to select an item
	 * 
	 * @param itemFigure
	 * @param location
	 */
	private void select(ItemFigure itemFigure, Point location) {
		selectedItems.add(itemFigure);
		updateOffsets(location);
		itemFigure.select();
	}

	/**
	 * A private helper method to update the current mouse location offsets from
	 * the items
	 * 
	 * @param location
	 */
	private void updateOffsets(Point location) {
		for (ItemFigure figure : selectedItems) {
			selectedItemOffsets.put(figure, figure.getLocation().getDifference(
					location));
		}
	}

	/**
	 * A private helper method to find an ItemFigure a particular point
	 * 
	 * @param point
	 * @return
	 */
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

}
