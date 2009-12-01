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

public class ItemFigure extends RectangleFigure {

	private ItemViewModel model;
	private Label label;
	private boolean showingHoverText = false;

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

	public String getName() {
		return model.getName();
	}

	public String getID() {
		return model.getItemID();
	}

	public Object getConstraint() {
		int width = Math.max(getPreferredSize().width + 5,
				getMinimumSize().width);
		int height = Math.max(getPreferredSize().height,
				getMinimumSize().height);
		return new Rectangle(getLocation(), new Dimension(width, height));
	}

	public void select() {
		setAlpha(150);
		setBackgroundColor(ColorConstants.red);
		// label.setOpaque(false);
	}

	public void deselect() {
		setAlpha(0);
		// label.setOpaque(true);
	}

	public void showHoverText() {
		add(label);
		showingHoverText = true;
		this.setValid(false);
		this.getParent().getLayoutManager()
				.setConstraint(this, getConstraint());
		this.getParent().repaint();
	}

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
	
	public void dispose(){
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
