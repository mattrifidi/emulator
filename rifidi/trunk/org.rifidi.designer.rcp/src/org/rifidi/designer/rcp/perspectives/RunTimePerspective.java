package org.rifidi.designer.rcp.perspectives;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.rifidi.designer.rcp.views.view3d.View3D;
/*
 * FIXME: Class comment and header.  
 */
public class RunTimePerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.addStandaloneView(View3D.ID, false, IPageLayout.RIGHT, 1.0f,
				layout.getEditorArea());
	}
}
