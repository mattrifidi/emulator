/**
 * 
 */
package org.rifidi.prototyper.model;

/**
 * @author kyle
 * 
 */
public class HotspotViewModel {
	private int x = 10;
	private int y = 10;
	private int minimumWidth = 50;
	private int minimumHeight = 50;
	private String name = "hs1";
	private String readerID = "reader1";
	private Integer antennaID;

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the readerID
	 */
	public String getReaderID() {
		return readerID;
	}

	/**
	 * @param readerID
	 *            the readerID to set
	 */
	public void setReaderID(String readerID) {
		this.readerID = readerID;
	}

	/**
	 * @return the antennaID
	 */
	public Integer getAntennaID() {
		return antennaID;
	}

	/**
	 * @param antennaID
	 *            the antennaID to set
	 */
	public void setAntennaID(Integer antennaID) {
		this.antennaID = antennaID;
	}

	/**
	 * @return the minimumWidth
	 */
	public int getMinimumWidth() {
		return minimumWidth;
	}

	/**
	 * @param minimumWidth
	 *            the minimumWidth to set
	 */
	public void setMinimumWidth(int minimumWidth) {
		this.minimumWidth = minimumWidth;
	}

	/**
	 * @return the minimumHeight
	 */
	public int getMinimumHeight() {
		return minimumHeight;
	}

	/**
	 * @param minimumHeight
	 *            the minimumHeight to set
	 */
	public void setMinimumHeight(int minimumHeight) {
		this.minimumHeight = minimumHeight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 != null) {
			if (arg0 instanceof HotspotViewModel) {
				HotspotViewModel model = (HotspotViewModel) arg0;
				if ((model.antennaID == this.antennaID)
						&& model.name.equals(this.name)) {
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String s = this.name + ":" + this.antennaID;
		return s.hashCode();
	}

	public String hashString() {
		return this.name + ":" + this.antennaID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Hotspot reader:" + name + " ant:" + antennaID;
	}

}
