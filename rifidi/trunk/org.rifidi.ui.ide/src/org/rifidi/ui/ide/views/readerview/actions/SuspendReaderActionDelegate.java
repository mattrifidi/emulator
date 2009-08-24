package org.rifidi.ui.ide.views.readerview.actions;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.ide.views.readerview.ReaderView;

/**
 * This is the suspend reader action which get invoked everytime the button for
 * suspending the reader is pressed. It's contributed to the RCP Application by
 * defining it in the plugin.xml.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class SuspendReaderActionDelegate implements IViewActionDelegate {

	private IViewPart view;
	private List<UIReader> readerList;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		for (UIReader reader : readerList) {
			reader.suspend();
			((ReaderView) view).update(reader);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action
	 * .IAction, org.eclipse.jface.viewers.ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		List<UIReader> list = ((IStructuredSelection) selection).toList();
		readerList = list;
	}
}
