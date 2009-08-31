/**
 * 
 */
package org.rifidi.emulator.readerview.views;

import java.util.concurrent.Semaphore;

import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.ui.common.registry.ReaderRegistryService;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ReaderView extends ViewPart {

	public static final String ID = "org.rifidi.emulator.readerview.view";
	private AbstractTreeViewer treeViewer;
	private ReaderRegistryService readerRegistry;
	private Semaphore s = new Semaphore(1);

	public ReaderView() {
		ServiceRegistry.getInstance().service(this);
	}

	/**
	 * @param readerRegistry
	 *            the readerRegistry to set
	 */
	@Inject
	public void setReaderRegistry(final ReaderRegistryService readerRegistry) {
		this.readerRegistry = readerRegistry;
		try {
			s.acquire();
			if (treeViewer != null) {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						treeViewer.setInput(readerRegistry);

					}
				});
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			s.release();
		}
	}

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
		treeViewer.setContentProvider(new ReaderViewContentProvider());
		treeViewer.setLabelProvider(new ReaderViewLabelProvider());
		//treeViewer.setComparator(new ViewerComparator());
		try {
			s.acquire();
			if (readerRegistry != null) {
				treeViewer.setInput(readerRegistry);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} finally {
			s.release();
		}
		createContextMenu();
		this.getSite().setSelectionProvider(treeViewer);

	}

	/**
	 * Create a context menu for this viewer
	 */
	private void createContextMenu() {
		// Create menu manager.
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				mgr
						.add(new GroupMarker(
								IWorkbenchActionConstants.MB_ADDITIONS));
			}
		});

		// Create menu.
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		// Register menu for extension.
		getSite().registerContextMenu(menuMgr, treeViewer);
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

}
