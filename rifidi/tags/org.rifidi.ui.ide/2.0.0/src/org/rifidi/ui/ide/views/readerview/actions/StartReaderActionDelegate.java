/**
 * 
 */
package org.rifidi.ui.ide.views.readerview.actions;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.ide.views.readerview.ReaderView;

/**
 * This is the start reader action which get invoked everytime the button for
 * starting the reader is pressed. It's conributed to the RCP Application by
 * defining it in the plugin.xml.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class StartReaderActionDelegate implements IViewActionDelegate, IWorkbenchWindowActionDelegate {

	private static IViewPart view;
	private List<UIReader> readerList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		StartReaderActionDelegate.view = (ReaderView) view;
	}
	
	public void init(IWorkbenchWindow window) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		for (UIReader reader : readerList) {
			try {
				reader.getReaderManager().turnReaderOn();
				reader.setReaderState("running");
				((ReaderView) view).update(reader);
			} catch (Exception e) {
				// TODO fix error Handling
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		List<UIReader> list = ((IStructuredSelection) selection).toList();
		readerList = list;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
	}

}
