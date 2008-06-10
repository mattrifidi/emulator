//TODO: fix test
/*
 *  ReaderFactoryTest.java
 *
 *  Created:	Jun 20, 2006
 *  Project:	RiFidi Emulator - A Software Simulation Tool for RFID Devices
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.module.factory;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

/**
 * A test case for the readerFactory class.
 * 
 * @author Matthew Dean - matt@pramari.com
 * @since <$INITIAL_VERSION$>
 * @version <$CURRENT_VERSION$>
 */
public class ReaderFactoryTest extends TestCase {

	/**
	 * The log4j logger for this class.
	 */
	private static Log logger = LogFactory
			.getLog(ReaderFactoryTest.class);

	/**
	 * Set up the TestCase
	 */
	public void setUp() {

	}

	/**
	 * Tear down the TestCase
	 */
	public void tearDown() {

	}

	public void testCreateFourReader() {

		GeneralReaderPropertyHolder newProp1 = new GeneralReaderPropertyHolder();
		newProp1.setReaderName("Awid_Reader_1");
//		newProp1.setReaderClass(AwidReaderModule.class);
//		newProp1.setXmlFilePath("properties/reader_xml_lib/AwidMPR.xml");
		newProp1.setNumAntennas(1);
		newProp1.setProperty("serial_port","COM2");
		//newFactory.addReader(newProp1);

		try {
			logger.debug("sleeping for 1 second: ");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			/* Do nothing */
		}

		
		newProp1.setReaderName("Awid_Reader_2");
//		newProp1.setReaderClass(AwidReaderModule.class);
//		newProp1.setXmlFilePath("properties/reader_xml_lib/AwidMPR.xml");
		newProp1.setNumAntennas(1);
		newProp1.setProperty("serial_port","COM4");
		//newFactory.addReader(newProp2);

		try {
			logger.debug("sleeping for 1 second: ");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			/* Do nothing */
		}

		GeneralReaderPropertyHolder newProp3 = new GeneralReaderPropertyHolder();
		newProp3.setReaderName("Alien_Reader_1");
//		newProp3.setReaderClass(AlienReaderModule.class);
		newProp3.setNumAntennas(3);
//		newProp3.setXmlFilePath("properties/reader_xml_lib/AlienALR9800.xml");
		//newProp3.setXmlRpcAddress(new InetSocketAddress("127.0.0.1",30001));
		newProp3.setProperty("inet_address", "127.0.0.1:30000");
		newProp3.setProperty("heartbeat_address", "255.255.255.255:3988");

		//newFactory.addReader(newProp3);

		try {
			logger.debug("sleeping for 1 second: ");
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			/* Do nothing */
		}

		newProp3.setReaderName("Alien_Reader_1");
//		newProp3.setReaderClass(AlienReaderModule.class);
		newProp3.setNumAntennas(3);
//		newProp3.setXmlFilePath("properties/reader_xml_lib/AlienALR9800.xml");
		//newProp3.setXmlRpcAddress(new InetSocketAddress("127.0.0.2",30001));
		newProp3.setProperty("inet_address", "127.0.0.2:30000");
		newProp3.setProperty("heartbeat_address", "255.255.255.255:3988");

		//newFactory.addReader(newProp4);

//		AwidReaderModule newReader1 = (AwidReaderModule) newFactory
//				.getReader("Awid_Reader_1");
//		AwidReaderModule newReader2 = (AwidReaderModule) newFactory
//				.getReader("Awid_Reader_2");
//		AlienReaderModule newReader3 = (AlienReaderModule) newFactory
//				.getReader("Alien_Reader_1");
//		AlienReaderModule newReader4 = (AlienReaderModule) newFactory
//				.getReader("Alien_Reader_2");

//		if (!newReader1.getName().equals("Awid_Reader_1")) {
//			fail();
//		}
//		if (!newReader2.getName().equals("Awid_Reader_2")) {
//			fail();
//		}
//		if (!newReader3.getName().equals("Alien_Reader_1")) {
//			fail();
//		}
//		if (!newReader4.getName().equals("Alien_Reader_2")) {
//			fail();
//		}

	}
}
