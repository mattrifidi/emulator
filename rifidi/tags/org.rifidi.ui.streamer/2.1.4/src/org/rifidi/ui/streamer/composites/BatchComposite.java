package org.rifidi.ui.streamer.composites;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.actions.GPIAction;
import org.rifidi.streamer.xml.actions.TagAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.ui.streamer.composites.items.GPIActionComposite;
import org.rifidi.ui.streamer.composites.items.TagActionComposite;
import org.rifidi.ui.streamer.composites.items.WaitActionComposite;
import org.rifidi.ui.streamer.composites.items.dialog.AddBatchActionDialog;
import org.rifidi.ui.streamer.data.ActionLabelProvider;
import org.rifidi.ui.streamer.data.BatchEventAwareWrapper;
import org.rifidi.ui.streamer.data.EventAwareContentProvider;
import org.rifidi.ui.streamer.utils.ChangeListOrderUtil;

/**
 * This displays all the properties of a Batch and makes it modifiable
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class BatchComposite extends Composite {

	// private Log logger = LogFactory.getLog(BatchActionListComposite.class);
	private Shell shell;

	// Corresponding Data Object
	private BatchEventAwareWrapper batch;

	// Action List
	private ListViewer listViewer;

	// Dynamic Composites
	private Group group;
	private ScrolledComposite groupScrolledComposite;
	private Composite selectedItemComposite;

	private Spinner actionIDSpinner;

	private boolean changesSaved = false;

	/**
	 * @param parent
	 * @param style
	 */
	public BatchComposite(Composite parent, int style, boolean isWizard) {
		super(parent, style);

		shell = parent.getShell();

		// MAIN Layout
		GridLayout mainLayout = new GridLayout();
		mainLayout.numColumns = 4;
		mainLayout.makeColumnsEqualWidth = true;
		this.setLayout(mainLayout);

		// FIRST ROW
		Label actionListIDLabel = new Label(this, SWT.NONE);
		actionListIDLabel.setText("ID :");
		actionIDSpinner = new Spinner(this, SWT.BORDER);
		actionIDSpinner.setEnabled(isWizard);

		Label actionTypeLabel = new Label(this, SWT.NONE);
		actionTypeLabel.setText("Actions:");
		GridData labelGridData = new GridData();
		labelGridData.horizontalSpan = 4;
		labelGridData.verticalSpan = 1;
		actionTypeLabel.setLayoutData(labelGridData);

		// SECOND ROW
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
		groupScrolledComposite = new ScrolledComposite(group, SWT.H_SCROLL
				| SWT.V_SCROLL);
		groupScrolledComposite.setExpandHorizontal(true);
		groupScrolledComposite.setExpandVertical(true);
		groupScrolledComposite.setLayout(new FillLayout());
		// groupSC.setAlwaysShowScrollBars(true);
		// groupSC.setBackground(getDisplay().getSystemColor(SWT.COLOR_RED));
		selectedItemComposite = new Composite(groupScrolledComposite, SWT.NONE);

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
		actionIDSpinner.addListener(SWT.Modify, new Listener() {

			@Override
			public void handleEvent(Event event) {
				batch.getBatch().setID(actionIDSpinner.getSelection());
			}
		});

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (!selection.isEmpty()) {
					Object o = selection.getFirstElement();
					// logger.debug(o.getClass().getSimpleName());
					if (o != null) {
						if (selectedItemComposite != null
								&& !selectedItemComposite.isDisposed()) {
							selectedItemComposite.dispose();
						}
						if (o instanceof TagAction) {
							selectedItemComposite = new TagActionComposite(
									groupScrolledComposite, SWT.NONE,
									(TagAction) o);
							groupScrolledComposite
									.setContent(selectedItemComposite);
							groupScrolledComposite.layout();
						}
						if (o instanceof WaitAction) {
							selectedItemComposite = new WaitActionComposite(
									groupScrolledComposite, SWT.NONE,
									(WaitAction) o);
							groupScrolledComposite
									.setContent(selectedItemComposite);
							groupScrolledComposite.layout();
						}
						if (o instanceof GPIAction) {
							selectedItemComposite = new GPIActionComposite(
									groupScrolledComposite, SWT.NONE,
									(GPIAction) o);
							groupScrolledComposite
									.setContent(selectedItemComposite);
							groupScrolledComposite.layout();
						}
					}
				} else {
					selectedItemComposite.dispose();
					selectedItemComposite = new Composite(
							groupScrolledComposite, SWT.NONE);
					groupScrolledComposite.setContent(selectedItemComposite);
					groupScrolledComposite.layout();
				}
				groupScrolledComposite.setMinSize(selectedItemComposite
						.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}

		});

		buttonAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				AddBatchActionDialog dialog = new AddBatchActionDialog(shell,
						"Add a Action", batch);
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
					if (!selectedItemComposite.isDisposed()) {
						selectedItemComposite.dispose();
					}
					selectedItemComposite = new Composite(
							groupScrolledComposite, SWT.NONE);
					groupScrolledComposite.setContent(selectedItemComposite);
					groupScrolledComposite.layout();
					batch.remove(currentSelection);
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
					if (!ChangeListOrderUtil.moveUp(batch.getList(), o)) {
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
					if (!ChangeListOrderUtil.moveDown(batch.getList(), o)) {
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

		actionIDSpinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				changesSaved = false;
			}
		});

		this.pack();

	}

	public void setBatch(BatchEventAwareWrapper batch) {
		this.batch = batch;
		if (batch != null) {
			listViewer.setInput(batch);
			actionIDSpinner.setSelection(batch.getBatch().getID());
		}
	}

	public void saveChanges() {
		batch.getBatch().setID(actionIDSpinner.getSelection());
		changesSaved = true;
	}

	public boolean isSaved() {
		return changesSaved;
	}
}
