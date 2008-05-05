/**
 * 
 */
package org.rifidi.emulator.reader.properties.xml;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.digester.Digester;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

/**
 * @author kyle
 * 
 */
public class PropertyXMLParser {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(PropertyXMLParser.class);

	/**
	 * Parses through an XML file and returns a hashmap of all of the \<property\> \<id\>
	 * and \<value\> tags.
	 * 
	 * @param xmlfile
	 * @return
	 */
	public HashMap<String, String> parseXML(String xmlfile) {
		HashMap<String, String> retVal=new HashMap<String, String>();

		try {
			Digester dig = new Digester();
			dig.setValidating(false);

			dig.addObjectCreate("properties", ReaderXMLPropertyHolder.class);

			dig.addObjectCreate("properties/property", ReaderXMLProperty.class);

			dig.addBeanPropertySetter("properties/property/id");

			dig.addBeanPropertySetter("properties/property/value");

			dig.addSetNext("properties/property", "addReaderXMlProperty");

			ReaderXMLPropertyHolder newHolder = (ReaderXMLPropertyHolder)dig.parse(xmlfile);
			
			for(ReaderXMLProperty i:newHolder.getReaderXMLPropertyList()) {
				retVal.put(i.getId(), i.getValue());
			}
		} catch (IOException e) {
			logger.error("Error in parseXML for PropertyXMLParser");
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			logger.error("Error in parseXML for PropertyXMLParser");
			e.printStackTrace();
			return null;
		}

		return retVal;
	}
}
