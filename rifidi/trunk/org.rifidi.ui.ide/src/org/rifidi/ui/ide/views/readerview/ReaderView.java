package org.rifidi.ui.ide.views.readerview;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.registry.RegistryChangeListener;
import org.rifidi.ui.ide.views.antennaview.AntennaView;
import org.rifidi.ui.ide.views.readerview.model.ReaderViewContentProvider;
import org.rifidi.ui.ide.views.readerview.model.ReaderViewLabelProvider;
import org.rifidi.ui.ide.views.readerview.model.UIReaderAdapterFactory;

/**
 * This View is representing the UIReader of the ReaderRegistry in a TreeView
 * and provides the appropriate actions for the Reader
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderView extends ViewPart implements RegistryChangeListener {

	private Log logger = LogFactory.getLog(ReaderView.class);

	public static final String ID = "org.rifidi.ui.ide.views.readerview.ReaderView";
	private TreeViewer treeViewer = null;

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {

		Platform.getAdapterManager().registerAdapters(
				new UIReaderAdapterFactory(), UIReader.class);

		treeViewer = new TreeViewer(parent);

		MenuManager menuMgr = new MenuManager("Reader State Menu");
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				// Make Extensions available
				mgr
						.add(new GroupMarker(
								IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});

		treeViewer.setContentProvider(new ReaderViewContentProvider());
		treeViewer.setLabelProvider(new ReaderViewLabelProvider());

		// Register Selection Provider
		getSite().setSelectionProvider(treeViewer);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, treeViewer);

		//getSite().getPage().
		
		ReaderRegistry registry = ReaderRegistry.getInstance();
		registry.addListener(this);
		treeViewer.setInput(registry);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {

	}

	/**
	 * Get the TreeViewer of the ReaderView
	 * 
	 * @return the treeViewer
	 */
	// Needs to be exposed because we want to listen for KeyEvents
	// (RemoveReaderActionDelegate)
	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	/* (non-Javadoc)
	 * @see org.rifidi.ui.common.registry.RegistryChangeListener#add(java.lang.Object)
	 */
	public void add(Object event) {
		logger.debug("Add Event called... try to select reader");
		treeViewer.refresh();
		if (event.getClass().equals(UIReader.class)) {
			TreeSelection selection = new TreeSelection(new TreePath(
					new Object[] { event }));
			treeViewer.setSelection(selection, true);
		}
	}

	/* (non-Javadoc)
	 * @see org.rifidi.ui.common.registry.RegistryChangeListener#remove(java.lang.Object)
	 */
	public void remove(Object event) {
		logger.debug("Remove Reader Event");
		treeViewer.refresh();
	}

	/* (non-Javadoc)
	 * @see org.rifidi.ui.common.registry.RegistryChangeListener#update(java.lang.Object)
	 */
	public void update(Object event) {
		IWorkbenchPage workbenchPage = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();

		treeViewer.refresh();
		if (event.getClass().equals(UIReader.class)) {
			TreeSelection selection = new TreeSelection(new TreePath(
					new Object[] { event }));
			treeViewer.setSelection(selection, true);
		}
		IViewReference antennaRef = workbenchPage.findViewReference(
				AntennaView.ID, ((UIReader) event).getReaderName());
		IViewReference consoleRef = workbenchPage.findViewReference(
				AntennaView.ID, ((UIReader) event).getReaderName());

		antennaRef.getView(false);
		consoleRef.getView(false);
	}
}
