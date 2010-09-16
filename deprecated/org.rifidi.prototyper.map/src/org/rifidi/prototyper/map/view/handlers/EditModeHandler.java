package org.rifidi.prototyper.map.view.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.prototyper.map.controller.ViewModelSingleton;

/**
 * 
 */

/**
 * @author kyle
 * 
 */
public class EditModeHandler extends AbstractHandler implements IHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		boolean oldState = HandlerUtil.toggleCommandState(event.getCommand());
		if (oldState) {
			ViewModelSingleton.getInstance().setEditMode(false);
		} else {
			ViewModelSingleton.getInstance().setEditMode(true);
		}

		return null;
	}

}
