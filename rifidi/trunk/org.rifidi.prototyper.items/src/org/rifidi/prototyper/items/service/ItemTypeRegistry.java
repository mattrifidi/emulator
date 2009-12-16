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
 * @author kyle
 * 
 */
public class ItemTypeRegistry {

	private static final Log logger = LogFactory.getLog(ItemTypeRegistry.class);
	private final Map<String, ItemType> types;

	public ItemTypeRegistry() {
		this.types = new HashMap<String, ItemType>();
	}

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
				} catch (JAXBException e) {
					logger.warn("Cannot Unmarshal file: " + file, e);
				}
			}
		} catch (JAXBException exception) {
			logger.warn("JAXB Exception: ", exception);
		}
	}
	
	public Set<String> getItemTypes(){
		return new HashSet<String>(types.keySet());
	}

}
