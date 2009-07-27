/**
 * 
 */
package org.rifidi.emulator.reader.symbol.module;

import org.rifidi.emulator.common.ControlSignal;
import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.emulator.reader.module.ReaderModule;
import org.rifidi.emulator.reader.module.ReaderModuleFactory;

/**
 * @author Kyle
 * 
 */
public class SymbolReaderModuleFactory extends ReaderModuleFactory {

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
		return new SymbolReaderModule(new ControlSignal<Boolean>(true),
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
		return SymbolReaderModule.READERTYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.rifidi.emulator.reader.module.ReaderModuleFactory#
	 * getReaderModuleClassName()
	 */
	@Override
	public String getReaderModuleClassName() {
		return SymbolReaderModule.class.getCanonicalName();
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
		return super.getStreamAsString(SymbolReaderModule.class
				.getResourceAsStream("/" + SymbolReaderModule.XMLLOCATION
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
		holder.setReaderClassName(SymbolReaderModule.class.getCanonicalName());
		holder.setReaderName("SymbolXR400");
		holder.setProperty("byte_address", "127.0.0.1:3000");
		holder.setProperty("http_address", "127.0.0.1:10080");
		return holder;
	}

}
