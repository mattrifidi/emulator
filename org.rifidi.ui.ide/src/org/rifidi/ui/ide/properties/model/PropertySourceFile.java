package org.rifidi.ui.ide.properties.model;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.rifidi.ui.common.reader.UIReader;

/**
 * PropertySourceFile
 * 
 * @author Matthew Dean - matt@pramari.com
 * @author Jeremy Choens - "Ghost" - jeremy@pramari.com
 * @author Andreas Huebner - andreas@pramari.com
 */
// TODO Check if there is some more unused code (Discussion about Properties)
public class PropertySourceFile implements IPropertySource {

	// private static Log logger = LogFactory.getLog(PropertySourceFile.class);

	/**
	 * A "button" property.
	 */
	public static final String BUTTON = "BUTTON"; // @jve:decl-index=0:

	/**
	 * A "read only" property.
	 */
	public static final String READ_ONLY = "READ_ONLY"; // @jve:decl-index=0:

	/**
	 * A "editable" property
	 */
	public static final String EDITABLE = "EDITABLE"; // @jve:decl-index=0:

	/**
	 * The table that contains the properties
	 */
	private HashMap<String, PropertyDescriptor> propertiesTable; // @jve:decl-index=0:

	/**
	 * The table containing all categories
	 */
	private ArrayList<String> categoryList;

	/**
	 * UIReader associated with this PropertyView
	 */
	private UIReader reader = null;

	// /**
	// * ReaderRegistry contains a reference to all existing readers
	// */
	// private ReaderRegistry readerRegistry;

	// /**
	// * Name of the associated Reader
	// */
	// private String readerName = null;

	/**
	 * The factory that contains the readers
	 */
	// private ReaderFactory factory; // @jve:decl-index=0:
	/**
	 * The map of commands bound to their display name
	 */
	// private HashMap<String, CommandObject> commandMap;
	// private AbstractReaderSharedResources readerResources;
	// private ReaderModule module;
	// private PropertySourceAdaptable element;
	// private RPCControllerSingleton controller = null;
	// private ReaderExporter readEx = null;
	// public PropertySourceFile(PropertySourceAdaptable adaptable) {
	// // controller = RPCControllerSingleton.getInstance();
	// // readEx = new ReaderExporter(controller);
	// initializeRMI();
	// readerName = adaptable.getLabel(null);
	// this.setPropertyDescriptors();
	// }
	public PropertySourceFile(UIReader adaptable) {
		// controller = RPCControllerSingleton.getInstance();
		// readEx = new ReaderExporter(controller);
		// initializeRMI();
		// readerName = adaptable.getReaderName();
		reader = adaptable;
		this.setPropertyDescriptors();
		// logger.debug("Property Source File was created for reader "
		// + reader.getReaderName());
	}

	// private void initializeRMI() {
	// readerRegistry = ReaderRegistry.getInstance();
	// }

	/**
	 * Sets the property descriptors for this method.
	 */
	public void setPropertyDescriptors() {

		categoryList = new ArrayList<String>(reader.getAllCategories());

		propertiesTable = new HashMap<String, PropertyDescriptor>();
		for (String categoryString : categoryList) {
			// Collection<CommandObject> listByCategory = module
			// .getCommandsByCategory(categoryString);
			HashMap<String, String> actionMap = null;
			actionMap = new HashMap<String, String>(reader
					.getCommandActionTagsByCategory(categoryString));

			// Find out the action type of the string, and set the category as
			// such
			for (String displayName : actionMap.keySet()) {
				if (((String) actionMap.get(displayName))
						.equalsIgnoreCase(BUTTON)) {
					propertiesTable.put(displayName, new PropertyDescriptor(
							displayName, displayName));
					propertiesTable.get(displayName)
							.setCategory(categoryString);
				} else if (((String) actionMap.get(displayName))
						.equalsIgnoreCase(READ_ONLY)) {
					propertiesTable.put(displayName, new PropertyDescriptor(
							displayName, displayName));
					propertiesTable.get(displayName)
							.setCategory(categoryString);
				} else if (((String) actionMap.get(displayName))
						.equalsIgnoreCase(EDITABLE)) {
					propertiesTable
							.put(displayName, new TextPropertyDescriptor(
									displayName, displayName));
					propertiesTable.get(displayName)
							.setCategory(categoryString);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	public Object getEditableValue() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		PropertyDescriptor[] retVal = new PropertyDescriptor[this.propertiesTable
				.size()];

		int propCount = 0;
		for (PropertyDescriptor i : this.propertiesTable.values()) {
			retVal[propCount] = i;
			propCount++;
		}
		return retVal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	public Object getPropertyValue(Object arg0) {

		String argument = (String) arg0;
		argument = argument.toLowerCase();

		String propertyValue = "NULL";
		try {
			propertyValue = reader.getReaderProperty(argument);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return propertyValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang
	 * .Object)
	 */
	public boolean isPropertySet(Object arg0) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object arg0, Object arg1) {
		String argument = (String) arg0;
		argument = argument.toLowerCase();
		reader.setReaderProperty(argument, (String) arg1);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java
	 * .lang.Object)
	 */
	public void resetPropertyValue(Object arg0) {
	}

}
