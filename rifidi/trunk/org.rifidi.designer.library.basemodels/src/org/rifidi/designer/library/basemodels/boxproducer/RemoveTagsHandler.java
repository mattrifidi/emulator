/*
 *  RemoveTagsHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.boxproducer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.rifidi.designer.entities.internal.RifidiTagWithParent;
import org.rifidi.services.annotations.Inject;
import org.rifidi.services.registry.ServiceRegistry;
import org.rifidi.services.tags.IRifidiTagService;
import org.rifidi.services.tags.model.IRifidiTagContainer;
import org.rifidi.tags.impl.RifidiTag;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 24, 2008
 * 
 */
public class RemoveTagsHandler extends AbstractHandler {
	/** Logger for this class. */
	private static final Log logger = LogFactory
			.getLog(RemoveTagsHandler.class);
	/** Reference to the tag service. */
	private IRifidiTagService tagService;

	/**
	 * 
	 */
	public RemoveTagsHandler() {
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
		Map<IRifidiTagContainer, Set<RifidiTag>> toRelease = new HashMap<IRifidiTagContainer, Set<RifidiTag>>();
		if (arg0.getApplicationContext() instanceof IEvaluationContext) {
			//collect the selected tags and pair them with their parents
			for (Object selected : (List<?>) ((IEvaluationContext) arg0
					.getApplicationContext()).getDefaultVariable()) {
				if (selected instanceof RifidiTagWithParent) {
					if (toRelease.get(((RifidiTagWithParent) selected).parent) == null) {
						toRelease.put(((RifidiTagWithParent) selected).parent,
								new HashSet<RifidiTag>());
					}
					toRelease.get(((RifidiTagWithParent) selected).parent).add(
							((RifidiTagWithParent) selected).tag);
				}
			}
			//release the tags
			for (IRifidiTagContainer container : toRelease.keySet()) {
				container.removeTags(toRelease.get(container));
				tagService.releaseRifidiTags(toRelease.get(container),
						container);
			}
			return null;
		}
		logger.error("Wrong application context type: "
				+ arg0.getApplicationContext().getClass());
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
