package org.rifidi.emulator.reader.sharedrc.radio.generic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.emulator.reader.sharedrc.radio.Antenna;
import org.rifidi.emulator.reader.sharedrc.tagmemory.TagMemory;
import org.rifidi.emulator.tags.Gen1Tag;
import org.rifidi.emulator.tags.impl.C1G1Tag;
import org.rifidi.emulator.tags.impl.C1G2Tag;
import org.rifidi.emulator.tags.impl.RifidiTag;

/**
 * This is the new GenericRadio for Reader. It's used to scan for tags and to
 * operate functions on tags.
 * 
 * The radio is the interface between the antennas and the tag memory. Each
 * reader should implement its own tag memory, which the radio will take care of
 * updating.
 * 
 * There are two ways to use the radio. The first is using it in "autonomous"
 * mode, where the tag memory is passed in at construct time. Then, whenever an
 * antenna notifies the radio that the tags has changed, the radio will
 * automatically scan the antennas and update the tag memory. In this way, the
 * tag memory will be kept constantly up to date.
 * 
 * The second way to use the radio is "polling". In this method, the reader
 * (probably in a handler method) will call the radio's scan method and pass in
 * the tag memory whenever it wants the radio to update the memory.
 * 
 * TODO: Currently, this radio implements the radio interface because the shared
 * resources requires an object that implements the radio interface. However,
 * after all readers have been switched over to use this radio, the Radio
 * interface should be deleted. The idea is that there is no need for multiple
 * radios.
 * 
 * @author Andreas Huebner - andreas@pramari.com
 * @author Kyle Neumeier - kyle@pramari.com
 * @author Matt Dean - matt@pramari.com
 * 
 */
public class GenericRadio implements Observer {

	/**
	 * The logger for this class.
	 */
	private static Log logger = LogFactory.getLog(GenericRadio.class);
	
	private static Log eventLogger = LogFactory.getLog("EventLogger");

	/**
	 * The time for a complete scan of tags
	 */
	private int minScanTime;

	/**
	 * If not polling the radio, a reader can pass in its tag memory at
	 * construction time and the radio will automatically update it whenever a
	 * tag has been added to an antenna
	 */
	private TagMemory tagMem = null;

	/**
	 * This hashmap of antennas from the shared resources
	 */
	private HashMap<Integer, Antenna> antennas;

	private Set<Integer> antennasToScan;

	private boolean suspended = false;

	private boolean shouldScanAfterResume = false;
	
	/**
	 * The name of the reader that this radio is attached to
	 */
	private String readerName;

	/**
	 * Constructor for the Radio.
	 * 
	 * @param antennas
	 *            List of Antennas available on Radio
	 * @param minScanTime
	 *            minimal Time one scan takes (seconds)
	 */
	public GenericRadio(HashMap<Integer, Antenna> antHash, int minScanTime, String readerName) {
		Iterator<Antenna> antennas = antHash.values().iterator();
		while (antennas.hasNext()) {
			antennas.next().addObserver(this);
		}
		this.antennas = antHash;
		this.minScanTime = minScanTime;
		this.readerName = readerName;
	}

	/**
	 * Constructor for the Radio.
	 * 
	 * @param antennas
	 *            List of Antennas available on Radio
	 * @param minScanTime
	 *            minimal Time one scan takes (seconds)
	 */
	public GenericRadio(HashMap<Integer, Antenna> antHash, int minScanTime, String readerName,
			TagMemory tagMem) {
		this(antHash, minScanTime, readerName);
		this.tagMem = tagMem;

	}

	/**
	 * Scans the set of denoted antennas for discoverable tags. Nonexistant
	 * antennas are ignored.<br>
	 * <i>Note</i>: Passing in a null as the set of antennas automatically
	 * scans all antennas available to the reader.
	 * 
	 * @param antennaIDs
	 *            List of Antennas whose fields should be scaned
	 * @return List of found Tags
	 */
	private List<RifidiTag> scan(Set<Integer> antennaIDs) {

		long scanStartTime = System.currentTimeMillis();

		/* Create the tag list to return. */
		List<RifidiTag> readTags = new ArrayList<RifidiTag>();

		for (Integer antID : antennaIDs) {
			if (antennas.containsKey(antID)) {
				/* Check the entire list of discoverable tags for intersections. */
				Collection<RifidiTag> tags = antennas.get(antID).getTagList();

				for (RifidiTag curTag : tags) {
					updateTag(curTag, antID);

					readTags.add(curTag);
				}
			}
		}

		// calculate the time to sleep
		long differenceTime = System.currentTimeMillis() - scanStartTime;
		long sleepTime = minScanTime - differenceTime;
		if (sleepTime > 0) {
			/* Sleep until the minimum scan time has been completed. */
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				/* Do nothing */
			}
		} else {
			logger.debug("scantime was longer than minScanTime");
		}

		eventLogger.info("[TAG EVENT]: Latest scan found " + readTags.size() + " tags on "
				+ antennaIDs.size() + " antennas");

		return readTags;
	}

	/**
	 * This method is used for polling. It updates a TagMemory with the new tags
	 * by calling the addCollectionTags() method of the Tag Memory
	 * 
	 * @param antennaIDs.
	 *            If null, use all antennas
	 * @param memory
	 */
	public void scan(Set<Integer> antennaIDs, TagMemory memory) {
		if (antennaIDs == null) {
			antennaIDs = this.antennas.keySet();
		} else if (!this.antennas.keySet().containsAll(antennaIDs)) {

			throw new IllegalArgumentException(
					"Scan() method given an antenna to scan that does not exist");
		}
		List<RifidiTag> tags = scan(antennaIDs);
		List<RifidiTag> tagsToAdd = new ArrayList<RifidiTag>();
		// don't add killed tags to buffer
		for (RifidiTag t : tags) {
			if (t.getTag().readId() != null) {
				tagsToAdd.add(t);
			}
		}
		memory.updateMemory(tagsToAdd);

	}

	/**
	 * Deletes the given Antenna and recreates a new Antenna at this position.
	 * 
	 * @param antNum
	 *            Name of the Antenna by index
	 * @return true if recreation was successful
	 */
	public Boolean purgeAntennaField(Integer antNum) {
		antennas.put(antNum, new Antenna(antNum, readerName));
		return true;
	}

	/**
	 * This method should only be used by XMLRPC. It should be gotten rid of
	 * once we get rid of XMLRPC.
	 */
	public Boolean addTagToField(Integer antNum, String data, Integer generation) {
		byte[] accessPass = { 0x00, 0x00, 0x00, 0x00 };
		byte[] killPass = { 0x00, 0x00, 0x00, 0x00 };
		return addTagToField(antNum, ByteAndHexConvertingUtility
				.fromHexString(data), generation, accessPass, killPass);
	}

	/**
	 * Use this method to add tags to the field for now. It is here now because
	 * xmlrpc adds tags using the radio. However, once we change over so that
	 * every reader is using the new radio, xmlrpc will add tags using the
	 * antenna
	 * 
	 * @param antNum
	 * @param data
	 *            the tag ID in bytes
	 * @param generation
	 *            1 if gen1, 2 if gen2
	 * @return true if sucessful, false otherwise
	 */
	public boolean addTagToField(int antennaID, byte[] data,
			Integer generation, byte[] accessPassword, byte[] killPassword) {
		RifidiTag container = generateTag(data, generation, accessPassword,
				killPassword);
		ArrayList<RifidiTag> tags = new ArrayList<RifidiTag>();
		tags.add(container);
		return this.antennas.get(antennaID).addTags(tags);

	}

	/**
	 * This method should only be temporary until there is a way to generate
	 * GEN2Tags in the UI.
	 */
	private RifidiTag generateTag(byte[] data, Integer generation,
			byte[] accessPass, byte[] killPass) {
		RifidiTag container = null;
		Gen1Tag tag;
		if (generation == 1) {

			tag = new C1G1Tag(data);
			container = new RifidiTag(tag);
		}
		if (generation == 2) {
			tag = new C1G2Tag(data, accessPass, killPass);
			container = new RifidiTag(tag);
		}
		return container;
	}

	/**
	 * This method sets the antennas to scan for "automatic" mode. If null is
	 * passed in, all antennas will scan. If an empty set is passed in, no
	 * antennas will be scanned. Otherwise it will scan the antennas in the set.
	 * 
	 * @param antennasToScan
	 */
	public void setAntennasToScan(Set<Integer> antennasToScan) {
		this.antennasToScan = antennasToScan;
	}

	/**
	 * This is the update method that the antenna calls.
	 */
	public void update(Observable arg0, Object arg1) {
		if (tagMem != null) {
			if (suspended) {
				shouldScanAfterResume = true;
			} else {
				this.scan(antennasToScan, tagMem);
			}
		}

	}

	public HashMap<Integer, Antenna> getAntennas() {
		return antennas;
	}

	/**
	 * Update information inside of tag when the tag is scanned
	 * 
	 * @param tag
	 * @param antID
	 */
	private void updateTag(RifidiTag tag, int antID) {
		if (tag.getDiscoveryDate() == null)
			tag.setDiscoveryDate(new Date());
		tag.setLastSeenDate(new Date());

		tag.setAntennaLastSeen(antID);
	}

	public void suspend() {
		this.suspended = true;
	}

	public void resume() {
		this.suspended = false;
		if (shouldScanAfterResume) {
			this.scan(antennasToScan, tagMem);
			shouldScanAfterResume = false;
		}
	}

}
