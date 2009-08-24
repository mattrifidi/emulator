/*
 *  NewReaderWizardPage.java
 *
 *  Created:	Feb 28, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.wizards.reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.reader.blueprints.PropertyBlueprint;
import org.rifidi.ui.common.reader.blueprints.ReaderBlueprint;
import org.rifidi.ui.common.registry.ReaderRegistryService;
import org.rifidi.ui.common.validators.BooleanValidator;
import org.rifidi.ui.common.validators.ReaderNameValidator;

/**
 * 
 * This pages displays everything needed to create a tag. The input is validated
 * using the data provided by the corresponding IdeTag-implementation
 * 
 * @see TagID
 * 
 * @author Jochen Mader - jochen@pramari.com
 */
public class NewReaderDynamicWizardPage extends WizardPage {

	private static Log logger = LogFactory
			.getLog(NewReaderDynamicWizardPage.class);

	private ReaderRegistryService readerRegistry;

	/**
	 * textwidget for the name of the reader
	 */
	private Text readerName;

	/**
	 * combo for selecting the number of antennas on the reader
	 */
	private Combo antennaCombo;

	/**
	 * the bean that represents the reader that should be created
	 */
	private ReaderWizardData data;

	/**
	 * Map containing the READERTYPEs as keys and the ReaderBlueprints as values
	 */
	private Map<String, ReaderBlueprint> availableReaders;

	/**
	 * the ReaderBlueprint used by this wizard
	 */
	private ReaderBlueprint readerBlueprint;

	/**
	 * Map used to cache validator instances
	 */
	@SuppressWarnings("unchecked")
	private Map<Class, ICellEditorValidator> validators = new HashMap<Class, ICellEditorValidator>();

	/**
	 * List containing all the dynamically created Text fields
	 */
	private List<Text> inputfields = new ArrayList<Text>();
	/**
	 * List containing all the dynamically created Boolean fields
	 */
	private List<Button> boolfields = new ArrayList<Button>();

	private Button hasGpio;
	private boolean enableGPIOs;
	private ReaderRegistryService registry;

	/**
	 * Constructor
	 * 
	 * @param name
	 *            name of this page
	 * @param data
	 *            data container object for the new reader
	 * @param availableReaders
	 *            a Map containing the available readers/ReaderBlueprints
	 */
	public NewReaderDynamicWizardPage(String name, ReaderWizardData data,
			Map<String, ReaderBlueprint> availableReaders) {
		super(name);
		ServiceRegistry.getInstance().service(this);

		setTitle("New reader wizard");
		setDescription("Fill out all fields and hit finish to add a reader");

		this.data = data;
		this.availableReaders = availableReaders;

		setPageComplete(false);
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
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	public void createControl(Composite parent) {
		enableGPIOs = false;
		readerBlueprint = availableReaders.get(data.readerType);
		setDescription(readerBlueprint.getDescription());

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		// we want two rows
		layout.numColumns = 2;
		composite.setLayout(layout);

		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);

		// create the label and textwidget to enter the reader name
		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText("Please enter a name");

		readerName = new Text(composite, SWT.BORDER);
		readerName.setLayoutData(gridData);
		readerName.setTextLimit(12);
		readerName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		// create the label and combobox for the antennas
		Label antennaLabel = new Label(composite, SWT.NONE);
		antennaLabel.setText("Select the number of antennas");
		antennaCombo = new Combo(composite, SWT.READ_ONLY);
		antennaCombo.setLayoutData(gridData);
		for (Integer count = 1; count <= readerBlueprint.getMaxantennas(); count++) {
			antennaCombo.add(count.toString());
		}
		antennaCombo.select(0);
		antennaCombo.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				dialogChanged();
			}
		});

		for (PropertyBlueprint propertyBlueprint : readerBlueprint
				.getProperties()) {
			Label tempLabel = new Label(composite, SWT.NONE);
			tempLabel.setText(propertyBlueprint.getDisplay());
			tempLabel.setToolTipText(propertyBlueprint.getTooltip());
			if (!propertyBlueprint.getValidatorclass().equals(
					BooleanValidator.class)) {
				Text tempText = new Text(composite, SWT.BORDER);
				tempText.setLayoutData(gridData);
				tempText.setText(propertyBlueprint.getDefaultvalue());
				tempText.setData(propertyBlueprint);
				tempText.addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent e) {
						dialogChanged();
					}
				});
				inputfields.add(tempText);
			} else {
				Button button = new Button(composite, SWT.CHECK);
				button.setLayoutData(gridData);
				button.setData(propertyBlueprint);
				if (!propertyBlueprint.getDefaultvalue().equals("0")) {
					button.setSelection(true);
				}
				button.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent e) {
						dialogChanged();
					}

					public void widgetSelected(SelectionEvent e) {
						dialogChanged();
					}

				});
				boolfields.add(button);
			}

		}

		logger.debug("Reader supports " + readerBlueprint.getMaxgpos()
				+ " GPO's and " + readerBlueprint.getMaxgpis() + " GPI's");

		if (readerBlueprint.getMaxgpos() != null
				|| readerBlueprint.getMaxgpis() != null) {
			if (readerBlueprint.getMaxgpos() > 0
					|| readerBlueprint.getMaxgpis() > 0) {
				Label hasGPIOLabel = new Label(composite, SWT.NONE);
				hasGPIOLabel.setText("Enable GPI/O for this Reader");

				hasGpio = new Button(composite, SWT.CHECK);
				hasGpio.setLayoutData(gridData);
				hasGpio.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						logger.debug("GPIO enable state changed");
						enableGPIOs = hasGpio.getSelection();
						dialogChanged();
					}
				});

				// TODO: Do we want the emulator plugins to choose its default
				// gpio enable state?
				hasGpio.setSelection(true);
				logger.debug("GPIO enable state changed");
				enableGPIOs = hasGpio.getSelection();
				dialogChanged();
			}
		} else {
			logger
					.error("Reader should define MaxGPI's and MaxGPO's in it's emulator.xml file");
		}

		setPageComplete(false);
		setControl(composite);
		composite.layout();
	}

	/**
	 * called everytime a combo's or textfield's input changes
	 * 
	 */
	private void dialogChanged() {
		StringBuilder sb = new StringBuilder();
		// logger.debug("ReaderName :" + readerName.getText());
		String ret = new ReaderNameValidator().isValid(readerName.getText());
		if (ret != null) {
			sb.append(ret);
		}
		for (Text text : inputfields) {
			if (sb.length() == 0) {
				PropertyBlueprint blueprint = (PropertyBlueprint) (text
						.getData());
				if (validators.get(blueprint.getValidatorclass()) == null) {
					try {
						validators.put(blueprint.getValidatorclass(),
								(ICellEditorValidator) blueprint
										.getValidatorclass().newInstance());
					} catch (InstantiationException ie) {
						logger.error("Couldn't instantiate validator: " + ie);
					} catch (IllegalAccessException iae) {
						logger.error("Couldn't instantiate validator: " + iae);
					}
				}
				ret = validators.get(blueprint.getValidatorclass()).isValid(
						text.getText());
				if (ret != null) {
					sb.append(blueprint.getDisplay() + " " + ret);
				} else {
					data.generalReaderHolder.setProperty(blueprint.getName(),
							text.getText());
				}
			} else {
				break;
			}
		}
		for (Button button : boolfields) {
			PropertyBlueprint blueprint = (PropertyBlueprint) (button.getData());
			data.generalReaderHolder.setProperty(blueprint.getName(), Boolean
					.toString(button.getSelection()));
		}
		if (readerRegistry.isNameAvailable(readerName.getText()))
			data.generalReaderHolder.setReaderName(readerName.getText());
		else
			sb.append("Name of the reader already taken");

		if (sb.length() == 0) {

			// Set the information of the WizardPage in the UI representation of
			// the Reader

			data.generalReaderHolder.setNumAntennas(antennaCombo
					.getSelectionIndex() + 1);

			// Properties are set a little bit earlier

			setErrorMessage(null);
			setPageComplete(true);
		} else {
			setErrorMessage(sb.toString());
			setPageComplete(false);
		}
	}

	@Override
	public IWizardPage getNextPage() {
		if (enableGPIOs) {
			return super.getNextPage();
		}
		return null;
	}

	@Override
	public Control getControl() {
		if (isControlCreated()) {
			/*
			 * see if the user change the reader type he wants to create.
			 */
			if (!availableReaders.get(data.readerType).getReaderclassname()
					.equals(readerBlueprint.getReaderclassname())) {
				/*
				 * clear all lists
				 */
				validators.clear();
				inputfields.clear();
				boolfields.clear();

				/*
				 * recreate the dynamic page.
				 */
				Control old = super.getControl();
				Composite parent = old.getParent();
				createControl(parent);
				old.dispose();

				/*
				 * tell the parent that things have changed.
				 */
				super.getControl().getParent().layout();
			}

		}
		return super.getControl();
	}

	public boolean enableGPIO() {
		return enableGPIOs;
	}

}
