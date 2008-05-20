package org.rifidi.emulator.tags.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.rifidi.emulator.tags.Gen1Tag;
import org.rifidi.emulator.tags.enums.TagGen;
import org.rifidi.emulator.tags.utils.RifidiTagFactory;
import org.rifidi.jaxb.map.MapElement;
import org.rifidi.utilities.formatting.ByteAndHexConvertingUtility;

/**
 * @author Andreas Huebner - andreas@pramari.com
 */
@XmlAccessorType(XmlAccessType.NONE)
public class RifidiTag implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** The Tag to whom this information belong * */
	private Gen1Tag tag;

	/**
	 * What type is the TagId (DoD, Custom96, ...)
	 */
	private String idFormat;

	/** Antenna the tag was last seen at */
	private int antennaLastSeen;

	/** Discovery date of tag */
	private Date discoveryDate;

	/** Last seen date of tag */
	private Date lastSeenDate;

	/**
	 * how many times the tag has been read
	 */
	private int readCount = 0;

	/**
	 * Status of this tag. Could be disabled and only seen by the IDE.
	 */
	public boolean isVisbile = true;
	
	/**
	 * The quality rating of the tag. A quality rating is from 0 to 100 and
	 * correlates to how well the tag may be read compared to a perfect tag (as
	 * a percentage). Thus, a rating 0 tag will be impossible to read, while a
	 * rating of 100 represents a perfect tag.
	 */
	private int qualityRating;

	/**
	 * Default constructor (used by jaxb)
	 */
	public RifidiTag() {
	}

	public RifidiTag(Gen1Tag tag) {
		this.tag = tag;
	}

	/**
	 * @return the idFormat
	 */
	@XmlElement
	public String getIdFormat() {
		return idFormat;
	}

	/**
	 * @param idFormat
	 *            the idFormat to set
	 */
	public void setIdFormat(String idFormat) {
		this.idFormat = idFormat;
	}

	public Integer getAntennaLastSeen() {
		return new Integer(antennaLastSeen);
	}

	public synchronized void setAntennaLastSeen(int antennaLastSeen) {
		this.antennaLastSeen = antennaLastSeen;
	}

	public final Date getDiscoveryDate() {
		if (discoveryDate == null) {
			return null;
		}
		return (Date) discoveryDate.clone();
	}

	public synchronized void setDiscoveryDate(Date discoveryDate) {
		if (this.discoveryDate == null)
			this.discoveryDate = discoveryDate;
	}

	public Date getLastSeenDate() {
		if (lastSeenDate == null) {
			return null;
		}
		return (Date) lastSeenDate.clone();
	}

	public synchronized void setLastSeenDate(Date lastSeenDate) {
		this.lastSeenDate = lastSeenDate;
	}

	public Integer getQualityRating() {
		return new Integer(qualityRating);
	}

	public synchronized void setQualityRating(int qualityRating) {
		this.qualityRating = qualityRating;
	}

	public TagGen getTagType() {
		return tag.getTagGeneration();
	}

	public Gen1Tag getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(Gen1Tag tag) {
		this.tag = tag;
	}

	public Integer getReadCount() {
		return new Integer(readCount);
	}

	public void incrementReadCount() {
		this.readCount++;
	}

	/**
	 * Overrides the Object's equal method for easier comparison
	 */
	public boolean equals(Object o) {
		if (o instanceof RifidiTag) {
			RifidiTag t = (RifidiTag) o;
			return this.hashCode() == t.hashCode();
		} else {
			return false;
		}
	}

	/**
	 * Overides the Object's hashCode() method for easier storing in a hashtable
	 */
	public int hashCode() {
		return Arrays.hashCode(tag.readId());
	}

	public String toString() {
		return ByteAndHexConvertingUtility.toHexString(this.tag.readId());
	}

	public byte[] toByte() {
		return this.tag.readId();
	}

	@XmlElementWrapper
	public List<MapElement> getTagProperty() {
		List<MapElement> map = new ArrayList<MapElement>();
		if ( tag != null ) {
			map.add( new MapElement( "id", ByteAndHexConvertingUtility.toHexString(tag.getId()) ) );
			map.add( new MapElement( "gen", tag.getTagGeneration().toString() ) );
		}
		return map;
	}

	public void setTagProperty( List<MapElement> values ) {
		String id, gen;
		id = gen = null;
		for ( MapElement e : values ) {
			if ( e.key.equals("id") )
				id = e.val;
			if ( e.key.equals("gen") )
				gen = e.val;
		}

		RifidiTag rt = RifidiTagFactory.createTag( TagGen.valueOf(gen),
				ByteAndHexConvertingUtility.fromHexString(id));
		tag = rt.getTag();
	}
}