/*
 *  RenameDialog.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.utilities.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Utility for showing a dialog with a text field that can be used to rename objects and such
 * @author Dan West - dan@pramari.com
 */
public class RenameDialog {
	private Shell parentShell;
	private Shell myShell;
	private String result;
	private Text text;

	/**
	 * Opens a dialog window for renaming something
	 * @param shell the parent shell of this dialog
	 * @param title the title for this dialog
	 * @param name the starting name to be renamed
	 */
	public RenameDialog( Shell shell, String title, String name ) {
		parentShell = shell;
		init(title,name);
	}

	/**
	 * Initializes the window
	 * @param title the title to use
	 * @param name the starting name for the textbox
	 */
	private void init( String title, String name ) {
		myShell = new Shell(parentShell);
		myShell.setLayout( new GridLayout(2,true) );
		myShell.setSize(300,100);
		myShell.setText(title);

		text = new Text(myShell,SWT.BORDER);
		GridData layoutData = new GridData(SWT.FILL,SWT.NONE,true,false);
		text.setLayoutData(layoutData);
		layoutData.horizontalSpan = 2;
		text.setText(name);
		text.setSelection(0, name.length());

		Button ok = new Button(myShell,SWT.PUSH);
		ok.setLayoutData( new GridData(SWT.CENTER,SWT.NONE,false,false) );
		ok.setText("Rename");
		ok.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = text.getText();
				myShell.dispose();
			}
		});

		Button cancel = new Button(myShell,SWT.PUSH);
		cancel.setLayoutData( new GridData(SWT.CENTER,SWT.NONE,false,false) );
		cancel.setText("Cancel");
		cancel.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				result = null;
				myShell.dispose();
			}
		});

		myShell.setDefaultButton(ok);
	}

	/**
	 * Opens the created dialog and returns the result
	 * @return the new name (or null if the user hit cancel)
	 */
	public String open() {
		myShell.open();
		while (!myShell.isDisposed()) {
			if (!myShell.getDisplay().readAndDispatch()) {
				myShell.getDisplay().sleep();
			}
		}

		return result;
	}
}