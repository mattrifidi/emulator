package org.rifidi.designer.octree;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;

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
public class CubeOctreeNode {
	/**
	 * Logger for this class.
	 */
	private static final Log logger = LogFactory.getLog(CubeOctreeNode.class);
	/**
	 * Center of this node.
	 */
	private Vector3f center;
	/**
	 * Sidelength of this node.
	 */
	private float extent;
	/**
	 * List of children of this node (empty if this node is not a leafnode).
	 */
	private List<VisualEntity> children;
	/**
	 * List of nodes attached to this node (empty if this is a leafnode).
	 */
	private List<CubeOctreeNode> nodes;
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
	 * Constructor.
	 * 
	 * @param center
	 *            center of the node
	 * @param sideLength
	 *            length of the side of the nodeenclosing cube
	 * @param targetSize
	 *            minimum size of the node
	 */
	public CubeOctreeNode(Vector3f center, float extent, float targetSize) {
		this.center = center;
		this.extent = extent;
		children = new ArrayList<VisualEntity>();
		nodes = new ArrayList<CubeOctreeNode>();
		// we are done if the sidelength equals the targetsize
		if (!(extent * 2 <= targetSize)) {
			float newExtent = extent / 2;
			// create the eight new nodes
			// the top 4 nodes
			nodes.add(new CubeOctreeNode(new Vector3f(center.x - newExtent,
					center.y + newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new CubeOctreeNode(new Vector3f(center.x + newExtent,
					center.y + newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new CubeOctreeNode(new Vector3f(center.x - newExtent,
					center.y + newExtent, center.z + newExtent), newExtent,
					targetSize));
			nodes.add(new CubeOctreeNode(new Vector3f(center.x + newExtent,
					center.y + newExtent, center.z + newExtent), newExtent,
					targetSize));
			// the bottom 4 nodes
			nodes.add(new CubeOctreeNode(new Vector3f(center.x - newExtent,
					center.y - newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new CubeOctreeNode(new Vector3f(center.x + newExtent,
					center.y - newExtent, center.z - newExtent), newExtent,
					targetSize));
			nodes.add(new CubeOctreeNode(new Vector3f(center.x - newExtent,
					center.y - newExtent, center.z + newExtent), newExtent,
					targetSize));
			nodes.add(new CubeOctreeNode(new Vector3f(center.x + newExtent,
					center.y - newExtent, center.z + newExtent), newExtent,
					targetSize));
		} else {
			leaf = true;
		}
	}

	/**
	 * Check if the given entity intersects this node.
	 * NOTE: 
	 * A node that touches another node is NOT intersecting a node.
	 * This is required to allow two entities to be placed directly next to each others.
	 * @param boundingNode
	 * @return
	 */
	public boolean intersects(Node boundingNode) {
		for (Spatial sp : boundingNode.getChildren()) {
			BoundingBox bb = (BoundingBox) sp.getWorldBound();
			if (center.x + extent <= Math.round(bb.getCenter().x - bb.xExtent)
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
		return false;
	}

	/**
	 * Add a new child to the tree.
	 * 
	 * @param entity
	 */
	public void addChild(VisualEntity entity) {
		hasChildren = true;
		if (leaf) {
			children.add(entity);
			return;
		}
		for (CubeOctreeNode node : nodes) {
			if (node.intersects(entity.getBoundingNode())) {
				node.addChild(entity);
			}
		}
	}

	/**
	 * Recursively checks if the given entity collides with the tree.
	 * 
	 * @param boundingNode
	 *            the boundingNode to check against
	 * @param colliders
	 *            the list that will contain all colliders
	 */
	public void findCollision(Node boundingNode, Set<VisualEntity> colliders) {
		if (hasChildren) {
			if (intersects(boundingNode)) {
				if (leaf) {
					colliders.addAll(children);
					return;
				}
				for (CubeOctreeNode node : nodes) {
					if (node.intersects(boundingNode)) {
						node.findCollision(boundingNode, colliders);
					}
				}
			}
		}
	}

	/**
	 * Recursively ask all nodes to remove the given entity.
	 * 
	 * @param entity
	 */
	public void removeChild(VisualEntity entity) {
		if (leaf) {
			children.remove(entity);
			return;
		}
		for (CubeOctreeNode node : nodes) {
			node.removeChild(entity);
		}
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
		if (leaf) {
			Box boxy = new Box("node", center.clone(), extent, extent, extent);
			boxy.setModelBound(new BoundingBox());
			node.attachChild(boxy);
			node.setModelBound(new BoundingBox());
			node.updateModelBound();
			node.updateWorldBound();
		}
		for (CubeOctreeNode no : nodes) {
			no.getTreeAsNode(node);
		}
	}
}
