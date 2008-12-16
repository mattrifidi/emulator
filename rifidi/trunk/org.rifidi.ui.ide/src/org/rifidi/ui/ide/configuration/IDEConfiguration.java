package org.rifidi.ui.ide.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.emulator.reader.module.GeneralReaderPropertyHolder;
import org.rifidi.tags.impl.RifidiTag;

/**
 * This is a bean that stores all the information necessary to save an IDE
 * configureation (Readers, Tags, and which tags are on which antennas)
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
@XmlRootElement
public class IDEConfiguration {

	/**
	 * This is a list of Configured readers.
	 */
	private List<GeneralReaderPropertyHolder> readers = new ArrayList<GeneralReaderPropertyHolder>();

	/**
	 * This is a list of tags
	 */
	private List<RifidiTag> tags = new ArrayList<RifidiTag>();

	/**
	 * A data structure that stores which tags are on which antennas of which
	 * readers
	 */
	private ReaderAntennaTagMap readerAntennaTagMap;

	/**
	 * @return the readerAntennaTagMap
	 */
	public ReaderAntennaTagMap getReaderAntennaTagMap() {
		return readerAntennaTagMap;
	}

	/**
	 * @param readerAntennaTagMap the readerAntennaTagMap to set
	 */
	public void setReaderAntennaTagMap(ReaderAntennaTagMap readerAntennaTagMap) {
		this.readerAntennaTagMap = readerAntennaTagMap;
	}

	/**
	 * @return the readers
	 */
	public List<GeneralReaderPropertyHolder> getReaders() {
		return readers;
	}

	/**
	 * @param readers
	 *            the readers to set
	 */
	public void setReaders(List<GeneralReaderPropertyHolder> readers) {
		this.readers = readers;
	}

	/**
	 * @return the tags
	 */
	public List<RifidiTag> getTags() {
		return tags;
	}

	/**
	 * @param tags
	 *            the tags to set
	 */
	public void setTags(List<RifidiTag> tags) {
		this.tags = tags;
	}
}
