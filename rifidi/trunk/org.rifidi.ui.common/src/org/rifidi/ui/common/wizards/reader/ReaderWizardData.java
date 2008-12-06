package org.rifidi.ui.common.wizards.reader;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

public class ReaderWizardData {

	GeneralReaderPropertyHolder generalReaderHolder = new GeneralReaderPropertyHolder();

	String readerType;
	
	/**
	 * @return the generalReaderHolder
	 */
	public GeneralReaderPropertyHolder getGeneralReaderHolder() {
		return generalReaderHolder;
	}

	/**
	 * @return the readerName
	 */
	public String getReaderType() {
		return readerType;
	}

}
