package org.rifidi.ui.streamer.wizards.component;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;

/**
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class NewReaderWizardStreamerPage extends WizardPage {

	private static Log logger = LogFactory
			.getLog(NewReaderWizardStreamerPage.class);

	private Combo readerCombo = null;
	private Map<String, ReaderBlueprint> supportedReaders;

	/**
	 * UI abstraction of Reader to create
	 */
	private UIReader reader;

	private ReaderComponent readerComponent;

	private Spinner componentIDSpinner;

	public NewReaderWizardStreamerPage(String pageName,
			ReaderComponent readerComponent,
			Map<String, ReaderBlueprint> supportedReaders) {
		super(pageName);

		setTitle("New reader wizard");
		setDescription("Fill out all fields and hit finish to add a reader");

		this.readerComponent = readerComponent;
		this.reader = readerComponent.getUIReader();
		this.supportedReaders = supportedReaders;

		setPageComplete(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		Label componentIDLabel = new Label(composite, SWT.NONE);
		componentIDLabel.setText("Component ID:");
		componentIDSpinner = new Spinner(composite, SWT.BORDER);

		componentIDSpinner.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				readerComponent.setID(componentIDSpinner.getSelection());
			}
		});

		// Create the Label for the Combo Field
		Label readerLabel = new Label(composite, SWT.NONE);
		readerLabel.setText("Select the reader type");

		// Create the Combo Field to select the reader
		readerCombo = new Combo(composite, SWT.READ_ONLY);
		readerCombo.setLayoutData(gridData);
		for (String reader : supportedReaders.keySet()) {
			readerCombo.add(reader);
		}

		readerCombo.addSelectionListener(new SelectionListener() {

			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				updateInput();
			}

		});

		setControl(composite);
		setPageComplete(false);
	}

	/**
	 * This method is invoked every time the input changes
	 */
	protected void updateInput() {
		String selectedReader = readerCombo.getItem(readerCombo
				.getSelectionIndex());
		logger.debug("Reader type selected : " + selectedReader);
		logger.debug("Reader class name : "
				+ supportedReaders.get(selectedReader).getReaderclassname());
		// Set the information from the dialog in the UI repesentation of the
		// reader
		reader.setReaderType(selectedReader);
		reader.setReaderClassName(supportedReaders.get(selectedReader)
				.getReaderclassname());

		setPageComplete(true);
	}

}
