/*
 *  IEntityObservable.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.databinding;

import org.eclipse.core.databinding.observable.list.IListChangeListener;

/**
 * Used for entities that contain a list that should be monitored.
 * 
 * @author Jochen Mader - jochen@pramari.com - Oct 28, 2008
 * 
 */
public interface IEntityObservable {
	public void addListChangeListener(IListChangeListener changeListener);
	public void removeListChangeListener(IListChangeListener changeListener);
}
