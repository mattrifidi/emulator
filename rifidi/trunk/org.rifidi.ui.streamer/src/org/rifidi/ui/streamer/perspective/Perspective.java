package org.rifidi.ui.streamer.perspective;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.rifidi.ui.streamer.views.batch.BatchView;
import org.rifidi.ui.streamer.views.components.ComponentView;
import org.rifidi.ui.streamer.views.scenario.ScenarioView;
import org.rifidi.ui.streamer.views.testSuite.TestSuiteView;
import org.rifidi.ui.streamer.views.testUnit.TestUnitView;

/**
 * Layout for the TagStreamer2 UI Application
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class Perspective implements IPerspectiveFactory {

	public static final String ID="org.rifidi.ui.streamer.perspective";
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IPerspectiveFactory#createInitialLayout(org.eclipse.ui.IPageLayout)
	 */
	public void createInitialLayout(IPageLayout layout) {

		// Disable Editor because we don't need it
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		// Add the TestSuiteView it's the main part of the application so we
		// don't want it to be closeable
		layout.addStandaloneView(TestSuiteView.ID, false, IPageLayout.LEFT,
				0.25f, editorArea);

		// Add a Placeholder for all the other views
		IFolderLayout mainFolder = layout.createFolder("mainFolder",
				IPageLayout.RIGHT, 0.75f, editorArea);

		mainFolder.addPlaceholder(BatchView.ID);
		mainFolder.addPlaceholder(ComponentView.ID);
		mainFolder.addPlaceholder(ScenarioView.ID);
		mainFolder.addPlaceholder(TestUnitView.ID);

		// For Testing purposes
		// mainFolder.addView(ScenarioView.ID);
		// mainFolder.addView(BatchView.ID);
		// mainFolder.addView(TestUnitView.ID);
		// mainFolder.addView(ComponentView.ID);

	}
}
