/**
 * 
 */
package org.rifidi.ui.common.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.ui.common.reader.UIReader;

/**
 * All selections must be UIReaders and in the NEW or STOPPED state
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class StartReaderHandler extends AbstractHandler implements IHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IStructuredSelection sel = (IStructuredSelection) HandlerUtil
				.getCurrentSelection(event);
		for (Object o : sel.toList()) {
			if (o instanceof UIReader) {
				((UIReader) o).start();
			}
		}
		return null;
	}

}
