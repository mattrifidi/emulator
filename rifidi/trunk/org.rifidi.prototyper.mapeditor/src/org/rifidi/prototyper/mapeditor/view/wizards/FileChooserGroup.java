/**
 * 
 */
package org.rifidi.prototyper.mapeditor.view.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.DrillDownComposite;

/**
 * This is a composite for displaying files in the workspace
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class FileChooserGroup extends Composite {

	private TreeViewer treeViewer;
	// Last selection made by user
	private IFile selectedFile;
	// handle on parts
	private Text fileNameField;
	// Enable user to type in new container name
	private boolean allowNewFileName = false;
	// The listener to notify of events
	private Listener listener;

	/**
	 * @param parent
	 * @param style
	 */
	public FileChooserGroup(Composite parent, Listener listener) {
		super(parent, SWT.None);
		this.listener = listener;
		createContents();
	}

	/**
	 * Creates the contents of the composite.
	 */
	public void createContents() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		setLayout(layout);
		setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(this, SWT.WRAP);
		label.setText("Pick A Rifidi Prototyper File");
		label.setFont(this.getFont());

		if (allowNewFileName) {
			fileNameField = new Text(this, SWT.SINGLE | SWT.BORDER);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 200;
			fileNameField.setLayoutData(gd);
			fileNameField.addListener(SWT.Modify, listener);
			fileNameField.setFont(this.getFont());
		} else {
			// filler...
			new Label(this, SWT.NONE);
		}

		createTreeViewer();
		Dialog.applyDialogFont(this);
	}

	/**
	 * Returns a new drill down viewer for this dialog.
	 * 
	 * @param heightHint
	 *            height hint for the drill down composite
	 */
	protected void createTreeViewer() {
		// Create drill down.
		DrillDownComposite drillDown = new DrillDownComposite(this, SWT.BORDER);
		GridData spec = new GridData(SWT.FILL, SWT.FILL, true, true);
		spec.widthHint = 400;
		spec.heightHint = 300;
		drillDown.setLayoutData(spec);

		// Create tree viewer inside drill down.
		treeViewer = new TreeViewer(drillDown, SWT.NONE);
		drillDown.setChildTree(treeViewer);
		PrototyperProjectContentProvider cp = new PrototyperProjectContentProvider();
		treeViewer.setContentProvider(cp);
		treeViewer.setLabelProvider(WorkbenchLabelProvider
				.getDecoratingWorkbenchLabelProvider());
		treeViewer.setComparator(new ViewerComparator());
		treeViewer.setUseHashlookup(true);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event
						.getSelection();

				if (selection.getFirstElement() instanceof IFile) {
					fileSelectionChanged((IFile) selection.getFirstElement());
				} else {
					fileSelectionChanged(null);
				}
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				ISelection selection = event.getSelection();
				if (selection instanceof IStructuredSelection) {
					Object item = ((IStructuredSelection) selection)
							.getFirstElement();
					if (item == null) {
						return;
					}
					if (treeViewer.getExpandedState(item)) {
						treeViewer.collapseToLevel(item, 1);
					} else {
						treeViewer.expandToLevel(item, 1);
					}
				}
			}
		});

		// This has to be done after the viewer has been laid out
		treeViewer.setInput(ResourcesPlugin.getWorkspace());
		treeViewer.expandAll();
	}

	/**
	 * The container selection has changed in the tree view. Update the
	 * container name field value and notify all listeners.
	 * 
	 * @param file
	 *            The container that changed
	 */
	public void fileSelectionChanged(IFile file) {
		selectedFile = file;

		if (allowNewFileName) {
			if (file == null) {
				fileNameField.setText("");//$NON-NLS-1$
			} else {
				String text = TextProcessor.process(file.getFullPath()
						.makeRelative().toString());
				fileNameField.setText(text);
				fileNameField.setToolTipText(text);
			}
		}

		// fire an event so the parent can update its controls
		if (listener != null) {
			Event changeEvent = new Event();
			changeEvent.type = SWT.Selection;
			changeEvent.widget = this;
			changeEvent.data = file;
			listener.handleEvent(changeEvent);
		}
	}

}
