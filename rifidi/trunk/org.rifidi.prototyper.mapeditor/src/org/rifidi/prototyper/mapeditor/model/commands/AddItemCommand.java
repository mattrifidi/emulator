/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AddItemCommand extends Command {

	private ItemPart itemToAdd;
	private ItemPart itemContainer;

	/**
	 * @param itemToAdd
	 * @param itemContainer
	 */
	public AddItemCommand(ItemPart itemToAdd, ItemPart itemContainer) {
		this.itemToAdd = itemToAdd;
		this.itemContainer = itemContainer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return itemContainer.getModelElement().isContainer();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		itemContainer.getModelElement().addContainedItem(
				itemToAdd.getModelElement());
	}

}
