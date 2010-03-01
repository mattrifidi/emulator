/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.rifidi.prototyper.mapeditor.model.ElementSet;
import org.rifidi.prototyper.mapeditor.model.ItemElement;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 *
 */
public class ItemInsertIntoContainerCommand extends Command {
	
	private ItemElement container;
	private ItemElement item;
	private ElementSet<ItemElement> itemLayer;
	
	/**
	 * @param container
	 * @param item
	 */
	public ItemInsertIntoContainerCommand(ItemElement container, ItemElement item, ElementSet<ItemElement> itemLayer) {
		super();
		this.container = container;
		this.item = item;
		this.itemLayer = itemLayer;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return super.canExecute();
	}
	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		itemLayer.removeElement(item);
		container.addContainedItem(item);
	}
	
}
