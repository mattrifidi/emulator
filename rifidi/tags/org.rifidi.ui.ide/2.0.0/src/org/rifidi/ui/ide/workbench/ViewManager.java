/**
 * 
 */
package org.rifidi.ui.ide.workbench;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.common.registry.ReaderRegistry;
import org.rifidi.ui.common.registry.RegistryChangeListener;
import org.rifidi.ui.ide.editors.ReaderEditor;
import org.rifidi.ui.ide.editors.ReaderEditorInput;
import org.rifidi.ui.ide.views.consoleview.ConsoleView;
import org.rifidi.ui.ide.views.readerview.ReaderView;

/**
 * This Manager listens for events like a reader add event. If it detects a new
 * Reader it will create the AntennaView and the ConsoleView for that reader. It
 * also listens for selection changes in the ReaderView to show the associated
 * Views.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ViewManager implements RegistryChangeListener {

	// private static Log logger =
	// LogFactory.getLog(RegistryChangeListener.class);

	private ReaderRegistry readerRegistry;
	private IWorkbenchWindow window;

	public ViewManager(IWorkbenchWindow window) {
		this.window = window;
		this.readerRegistry = ReaderRegistry.getInstance();
		readerRegistry.addListener(this);

		// Register a listener in the ReaderViews SelectionService to bring up
		// the actual selected Reader
		// window.getActivePage().addSelectionListener(ReaderView.ID,
		// new ISelectionListener() {
		//
		// public void selectionChanged(IWorkbenchPart part,
		// ISelection selection) {
		//
		// Object o = ((IStructuredSelection) selection)
		// .getFirstElement();
		// if (o instanceof UIReader) {
		// UIReader reader = (UIReader) o;
		// if (reader != null) {
		// TODO DoubleClickListener
		// updateSelectedView(reader);
		// }
		// }
		// }
		//
		// });

		TreeViewer viewer = ((ReaderView) window.getActivePage().findView(
				ReaderView.ID)).getTreeViewer();
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();
				if (selection.getFirstElement().getClass().equals(
						UIReader.class)) {
					UIReader selectedReader = (UIReader) selection
							.getFirstElement();

					add(selectedReader);
				}
			}
		});
	}

	public void add(Object event) {
		UIReader reader = (UIReader) event;

		// Initialize or show the ReaderEditor for this reader
		try {
			// window.getActivePage().showView(AntennaView.ID,
			// reader.getReaderName(), IWorkbenchPage.VIEW_ACTIVATE);
			window.getActivePage().openEditor(
					new ReaderEditorInput((UIReader) event), ReaderEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		// Initialize or show the ConsoleView for this reader
		try {
			window.getActivePage().showView(ConsoleView.ID,
					reader.getReaderName(), IWorkbenchPage.VIEW_ACTIVATE);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

		// Select ReadView again
		window.getActivePage().activate(
				window.getActivePage().findView(ReaderView.ID));

	}

	public void remove(Object event) {
		UIReader reader = (UIReader) event;

		IWorkbenchPage workbenchPage = window.getActivePage();

		IViewReference consoleView = workbenchPage.findViewReference(
				ConsoleView.ID, reader.getReaderName());
		if (consoleView != null) {
			((ConsoleView) consoleView.getView(false))
					.stopConsoleUpdaterThread();
		}

		IEditorPart editorToClose = workbenchPage
				.findEditor(new ReaderEditorInput((UIReader) event));
		// Hide AntennaView for this reader
		// workbenchPage.hideView(workbenchPage.findViewReference(AntennaView.ID,
		// reader.getReaderName()));
		if (editorToClose != null)
			workbenchPage.closeEditor(editorToClose, false);
		// Hide ConsoleView for this reader
		workbenchPage.hideView(workbenchPage.findViewReference(ConsoleView.ID,
				reader.getReaderName()));

		// Select ReadView again
		window.getActivePage().activate(
				window.getActivePage().findView(ReaderView.ID));
	}

	public void update(Object event) {
		// Select ReadView again
		window.getActivePage().activate(
				window.getActivePage().findView(ReaderView.ID));
	}

	public void updateSelectedView(UIReader reader) {
		// bring up the associated Views
		this.add(reader);
	}
}
