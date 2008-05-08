/**
 * 
 */
package org.rifidi.ui.ide.editors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.rifidi.ui.common.reader.UIReader;
import org.rifidi.ui.ide.views.antennaview.GPIOPGroup;
import org.rifidi.ui.ide.views.antennaview.TagPGroup;

/**
 * ReaderDetails Editor - This shows the antennas of the selected reader
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ReaderEditor extends EditorPart {

	public static final String ID = "org.rifidi.ui.ide.editors.ReaderEditor";

	private Log logger = LogFactory.getLog(ReaderEditor.class);

	private IEditorSite site;
	private ReaderEditorInput input;
	private UIReader reader;

	private ScrolledComposite mainComposite;
	private CTabItem[] tabItems;
	private Composite gpioChild;

	private CTabFolder tabFolder;

	/**
	 * Default Constructor
	 */
	public ReaderEditor() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// Not needed yet
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {
		// Not needed yet
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		this.site = site;
		this.input = (ReaderEditorInput) input;
		this.reader = this.input.getUIReader();
		// Eclipse specific code to make the editor work properly
		setSite(site);
		setInput(input);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isDirty()
	 */
	@Override
	public boolean isDirty() {
		// As no save function is implemented always false
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		// As no save function is implemented always false
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new FillLayout());

		// Set the title to the name of the reader
		this.setPartName(reader.getReaderName());
		// Number of Antennas of this Reader
		int numAnntennas = reader.getNumAntennas();

		// main composite with scroll support
		mainComposite = new ScrolledComposite(parent, SWT.H_SCROLL
				| SWT.V_SCROLL);
		mainComposite.setLayout(new GridLayout());
		mainComposite
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		mainComposite.setExpandHorizontal(true);
		mainComposite.setExpandVertical(true);

		// Create a Folder for the Antenna tabs
		tabFolder = new CTabFolder(mainComposite, SWT.BOTTOM);
		tabFolder.setLayout(new GridLayout());
		tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		tabFolder.setSimple(false);
		// Create the TabItems for each antenna
		tabItems = new CTabItem[numAnntennas];
		for (int i = 0; i < numAnntennas; i++) {
			CTabItem item = new CTabItem(tabFolder, SWT.NONE);
			item.setText("Antenna #" + i);
			Composite composite = new Composite(tabFolder, SWT.NONE);
			composite
					.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			composite.setLayout(new FillLayout());
			TagPGroup tagcomposite = new TagPGroup(composite, SWT.SMOOTH,
					reader.getAntenna(i));
			tagcomposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
					true));
			item.setControl(composite);
		}
		// Select the first antenna tab to be displayed
		tabFolder.setSelection(0);

		// Create GPIO Composite
		if (reader.getNumGPIs() > 0 || reader.getNumGPOs() > 0) {
			gpioChild = new Composite(mainComposite, SWT.NONE);
			gpioChild
					.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
			gpioChild.setLayout(new GridLayout());
			GPIOPGroup gpioView = new GPIOPGroup(gpioChild, SWT.SMOOTH, reader);
			gpioView
					.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		}
		// getSite().setSelectionProvider(this);

		// Set the content of the scrolled composite
		mainComposite.setContent(tabFolder);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	@Override
	public void setFocus() {
	}

	public void switchView() {
		if (reader.getNumGPIs() > 0 || reader.getNumGPOs() > 0) {
			if (mainComposite.getContent().equals(gpioChild)) {
				mainComposite.setContent(tabFolder);
			} else {
				mainComposite.setContent(gpioChild);
			}
			mainComposite.update();
		}
	}

}
