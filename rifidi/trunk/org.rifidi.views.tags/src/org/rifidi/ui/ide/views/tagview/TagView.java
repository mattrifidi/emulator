package org.rifidi.ui.ide.views.tagview;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.impl.RifidiTag;
import org.rifidi.services.tags.registry.ITagRegistry;
import org.rifidi.ui.ide.views.tagview.model.TagViewContentProvider;
import org.rifidi.ui.ide.views.tagview.model.TagViewLabelProvider;

/**
 * This is the TagView displaying the new created tags in a table. It's also a
 * source for the drag&drop functionality to the AntennaView.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class TagView extends ViewPart {

	private Log logger = LogFactory.getLog(TagView.class);

	public static final String ID = "org.rifidi.ui.ide.views.tagsview.TagsView";

	private TableViewer tableViewer = null;

	private ITagRegistry tagRegistry;

	private Listener selChangeListener = new Listener() {
		public void handleEvent(Event event) {
			tableViewer.getTable().setSortColumn((TableColumn) event.widget);
			tableViewer.refresh();
		}
	};

	public TagView() {
		System.out.println("booom");
		logger.debug("Initializing the Tag Service");
		ServiceRegistry.getInstance().service(this);
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		Layout layout = new FillLayout();
		composite.setLayout(layout);

		tableViewer = new TableViewer(composite, SWT.MULTI | SWT.FULL_SELECTION);
		tableViewer.getTable().setLinesVisible(true);
		tableViewer.getTable().setHeaderVisible(true);

		String[] columnNames = new String[] { "Gen", "Data Type", "Tag ID" };
		int[] columnWidths = new int[] { 50, 100, 100 };
		int[] columnAlignments = new int[] { SWT.LEFT, SWT.LEFT, SWT.LEFT };

		List<TableColumn> tableColumns = new ArrayList<TableColumn>();
		// create columns and add listeners to them
		for (int i = 0; i < columnNames.length; i++) {
			TableColumn tableColumn = new TableColumn(tableViewer.getTable(),
					columnAlignments[i]);
			tableColumn.setText(columnNames[i]);
			tableColumn.setWidth(columnWidths[i]);
			// Add Listener
			tableColumn.addListener(SWT.Selection, this.selChangeListener);
			tableColumns.add(tableColumn);
		}

		// enable drag'n'drop
		int ops = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[] { TextTransfer.getInstance() };
		tableViewer.addDragSupport(ops, transfers, new DragSourceListener() {

			public void dragStart(DragSourceEvent event) {

			}

			@SuppressWarnings("unchecked")
			public void dragSetData(DragSourceEvent event) {
				if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
					StringBuilder sb = new StringBuilder();
					Iterator iter = ((IStructuredSelection) (tableViewer
							.getSelection())).iterator();
					// get the selected tags and add their ids to a String
					sb.append("RIFIDITAGS\n");
					while (iter.hasNext()) {
						sb.append(((RifidiTag) iter.next()).getTagEntitiyID()
								+ "\n");
					}
					event.data = sb.toString();
				}
			}

			public void dragFinished(DragSourceEvent event) {
			}
		});

		// tableViewer.add
		tableViewer.setLabelProvider(new TagViewLabelProvider());
		TagViewContentProvider provider = new TagViewContentProvider();
		tableViewer.setContentProvider(provider);
		tagRegistry.addPropertyChangeListener(provider);
		tableViewer.setInput(tagRegistry);

		getSite().setSelectionProvider(tableViewer);

	}

	@Override
	public void setFocus() {

	}

	public void refresh() {
		tableViewer.refresh();
	}

	/**
	 * @return the tableViewer
	 */
	public TableViewer getTableViewer() {
		return tableViewer;
	}

	@Inject
	public void setTagRegistryService(ITagRegistry tagRegisrty) {
		this.tagRegistry = tagRegisrty;

	}

}
