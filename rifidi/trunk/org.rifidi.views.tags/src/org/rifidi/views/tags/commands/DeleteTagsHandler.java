/*
 *  DeleteTagsHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.views.tags.commands;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.handlers.HandlerUtil;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.tags.impl.RifidiTag;
import org.rifidi.ui.ide.views.tagview.TagView;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Jan 12, 2009
 * 
 */
public class DeleteTagsHandler extends AbstractHandler {

	/** Reference to the tag service. */
	private IRifidiTagService tagService;

	/**
	 * Constructor.
	 */
	public DeleteTagsHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		MessageBox messageBox = new MessageBox(Display.getCurrent()
				.getActiveShell(), SWT.OK | SWT.CANCEL | SWT.ICON_WARNING);
		messageBox.setMessage("Do you really want to delete these Tags?");
		messageBox.setText("Warning");

		if (messageBox.open() == SWT.OK) {
			Iterator<?> iterator = ((IStructuredSelection) HandlerUtil
					.getCurrentSelectionChecked(arg0)).iterator();
			Set<RifidiTag> tags=new HashSet<RifidiTag>();
			while(iterator.hasNext()){
				tags.add((RifidiTag)iterator.next());
			}
			tagService.deleteTags(tags);
		}
		return null;
	}

	/**
	 * @param tagService
	 *            the tagService to set
	 */
	@Inject
	public void setTagService(IRifidiTagService tagService) {
		this.tagService = tagService;
	}

}
