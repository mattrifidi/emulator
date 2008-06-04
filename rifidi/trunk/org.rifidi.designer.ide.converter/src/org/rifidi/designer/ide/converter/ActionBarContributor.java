/*
 *  ActionBarContributor.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.ide.converter;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.rifidi.designer.ide.converter.actions.SaveAsJMEAction;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Jun 4, 2008
 * 
 */
public class ActionBarContributor extends EditorActionBarContributor {

	private IEditorPart iEditorPart;
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#setActiveEditor(org.eclipse.ui.IEditorPart)
	 */
	@Override
	public void setActiveEditor(IEditorPart targetEditor) {
		super.setActiveEditor(targetEditor);
		iEditorPart=targetEditor;
	}

	/**
	 * 
	 */
	public ActionBarContributor() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.EditorActionBarContributor#contributeToToolBar(org.eclipse.jface.action.IToolBarManager)
	 */
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
		super.contributeToToolBar(toolBarManager);
//		toolBarManager.add(new SaveAsJMEAction("blubb"));
	}

}
