package org.rifidi.prototyper.map.view.figures;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.rifidi.prototyper.model.ItemViewModel;

/**
 * A class that extends Figure and wraps a ItemViewModel. Its purpose is to
 * display the model that it wraps.
 * 
 * This figure assumes that the parent has an XYLayout manager.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemFigure extends RectangleFigure {

	/** The wrapped model */
	private ItemViewModel model;
	/** The label to display */
	private Label label;
	/** Whether or not we are currently displaying the hover text */
	private boolean showingHoverText = false;

	/**
	 * Constructor
	 * 
	 * @param model
	 *            The wrapped model
	 */
	public ItemFigure(ItemViewModel model) {
		this.model = model;
		GridLayout layout = new GridLayout(1, false);
		setLayoutManager(layout);
		setAlpha(0);
		add(new ImageFigure(model.getImage()));
		label = new Label("Name: " + model.getName() + "\nID: "
				+ model.getItemID());
		label.setBackgroundColor(ColorConstants.lightGray);
		label.setOpaque(true);
		Font font = new Font(null, "Arial", 11, SWT.None);
		label.setFont(font);

		setLocation(new Point(model.getX(), model.getY()));
		setMinimumSize(new Dimension(10, 10));
	}

	/**
	 * Move this item to the given location.
	 * 
	 * @param toPoint
	 */
	public void move(Point toPoint) {
		Point p = this.getLocation();
		model.setX(toPoint.x);
		model.setY(toPoint.y);
		Dimension delta = toPoint.getDifference(p);
		translate(delta.width, delta.height);
		this.setValid(false);
		this.getParent().getLayoutManager()
				.setConstraint(this, getConstraint());
		this.getParent().repaint();
	}

	/**
	 * Get the name of this item
	 * 
	 * @return
	 */
	public String getName() {
		return model.getName();
	}

	/**
	 * Get the item ID.
	 * 
	 * TODO: this is sort of ugly since right now this item id is also the EPC.
	 * 
	 * @return
	 */
	public String getID() {
		return model.getItemID();
	}

	/**
	 * Get the layout contraints for this figure. Assumes the parent has an
	 * XYLayout Manager.
	 * 
	 * @return
	 */
	public Object getConstraint() {
		int width = Math.max(getPreferredSize().width + 5,
				getMinimumSize().width);
		int height = Math.max(getPreferredSize().height,
				getMinimumSize().height);
		return new Rectangle(getLocation(), new Dimension(width, height));
	}

	/**
	 * Change the effect when the item has been selected.
	 */
	public void select() {
		setAlpha(150);
		setBackgroundColor(ColorConstants.red);
	}

	/**
	 * Change the effect when the item has been deselected.
	 */
	public void deselect() {
		setAlpha(0);
	}

	/**
	 * Show extra information when the mouse is hovering
	 */
	public void showHoverText() {
		add(label);
		showingHoverText = true;
		this.setValid(false);
		this.getParent().getLayoutManager()
				.setConstraint(this, getConstraint());
		this.getParent().repaint();
	}

	/**
	 * Hide the extra information when the mouse is no longer hovering.
	 */
	public void hideHoverText() {
		if (showingHoverText) {
			remove(label);
			this.setValid(false);
			this.getParent().getLayoutManager().setConstraint(this,
					getConstraint());
			this.getParent().repaint();
			showingHoverText = false;
		}
	}

	/**
	 * Dispose.
	 */
	public void dispose() {
		model.getImage().dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Item : " + this.getName() + " (ID: " + this.getID() + ")";
	}

}
