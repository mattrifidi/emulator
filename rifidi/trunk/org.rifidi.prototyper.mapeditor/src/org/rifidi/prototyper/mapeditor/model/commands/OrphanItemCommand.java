/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.rifidi.prototyper.mapeditor.view.parts.ItemLayerPart;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class OrphanItemCommand extends Command {

	private ItemLayerPart layer;
	private ItemPart itemToOrphan;

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
