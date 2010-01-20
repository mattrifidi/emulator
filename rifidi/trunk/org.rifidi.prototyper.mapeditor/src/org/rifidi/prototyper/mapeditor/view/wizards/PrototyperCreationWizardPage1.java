/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.rifidi.prototyper.mapeditor.view.wizards.PrototyperCreationWizard.ProjectType;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class PrototyperCreationWizardPage1 extends WizardPage {

	private Button exampleProject;
	private Button customProject;
	private Text projectName;
	private Group exampleProjectGroup;
	private Group customProjectGroup;
	private Combo exampleProjectCombo;
	private Text pathToBlueprintText;
	private Button chooseFile;
	private String[] projectTypes = new String[] {
			ProjectType.WAREHOUSE.toString(), ProjectType.HOSPITAL.toString() };

	/**
	 * @param pageName
	 */
	protected PrototyperCreationWizardPage1(String pageName) {
		super(pageName);
		this.setTitle("New Prototype");
		this.setDescription("Choose a project type");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createControl(Composite parent) {
		Composite projectComposite = new Composite(parent, SWT.None);
		projectComposite.setLayout(new GridLayout(2, false));

		Label projectNameLabel = new Label(projectComposite, SWT.NONE);
		projectNameLabel.setText("Project Name: ");
		projectName = new Text(projectComposite, SWT.BORDER);
		projectName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectName.addKeyListener(new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {
				validate();

			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}
		});

		exampleProject = new Button(projectComposite, SWT.RADIO);
		exampleProject.setText("Example Project");
		exampleProject.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				validate();

			}
		});

		Text exampleDesc = new Text(projectComposite, SWT.WRAP | SWT.MULTI);
		exampleDesc
				.setText("Create a prototyper project with a preconfigured blueprint.");
		exampleDesc.setEditable(false);

		customProject = new Button(projectComposite, SWT.RADIO);
		customProject.setText("Custom Project");
		customProject.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				validate();

			}
		});
		Text customDesc = new Text(projectComposite, SWT.WRAP | SWT.MULTI);
		customDesc
				.setText("Supply and configure your own blueprint.");
		customDesc.setEditable(false);

		exampleProjectGroup = new Group(projectComposite, SWT.NONE);
		exampleProjectGroup.setText("Choose a Preconfigured Project");
		GridData exampleProjectData = new GridData(GridData.FILL_HORIZONTAL);
		exampleProjectData.horizontalSpan = 2;
		exampleProjectData.verticalIndent = 10;
		//exampleProjectData.widthHint = 500;
		exampleProjectGroup.setLayoutData(exampleProjectData);
		exampleProjectGroup.setLayout(new GridLayout());

		exampleProjectCombo = new Combo(exampleProjectGroup, SWT.NONE);
		exampleProjectCombo
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		exampleProjectCombo.add(ProjectType.WAREHOUSE.toString());
		exampleProjectCombo.add(ProjectType.HOSPITAL.toString());
		exampleProjectCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				validate();

			}
		});

		customProjectGroup = new Group(projectComposite, SWT.NONE);
		customProjectGroup.setText("Choose a Blueprint");
		customProjectGroup.setLayoutData(exampleProjectData);
		customProjectGroup.setLayout(new GridLayout(2, false));

		pathToBlueprintText = new Text(customProjectGroup, SWT.BORDER);
		pathToBlueprintText
				.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		chooseFile = new Button(customProjectGroup, SWT.PUSH);
		chooseFile.setText("Browse");
		chooseFile.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
				pathToBlueprintText.setText(fd.open());
				validate();

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		setControl(projectComposite);
		updateControls();
		validate();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.DialogPage#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			updateControls();
		}
		super.setVisible(visible);
	}

	/**
	 * Helper method that initializes controls to the values stored in the
	 * wizard
	 */
	private void updateControls() {
		PrototyperCreationWizard wizard = (PrototyperCreationWizard) getWizard();
		if (wizard.custom) {
			setCustom(true);
		} else {
			setCustom(false);
		}
		exampleProjectCombo.select(getIndex(wizard.exampleProject));
		projectName.setText(wizard.projectName);
		this.pathToBlueprintText.setText(wizard.pathToFile);
	}

	/**
	 * Validates input and sets error messages
	 */
	private void validate() {
		PrototyperCreationWizard wizard = (PrototyperCreationWizard) getWizard();
		if (exampleProject.getSelection()) {
			wizard.custom = false;
			setCustom(false);
		} else if (customProject.getSelection()) {
			wizard.custom = true;
			setCustom(true);
		}
		String proj = projectName.getText();
		if (proj != null && !proj.equals("")) {
			wizard.projectName = proj;
			if (wizard.custom) {
				String path = pathToBlueprintText.getText();
				if (path == null || path.equals("")) {
					setErrorMessage("Please supply a path to an blueprint");
					setPageComplete(false);
					return;
				}
				wizard.pathToFile=path;

			} else {
				int selIndex = exampleProjectCombo.getSelectionIndex();
				if (selIndex < 0 || selIndex >= projectTypes.length) {
					setPageComplete(false);
					return;
				}
				String selName = projectTypes[selIndex];
				ProjectType pt = ProjectType.getProjectType(selName);
				if (pt != null) {
					wizard.exampleProject = pt;
				} else {
					setErrorMessage("Please choose an example project");
					setPageComplete(false);
					return;
				}
			}
		} else {
			setErrorMessage("Please supply a project name");
			setPageComplete(false);
			return;
		}
		setErrorMessage(null);
		setPageComplete(true);

	}

	/**
	 * Returns the project type index
	 * 
	 * @param projecType
	 * @return
	 */
	private int getIndex(ProjectType projecType) {
		for (int i = 0; i < projectTypes.length; i++) {
			if (projectTypes[i].toString().equalsIgnoreCase(
					projecType.toString())) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Helper method to change to controls to match up with either a custom
	 * project type or an example project type
	 * 
	 * @param custom
	 */
	private void setCustom(boolean custom) {
		if (custom) {
			customProject.setSelection(true);
			exampleProject.setSelection(false);
			this.exampleProjectCombo.setEnabled(false);
			this.pathToBlueprintText.setEnabled(true);
			this.chooseFile.setEnabled(true);
		} else {
			exampleProject.setSelection(true);
			customProject.setSelection(false);
			this.exampleProjectCombo.setEnabled(true);
			this.pathToBlueprintText.setEnabled(false);
			this.chooseFile.setEnabled(false);
		}
	}

}
