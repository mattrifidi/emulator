package org.rifidi.designer.octree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.shape.Box;

/**
 * This class is the base buildingblock for the octree.
 * 
 * @author Jochen Mader - jochen@pramari.com - Jul 4, 2008
 * 
 */
public class RoomOctreeNode {
	
	public static enum Sides{LEFT,RIGHT,FRONT,BACK};
	/**
	 * Logger for this class.
	 */
	private static final Log logger = LogFactory.getLog(RoomOctreeNode.class);
	/**
	 * Center of this node.
	 */
	private Vector3f center;
	/**
	 * Sidelength of this node.
	 */
	private float extent;
	/**
	 * List of nodes attached to this node (empty if this is a leafnode).
	 */
	private List<RoomOctreeNode> nodes;
	/**
	 * If this node is a leafnode set this to true.
	 */
	private boolean leaf = false;
	/**
	 * True if any leafs of this node have a child.
	 */
	private boolean hasChildren = false;
	/**
	 * Size of the tree.
	 */
	public static int size = 0;
	/**
	 * Minimum size of cube.
	 */
	private float targetSize;

	/**
	 * Constructor.
	 * 
	 * @param center
	 *            center of the node
	 * @param sideLength
	 *            length of the side of the nodeenclosing cube
	 * @param targetSize
	 *            minimum size of the node
	 */
	public RoomOctreeNode(Vector3f center, float extent, float targetSize) {
		this.center = center;
		this.extent = extent;
		this.targetSize = targetSize;
		nodes = new ArrayList<RoomOctreeNode>();
		// we are done if the sidelength equals the targetsize
		if (extent * 2 <= targetSize) {
			leaf = true;
		}
	}
    
	/**
	 * Checks if the given triangle would bleed into an adjacent triangle.
	 * @param triangle
	 * @return
	 */
	private boolean checkSides(Vector3f[] triangle){
		//avoid bleeding into the cube in front
		if(!(triangle[0].z>=center.z-extent && triangle[1].z>=center.z-extent && triangle[2].z>=center.z-extent)){
			return false;
		}
		//avoid bleeding into the cube in the back
		if(!(triangle[0].z<=center.z+extent && triangle[1].z<=center.z+extent && triangle[2].z<=center.z+extent)){
			return false;
		}
		//avoid bleeding into the cube on top
		if(!(triangle[0].y>=center.y-extent && triangle[1].y>=center.y-extent && triangle[2].y>=center.y-extent)){
			return false;
		}
		//avoid bleeding into the cube on the left
		if(!(triangle[0].x<=center.x+extent && triangle[1].x<=center.x+extent && triangle[2].x<=center.x+extent)){
			return false;
		}
		//avoid bleeding into the cube on the right
		if(!(triangle[0].x>=center.x-extent && triangle[1].x>=center.x-extent && triangle[2].x>=center.x-extent)){
			return false;
		}
		return true;
	}
	
	/**
	 * Check if the given entity intersects this node. NOTE: A node that touches
	 * another node is NOT intersecting a node. This is required to allow two
	 * entities to be placed directly next to each others.
	 * 
	 * @param boundingNode
	 * @return
	 */
	public boolean intersects(Vector3f[] triangle, Sides side) {
		if(side.equals(Sides.LEFT)){
			//avoid bleeding into the adjacent cube
			if(triangle[0].x==center.x+extent && triangle[1].x==center.x+extent && triangle[2].x==center.x+extent){
				return false;
			}
			if(!checkSides(triangle)){
				return false;
			}
		}
		else if(side.equals(Sides.RIGHT)){
			//avoid bleeding into the adjacent cube
			if(triangle[0].x==center.x-extent && triangle[1].x==center.x-extent && triangle[2].x==center.x-extent){
				return false;
			}
			if(!checkSides(triangle)){
				return false;
			}
		}
		else if(side.equals(Sides.FRONT)){
			if(triangle[0].z==center.z-extent && triangle[1].z==center.z-extent && triangle[2].z==center.z-extent){
				return false;
			}
			if(!checkSides(triangle)){
				return false;
			}
		}
		else if(side.equals(Sides.BACK)){
			if(triangle[0].z==center.z+extent && triangle[1].z==center.z+extent && triangle[2].z==center.z+extent){
				return false;
			}
			if(!checkSides(triangle)){
				return false;
			}
		}
		
		for (Vector3f point : triangle) {
			if (center.x + extent < point.x || center.x - extent > point.x)
				;
			else if (center.y + extent < point.y || center.y - extent > point.y)
				;
			else if (center.z + extent < point.z || center.z - extent > point.z)
				;
			else {
				return true;
			}
		}
		return false;
	}

	/**
	 * Check if the given entity intersects this node. NOTE: A node that touches
	 * another node is NOT intersecting a node. This is required to allow two
	 * entities to be placed directly next to each others.
	 * 
	 * @param boundingNode
	 * @return
	 */
	public boolean intersects(Node boundingNode) {
		if (boundingNode.getChildren() != null) {
			for (Spatial sp : boundingNode.getChildren()) {
				BoundingBox bb = (BoundingBox) sp.getWorldBound();
				if (center.x + extent <= Math.round(bb.getCenter().x
						- bb.xExtent)
						|| center.x - extent >= Math.round(bb.getCenter().x
								+ bb.xExtent))
					;
				else if (center.y + extent <= Math.round(bb.getCenter().y
						- bb.yExtent)
						|| center.y - extent >= Math.round(bb.getCenter().y
								+ bb.yExtent))
					;
				else if (center.z + extent <= Math.round(bb.getCenter().z
						- bb.zExtent)
						|| center.z - extent >= Math.round(bb.getCenter().z
								+ bb.zExtent))
					;
				else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Add a new child to the tree.
	 * 
	 * @param entity
	 */
	public void addChild(List<Vector3f[]> triangles, Sides side) {
		hasChildren = true;
		if (leaf) {
			return;
		}
		float newExtent = extent / 2;
		if (nodes.size() == 0) {
			// create the eight new nodes
			// the top 4 nodes
			nodes.add(new RoomOctreeNode(new Vector3f(center.x - newExtent,
					center.y + newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new RoomOctreeNode(new Vector3f(center.x + newExtent,
					center.y + newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new RoomOctreeNode(new Vector3f(center.x - newExtent,
					center.y + newExtent, center.z + newExtent), newExtent,
					targetSize));
			nodes.add(new RoomOctreeNode(new Vector3f(center.x + newExtent,
					center.y + newExtent, center.z + newExtent), newExtent,
					targetSize));
			// the bottom 4 nodes
			nodes.add(new RoomOctreeNode(new Vector3f(center.x - newExtent,
					center.y - newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new RoomOctreeNode(new Vector3f(center.x + newExtent,
					center.y - newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new RoomOctreeNode(new Vector3f(center.x - newExtent,
					center.y - newExtent, center.z + newExtent), newExtent,
					targetSize));
			nodes.add(new RoomOctreeNode(new Vector3f(center.x + newExtent,
					center.y - newExtent, center.z + newExtent), newExtent,
					targetSize));

		}

		for (RoomOctreeNode node : nodes) {
			List<Vector3f[]> intersectors = new ArrayList<Vector3f[]>();
			for (Vector3f[] triangle : triangles) {
				if (node.intersects(triangle, side)) {
					intersectors.add(triangle);
				}
			}
			if (intersectors.size() > 0) {
				node.addChild(intersectors, side);
			}
		}
	}

	/**
	 * Recursively checks if the given entity collides with the tree.
	 * 
	 * @param boundingNode
	 *            the boundingNode to check against
	 */
	public boolean findCollision(Node boundingNode) {
		if (hasChildren) {
			if (leaf) {
				return true;
			}
			for (RoomOctreeNode node : nodes) {
				if (node.intersects(boundingNode)) {
					boolean ret=node.findCollision(boundingNode);
					if(ret==true){
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * @return the hasChildren
	 */
	public boolean hasChildren() {
		return this.hasChildren;
	}

	/**
	 * Add all created CubeOctreeNodes as a box representation to the given
	 * node.
	 * 
	 * @param node
	 */
	private static int count = 0;

	public void getTreeAsNode(Node node) {
		if (leaf && hasChildren) {
			Box boxy = new Box("node", center.clone(), extent, extent, extent);
			boxy.setModelBound(new BoundingBox());
			node.attachChild(boxy);
			node.setModelBound(new BoundingBox());
			node.updateModelBound();
			node.updateWorldBound();
		}
		for (RoomOctreeNode no : nodes) {
			no.getTreeAsNode(node);
		}
	}
}
