/*
 *  GateEntityWizardPage.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.gate;

import java.util.HashMap;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;

/**
 * Wizard page for the GateEntity.
 * 
 * @author Jochen Mader Oct 1, 2007
 * 
 */
public class GateEntityWizardPage extends WizardPage {
	/**
	 * Container for the page.
	 */
	private Composite pageComposite;
	/**
	 * Widget for the gate name.
	 */
	private Text nameText;
	/**
	 * Widget for reader selection.
	 */
	private Combo readerCombo;
	/**
	 * Map containing the available readers and their blueprints.
	 */
	private HashMap<String, ReaderBlueprint> availableReaders;
	/**
	 * Reader instance.
	 */
	private UIReader reader;

	/**
	 * Constructor.
	 * 
	 * @param pageName
	 * @param availableReaders
	 * @param reader
	 */
	public GateEntityWizardPage(String pageName,
			HashMap<String, ReaderBlueprint> availableReaders, UIReader reader) {
		super(pageName);
		setTitle("Gate Wizard");
		setMessage("Please give a name for the gate.");
		this.availableReaders = availableReaders;
		this.reader = reader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		pageComposite = new Composite(parent, SWT.NONE);
		GridLayout pageLayout = new GridLayout();
		pageLayout.numColumns = 2;
		pageComposite.setLayout(pageLayout);
		GridData layoutData = new GridData();
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.widthHint = 100;

		GridData grabTwo = new GridData();
		grabTwo.horizontalSpan = 2;
		grabTwo.grabExcessHorizontalSpace = true;
		grabTwo.widthHint = 200;

		Label label = new Label(pageComposite, SWT.None);
		label.setText("Name");
		nameText = new Text(pageComposite, SWT.SINGLE | SWT.BORDER);
		nameText.setLayoutData(layoutData);
		// create the label and combo for the reader type
		Label readerLabel = new Label(pageComposite, SWT.NONE);
		readerLabel.setText("Select the reader type");
		readerCombo = new Combo(pageComposite, SWT.READ_ONLY);
		readerCombo.setLayoutData(layoutData);
		for (String reader : availableReaders.keySet()) {
			readerCombo.add(reader);
		}
		readerCombo.addSelectionListener(new SelectionListener() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetSelected(SelectionEvent e) {
				String selectedReaderType = readerCombo.getItem(readerCombo
						.getSelectionIndex());
				reader.setReaderType(selectedReaderType);
				reader.setReaderClassName(availableReaders.get(
						selectedReaderType).getReaderclassname());
				// for(String type:availableReaders.keySet()){
				// if(availableReaders.get(type).getReaderclass().equals(generalReaderPropertyHolder.getReaderClassName())){
				// generalReaderPropertyHolder.setReaderType(type);
				// break;
				// }
				// }
				setPageComplete(true);
			}

		});
		setControl(pageComposite);
		setPageComplete(false);
	}

	/**
	 * Get the name for the new gate.
	 * 
	 * @return gate name
	 */
	public String getName() {
		return nameText.getText();
	}
}
