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

	/* (non-Javadoc)
	 * @see org.rifidi.emulator.reader.module.ReaderModuleFactory#getDefaultProperties()
	 */
	@Override
	public GeneralReaderPropertyHolder getDefaultProperties() {
		GeneralReaderPropertyHolder holder=new GeneralReaderPropertyHolder();
		holder.setNumAntennas(2);
		holder.setNumGPIs(0);
		holder.setNumGPOs(0);
		holder.setReaderClassName(ThingMagicReaderModule.class.getCanonicalName());
		holder.setReaderName("ThingMagicMercury4");
		holder.setProperty("rql_address", "127.0.0.1:8080");
		return holder;
	}
	
}
