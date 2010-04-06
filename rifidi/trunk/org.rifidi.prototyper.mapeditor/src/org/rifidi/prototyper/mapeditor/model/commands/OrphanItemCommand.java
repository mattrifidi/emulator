/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.rifidi.prototyper.mapeditor.view.parts.ItemLayerPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * This command is used to remove an item from it's parent layer.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class OrphanItemCommand extends Command {

	/** The layer to remove the item from */
	private ItemLayerPart layer;
	/** The item to remove */
	private ItemPart itemToOrphan;

	/**
	 * Constructor
	 * 
	 * @param itemToOrphan
	 * @param layer
	 */
	public OrphanItemCommand(ItemPart itemToOrphan, ItemLayerPart layer) {
		this.layer = layer;
		this.itemToOrphan = itemToOrphan;
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
		layer.getModelElement().removeElement(itemToOrphan.getModelElement());
	}

}
