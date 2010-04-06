/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.rifidi.prototyper.mapeditor.view.parts.ItemPart;

/**
 * This class is a command to add an item to a container item.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AddItemCommand extends Command {

	/**The Item to add*/
	private ItemPart itemToAdd;
	/**The part to add this */
	private ItemPart itemContainer;

	/**
	 * Constructor
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
