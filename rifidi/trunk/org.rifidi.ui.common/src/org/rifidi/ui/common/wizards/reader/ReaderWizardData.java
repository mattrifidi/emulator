package org.rifidi.ui.common.wizards.reader;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;

public class ReaderWizardData {

	GeneralReaderPropertyHolder generalReaderHolder = new GeneralReaderPropertyHolder();

	String readerType;
	
	/**
	 * @param generalReaderHolder the generalReaderHolder to set
	 */
	public void setGeneralReaderHolder(
			GeneralReaderPropertyHolder generalReaderHolder) {
		this.generalReaderHolder = generalReaderHolder;
	}

	/**
	 * @param readerType the readerType to set
	 */
	public void setReaderType(String readerType) {
		this.readerType = readerType;
	}

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
