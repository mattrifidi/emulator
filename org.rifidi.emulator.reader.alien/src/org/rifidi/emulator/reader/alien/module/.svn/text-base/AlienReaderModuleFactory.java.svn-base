/**
 * 
 */
package org.rifidi.emulator.reader.alien.module;

import java.io.InputStream;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class AlienReaderModuleFactory extends ReaderModuleFactory {

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
		return new AlienReaderModule(new ControlSignal<Boolean>(true),
				properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getReaderType()
	 */
	@Override
	public String getReaderType() {
		return AlienReaderModule.READERTYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return AlienReaderModule.class.getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getReaderXMLDescription
	 * (java.lang.String)
	 */
	@Override
	public String getReaderXMLDescription() {
		InputStream stream = AlienReaderModuleFactory.class
				.getResourceAsStream("/" + AlienReaderModule.XMLLOCATION
						+ "emulator.xml");
		return super.getStreamAsString(stream);
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModuleFactory#getDefaultProperties()
	 */
	@Override
	public GeneralReaderPropertyHolder getDefaultProperties() {
		GeneralReaderPropertyHolder alienDefaultHolder=new GeneralReaderPropertyHolder();
		alienDefaultHolder.setNumAntennas(2);
		alienDefaultHolder.setNumGPIs(4);
		alienDefaultHolder.setNumGPOs(8);
		alienDefaultHolder.setReaderClassName(AlienReaderModule.class.getCanonicalName());
		alienDefaultHolder.setReaderName("Alien9800");
		alienDefaultHolder.setProperty("heartbeat_address", "255.255.255.255:3988");
		alienDefaultHolder.setProperty("heartbeat_power", "0");
		alienDefaultHolder.setProperty("inet_address", "127.0.0.1:20000");
		return alienDefaultHolder;
	}
	
	
}
