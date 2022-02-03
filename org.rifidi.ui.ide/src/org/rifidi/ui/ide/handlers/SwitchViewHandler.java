/**
 * 
 */
package org.rifidi.ui.ide.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.ui.ide.editors.ReaderEditor;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class SwitchViewHandler extends AbstractHandler {
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if (editor != null && editor instanceof ReaderEditor) {
			ReaderEditor readerEditor = (ReaderEditor) editor;
			readerEditor.switchView();
		}
		return null;
	}

}
