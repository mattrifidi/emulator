/*
 *  QuadTree.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.jme.quadtree;


import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.jme.quadtree.FrustumCheckCamera;
import org.rifidi.jme.quadtree.QuadNode;
import org.rifidi.jme.quadtree.QuadTree;
import org.rifidi.jme.quadtree.TargetData;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.renderer.Camera;
import com.jme.renderer.Renderer;
import com.jme.scene.Geometry;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.system.DisplaySystem;

/**
 * @author Jochen Mader Oct 24, 2007
 *
 */
public class QuadTree extends Node {
	private static Log logger=LogFactory.getLog(QuadTree.class);
	private Integer extent;
	private BoundingBox boundingBox;
	private List<QuadNode> quadNodes;
	private List<TargetData> targets;
	private FrustumCheckCamera frustumCheckCamera;
	private Set<TargetData> renderTargets;
	/**
	 * Creates a QuadTree.
	 * The center of the tree is at 0,0,0
	 * 
	 * @param name name of the tree
	 * @param extent x,y extent
	 */
	public QuadTree(String name, Integer extent) {
		super(name);
		this.extent=extent;
		this.boundingBox=new BoundingBox(new Vector3f(),(float)extent,1f,(float)extent);
		quadNodes=new ArrayList<QuadNode>();
		targets=new ArrayList<TargetData>();
		createQuadTree();
		DisplaySystem display=DisplaySystem.getDisplaySystem();
		frustumCheckCamera = new FrustumCheckCamera(display.getWidth(), display.getHeight());

		frustumCheckCamera.setFrustumPerspective(20.0f, (float) display.getWidth()
				/ (float) display.getHeight(), 2f, 300f);
		frustumCheckCamera.setParallelProjection(false);
		frustumCheckCamera.update();
		renderTargets=new HashSet<TargetData>();
		logger.info("Created QuadTree("+name+") with "+quadNodes.size()+" quads plus root");
	}
	
	public void sortInto(Node target){
		TargetData targetData=new TargetData();
		targetData.setBoundingBox(createBoundingBox(target));
		targetData.setNode(target);
		targetData.setParents(new ArrayList<QuadNode>());
		targets.add(targetData);
		targetData.getBoundingBox().setCenter(targetData.getNode().getLocalTranslation());
		for(Spatial sp:getChildren()){
			if(((QuadNode)sp).getBoundingBox().intersects(targetData.getBoundingBox())){
				sortInto((QuadNode)sp,targetData);
			}	
		}
	}
	
	private void sortInto(QuadNode quadNode,TargetData targetData){
		for(Spatial sp:quadNode.getChildren()){
			if(((QuadNode)sp).getBoundingBox().intersects(targetData.getBoundingBox())){
				if(((QuadNode)sp).getQuadType().equals(QuadNode.Type.LEAF)){
					((QuadNode)sp).addTarget(targetData);
					targetData.addParent((QuadNode)sp);
				}
				else{
					sortInto((QuadNode)sp,targetData);
				}
			}	
		}
	}
	
	/**
	 * Helpermethod to create a boundingbox from a node.
	 * @param node
	 * @return
	 */
	private BoundingBox createBoundingBox(Node node){
		FloatBuffer vertexBuffer=null;
		BoundingBox bbox=new BoundingBox();
		//walk through all children
		for(Spatial child:node.getChildren()){
			//check if it contains tri data
			if(child instanceof Geometry){
				Geometry geom=((Geometry)child);
				for(int count=0;count<geom.getBatchCount();count++){
					if(vertexBuffer==null){
						vertexBuffer=geom.getVertexBuffer(count);	
					}
					else{
						FloatBuffer tempBuffer=FloatBuffer.allocate(vertexBuffer.capacity()+geom.getVertexBuffer(count).capacity());
						tempBuffer.put(vertexBuffer);
						tempBuffer.put(geom.getVertexBuffer(count));
						vertexBuffer=tempBuffer;
					}
				}
				//compute the boundingbox
				bbox.computeFromPoints(vertexBuffer);		
			}
		}
		if(node.getLocalTranslation()==new Vector3f()){
			bbox.setCenter(node.getLocalTranslation());	
		}
		return bbox;
	}
	
	private void createQuadTree(){
		float newExtent=extent/2;
		QuadNode leftTop=new QuadNode("leftTop",new BoundingBox(new Vector3f(-newExtent,0,-newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
		quadNodes.add(leftTop);
		attachChild(leftTop);
		createQuadTree(leftTop);
		QuadNode rightTop=new QuadNode("rightTop",new BoundingBox(new Vector3f(newExtent,0,-newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
		quadNodes.add(rightTop);
		attachChild(rightTop);
		createQuadTree(rightTop);
		QuadNode leftBottom=new QuadNode("leftBottom",new BoundingBox(new Vector3f(-newExtent,0,newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
		quadNodes.add(leftBottom);
		attachChild(leftBottom);
		createQuadTree(leftBottom);
		QuadNode rightBottom=new QuadNode("rightBottom",new BoundingBox(new Vector3f(newExtent,0,newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
		attachChild(rightBottom);
		quadNodes.add(rightBottom);		
		createQuadTree(rightBottom);	
	}
	
	private void createQuadTree(QuadNode quadNode){
		float newExtent=quadNode.getBoundingBox().xExtent/2;
		float centerX=quadNode.getBoundingBox().getCenter().x;
		float centerZ=quadNode.getBoundingBox().getCenter().z;
		if(newExtent>0.5){
			QuadNode leftTop=new QuadNode("leftTop",new BoundingBox(new Vector3f(centerX-newExtent,0,centerZ-newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
			quadNodes.add(leftTop);
			quadNode.attachChild(leftTop);
			createQuadTree(leftTop);
			QuadNode rightTop=new QuadNode("rightTop",new BoundingBox(new Vector3f(centerX+newExtent,0,centerZ-newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
			quadNodes.add(rightTop);
			quadNode.attachChild(rightTop);
			createQuadTree(rightTop);
			QuadNode leftBottom=new QuadNode("leftBottom",new BoundingBox(new Vector3f(centerX-newExtent,0,centerZ+newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
			quadNodes.add(leftBottom);
			quadNode.attachChild(leftBottom);
			createQuadTree(leftBottom);
			QuadNode rightBottom=new QuadNode("rightBottom",new BoundingBox(new Vector3f(centerX+newExtent,0,centerZ+newExtent),newExtent,1,newExtent),QuadNode.Type.BRANCH);
			quadNodes.add(rightBottom);
			quadNode.attachChild(rightBottom);
			createQuadTree(rightBottom);
		}
		//congratulations it's a leaf
		else{
			QuadNode leftTop=new QuadNode("leftTop",new BoundingBox(new Vector3f(centerX-newExtent,0,centerZ-newExtent),newExtent,1,newExtent),QuadNode.Type.LEAF);
			quadNodes.add(leftTop);
			quadNode.attachChild(leftTop);
			QuadNode rightTop=new QuadNode("rightTop",new BoundingBox(new Vector3f(centerX+newExtent,0,centerZ-newExtent),newExtent,1,newExtent),QuadNode.Type.LEAF);
			quadNodes.add(rightTop);
			quadNode.attachChild(rightTop);
			QuadNode leftBottom=new QuadNode("leftBottom",new BoundingBox(new Vector3f(centerX-newExtent,0,centerZ+newExtent),newExtent,1,newExtent),QuadNode.Type.LEAF);
			quadNodes.add(leftBottom);
			quadNode.attachChild(leftBottom);
			QuadNode rightBottom=new QuadNode("rightBottom",new BoundingBox(new Vector3f(centerX+newExtent,0,centerZ+newExtent),newExtent,1,newExtent),QuadNode.Type.LEAF);
			quadNodes.add(rightBottom);
			quadNode.attachChild(rightBottom);
		}
	}
	
	public String toString(){
		StringBuffer sb=new StringBuffer();
		sb.append("QuadTree("+name+") with "+quadNodes.size()+" quads plus root\n");
		for (TargetData target:targets){
			sb.append("\t Name: "+target.getNode().getName()+"\n");
			sb.append("\t BBox:"+target.getBoundingBox()+"\n");
			for(QuadNode quadnode:target.getParents()){
				sb.append("\t\tQuad: "+quadnode.getBoundingBox()+"\n");
			}
		}
		sb.append("QuadNodes\n");
		for(QuadNode quadNode:quadNodes){
			sb.append("\t"+quadNode.getBoundingBox()+"\n");
		}
		return sb.toString();
	}

	/* (non-Javadoc)
	 * @see com.jme.scene.Node#draw(com.jme.renderer.Renderer)
	 */
	@Override
	public void draw(Renderer r) {
		r.renderQueue();
		frustumCheckCamera.getLocation().set(r.getCamera().getLocation());
		frustumCheckCamera.getLeft().set(r.getCamera().getLeft());
		frustumCheckCamera.getUp().set(r.getCamera().getUp());
		frustumCheckCamera.setFrustumBottom(r.getCamera().getFrustumBottom());
		frustumCheckCamera.setFrustumTop(r.getCamera().getFrustumTop());
		frustumCheckCamera.setFrustumLeft(r.getCamera().getFrustumLeft());
		frustumCheckCamera.setFrustumRight(r.getCamera().getFrustumRight());
		frustumCheckCamera.setFrustumNear(r.getCamera().getFrustumNear());
		frustumCheckCamera.setFrustumFar(r.getCamera().getFrustumFar());
		frustumCheckCamera.getDirection().set(r.getCamera().getDirection());
		frustumCheckCamera.update();

		int state=r.getCamera().getPlaneState();
		renderTargets.clear();
		for(Spatial sp:getChildren()){
			QuadNode node=(QuadNode)sp;
			frustumCheckCamera.setPlaneState(0);
			node.getBoundingBox().setCheckPlane(0);
			int checkState = frustumCheckCamera.contains(node.getBoundingBox());
			if (checkState == Camera.INSIDE_FRUSTUM || checkState == Camera.INTERSECTS_FRUSTUM){
				drawRecursive(r, node);
			}
		}
		r.getCamera().setPlaneState(state);
		for(TargetData dat:renderTargets){		
			dat.getNode().draw(r);
		}
	}
	
	public void drawRecursive(Renderer r, QuadNode target){
		for(Spatial sp:target.getChildren()){
			QuadNode node=(QuadNode)sp;
			frustumCheckCamera.setPlaneState(0);
			node.getBoundingBox().setCheckPlane(0);
			int checkState = frustumCheckCamera.contains(node.getBoundingBox());
			if (checkState == Camera.INSIDE_FRUSTUM || checkState == Camera.INTERSECTS_FRUSTUM) {
				if(node.getQuadType().equals(QuadNode.Type.BRANCH)){
					drawRecursive(r, node);
				}
				else{
					for(Object targetData:node.getTargetArray()){
						renderTargets.add((TargetData)targetData);
					}
				}
			}
		}
	}

	public void onDraw(Renderer r) {
		int cm = getCullMode();
		if (cm == CULL_ALWAYS) {
			return;
		}

		draw(r);
	}

}
