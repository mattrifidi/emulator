package org.rifidi.ui.ide.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.rifidi.ui.ide.views.antennaview.AntennaView;
import org.rifidi.ui.ide.views.consoleview.ConsoleView;
import org.rifidi.ui.ide.views.readerview.ReaderView;
import org.rifidi.ui.ide.views.tagview.TagView;

/**
 * This is the initial Layout for the RifidiEmulator. It's creating 4 view
 * areas. The mainFolder and the bottomFolder are space holders for views which
 * will created later.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {

		String editorArea = layout.getEditorArea();
		// layout.setEditorAreaVisible(false);

		layout.addStandaloneView(ReaderView.ID, true, IPageLayout.LEFT, 0.26f,
				editorArea);

		layout.addStandaloneView(TagView.ID, true, IPageLayout.BOTTOM, 0.40f,
				ReaderView.ID);

		// IFolderLayout mainFolder = layout.createFolder("mainFolder",
		// IPageLayout.TOP, 0.666f, editorArea);
		// mainFolder.addPlaceholder(AntennaView.ID + ":*");

		IFolderLayout bottomFolder = layout.createFolder("bottomFolder",
				IPageLayout.BOTTOM, .75f, editorArea);
		bottomFolder.addView(IPageLayout.ID_PROP_SHEET);
		bottomFolder.addPlaceholder(ConsoleView.ID + ":*");

	}
}
