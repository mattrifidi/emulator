/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.rifidi.prototyper.mapeditor.model.ItemElement;
import org.rifidi.prototyper.mapeditor.model.ElementSet;

/**
 * This command creates a new Item
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemCreateCommand extends Command {

	/** The element set of items */
	private ElementSet<ItemElement> itemElements;
	/** The request to create the item */
	private CreateRequest request;
	/** The item */
	private ItemElement item;

	/**
	 * @param itemElements
	 */
	public ItemCreateCommand(ElementSet<ItemElement> itemElements,
			CreateRequest request) {
		super();
		this.itemElements = itemElements;
		this.request = request;
		item = (ItemElement) request.getNewObject();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return !itemElements.contains(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		item.setLocation(request.getLocation());
		itemElements.addElement(item);
		super.execute();
	}

}
