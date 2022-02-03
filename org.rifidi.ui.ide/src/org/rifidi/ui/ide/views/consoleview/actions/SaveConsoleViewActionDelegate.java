package org.rifidi.ui.ide.views.consoleview.actions;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.rifidi.ui.ide.views.consoleview.ConsoleView;

/**
 * Save the console content of this reader to a file
 * 
 * @author Andreas Huebner - andreas@pramari.com
 *
 */
public class SaveConsoleViewActionDelegate implements IViewActionDelegate {

	private IViewPart view;
	private FileDialog fileDialog;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		save();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		// TODO Auto-generated method stub

	}

	/**
	 * The save action itself. Opens the save dialog and than 
	 * 
	 * @return status of save action
	 */
	// get filename where to save console output and call write to save to file
	private boolean save() {
		String fileName = null;
		boolean done = false;
		while (!done) {
			fileDialog = new FileDialog(view.getSite().getShell(), SWT.SAVE);
			fileName = fileDialog.open();
			if (fileName == null) {
				// User has pressed cancel
				return false;
			}
			File file = new File(fileName);
			if (file.exists()) {

				MessageBox mb = new MessageBox(view.getSite().getShell(),
						SWT.ICON_WARNING | SWT.YES | SWT.NO);
				mb.setMessage(fileName
						+ " exists. \nDo you want to overwrite it?");
				int ans = mb.open();
				if (ans == SWT.YES) {
					return write(file);
				}
			} else {
				return write(file);
			}
		}
		return false;
	}

	/**
	 * @param file -
	 *            Filename to save console to
	 * @return status of save action (true if saved / false if problems)
	 */
	// save the console-log to given filename
	private boolean write(File file) {
		try {
			FileWriter fw = new FileWriter(file);
			String consoleLog = ((ConsoleView) view).getText();
			String.format(Locale.getDefault(), consoleLog);
			if (consoleLog.length() <= 0) {
				MessageBox mb = new MessageBox(view.getSite().getShell(),
						SWT.ICON_INFORMATION | SWT.OK);
				mb.setMessage("Nothing to save.\nCancel file save.");
				mb.open();
				return false;
			}
			fw.write(consoleLog);
			fw.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}

}
