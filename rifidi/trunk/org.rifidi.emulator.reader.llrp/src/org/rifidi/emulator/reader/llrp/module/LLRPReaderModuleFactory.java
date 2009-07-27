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

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModuleFactory#getDefaultProperties()
	 */
	@Override
	public GeneralReaderPropertyHolder getDefaultProperties() {
		GeneralReaderPropertyHolder holder=new GeneralReaderPropertyHolder();
		holder.setNumAntennas(2);
		holder.setNumGPIs(4);
		holder.setNumGPOs(4);
		holder.setReaderClassName(LLRPReaderModule.class.getCanonicalName());
		holder.setReaderName("LLRP");
		holder.setProperty("inet_address", "127.0.0.1:10101");
		holder.setProperty("llrp_inet_address", "127.0.0.1:5084");
		holder.setProperty("servermode", "1");
		return holder;
	}
}
