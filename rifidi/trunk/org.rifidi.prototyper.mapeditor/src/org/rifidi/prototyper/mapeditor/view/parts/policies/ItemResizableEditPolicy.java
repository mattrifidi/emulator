/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.parts.policies;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.requests.LocationRequest;
import org.rifidi.prototyper.mapeditor.view.MapScalableRootEditPart;
import org.rifidi.prototyper.mapeditor.view.parts.AbstractMapPart;
import org.rifidi.prototyper.mapeditor.view.parts.HotspotPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * This edit part is used by both hotspots and items to provide hover text and
 * to lock the edit parts according to edit mode.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemResizableEditPolicy extends ResizableEditPolicy {

	private RectangleFigure hoverFigure;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#showTargetFeedback(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public void showTargetFeedback(Request request) {
		if (REQ_SELECTION_HOVER.equals(request.getType())) {
			showHover(request);
		} else {
			super.showTargetFeedback(request);
		}
	}

	private void showHover(Request request) {
		LocationRequest locReq = (LocationRequest) request;
		String hoverText = ((AbstractMapPart<?>) getHost()).getHoverText();
		if (hoverText != null) {
			if (hoverFigure != null) {
				super.removeFeedback(hoverFigure);
			}
			hoverFigure = new RectangleFigure();
			FlowLayout layout = new FlowLayout(true);
			layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
			hoverFigure.setLayoutManager(layout);
			Label hoverLabel = new Label();
			hoverFigure.add(hoverLabel);
			hoverLabel.setText(hoverText);
			hoverLabel.setForegroundColor(ColorConstants.black);
			hoverFigure.setBackgroundColor(ColorConstants.tooltipBackground);
			hoverFigure.setBorder(new LineBorder(ColorConstants.black, 1));
			Point mouseLoc = new Point(locReq.getLocation().x + 10, locReq
					.getLocation().y);
			Dimension textDim = FigureUtilities.getTextExtents(hoverText,
					getHostFigure().getFont());
			hoverFigure.setBounds(new Rectangle(mouseLoc, new Dimension(
					textDim.width + 30, textDim.height + 10)));
			super.addFeedback(hoverFigure);

		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#eraseTargetFeedback(org
	 * .eclipse.gef.Request)
	 */
	@Override
	public void eraseTargetFeedback(Request request) {
		if (REQ_SELECTION_HOVER.equals(request.getType())) {
			if (hoverFigure != null) {
				super.removeFeedback(hoverFigure);
				hoverFigure = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#isDragAllowed()
	 */
	@Override
	public boolean isDragAllowed() {
		Boolean editMode = ((MapScalableRootEditPart) getHost().getRoot())
				.getEditMode();
		if (editMode && (getHost() instanceof HotspotPart)) {
			return true;
		}
		if (!editMode && (getHost() instanceof ItemPart)) {
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
	 */
	@Override
	protected List createSelectionHandles() {
		Boolean editMode = ((MapScalableRootEditPart) getHost().getRoot())
				.getEditMode();
		setResizeDirections(PositionConstants.NSEW);
		if (!editMode && (getHost() instanceof HotspotPart)) {
			setResizeDirections(PositionConstants.NONE);
		}
		if (editMode && (getHost() instanceof ItemPart)) {
			setResizeDirections(PositionConstants.NONE);
		}
		return super.createSelectionHandles();
	}

}
