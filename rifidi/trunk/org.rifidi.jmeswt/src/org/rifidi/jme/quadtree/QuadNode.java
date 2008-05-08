/*
 *  QuadNodeOld.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jme.quadtree;

import java.util.ArrayList;

import org.rifidi.jme.quadtree.TargetData;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;

/**
 * @author Jochen Mader Oct 24, 2007
 *
 */
public class QuadNode extends Node{
	public enum Type{BRANCH, LEAF};
	private BoundingBox boundingBox;
	private Type quadType;
	private ArrayList<TargetData> targets;
	
	
	public QuadNode(String name, BoundingBox boundingBox, Type type) {
		super(name);
		this.boundingBox=boundingBox;
		this.quadType=type;
		targets=new ArrayList<TargetData>();
	}

	/**
	 * @return the boundingBox
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	/**
	 * @return the quadType
	 */
	public Type getQuadType() {
		return quadType;
	}

	/**
	 * @param quadType the quadType to set
	 */
	public void setQuadType(Type quadType) {
		this.quadType = quadType;
	}
	
	public void addTarget(TargetData targetData){
		targets.add(targetData);
	}
	
	public void removeTarget(TargetData targetData){
		targets.remove(targetData);
	}
	
	public Object[] getTargetArray(){
		return targets.toArray();
	}
}
