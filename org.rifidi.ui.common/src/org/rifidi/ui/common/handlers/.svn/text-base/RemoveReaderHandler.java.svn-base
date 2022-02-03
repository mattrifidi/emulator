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
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * Selection must be a UIReaders. Can remove more than one UIreader
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class RemoveReaderHandler extends AbstractHandler implements IHandler {

	/** The reader regsitry */
	private ReaderRegistryService readerRegistry;

	/**
	 * Constructor.
	 */
	public RemoveReaderHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * @param readerRegistry
	 *            the readerRegistry to set
	 */
	@Inject
	public void setReaderRegistry(ReaderRegistryService readerRegistry) {
		this.readerRegistry = readerRegistry;
	}

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
		for (Object curSel : sel.toList()) {
			if (curSel instanceof UIReader) {
				this.readerRegistry.remove((UIReader) curSel);
			}
		}

		return null;

	}

}
