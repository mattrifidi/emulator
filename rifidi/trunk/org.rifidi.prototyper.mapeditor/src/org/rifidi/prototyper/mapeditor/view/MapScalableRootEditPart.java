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
import org.rifidi.prototyper.mapeditor.view.parts.MapPart;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class MapScalableRootEditPart extends ScalableRootEditPart {

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

	public boolean getEditMode() {
		return editMode;
	}

	class FeedbackLayer extends Layer {
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
