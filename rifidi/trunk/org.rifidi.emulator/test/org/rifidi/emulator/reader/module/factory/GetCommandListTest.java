//TODO: fix test
package org.rifidi.emulator.reader.module.factory;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

public class GetCommandListTest extends TestCase {

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

			GeneralReaderPropertyHolder newProp3 = new GeneralReaderPropertyHolder();
			newProp3.setReaderName("Alien_Reader_1");
//			newProp3.setReaderClass(AlienReaderModule.class);
			newProp3.setNumAntennas(3);
//			newProp3.setXmlFilePath("properties/reader_xml_lib/AlienALR9800.xml");
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

//			AlienReaderModule newReader1 = (AlienReaderModule) newFactory
//					.getReader("Alien_Reader_1");
			
//			System.out.println(newReader1.getCommandList());
			
			
			System.out.println("\n\n\n\n\n");
			
//			System.out.println("the categories:" + newReader1.getAllCategories());

		}

}
