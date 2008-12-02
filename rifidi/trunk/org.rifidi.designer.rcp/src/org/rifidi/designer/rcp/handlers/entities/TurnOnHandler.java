/*
 *  NewSceneDataHandler.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.rcp.handlers.entities;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.services.IEvaluationService;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.grouping.EntityGroup;
import org.rifidi.designer.entities.interfaces.IHasSwitch;
import org.rifidi.services.registry.ServiceRegistry;

/**
 * Handler for turning on the entities that are currently selected in the
 * selection provider.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 17, 2008
 * 
 */
public class TurnOnHandler extends AbstractHandler {

	/**
	 * Constructor.
	 */
	public TurnOnHandler() {
		super();
		ServiceRegistry.getInstance().service(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		Iterator iterator = ((IStructuredSelection) HandlerUtil
				.getCurrentSelectionChecked(arg0)).iterator();

		while (iterator.hasNext()) {
			Object sw = iterator.next();
			if (sw instanceof EntityGroup) {
				for (Entity entity : ((EntityGroup) sw).getEntities()) {
					if (entity instanceof IHasSwitch) {
						((IHasSwitch) entity).turnOn();
					}
				}
			} else if (sw instanceof IHasSwitch) {
				((IHasSwitch) sw).turnOn();
			}
		}
		IEvaluationService service = (IEvaluationService) PlatformUI
				.getWorkbench().getService(IEvaluationService.class);
		service.requestEvaluation("org.rifidi.designer.rcp.entities.running");
		return null;
	}

}
