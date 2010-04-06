/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.rifidi.prototyper.mapeditor.model.ItemElement;

/**
 * Command to move an item
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemMoveCommand extends Command {

	/** The item to move */
	private ItemElement model;
	/** The request to move the item */
	private ChangeBoundsRequest request;
	/** The new bounds of the item */
	private Rectangle newBounds;

	/**
	 * @param model
	 * @param request
	 * @param newBounds
	 */
	public ItemMoveCommand(ItemElement model, ChangeBoundsRequest request,
			Rectangle newBounds) {
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
		model.setDimension(newBounds.getSize());
	}
}
