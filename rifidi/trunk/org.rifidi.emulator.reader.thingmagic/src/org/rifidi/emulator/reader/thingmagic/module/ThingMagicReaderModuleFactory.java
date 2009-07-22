/**
 * 
 */
package org.rifidi.emulator.reader.thingmagic.module;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Kyle
 * 
 */
public class ThingMagicReaderModuleFactory extends ReaderModuleFactory {

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
		return new ThingMagicReaderModule(new ControlSignal<Boolean>(true),
				properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return ThingMagicReaderModule.class.getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getReaderType()
	 */
	@Override
	public String getReaderType() {
		return ThingMagicReaderModule.READERTYPE;
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
		return super.getStreamAsString(ThingMagicReaderModule.class
				.getResourceAsStream("/" + ThingMagicReaderModule.XMLLOCATION
						+ "emulator.xml"));
	}

}
