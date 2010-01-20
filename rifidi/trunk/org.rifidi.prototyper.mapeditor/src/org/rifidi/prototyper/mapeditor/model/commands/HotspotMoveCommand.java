/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.rifidi.prototyper.mapeditor.model.HotspotElement;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class HotspotMoveCommand extends Command {

	private HotspotElement model;
	private ChangeBoundsRequest request;
	private Rectangle newBounds;

	/**
	 * @param model
	 * @param request
	 * @param newBounds
	 */
	public HotspotMoveCommand(HotspotElement model,
			ChangeBoundsRequest request, Rectangle newBounds) {
		this.model = model;
		this.request = request;
		this.newBounds = newBounds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		Object type = request.getType();
		//if (RequestConstants.REQ_MOVE.equals(type)
		//		|| RequestConstants.REQ_MOVE_CHILDREN.equals(type)) {
		//	return true;
		//}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		model.setLocation(newBounds.getLocation());
		model.setSize(newBounds.getSize());
	}

}
