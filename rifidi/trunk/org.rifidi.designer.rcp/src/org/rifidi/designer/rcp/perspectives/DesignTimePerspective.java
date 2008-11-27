package org.rifidi.designer.rcp.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.rifidi.designer.rcp.views.entityview.EntityView;
import org.rifidi.designer.rcp.views.libraryview.LibraryView;
import org.rifidi.designer.rcp.views.minimapview.MiniMapView;
import org.rifidi.designer.rcp.views.view3d.View3D;

public class DesignTimePerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		IFolderLayout leftFolder = layout.createFolder("leftFolder",
				IPageLayout.LEFT, 0.25f, layout.getEditorArea());
		leftFolder.addView(EntityView.ID);
		leftFolder.addView(LibraryView.ID);
		layout.addStandaloneView(View3D.ID, false, IPageLayout.RIGHT, 0f,
				layout.getEditorArea());
		layout.addStandaloneView(MiniMapView.ID, true, IPageLayout.BOTTOM,
				0.55f, EntityView.ID);
		IFolderLayout bottomFolder = layout.createFolder("bottomFolder",
				IPageLayout.BOTTOM, 0.75f, View3D.ID);
		bottomFolder.addView(IPageLayout.ID_PROP_SHEET);
		bottomFolder.addView("org.eclipse.ui.console.ConsoleView");
		bottomFolder.addView("org.rifidi.ui.ide.views.tagsview");
	}
}
