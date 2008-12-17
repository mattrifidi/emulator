/*
 *  RifidiTagDecorator.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.library.basemodels.boxproducer;

import java.beans.PropertyChangeListener;
import java.util.Date;

import org.rifidi.tags.IGen1Tag;
import org.rifidi.tags.enums.TagGen;
import org.rifidi.tags.id.TagType;
import org.rifidi.tags.impl.RifidiTag;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - Dec 11, 2008
 * 
 */
public class RifidiTagDecorator extends RifidiTag{
	/**
	 * @param rifidiTag
	 * @param parent
	 */
	public RifidiTagDecorator(RifidiTag rifidiTag, BoxproducerEntity parent) {
		super();
		this.rifidiTag = rifidiTag;
		this.parent = parent;
	}

	private RifidiTag rifidiTag;
	private BoxproducerEntity parent;
	/**
	 * @param propertyChangeListener
	 * @see org.rifidi.services.tags.impl.RifidiTag#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		this.rifidiTag.addPropertyChangeListener(propertyChangeListener);
	}

	/**
	 * @param o
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		return this.rifidiTag.equals(o);
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getAntennaLastSeen()
	 */
	public Integer getAntennaLastSeen() {
		return this.rifidiTag.getAntennaLastSeen();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getLastSeenDate()
	 */
	public Date getLastSeenDate() {
		return this.rifidiTag.getLastSeenDate();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getQualityRating()
	 */
	public Integer getQualityRating() {
		return this.rifidiTag.getQualityRating();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getReadCount()
	 */
	public Integer getReadCount() {
		return this.rifidiTag.getReadCount();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getTag()
	 */
	public IGen1Tag getTag() {
		return this.rifidiTag.getTag();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getTagEntitiyID()
	 */
	public long getTagEntitiyID() {
		return this.rifidiTag.getTagEntitiyID();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getTagGen()
	 */
	public TagGen getTagGen() {
		return this.rifidiTag.getTagGen();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#getTagType()
	 */
	public TagType getTagType() {
		return this.rifidiTag.getTagType();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#hashCode()
	 */
	public int hashCode() {
		return this.rifidiTag.hashCode();
	}

	/**
	 * 
	 * @see org.rifidi.services.tags.impl.RifidiTag#incrementReadCount()
	 */
	public void incrementReadCount() {
		this.rifidiTag.incrementReadCount();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#isDeleted()
	 */
	public boolean isDeleted() {
		return this.rifidiTag.isDeleted();
	}

	/**
	 * @param propertyChangeListener
	 * @see org.rifidi.services.tags.impl.RifidiTag#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(
			PropertyChangeListener propertyChangeListener) {
		this.rifidiTag.removePropertyChangeListener(propertyChangeListener);
	}

	/**
	 * @param antennaLastSeen
	 * @see org.rifidi.services.tags.impl.RifidiTag#setAntennaLastSeen(int)
	 */
	public void setAntennaLastSeen(int antennaLastSeen) {
		this.rifidiTag.setAntennaLastSeen(antennaLastSeen);
	}

	/**
	 * @param deleted
	 * @see org.rifidi.services.tags.impl.RifidiTag#setDeleted(boolean)
	 */
	public void setDeleted(boolean deleted) {
		this.rifidiTag.setDeleted(deleted);
	}

	/**
	 * @param discoveryDate
	 * @see org.rifidi.services.tags.impl.RifidiTag#setDiscoveryDate(java.util.Date)
	 */
	public void setDiscoveryDate(Date discoveryDate) {
		this.rifidiTag.setDiscoveryDate(discoveryDate);
	}

	/**
	 * @param lastSeenDate
	 * @see org.rifidi.services.tags.impl.RifidiTag#setLastSeenDate(java.util.Date)
	 */
	public void setLastSeenDate(Date lastSeenDate) {
		this.rifidiTag.setLastSeenDate(lastSeenDate);
	}

	/**
	 * @param qualityRating
	 * @see org.rifidi.services.tags.impl.RifidiTag#setQualityRating(int)
	 */
	public void setQualityRating(int qualityRating) {
		this.rifidiTag.setQualityRating(qualityRating);
	}

	/**
	 * @param tag
	 * @see org.rifidi.services.tags.impl.RifidiTag#setTag(org.rifidi.services.tags.IGen1Tag)
	 */
	public void setTag(IGen1Tag tag) {
		this.rifidiTag.setTag(tag);
	}

	/**
	 * @param tagEntitiyID
	 * @see org.rifidi.services.tags.impl.RifidiTag#setTagEntitiyID(long)
	 */
	public void setTagEntitiyID(long tagEntitiyID) {
		this.rifidiTag.setTagEntitiyID(tagEntitiyID);
	}

	/**
	 * @param tagType
	 * @see org.rifidi.services.tags.impl.RifidiTag#setTagType(org.rifidi.services.tags.id.TagType)
	 */
	public void setTagType(TagType tagType) {
		this.rifidiTag.setTagType(tagType);
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#toByte()
	 */
	public byte[] toByte() {
		return this.rifidiTag.toByte();
	}

	/**
	 * @return
	 * @see org.rifidi.services.tags.impl.RifidiTag#toString()
	 */
	public String toString() {
		return this.rifidiTag.toString();
	}

	/**
	 * @return the parent
	 */
	public BoxproducerEntity getParent() {
		return this.parent;
	}

	/**
	 * @param parent the parent to set
	 */
	public void setParent(BoxproducerEntity parent) {
		this.parent = parent;
	}
}
