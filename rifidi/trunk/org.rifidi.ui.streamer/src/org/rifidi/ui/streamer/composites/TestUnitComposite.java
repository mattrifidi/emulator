package org.rifidi.ui.streamer.composites;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.actions.BatchAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.ui.streamer.composites.items.BatchActionComposite;
import org.rifidi.ui.streamer.composites.items.WaitActionComposite;
import org.rifidi.ui.streamer.composites.items.dialog.AddTestUnitActionDialog;
import org.rifidi.ui.streamer.data.ActionLabelProvider;
import org.rifidi.ui.streamer.data.EventAwareContentProvider;
import org.rifidi.ui.streamer.data.TestUnitEventAwareWrapper;
import org.rifidi.ui.streamer.utils.ChangeListOrderUtil;

public class TestUnitComposite extends Composite {

	// Action List
	private ListViewer listViewer;

	// Dynamic Composites
	private Group group;
	private Composite selectedItemComposite;

	private Label actionListIDLabel;
	private Spinner actionIDSpinner;
	private Label actionTypeLabel;

	private TestUnitEventAwareWrapper testUnit;

	private Spinner iterationSpinner;

	private Button iterationCheckBox;

	private Shell shell;

	private Spinner preWaitSpinner;

	private Spinner postWaitSpinner;

	/**
	 * @param parent
	 * @param style
	 */
	public TestUnitComposite(Composite parent, int style, boolean isWizard) {

		super(parent, style);

		this.shell = parent.getShell();

		// MAIN Layout
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 4;
		mainLayout.makeColumnsEqualWidth = true;
		this.setLayout(mainLayout);

		// first row
		actionListIDLabel = new Label(this, SWT.NONE);
		actionListIDLabel.setText("ID :");
		actionIDSpinner = new Spinner(this, SWT.BORDER);
		actionIDSpinner.setMaximum(1000);
		actionIDSpinner.setEnabled(isWizard);
		GridData actionIDSpinnerGridData = new GridData();
		actionIDSpinnerGridData.horizontalSpan = 3;
		actionIDSpinner.setLayoutData(actionIDSpinnerGridData);

		actionIDSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				testUnit.getTestUnit().setID(actionIDSpinner.getSelection());
			}

		});

		// second row
		Label testUnitIterationLabel = new Label(this, SWT.NONE);
		testUnitIterationLabel.setText("Iterations :");
		iterationCheckBox = new Button(this, SWT.CHECK);
		iterationCheckBox.setText("is infinite");
		iterationSpinner = new Spinner(this, SWT.BORDER);
		iterationSpinner.setMaximum(10000000);
		iterationSpinner.setEnabled(false);
		GridData iterationSpinnerGridData = new GridData();
		iterationSpinnerGridData.horizontalSpan = 2;
		iterationSpinner.setLayoutData(iterationSpinnerGridData);

		// third row
		Label preWaitLabel = new Label(this, SWT.NONE);
		preWaitLabel.setText("Pre wait time (ms):");
		preWaitSpinner = new Spinner(this, SWT.BORDER);
		preWaitSpinner.setMaximum(1000000000);
		Label postWaitLabel = new Label(this, SWT.NONE);
		postWaitLabel.setText("Post wait time (ms):");
		postWaitSpinner = new Spinner(this, SWT.BORDER);
		postWaitSpinner.setMaximum(1000000000);

		// fourth row
		actionTypeLabel = new Label(this, SWT.NONE);
		actionTypeLabel.setText("Actions:");
		GridData labelGridData = new GridData();
		labelGridData.horizontalSpan = 4;
		labelGridData.verticalSpan = 1;
		actionTypeLabel.setLayoutData(labelGridData);

		// fifth row
		GridData listGridData = new GridData();
		listGridData.horizontalSpan = 1;
		listGridData.grabExcessVerticalSpace = true;
		listGridData.horizontalAlignment = SWT.FILL;
		listGridData.verticalAlignment = SWT.FILL;
		listViewer = new ListViewer(this);
		listViewer.getList().setLayoutData(listGridData);
		listViewer.setContentProvider(new EventAwareContentProvider());
		listViewer.setLabelProvider(new ActionLabelProvider());

		group = new Group(this, SWT.SHADOW_OUT);
		GridData groupGridData = new GridData(GridData.FILL_BOTH);
		groupGridData.horizontalSpan = 3;
		group.setText("Selected Action Properties:");
		group.setLayoutData(groupGridData);
		group.setLayout(new FillLayout());
		selectedItemComposite = new Composite(group, SWT.NONE);

		// THIRD ROW (Buttons)
		GridData buttonGridData = new GridData();
		buttonGridData.grabExcessHorizontalSpace = true;
		Button buttonUp = new Button(this, SWT.PUSH);
		buttonUp.setText("Up");
		buttonUp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonDown = new Button(this, SWT.PUSH);
		buttonDown.setText("Down");
		buttonDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonAdd = new Button(this, SWT.PUSH);
		buttonAdd.setText("Add");
		buttonAdd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonRemove = new Button(this, SWT.PUSH);
		buttonRemove.setText("Remove");
		buttonRemove.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// ACTIONS
		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				if (!selection.isEmpty()) {
					Object o = selection.getFirstElement();
					if (o != null) {
						if (o instanceof WaitAction) {
							selectedItemComposite.dispose();
							selectedItemComposite = new WaitActionComposite(
									group, SWT.NONE, (WaitAction) o);
							group.layout();
						}
						if (o instanceof BatchAction) {
							selectedItemComposite.dispose();
							selectedItemComposite = new BatchActionComposite(
									group, SWT.NONE, (BatchAction) o);
							group.layout();
						}
					}
				} else {
					// selectedItemComposite.dispose();
					// selectedItemComposite = new Composite(group, SWT.NONE);
					group.layout();
				}

			}
		});

		iterationCheckBox.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean isInfinite = ((Button) e.getSource()).getSelection();
				if (isInfinite) {
					iterationSpinner.setEnabled(false);
					testUnit.getTestUnit().setIterations(-1);
				} else {
					iterationSpinner.setEnabled(true);
				}
			}
		});

		iterationSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				testUnit.getTestUnit().setIterations(
						iterationSpinner.getSelection());
			}

		});

		preWaitSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				testUnit.getTestUnit()
						.setPreWait(preWaitSpinner.getSelection());
			}
		});

		postWaitSpinner.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				testUnit.getTestUnit().setPostWait(
						postWaitSpinner.getSelection());
			}
		});

		buttonAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				AddTestUnitActionDialog dialog = new AddTestUnitActionDialog(
						shell, "Add a Action", testUnit);
				dialog.open();
				listViewer.refresh();
			}

		});

		buttonRemove.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object currentSelection = ((IStructuredSelection) listViewer
						.getSelection()).getFirstElement();
				if (currentSelection != null) {
					selectedItemComposite.dispose();
					selectedItemComposite = new Composite(group, SWT.NONE);
					group.layout();
					testUnit.remove(currentSelection);
				}
			}
		});

		buttonUp.addSelectionListener(new SelectionListener() {
			int count = 0;

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer
						.getSelection();
				if (!selection.isEmpty()) {
					Object o = selection.getFirstElement();
					if (!ChangeListOrderUtil.moveUp(testUnit.getList(), o)) {
						count++;
						if (count == 10) {
							// this is a Easter Egg
							MessageDialog
									.openError(getShell(),
											"Could someone stop this?",
											"Stopp doing this it is obviously not working!!!");
							count = 0;
						}
					}
					listViewer.refresh();
				}
			}
		});

		buttonDown.addSelectionListener(new SelectionListener() {
			int count = 0;

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer
						.getSelection();
				if (!selection.isEmpty()) {
					Object o = selection.getFirstElement();
					if (!ChangeListOrderUtil.moveDown(testUnit.getList(), o)) {
						count++;
						if (count == 10) {
							// this is a Easter Egg
							MessageDialog
									.openError(getShell(),
											"Could someone stop this?",
											"Stopp doing this it is obviously not working!!!");
							count = 0;
						}
					}
					listViewer.refresh();
				}
			}
		});

		this.pack();

	}

	public void setTestUnit(TestUnitEventAwareWrapper testUnit) {
		this.testUnit = testUnit;
		if (testUnit != null) {
			listViewer.setInput(testUnit);
			actionIDSpinner.setSelection(testUnit.getTestUnit().getID());
			int iterations = testUnit.getTestUnit().getIterations();
			if (iterations > 0) {
				iterationSpinner.setSelection(iterations);
				iterationSpinner.setEnabled(true);
			} else {
				iterationSpinner.setEnabled(false);
				iterationCheckBox.setSelection(true);
			}
			preWaitSpinner.setSelection((int)testUnit.getTestUnit().getPreWait());
			postWaitSpinner.setSelection((int)testUnit.getTestUnit().getPostWait());
		}
	}
}
