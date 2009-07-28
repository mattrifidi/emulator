/*
 *  SiritReaderModuleFactory.java
 *
 *  Created:	28.07.2009
 *  Project:	RiFidi org.rifidi.emulator.reader.sirit
 *  				http://www.rifidi.org
 *  				http://rifidi.sourceforge.net
 *  Copyright:	Pramari LLC and the Rifidi Project
 *  License:	Lesser GNU Public License (LGPL)
 *  				http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.emulator.reader.sirit.module;

import java.io.InputStream;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Stefan Fahrnbauer - stefan@pramari.com
 * 
 */
public class SiritReaderModuleFactory extends ReaderModuleFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#createReaderModule
	 * (org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder)
	 */
	@Override
	public ReaderModule createReaderModule(
			GeneralReaderPropertyHolder properties) {
		return new SiritReaderModule(new ControlSignal<Boolean>(true),
				properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getDefaultProperties
	 * ()
	 */
	@Override
	public GeneralReaderPropertyHolder getDefaultProperties() {
		GeneralReaderPropertyHolder siritDefaultHolder = new GeneralReaderPropertyHolder();
		siritDefaultHolder.setNumAntennas(4);
		siritDefaultHolder.setNumGPIs(0);
		siritDefaultHolder.setNumGPOs(0);
		siritDefaultHolder.setReaderClassName(SiritReaderModule.class
				.getCanonicalName());
		siritDefaultHolder.setReaderName("Sirit INfinity 510");
		siritDefaultHolder.setProperty("ipaddress", "127.0.0.1:50007");
		
		return siritDefaultHolder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return SiritReaderModule.class.getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getReaderType()
	 */
	@Override
	public String getReaderType() {
		return SiritReaderModule.READERTYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getReaderXMLDescription
	 * ()
	 */
	@Override
	public String getReaderXMLDescription() {
		InputStream stream = SiritReaderModuleFactory.class
				.getResourceAsStream("/" + SiritReaderModule.XMLLOCATION
						+ "emulator.xml");
		return super.getStreamAsString(stream);
	}
}
