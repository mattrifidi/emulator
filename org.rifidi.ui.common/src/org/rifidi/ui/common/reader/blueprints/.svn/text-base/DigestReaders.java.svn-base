/*
 *  DigestReaders.java
 *
 *  Created:	Apr 5, 2007
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.ui.common.reader.blueprints;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * This class takes an InputStream and digests it as an xml-file.
 * 
 * Below an example of the xmlfile, the roottag is the reader-tag: <reader>
 * <readerclassname
 * >org.rifidi.emulator.reader.module.alien.AlienReaderModule</readerclassname>
 * <!-- name of the baseclass of the reader ---> <description>Emulator for the
 * Alien 9800</description> <!-- a brief description -->
 * <maxantennas>4</maxantennas> <!-- max numbers of antennas this reader
 * supports --> <!-- the following is used to map readerproperties to the ui -->
 * <property> <required>1</required> <!-- is this property required 1/0 -->
 * TODO: currently the required field is ignored <name>inet_address</name> <!--
 * name of the property to be used in the GeneralPropertyHolder--> <display>ip
 * address</display> <!-- name to be displayed in the UI --> <tooltip>The
 * address the reader should be reachable through</tooltip> <!-- UI tooltip -->
 * <defaultvalue>127.0.0.1:10000</defaultvalue> <!-- a default value to be
 * displayed in the UI-->
 * <validatorclassname>IpAddressValidator</validatorclassname> <!-- the
 * validator to be used for this parameter, if just a classname is given it will
 * resolve to org.rifidi.ide.validators.<classname> otherwise the specified
 * packagename will be used--> </property> </reader>
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com
 * 
 */
public class DigestReaders {
	private Digester digester;

	/**
	 * Constructor
	 * 
	 */
	public DigestReaders() {
		digester = new Digester();
		digester.setClassLoader(this.getClass().getClassLoader());

		digester.setValidating(false);
		digester.addObjectCreate("reader", ReaderBlueprint.class);
		digester.addBeanPropertySetter("reader/readerclassname");
		digester.addBeanPropertySetter("reader/description");
		digester.addBeanPropertySetter("reader/maxantennas");
		digester.addBeanPropertySetter("reader/maxgpis");
		digester.addBeanPropertySetter("reader/maxgpos");
		digester.addObjectCreate("reader/property", PropertyBlueprint.class);
		digester.addSetNestedProperties("reader/property");
		digester.addSetNext("reader/property", "addProperty");
	}

	/**
	 * Takes an InputSTream and digests it
	 * 
	 * @param is
	 *            an InputStream containing a reader description xml-file
	 * @return the created blueprint for the ui
	 * @throws SAXException
	 * @throws IOException
	 */
	public ReaderBlueprint digest(InputStream is) throws SAXException,
			IOException {
		digester.clear();

		try {
			ReaderBlueprint bp = (ReaderBlueprint) digester.parse(is);
			return bp;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
}
