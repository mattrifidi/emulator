/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.rifidi.prototyper.mapeditor.model.AbstractMapModelElement;
import org.rifidi.prototyper.mapeditor.model.ElementSet;

/**
 * This command can be used to delete either Items or Hotspots from the
 * appropriate layer.
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class DeleteCommand extends Command {

	private ElementSet<AbstractMapModelElement> layerModel;
	private List<AbstractMapModelElement> elementsToDelete;

	/**
	 * @param layerModel
	 * @param elementsToDelete
	 */
	public DeleteCommand(ElementSet<AbstractMapModelElement> layerModel,
			List<AbstractMapModelElement> elementsToDelete) {
		this.layerModel = layerModel;
		this.elementsToDelete = elementsToDelete;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		// TODO Check to see if we are in edit mode
		return super.canExecute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		for (AbstractMapModelElement e : elementsToDelete) {
			layerModel.removeElement(e);
		}
	}

}
