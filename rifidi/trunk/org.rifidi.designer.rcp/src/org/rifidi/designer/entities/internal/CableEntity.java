/*
 *  CableEntity.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.entities.internal;

import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import org.rifidi.designer.entities.Entity;
import org.rifidi.designer.entities.interfaces.GPI;
import org.rifidi.designer.entities.interfaces.GPO;

/**
 * A virtual cabler between a GPO and a GPI.
 * 
 * @author Jochen Mader - jochen@pramari.com - Feb 27, 2008
 * 
 */
@XmlRootElement
public class CableEntity extends Entity {

	/**
	 * GP output.
	 */
	private Entity gpo;

	/**
	 * GP input.
	 */
	private Entity gpi;

	/**
	 * Port on the GPO where the signal originates from.
	 */
	private int sourcePort;

	/**
	 * Target port on the GPI.
	 */
	private int targetPort;

	/**
	 * Constructor.
	 * 
	 * @param gpo
	 * @param gpi
	 */
	public CableEntity(GPO gpo, GPI gpi) {
		this.gpo = (Entity)gpo;
		this.gpi = (Entity)gpi;
		sourcePort = 0;
		targetPort = 1;
	}

	/**
	 * Constructor.
	 */
	public CableEntity() {
		sourcePort = 0;
		targetPort = 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.rifidi.designer.entities.Entity#destroy()
	 */
	@Override
	public void destroy() {
	}

	/**
	 * @return the gpo
	 */
	public Entity getGpo() {
		return gpo;
	}

	/**
	 * @param gpo
	 *            the gpo to set
	 */
	@XmlIDREF
	public void setGpo(Entity gpo) {
		this.gpo = gpo;
	}

	/**
	 * @return the gpi
	 */
	public Entity getGpi() {
		return gpi;
	}

	/**
	 * @param gpi
	 *            the gpi to set
	 */
	@XmlIDREF
	public void setGpi(Entity gpi) {
		this.gpi = gpi;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof CableEntity) {
			return (obj.hashCode() == hashCode());
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
		if (gpi == null && gpo == null) {
			return "".hashCode();
		} else if (gpi == null) {
			return ((Entity) gpo).getName().hashCode();
		} else if (gpo == null) {
			return ((Entity) gpi).getName().hashCode();
		}
		return (((Entity) gpi).getName() + ((Entity) gpo).getName()).hashCode();
	}

	/**
	 * @return the sourcePort
	 */
	public int getSourcePort() {
		return this.sourcePort;
	}

	/**
	 * @param sourcePort
	 *            the sourcePort to set
	 */
	public void setSourcePort(int sourcePort) {
		this.sourcePort = sourcePort;
	}

	/**
	 * @return the targetPort
	 */
	public int getTargetPort() {
		return this.targetPort;
	}

	/**
	 * @param targetPort
	 *            the targetPort to set
	 */
	public void setTargetPort(int targetPort) {
		this.targetPort = targetPort;
	}

}
