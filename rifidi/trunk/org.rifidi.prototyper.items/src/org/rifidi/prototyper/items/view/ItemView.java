/**
 * 
 */
package org.rifidi.prototyper.items.view;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.prototyper.items.model.ItemDNDSupport;
import org.rifidi.prototyper.items.model.TaggedItem;

/**
 * The view of all the items that have been made
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemView extends ViewPart implements DragSourceListener {

	/** The tree viewer for displaying the model */
	private AbstractTreeViewer treeViewer;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);

		treeViewer = new TreeViewer(parent);
		GridData treeViewerLayoutData = new GridData(GridData.FILL_BOTH);
		treeViewerLayoutData.horizontalSpan = 2;
		treeViewer.getControl().setLayoutData(treeViewerLayoutData);
		treeViewer.setContentProvider(new ItemViewContentProvider());
		treeViewer.setLabelProvider(new ItemViewLabelProvider());
		treeViewer.setComparator(new ViewerComparator());

		treeViewer.setInput(ItemModelProviderSingleton.getModelProvider()
				.getItems());

		treeViewer.addDragSupport(DND.DROP_COPY, new Transfer[] { TextTransfer
				.getInstance() }, this);
		createContextMenu();
		this.getSite().setSelectionProvider(treeViewer);

	}

	/**
	 * Private method to do the work of creating the context menu
	 */
	private void createContextMenu() {
		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		menuManager.addMenuListener(new IMenuListener() {
			@Override
			public void menuAboutToShow(IMenuManager manager) {
				manager.add(new GroupMarker(
						IWorkbenchActionConstants.MB_ADDITIONS));

			}
		});
		Menu menu = menuManager.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuManager, treeViewer);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DragSourceListener#dragFinished(org.eclipse.swt.dnd
	 * .DragSourceEvent)
	 */
	@Override
	public void dragFinished(DragSourceEvent event) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DragSourceListener#dragSetData(org.eclipse.swt.dnd
	 * .DragSourceEvent)
	 */
	@Override
	public void dragSetData(DragSourceEvent event) {
		if (TextTransfer.getInstance().isSupportedType(event.dataType)) {
			ISelection sel = treeViewer.getSelection();
			Object o = ((TreeSelection) sel).getFirstElement();
			event.data = ItemDNDSupport.getTextTranfer(((TaggedItem) o)
					.getTag());
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.dnd.DragSourceListener#dragStart(org.eclipse.swt.dnd.
	 * DragSourceEvent)
	 */
	@Override
	public void dragStart(DragSourceEvent event) {
		ISelection sel = treeViewer.getSelection();
		Object o = ((TreeSelection) sel).getFirstElement();
		if (o instanceof TaggedItem) {
			event.doit = true;
			return;
		}
		event.doit = false;

	}

}
