/*
 *  AdapterFactory.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.IActionFilter;
import org.eclipse.ui.model.IWorkbenchAdapter;
import org.eclipse.ui.views.properties.IPropertySource;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.adapters.DefaultEntityFilterAdapter;
import org.rifidi.designer.entities.adapters.SwitchActionFilterAdapter;
import org.rifidi.designer.entities.properties.DefaultPropertySource;
import org.rifidi.designer.library.basemodels.boxproducer.BoxproducerEntity;
import org.rifidi.designer.library.basemodels.boxproducer.BoxproducerEntityWorkbenchAdapter;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntity;
import org.rifidi.designer.library.basemodels.cardbox.CardboxEntityWorkbenchAdapter;
import org.rifidi.designer.library.basemodels.conveyor.ConveyorEntity;
import org.rifidi.designer.library.basemodels.conveyor.ConveyorEntityWorkbenchAdapter;
import org.rifidi.designer.library.basemodels.conveyor90.Conveyor90Entity;
import org.rifidi.designer.library.basemodels.conveyor90.Conveyor90EntityWorkbenchAdapter;
import org.rifidi.designer.library.basemodels.gate.GateEntity;
import org.rifidi.designer.library.basemodels.gate.GateEntityWorkbenchAdapter;
import org.rifidi.designer.library.basemodels.infrared.InfraredEntity;
import org.rifidi.designer.library.basemodels.infrared.InfraredEntityWorkbenchAdapterAdapter;
import org.rifidi.designer.library.basemodels.pusharm.PusharmEntity;
import org.rifidi.designer.library.basemodels.pusharm.PusharmEntityWorkbenchAdapter;

/**
 * AdapterFactory for the primitves library.
 * 
 * @author Jochen Mader Jan 25, 2008
 * @tags
 * 
 */
public class AdapterFactory implements IAdapterFactory {
	/**
	 * Supported adapters.
	 */
	@SuppressWarnings("unchecked")
	private Class[] adapters = new Class[] { IActionFilter.class,
			IWorkbenchAdapter.class, IPropertySource.class };

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapter(java.lang.Object,
	 *      java.lang.Class)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (adaptableObject instanceof BoxproducerEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new BoxproducerEntityWorkbenchAdapter();
			}
			if (IActionFilter.class.equals(adapterType)) {
				return new SwitchActionFilterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		} else if (adaptableObject instanceof CardboxEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new CardboxEntityWorkbenchAdapter();
			}
			if (IActionFilter.class.equals(adapterType)) {
				return new DefaultEntityFilterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		} else if (adaptableObject instanceof ConveyorEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new ConveyorEntityWorkbenchAdapter();
			}
			if (IActionFilter.class.equals(adapterType)) {
				return new SwitchActionFilterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		} else if (adaptableObject instanceof Conveyor90Entity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new Conveyor90EntityWorkbenchAdapter();
			}
			if (IActionFilter.class.equals(adapterType)) {
				return new SwitchActionFilterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		} else if (adaptableObject instanceof GateEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new GateEntityWorkbenchAdapter();
			}
			if (IActionFilter.class.equals(adapterType)) {
				return new SwitchActionFilterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		} else if (adaptableObject instanceof PusharmEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new PusharmEntityWorkbenchAdapter();
			}
			if (IActionFilter.class.equals(adapterType)) {
				return new SwitchActionFilterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		} else if (adaptableObject instanceof InfraredEntity) {
			if (IWorkbenchAdapter.class.equals(adapterType)) {
				return new InfraredEntityWorkbenchAdapterAdapter();
			}
			if (IPropertySource.class.equals(adapterType)) {
				return new DefaultPropertySource((Entity) adaptableObject);
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdapterFactory#getAdapterList()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Class[] getAdapterList() {
		return adapters;
	}

}
