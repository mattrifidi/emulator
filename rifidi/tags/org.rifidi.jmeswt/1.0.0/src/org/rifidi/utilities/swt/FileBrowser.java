/**
 * 
 */
package org.rifidi.utilities.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author dan
 */
public class FileBrowser {
	private int browserStyle;
	private Text fileEntry;

	public FileBrowser( String labeltext, String dialogText, Composite parent, int browserStyle ) {
		createWidget(labeltext,dialogText,parent);
		this.browserStyle = browserStyle;
	}

	private void createWidget( String labeltext, final String dialogText, Composite parent ) {
		Composite comp = new Composite(parent,SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		comp.setLayout( gl );
		comp.setLayoutData( new GridData(SWT.FILL,SWT.CENTER,true,true));

		// create label
		Label label = new Label(comp,SWT.NONE);
		label.setText(labeltext);

		// create text entry box
		fileEntry = new Text(comp,SWT.BORDER);
		fileEntry.setLayoutData( new GridData(SWT.FILL,SWT.CENTER,true,false) );

		// create browse button
		Button browseButton = new Button(comp,SWT.NONE);
		browseButton.setText("Browse...");
		browseButton.addSelectionListener( new SelectionAdapter() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(new Shell(),browserStyle);
				fd.setText(dialogText);
				String result = fd.open();

				if ( result != null ) {
					fileEntry.setText(result);
				}
			}
		});
	}

	public String getFile() {
		return fileEntry.getText();
	}

	public Text getTextbox() {
		return fileEntry;
	}
}