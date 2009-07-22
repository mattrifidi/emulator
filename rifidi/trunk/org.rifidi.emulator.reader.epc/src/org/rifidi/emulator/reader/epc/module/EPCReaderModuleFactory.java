/**
 * 
 */
package org.rifidi.emulator.reader.epc.module;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Kyle
 * 
 */
public class EPCReaderModuleFactory extends ReaderModuleFactory {

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
		return new EPCReaderModule(new ControlSignal<Boolean>(true), properties);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return EPCReaderModule.class.getCanonicalName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.rifidi.emulator.reader.module.ReaderModuleFactory#getReaderType()
	 */
	@Override
	public String getReaderType() {
		return EPCReaderModule.READERTYPE;
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
		return super.getStreamAsString(EPCReaderModule.class
				.getResourceAsStream("/" + EPCReaderModule.XMLLOCATION
						+ "emulator.xml"));
	}

}
