package org.rifidi.prototyper.items.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.prototyper.items.model.ItemModel;
import org.rifidi.prototyper.items.view.ItemModelProviderSingleton;

/**
 * This class is a handler for the delete Item command
 * 
 * @author kyle Neumeier - kyle@pramari.com
 * 
 */
public class DeleteItemHandler extends AbstractHandler implements IHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof IStructuredSelection) {
			IStructuredSelection ssel = (IStructuredSelection) sel;
			for (Object o : ssel.toArray()) {
				if (o instanceof ItemModel) {
					ItemModelProviderSingleton.getModelProvider().removeItem((ItemModel) o);
				}
			}
		}
		return null;
	}

}
