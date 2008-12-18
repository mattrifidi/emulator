/**
 * 
 */
package org.rifidi.ui.streamer.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.rifidi.streamer.xml.scenario.PathItem;
import org.rifidi.ui.streamer.composites.items.dialog.AddPathItemDialog;
import org.rifidi.ui.streamer.data.EventAwareContentProvider;
import org.rifidi.ui.streamer.data.ScenarioEventAwareWrapper;
import org.rifidi.ui.streamer.utils.ChangeListOrderUtil;

/**
 * This is a composite containing all the fields of a Scenario and make it
 * modifiable
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ScenarioComposite extends Composite {

	private Spinner scenarioIDSpinner;
	private TableViewer tableViewer;
	private ScenarioEventAwareWrapper scenario;
	private Shell shell;

	protected abstract class AbstractEditingSupport extends EditingSupport {
		private TextCellEditor editor;

		public AbstractEditingSupport(TableViewer viewer) {
			super(viewer);
			this.editor = new TextCellEditor(viewer.getTable());
		}

		protected boolean canEdit(Object element) {
			return true;
		}

		protected CellEditor getCellEditor(Object element) {
			return editor;
		}

		protected void setValue(Object element, Object value) {
			doSetValue(element, value);
			getViewer().update(element, null);
		}

		protected abstract void doSetValue(Object element, Object value);
	}

	public ScenarioComposite(Composite parent, int style, boolean isWizard) {
		super(parent, style);

		this.shell = parent.getShell();

		// MAIN COMPOSITE
		GridLayout mainLayout = new GridLayout(4, true);
		this.setLayout(mainLayout);

		// 1st Row
		Label scenarioIDLabel = new Label(this, SWT.NULL);
		scenarioIDLabel.setText("Scenario ID: ");
		scenarioIDLabel.setLayoutData(new GridData(GridData.FILL));
		scenarioIDSpinner = new Spinner(this, SWT.BORDER);
		scenarioIDSpinner.setMaximum(1000);
		scenarioIDSpinner.setEnabled(isWizard);

		scenarioIDSpinner.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				scenario.getScenario().setID(scenarioIDSpinner.getSelection());
			}
		});

		// 2nd Row
		Label scenarioLabel = new Label(this, SWT.NULL);
		scenarioLabel.setText("Path Items in this Scenario:");
		GridData scenarioGridData = new GridData(GridData.FILL_HORIZONTAL);
		scenarioGridData.horizontalSpan = 4;
		scenarioLabel.setLayoutData(scenarioGridData);

		tableViewer = new TableViewer(this, SWT.BORDER);
		GridData tableViewerGridData = new GridData(GridData.FILL_BOTH);
		tableViewerGridData.horizontalSpan = 4;
		tableViewer.getTable().setLayoutData(tableViewerGridData);
		tableViewer.setContentProvider(new EventAwareContentProvider());

		// TableColumn
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		String[] columnNames = new String[] { "PathItem", "Component ID",
				"Antenna #", "TravelTime (ms)" };
		int[] columnWidths = new int[] { 200, 100, 100, 100 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT, SWT.LEFT };
		List<TableViewerColumn> tableColumns = new ArrayList<TableViewerColumn>();
		// create columns and add listeners to them
		for (int i = 0; i < columnNames.length; i++) {
			TableViewerColumn tableColumn = new TableViewerColumn(tableViewer,
					columnAlignments[i]);
			tableColumn.getColumn().setText(columnNames[i]);
			tableColumn.getColumn().setWidth(columnWidths[i]);
			// Add Listener
			// tableColumn.addListener(SWT.Selection, this.selChangeListener);
			tableColumns.add(tableColumn);
		}

		tableColumns.get(0).setLabelProvider(new ColumnLabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				return "Reader " + ((PathItem) element).getReaderID();
			}

		});

		tableColumns.get(1).setLabelProvider(new ColumnLabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				return "" + ((PathItem) element).getReaderID();
			}

		});

		(tableColumns.get(2)).setLabelProvider(new ColumnLabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				return "" + ((PathItem) element).getAntennaNum();
			}

		});

		(tableColumns.get(3)).setLabelProvider(new ColumnLabelProvider() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.ColumnLabelProvider#getText(java.lang.Object)
			 */
			@Override
			public String getText(Object element) {
				return "" + ((PathItem) element).getTravelTime();
			}

		});

		tableColumns.get(1).setEditingSupport(
				new AbstractEditingSupport(tableViewer) {

					@Override
					protected void doSetValue(Object element, Object value) {
						((PathItem) element).setReaderID(Integer
								.valueOf((String) value));
					}

					@Override
					protected Object getValue(Object element) {
						return String.valueOf(((PathItem) element)
								.getReaderID());
					}
				});

		tableColumns.get(2).setEditingSupport(
				new AbstractEditingSupport(tableViewer) {

					@Override
					protected void doSetValue(Object element, Object value) {
						((PathItem) element).setAntennaNum(Integer
								.valueOf((String) value));
					}

					@Override
					protected Object getValue(Object element) {
						return String.valueOf(((PathItem) element)
								.getAntennaNum());
					}
				});

		tableColumns.get(3).setEditingSupport(
				new AbstractEditingSupport(tableViewer) {

					@Override
					protected void doSetValue(Object element, Object value) {
						((PathItem) element).setTravelTime(Long
								.valueOf((String) value));
					}

					@Override
					protected Object getValue(Object element) {
						return String.valueOf(((PathItem) element)
								.getTravelTime());
					}
				});

		// COMPOSITE 3 Contents
		Button buttonUP = new Button(this, SWT.PUSH);
		buttonUP.setText("Up");
		buttonUP.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonDown = new Button(this, SWT.PUSH);
		buttonDown.setText("Down");
		buttonDown.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonAdd = new Button(this, SWT.PUSH);
		buttonAdd.setText("Add");
		buttonAdd.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button buttonRemove = new Button(this, SWT.PUSH);
		buttonRemove.setText("Remove");
		buttonRemove.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		buttonAdd.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				AddPathItemDialog dialog = new AddPathItemDialog(shell,
						"Add new PathItem", scenario);
				dialog.open();
			}
		});

		buttonRemove.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				Object currentSelection = ((IStructuredSelection) tableViewer
						.getSelection()).getFirstElement();
				scenario.remove(currentSelection);
			}
		});

		buttonUP.addSelectionListener(new SelectionListener() {
			int count = 0;

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				if (!selection.isEmpty()) {
					Object o = selection.getFirstElement();
					if (!ChangeListOrderUtil.moveUp(scenario.getList(), o)) {
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
					tableViewer.refresh();
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
				IStructuredSelection selection = (IStructuredSelection) tableViewer
						.getSelection();
				if (!selection.isEmpty()) {
					Object o = selection.getFirstElement();
					if (!ChangeListOrderUtil.moveDown(scenario.getList(), o)) {
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
					tableViewer.refresh();
				}
			}
		});

	}

	public void setScenario(ScenarioEventAwareWrapper scenario) {
		this.scenario = scenario;
		if (scenario != null) {
			tableViewer.setInput(scenario);
			scenarioIDSpinner.setSelection(scenario.getScenario().getID());
		}
	}

}
