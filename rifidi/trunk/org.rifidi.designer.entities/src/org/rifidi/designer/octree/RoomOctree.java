/*
 *  CollisionOctree.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.octree;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.designer.entities.VisualEntity;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.TriMesh;

/**
 * This interface defines the methods for an octree that can be used for
 * collision detection between two visual entites.
 * 
 * @author Jochen Mader - jochen@pramari.com - Jul 7, 2008
 * 
 */
public class RoomOctree {
	/**
	 * Logger for this class.
	 */
	private static final Log logger = LogFactory.getLog(RoomOctree.class);
	/**
	 * Root of the tree.
	 */
	private RoomOctreeNode root;

	/**
	 * @param minCubeSize
	 * @param startVolume
	 */
	public RoomOctree(Float minCubeSize, BoundingBox startVolume) {
		logger.debug("Created new Octree");
		// create a cube
		float side = startVolume.xExtent > startVolume.zExtent ? startVolume.xExtent
				: startVolume.zExtent;
		int sideCount = (int) Math.ceil(side / minCubeSize);
		// no log2 in java, darn
		int count = 2;
		while (count < sideCount) {
			count *= 2;
		}
		sideCount = count;
		side = count * minCubeSize;
		root = new RoomOctreeNode(new Vector3f(side, side, side), side,
				minCubeSize);
	}

	/**
	 * Insert the given entity into the collision tree.
	 * 
	 * @param entity
	 */
	public void insertMesh(TriMesh entity) {
		List<Vector3f[]> trianglesLeft = new ArrayList<Vector3f[]>();
		List<Vector3f[]> trianglesRight = new ArrayList<Vector3f[]>();
		List<Vector3f[]> trianglesTop = new ArrayList<Vector3f[]>();
		List<Vector3f[]> trianglesBottom = new ArrayList<Vector3f[]>();
		List<Vector3f[]> trianglesBack = new ArrayList<Vector3f[]>();
		List<Vector3f[]> trianglesFront = new ArrayList<Vector3f[]>();
		float left=((BoundingBox)entity.getWorldBound()).getCenter().x-((BoundingBox)entity.getWorldBound()).xExtent;
		float right=((BoundingBox)entity.getWorldBound()).getCenter().x+((BoundingBox)entity.getWorldBound()).xExtent;
		float back=((BoundingBox)entity.getWorldBound()).getCenter().z-((BoundingBox)entity.getWorldBound()).zExtent;
		float front=((BoundingBox)entity.getWorldBound()).getCenter().z+((BoundingBox)entity.getWorldBound()).zExtent;
		float top=((BoundingBox)entity.getWorldBound()).getCenter().y-((BoundingBox)entity.getWorldBound()).yExtent;
		float bottom=((BoundingBox)entity.getWorldBound()).getCenter().y+((BoundingBox)entity.getWorldBound()).yExtent;
		for (int count = 0; count < entity.getTriangleCount(); count++) {
			// collect all triangles
			Vector3f[] triangle = new Vector3f[] { new Vector3f(),
					new Vector3f(), new Vector3f() };
			entity.getTriangle(count, triangle);
			triangle[0].addLocal(entity.getWorldTranslation());
			triangle[1].addLocal(entity.getWorldTranslation());
			triangle[2].addLocal(entity.getWorldTranslation());
			if(triangle[0].x==left && triangle[1].x==left && triangle[2].x==left){
				trianglesLeft.add(triangle);
			}
			else if(triangle[0].x==right && triangle[1].x==right && triangle[2].x==right){
				trianglesRight.add(triangle);
			}
			else if(triangle[0].z==back && triangle[1].z==back && triangle[2].z==back){
				trianglesBack.add(triangle);
			}
			else if(triangle[0].z==front && triangle[1].z==front && triangle[2].z==front){
				trianglesFront.add(triangle);
			}
			else if(triangle[0].y==top && triangle[1].y==top && triangle[2].y==top){
				trianglesTop.add(triangle);
			}
			else if(triangle[0].y==bottom && triangle[1].y==bottom && triangle[2].y==bottom){
				trianglesBottom.add(triangle);
			}
		}
		root.addChild(trianglesLeft, RoomOctreeNode.Sides.LEFT);
		root.addChild(trianglesRight, RoomOctreeNode.Sides.RIGHT);
		root.addChild(trianglesFront, RoomOctreeNode.Sides.FRONT);
		root.addChild(trianglesBack, RoomOctreeNode.Sides.BACK);
	}

	/**
	 * Find all colliders for the given entity.
	 * 
	 * @param entity
	 * @param colliders
	 */
	public boolean findCollisions(VisualEntity entity) {
		return root.findCollision(entity.getBoundingNode());
	}

	public Node getTreeAsNode() {
		Node node = new Node();
		node.setModelBound(new BoundingBox());
		root.getTreeAsNode(node);
		node.updateGeometricState(0f, true);
		node.updateModelBound();
		node.updateWorldBound();
		return node;
	}
}