package org.rifidi.tags.impl;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.rifidi.common.utilities.ByteAndHexConvertingUtility;
import org.rifidi.tags.IGen1Tag;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.id.TagType;

/**
 * RifidiTag is a wrapper aound Gen1Tag that stores extra information about a
 * RFID tag that is not part of the EPCGlobal Gen1 or Gen2 spec, such as
 * discovery date, readcount, etc. A tag is identified internally through a long
 * value (tagEntitiyID), and not through its EPCGlobal byte id. This allows us
 * to separate the physical tag from the EPCGlobal ID, which is especially
 * necessary when writing to a tag
 * 
 * @author Andreas Huebner - andreas@pramari.com
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class RifidiTag implements Serializable {

	/** Serial version for serializable. */
	private static final long serialVersionUID = 1L;

	private float speed = 0.0f;

	private float rssi = 0.0f;

	/** The unique ID for identify this tag internally */
	private long tagEntitiyID;
	/** The Tag to whom this information belongs */
	@XmlElement(type = Object.class)
	private IGen1Tag tag;
	/** What type is the TagId (DoD, Custom96, ...) */
	private TagType tagType;
	/** Antenna the tag was last seen at */
	private int antennaLastSeen;
	/** Discovery date of tag */
	private Date discoveryDate;
	/** Last seen date of tag */
	private Date lastSeenDate;
	/** ID used for XML documents. */
	@SuppressWarnings("unused")
	@XmlID
	private String xmlID;
	/** If the tag got deleted in the registry this property is set to true */
	private boolean deleted;
	/** How many times the tag has been read. */
	private int readCount = 0;
	/** Status of this tag. Could be disabled and only seen by the IDE. */
	public boolean isVisbile = true;

	@XmlTransient
	transient protected PropertyChangeSupport propertyChangeSupport;
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
		init();
	}

	/**
	 * This method is needed so that the init method is called when the object
	 * is deserialized.
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		in.defaultReadObject();
		init();
	}

	/**
	 * Any custom work that needs to happen when serializing can go here.
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	/**
	 * Constructor.
	 * 
	 * @param tag
	 */
	public RifidiTag(IGen1Tag tag) {
		this.tag = tag;
		init();
	}

	/**
	 * This method performs initialization of transient (those that are not
	 * brought back to life by serizalization) objects.
	 */
	private void init() {
		propertyChangeSupport = new PropertyChangeSupport(this);
	}

	/**
	 * @return the idFormat
	 */
	public TagType getTagType() {
		return tagType;
	}

	/**
	 * @return the speed
	 */
	public Float getSpeed() {
		return speed;
	}

	/**
	 * @param speed
	 *            the speed to set
	 */
	public void setSpeed(Float speed) {
		this.speed = speed;
	}

	/**
	 * @return the rssi
	 */
	public float getRssi() {
		return rssi;
	}

	/**
	 * @param rssi
	 *            the rssi to set
	 */
	public void setRssi(float rssi) {
		this.rssi = rssi;
	}

	/**
	 * @param idFormat
	 *            the idFormat to set
	 */
	public void setTagType(TagType tagType) {
		this.tagType = tagType;
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

	public TagGen getTagGen() {
		return tag.getTagGeneration();
	}

	public IGen1Tag getTag() {
		return tag;
	}

	/**
	 * @param tag
	 *            the tag to set
	 */
	public void setTag(IGen1Tag tag) {
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
		return new Long(tagEntitiyID).hashCode();
	}

	public String toString() {
		return ByteAndHexConvertingUtility.toHexString(this.tag.readId());
	}

	public byte[] toByte() {
		return this.tag.readId();
	}

	// @XmlElementWrapper
	// public List<MapElement> getTagProperty() {
	// List<MapElement> map = new ArrayList<MapElement>();
	// if (tag != null) {
	// map.add(new MapElement("id", ByteAndHexConvertingUtility
	// .toHexString(tag.getId())));
	// map.add(new MapElement("gen", tag.getTagGeneration().toString()));
	// }
	// return map;
	// }
	//
	// public void setTagProperty(List<MapElement> values) {
	// String id, gen;
	// id = gen = null;
	// for (MapElement e : values) {
	// if (e.key.equals("id"))
	// id = e.val;
	// if (e.key.equals("gen"))
	// gen = e.val;
	// }
	//
	// if (TagGen.valueOf(gen) == TagGen.GEN2) {
	// byte[] pass = { 0x00, 0x00, 0x00, 0x00 };
	// tag = new C1G2Tag(ByteAndHexConvertingUtility.fromHexString(id),
	// pass, pass.clone());
	// } else {
	// tag = new C1G1Tag(ByteAndHexConvertingUtility.fromHexString(id));
	// }
	// }

	public long getTagEntitiyID() {
		return tagEntitiyID;
	}

	public void setTagEntitiyID(long tagEntitiyID) {
		this.tagEntitiyID = tagEntitiyID;
		this.xmlID = "RIFIDITAG-" + tagEntitiyID;
	}

	/**
	 * @return the deleted
	 */
	public boolean isDeleted() {
		return this.deleted;
	}

	/**
	 * @param deleted
	 *            the deleted to set
	 */
	public void setDeleted(boolean deleted) {
		propertyChangeSupport.firePropertyChange("deleted", this.deleted,
				this.deleted = deleted);
	}

	/**
	 * Add a listener for property change events.
	 * 
	 * @param propertyChangeListener
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}

	/**
	 * Remove a listener for property change events.
	 * 
	 * @param propertyChangeListener
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		propertyChangeSupport.addPropertyChangeListener(propertyChangeListener);
	}
}