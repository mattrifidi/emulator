package org.rifidi.ui.ide.views.readerview.actions;

import java.util.List;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.ide.views.readerview.ReaderView;

/**
 * This is the Action which is invoked everytime the Button for removing a
 * reader is pressed. It contributed to the RCP Application by defining it in
 * the plugin.xml
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class RemoveReaderActionDelegate implements IViewActionDelegate, IWorkbenchWindowActionDelegate {

	private static IViewPart view;
	private List<UIReader> currentSelection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		RemoveReaderActionDelegate.view = view;

		((ReaderView) view).getTreeViewer().getControl().addKeyListener(
				new KeyListener() {

					public void keyPressed(KeyEvent e) {
						if (e.character == SWT.DEL) {
							deleteReader();
						}
					}

					public void keyReleased(KeyEvent e) {
						// TODO Auto-generated method stub
					}

				});
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init(IWorkbenchWindow window) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		deleteReader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		currentSelection = ((IStructuredSelection) selection).toList();
	}

	/*
	 * shows a Dialog and than deletes the selected readers
	 */
	private void deleteReader() {
		MessageBox messageBox = new MessageBox(view.getSite().getShell(),
				SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
		messageBox.setText("Warning");
		messageBox.setMessage("Do you really want to "
				+ "delete the selected readers?");
		if (messageBox.open() == SWT.OK) {
			for (UIReader reader : currentSelection) {
				ReaderRegistry.getInstance().remove(reader);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#dispose()
	 */
	public void dispose() {
	}

	

}
