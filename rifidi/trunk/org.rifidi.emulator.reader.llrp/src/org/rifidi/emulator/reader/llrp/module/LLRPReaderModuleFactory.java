/**
 * 
 */
package org.rifidi.emulator.reader.llrp.module;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Kyle
 * 
 */
public class LLRPReaderModuleFactory extends ReaderModuleFactory {

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
		return new LLRPReaderModule(new ControlSignal<Boolean>(true),
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
		return LLRPReaderModule.READERTYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return LLRPReaderModule.class.getName();
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
		return super.getStreamAsString(LLRPReaderModule.class
				.getResourceAsStream("/" + LLRPReaderModule.XMLLOCATION
						+ "emulator.xml"));
	}

}
