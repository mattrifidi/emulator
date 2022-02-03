/**
 * 
 */
package org.rifidi.emulator.reader.awid.module;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Kyle
 * 
 */
public class AwidReaderModuleFactory extends ReaderModuleFactory {

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
		return new AwidReaderModule(new ControlSignal<Boolean>(true),
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
		return AwidReaderModule.READERTYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return AwidReaderModule.class.getCanonicalName();
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
		return super.getStreamAsString(AwidReaderModule.class
				.getResourceAsStream("/" + AwidReaderModule.XMLLOCATION
						+ "emulator.xml"));
	}

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModuleFactory#getDefaultProperties()
	 */
	@Override
	public GeneralReaderPropertyHolder getDefaultProperties() {
		GeneralReaderPropertyHolder holder=new GeneralReaderPropertyHolder();
		holder.setNumAntennas(2);
		holder.setNumGPIs(0);
		holder.setNumGPOs(0);
		holder.setReaderClassName(AwidReaderModule.class.getCanonicalName());
		holder.setReaderName("AWIDMPR");
		holder.setProperty("serial_port", "COM1");
		return holder;
	}

}
