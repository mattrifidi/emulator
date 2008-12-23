/*
 *  TagViewPart.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.views.tags;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.RifidiTagServiceChangeListener;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.ui.ide.views.tagview.model.TagViewContentProvider;
import org.rifidi.ui.ide.views.tagview.model.TagViewLabelProvider;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 19, 2008
 * 
 */
public class TagViewPart extends ViewPart implements
		RifidiTagServiceChangeListener {

	public static final String ID = "org.rifidi.views.tags.tagview2";

	private TableViewer tableViewer = null;

	private IRifidiTagService tagService;

	private Listener selChangeListener = new Listener() {
		public void handleEvent(Event event) {
			tableViewer.getTable().setSortColumn((TableColumn) event.widget);
			tableViewer.refresh();
		}
	};

	/**
	 * 
	 */
	public TagViewPart() {
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite composite) {
		composite.setLayout(new FillLayout());
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
		getSite().setSelectionProvider(tableViewer);
		tableViewer.setInput(tagService);
		tagService.addRifidiTagServiceChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	/**
	 * @param tagService
	 *            the tagService to set
	 */
	@Inject
	public void setTagService(IRifidiTagService tagService) {
		this.tagService = tagService;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.services.tags.RifidiTagServiceChangeListener#tagsChanged()
	 */
	@Override
	public void tagsChanged() {
		tableViewer.refresh();
	}

}
