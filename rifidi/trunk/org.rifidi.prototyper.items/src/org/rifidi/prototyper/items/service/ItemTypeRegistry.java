/**
 * 
 */
package org.rifidi.prototyper.items.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.prototyper.items.Activator;
import org.rifidi.prototyper.items.model.ItemType;

/**
 * This registry keeps track of ItemType objects
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class ItemTypeRegistry {

	/** The logger for this clases */
	private static final Log logger = LogFactory.getLog(ItemTypeRegistry.class);
	/** The registered types, where the key is the type name */
	private final Map<String, ItemType> types;
	/** Registered types by category */
	private final Map<String, Set<ItemType>> categoryToTypes;
	/** The default categories */
	private final HashSet<String> defaultCategory = new HashSet<String>();

	/**
	 * Constructor
	 */
	public ItemTypeRegistry() {
		this.types = new HashMap<String, ItemType>();
		this.categoryToTypes = new HashMap<String, Set<ItemType>>();
		defaultCategory.add("unspecified");
	}

	/**
	 * This method loads XML ItemType objects into the registry. It must be
	 * called from within the Eclipse thread since it accesses the image
	 * registry.
	 * 
	 * @param resources
	 */
	public void loadItemTypes(Set<String> resources) {
		try {
			JAXBContext context = JAXBContext.newInstance(ItemType.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			for (String file : resources) {
				// File f = new File(file);\
				InputStream s = getClass().getResourceAsStream(file);
				try {
					ItemType itemType = (ItemType) unmarshaller.unmarshal(s);
					if (logger.isDebugEnabled()) {
						logger.debug("unmarshalled: " + file);
					}
					String pathToImage = file.substring(0, file
							.lastIndexOf('/') + 1)
							+ itemType.getImagePath();
					Activator.getDefault().getImageRegistry().put(
							itemType.getType(),
							Activator.getImageDescriptor(pathToImage));
					types.put(itemType.getType(), itemType);

					// if the item type does not have a category, give it the
					// default.
					if (itemType.getCategories() == null
							|| itemType.getCategories().isEmpty()) {
						itemType.setCategories(defaultCategory);
					}
					for (String category : itemType.getCategories()) {
						// if the category does not exist yet, create it
						if (!categoryToTypes.containsKey(category)) {
							categoryToTypes.put(category,
									new HashSet<ItemType>());
						}
						// add this item to the category map
						categoryToTypes.get(category).add(itemType);
					}
				} catch (JAXBException e) {
					logger.warn("Cannot Unmarshal file: " + file, e);
				}
			}
		} catch (JAXBException exception) {
			logger.warn("JAXB Exception: ", exception);
		}
	}

	/**
	 * 
	 * @return All the categories in use
	 */
	public Set<String> getItemCategories() {
		return new HashSet<String>(categoryToTypes.keySet());
	}

	/**
	 * Get the items registered under a given category
	 * 
	 * @param category
	 * @return
	 */
	public Set<ItemType> getItemTypes(String category) {
		if (category != null) {
			if (categoryToTypes.containsKey(category)) {
				return new HashSet<ItemType>(categoryToTypes.get(category));
			}
		}
		return new HashSet<ItemType>();
	}

	/**
	 * @return All the available item types.
	 */
	public Set<String> getAllItemTypes() {
		return new HashSet<String>(types.keySet());
	}

}
