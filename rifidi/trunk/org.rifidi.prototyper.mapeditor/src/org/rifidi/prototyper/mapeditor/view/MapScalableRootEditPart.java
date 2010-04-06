/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Layer;
import org.eclipse.draw2d.ScalableLayeredPane;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableRootEditPart;

/**
 * This is the root EditPart for the Prototyper editor window.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapScalableRootEditPart extends ScalableRootEditPart {

	/** The state of the edit mode */
	private boolean editMode = false;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.ScalableRootEditPart#createScaledLayers()
	 */
	@Override
	protected ScalableLayeredPane createScaledLayers() {
		ScalableLayeredPane layers = new ScalableLayeredPane();
		layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
		layers.add(createGridLayer(), GRID_LAYER);
		layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
		return layers;
	}

	/**
	 * Change the state of edit mode
	 * 
	 * @param editMode
	 */
	public void setEditMode(boolean editMode) {
		if (this.editMode != editMode) {
			this.editMode = editMode;
			if (editMode) {
				getViewer().setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
			} else {
				getViewer()
						.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, false);
			}
		}
	}

	/**
	 * Return the state of edit mode
	 * 
	 * @return
	 */
	public boolean getEditMode() {
		return editMode;
	}

	/**
	 * Create a new feedback layer to use that overrides the get prefered size
	 * so that it fits the proper size.
	 * 
	 * @author Kyle Neumeier - kyle@pramari.com
	 * 
	 */
	class FeedbackLayer extends Layer {

		/**
		 * Constructor
		 */
		FeedbackLayer() {
			setEnabled(false);
		}

		/**
		 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
		 */
		public Dimension getPreferredSize(int wHint, int hHint) {
			Rectangle rect = new Rectangle();
			for (int i = 0; i < getChildren().size(); i++)
				rect.union(((IFigure) getChildren().get(i)).getBounds());
			return rect.getSize();
		}

	}
}
