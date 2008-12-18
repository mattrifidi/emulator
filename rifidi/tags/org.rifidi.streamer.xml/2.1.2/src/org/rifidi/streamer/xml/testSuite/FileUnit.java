/*
 *  FileUnit.java
 *
 *  Project:		Rifidi - A developer tool for RFID
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.streamer.xml.testSuite;

/**
 * This is depricated. Please don't use ist anymore.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
@Deprecated
public class FileUnit {

	public static enum FileType {
		SCENARIO, BATCH, COMPONENT
	};

	private String fileName;
	private FileType fileType;

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return the fileType
	 */
	public FileType getFileType() {
		return fileType;
	}

	/**
	 * @param fileType
	 *            the fileType to set
	 */
	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}
}
