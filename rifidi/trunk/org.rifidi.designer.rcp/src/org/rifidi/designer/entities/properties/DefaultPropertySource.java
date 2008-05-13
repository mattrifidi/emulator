/*
 *  DefaultPropertySource.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.properties;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.annotations.Property;

/**
 * This PorpertySource is used by every entity. It uses annotations to get
 * information about properties.
 * 
 * @see Property
 * @author Jochen Mader
 * 
 */
public class DefaultPropertySource implements IPropertySource {
	/**
	 * Logger for this class.
	 */
	private static Log logger = LogFactory.getLog(DefaultPropertySource.class);
	/**
	 * List of property descriptors in use.
	 */
	private List<PropertyDescriptor> propertyDescriptors;
	/**
	 * The entity this property source is responsible for.
	 */
	private Entity entity;

	/**
	 * Constructor.
	 * @param entity
	 */
	public DefaultPropertySource(Entity entity) {
		this.entity = entity;
		try {
			propertyDescriptors = new ArrayList<PropertyDescriptor>();
			for (java.beans.PropertyDescriptor prop : Introspector.getBeanInfo(
					entity.getClass()).getPropertyDescriptors()) {
				//only check properties that have a setter method
				if (prop.getWriteMethod() != null
						&& prop.getWriteMethod().getAnnotation(Property.class) != null) {
					Property property = prop.getWriteMethod().getAnnotation(
							Property.class);
					String displayName="";
					if (property.unit().length() > 0) {
						displayName=property.displayName()+" ("+property.unit()+")";
					} else {
						displayName=property.displayName();
					}
					if (property.readonly()) {
						propertyDescriptors.add(new PropertyDescriptor(prop,
								displayName));
					} else if (prop.getDisplayName().equals("group")) {
						ComboPropertyDescriptor coProp = new ComboPropertyDescriptor(
								prop, displayName);
						coProp.setLabelProvider(new ComboLabelProvider());
						propertyDescriptors.add(coProp);
					} else if (prop.getPropertyType().equals(String.class)) {
						propertyDescriptors.add(new TextPropertyDescriptor(
								prop, displayName));
					} else if (prop.getPropertyType().equals(Float.class)
							|| prop.getPropertyType().getCanonicalName()
									.equals("float")) {
						propertyDescriptors.add(new FloatPropertyDescriptor(
								prop, displayName));
					} else {
						propertyDescriptors.add(new PropertyDescriptor(prop,
								displayName));
					}
				}
			}
		} catch (IntrospectionException e) {
			logger.fatal(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return entity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor[] ret = new IPropertyDescriptor[propertyDescriptors
				.size()];
		System.arraycopy(propertyDescriptors.toArray(), 0, ret, 0,
				propertyDescriptors.size());
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(final Object id) {
		try {
			return ((java.beans.PropertyDescriptor) id).getReadMethod().invoke(
					entity, (Object[]) null);
		} catch (IllegalArgumentException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		} catch (InvocationTargetException e) {
			logger.error(e);
		}
		return "unable to read value";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
	 */
	public boolean isPropertySet(final Object id) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
	 */
	public void resetPropertyValue(final Object id) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object,
	 *      java.lang.Object)
	 */
	public void setPropertyValue(final Object id, final Object value) {
		if (!((Property) ((java.beans.PropertyDescriptor) id).getWriteMethod()
				.getAnnotation(Property.class)).readonly()) {
			try {
				if ((((java.beans.PropertyDescriptor) id).getWriteMethod()
						.getParameterTypes()[0]).getCanonicalName().equals(
						"float")) {
					((java.beans.PropertyDescriptor) id).getWriteMethod()
							.invoke(
									entity,
									new Object[] { Float
											.parseFloat((String) value) });
				} else {
					((java.beans.PropertyDescriptor) id).getWriteMethod()
							.invoke(entity, new Object[] { value });
				}
			} catch (IllegalArgumentException e) {
				logger.error(e);
			} catch (IllegalAccessException e) {
				logger.error(e);
			} catch (InvocationTargetException e) {
				logger.error(e);
			}
		}
	}
}
