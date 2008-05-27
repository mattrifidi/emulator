/**
 * 
 */
package org.rifidi.ui.streamer.composites;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.rifidi.streamer.xml.components.ReaderComponent;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.streamer.data.ReaderCompositeContentProvider;

/**
 * This is the composite containing all fields to display a ReaderComponent and
 * make it modifiable
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderComposite extends Composite {

	private ReaderComponent component;
	private Spinner readerIDSpinner;
	private Text readerNameText;
	private Spinner numAntennasSpinner;
	private Spinner numGPISpinner;
	private Spinner numGPOSpinner;
	private TableViewer tableViewer;

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

	public ReaderComposite(Composite parent, int style, boolean isWizard) {
		super(parent, style);

		GridLayout layout = new GridLayout(2, false);
		this.setLayout(layout);

		Label readerIDLabel = new Label(this, SWT.NONE);
		readerIDLabel.setText("Component ID:");
		readerIDSpinner = new Spinner(this, SWT.BORDER);
		readerIDSpinner.setMaximum(1000);
		readerIDSpinner.setEnabled(isWizard);

		Label readerNameLabel = new Label(this, SWT.NONE);
		readerNameLabel.setText("Reader Name");
		readerNameText = new Text(this, SWT.BORDER);
		readerNameText.setTextLimit(50);
		// Set the size of the Text Widget
		int characters = 30; // should be 5 but 6 because of average size
		GC gc = new GC(readerNameText);
		FontMetrics fm = gc.getFontMetrics();
		int width = characters * fm.getAverageCharWidth();
		int height = fm.getHeight();
		gc.dispose();
		GridData prefixTextGridData = new GridData(width, height);
		readerNameText.setLayoutData(prefixTextGridData);

		Label numAntennasLabel = new Label(this, SWT.NONE);
		numAntennasLabel.setText("Number Antennas");
		numAntennasSpinner = new Spinner(this, SWT.BORDER);
		numAntennasSpinner.setMaximum(10);

		Label numGPILabel = new Label(this, SWT.NONE);
		numGPILabel.setText("Number GPI's");
		numGPISpinner = new Spinner(this, SWT.BORDER);
		numGPISpinner.setMaximum(10);

		Label numGPOLabel = new Label(this, SWT.NONE);
		numGPOLabel.setText("Number GPO's");
		numGPOSpinner = new Spinner(this, SWT.BORDER);
		numGPOSpinner.setMaximum(10);

		Group tableGroup = new Group(this, SWT.NONE);
		tableGroup.setText("Properties");
		GridData propertiesTableGridData = new GridData(GridData.FILL_BOTH);
		propertiesTableGridData.horizontalSpan = 2;
		tableGroup.setLayoutData(propertiesTableGridData);

		FillLayout fillLayout = new FillLayout();
		fillLayout.spacing = 5;
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		tableGroup.setLayout(fillLayout);

		tableViewer = new TableViewer(tableGroup, SWT.BORDER);
		tableViewer.setContentProvider(new ReaderCompositeContentProvider());

		// TableColumn
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);
		String[] columnNames = new String[] { "Key", "Value" };
		int[] columnWidths = new int[] { 200, 200 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT };
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

			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				Entry<String, String> entry = (Map.Entry<String, String>) element;
				return entry.getKey();
			}

		});

		tableColumns.get(1).setLabelProvider(new ColumnLabelProvider() {

			@SuppressWarnings("unchecked")
			@Override
			public String getText(Object element) {
				Entry<String, String> entry = (Map.Entry<String, String>) element;
				return entry.getValue();
			}

		});

		tableColumns.get(1).setEditingSupport(
				new AbstractEditingSupport(tableViewer) {

					@SuppressWarnings("unchecked")
					@Override
					protected void doSetValue(Object element, Object value) {
						Entry<String, String> entry = (Map.Entry<String, String>) element;
						entry.setValue((String) value);
					}

					@SuppressWarnings("unchecked")
					@Override
					protected Object getValue(Object element) {
						Entry<String, String> entry = (Map.Entry<String, String>) element;
						return entry.getValue();
					}

				});

	}

	public void addChangeListeners() {
		Listener modifyListener = new Listener() {

			@Override
			public void handleEvent(Event event) {
				saveChanges();
			}

		};
		readerNameText.addListener(SWT.Modify, modifyListener);
		numAntennasSpinner.addListener(SWT.Modify, modifyListener);
		numGPISpinner.addListener(SWT.Modify, modifyListener);
		numGPOSpinner.addListener(SWT.Modify, modifyListener);
	}

	public void setComponent(ReaderComponent component) {
		this.component = component;
		updateWidgets();

		// Change Listeners
		addChangeListeners();
	}

	private void updateWidgets() {
		readerIDSpinner.setSelection(component.getID());
		UIReader reader = component.getUIReader();
		readerNameText.setText(reader.getReaderName());
		numAntennasSpinner.setSelection(reader.getNumAntennas());
		numGPISpinner.setSelection(reader.getNumGPIs());
		numGPOSpinner.setSelection(reader.getNumGPOs());

		tableViewer.setInput(reader);
	}

	private void saveChanges() {
		// component.setID(readerIDSpinner.getSelection());
		UIReader reader = component.getUIReader();
		reader.setReaderName(readerNameText.getText());
		reader.setNumAntennas(numAntennasSpinner.getSelection());
		reader.setNumGPIs(numGPISpinner.getSelection());
		reader.setNumGPOs(numGPOSpinner.getSelection());
	}
}
