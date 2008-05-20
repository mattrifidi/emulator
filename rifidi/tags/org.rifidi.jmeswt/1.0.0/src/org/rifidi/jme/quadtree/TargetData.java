/*
 *  TargetData.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jme.quadtree;

import java.util.List;

import org.rifidi.jme.quadtree.QuadNode;

import com.jme.bounding.BoundingBox;
import com.jme.scene.Node;

/**
 * @author Jochen Mader Oct 24, 2007
 *
 */
public class TargetData {
	private Node node;
	private BoundingBox boundingBox;
	private List<QuadNode> parents;
	/**
	 * @return the node
	 */
	public Node getNode() {
		return node;
	}
	/**
	 * @param node the node to set
	 */
	public void setNode(Node node) {
		this.node = node;
	}
	/**
	 * @return the boundingBox
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	/**
	 * @param boundingBox the boundingBox to set
	 */
	public void setBoundingBox(BoundingBox boundingBox) {
		this.boundingBox = boundingBox;
	}
	/**
	 * @return the parents
	 */
	public List<QuadNode> getParents() {
		return parents;
	}
	/**
	 * @param parents the parents to set
	 */
	public void setParents(List<QuadNode> parents) {
		this.parents = parents;
	}
	
	public void addParent(QuadNode quadNode){
		parents.add(quadNode);
	}
	
	public void removeParent(QuadNode quadNode){
		parents.remove(quadNode);
	}	
}
