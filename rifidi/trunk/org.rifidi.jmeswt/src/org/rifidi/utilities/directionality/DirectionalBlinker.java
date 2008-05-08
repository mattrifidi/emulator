package org.rifidi.utilities.directionality;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.utilities.parasitism.IThreadParasite;
import org.rifidi.utilities.parasitism.IThreadParasiteHost;

import com.jme.bounding.BoundingVolume;
import com.jme.intersection.CollisionResults;
import com.jme.intersection.PickResults;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Ray;
import com.jme.math.Vector3f;
import com.jme.renderer.Renderer;
import com.jme.scene.Controller;
import com.jme.scene.Geometry;
import com.jme.scene.Line;
import com.jme.scene.Node;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.util.export.JMEExporter;
import com.jme.util.export.JMEImporter;
import com.jme.util.export.Savable;

/**
 * 
 * @author Jeremy Choens - "Ghost" - jeremy@pramari.com
 */
public class DirectionalBlinker implements IThreadParasite{
	private Log loggy = LogFactory.getLog(DirectionalBlinker.class);
	
	/**
	 * Wrapped node.  All public methods are delegated.  Models attached to this node are the models that will "blink."
	 */
	private Node representingNode = new Node();
	
	private boolean inited = false;
	
	private boolean reversed = false;
	
	private int currentIndex = 0;
	
	private IThreadParasiteHost host;
	
	private List<Vector3f> vertList = new ArrayList<Vector3f>();
	private List<Vector3f> faceList = new ArrayList<Vector3f>();
	
	/**
	 * Node that this blinker signifies direction for.  On init() it is set as the jme parent.
	 */
	private Node directedNode = null;
	
	private long timePerBlink = 250;
	
	private long leftoverTime = 0;
	
	public DirectionalBlinker(){
		//Empty constructor
		loggy.debug("For simplicity's sake, don't use the empty constructor.");
	}
	
	/*
	 * TODO make a constructor to take in bezier curves and a precision element.
	 * Conversely, make an algorithm for it that takes in a line.
	 */
	public DirectionalBlinker(Line sourceLine) {
		FloatBuffer vBuff = sourceLine.getVertexBuffer(0);
	
		Vector3f vect = null;
		Vector3f faceVect = null;
		
		for(int z = 0 ; z < vBuff.limit() ; z += 3){
			vect = new Vector3f(vBuff.get(z), vBuff.get(z+1), vBuff.get(z+2));
			vertList.add(vect);
		}
		for(int z = 0 ; z < vertList.size() - 1 ; z++){
			faceList.add(vertList.get(z+1).subtract(vertList.get(z)));
		}
		faceList.add((Vector3f)faceList.get(faceList.size() - 1).clone());
	}

	public void setHost(IThreadParasiteHost host) {
		this.host = host;		
	}
	
	public void clearHost() {
		host = null;		
	}

	public void deInit() {
		if(inited){
			removeFromParent();
			currentIndex = 0;
			inited = false;
		} else {
			loggy.debug("Blinker is already un-initialized.");
		}
	}

	public void init() {
		if(!inited){
			if(vertList.size() == faceList.size()  && directedNode != null){
				directedNode.attachChild(representingNode);
				inited = true;
			} else {
				loggy.error("Unable to initialize parasite.  Ensure directedNode is set and face/vert lists are of equal size.");
			}
		} else {
			loggy.debug("Blinker is already initialized.");
		}
	}

	public IThreadParasiteHost getHost() {
		return host;
	}
	
	public Node getDirectedNode() {
		return directedNode;
	}

	/**
	 * Equivalent to setting the parent node of the blinker.
	 * Disabled if the blinker is in an initialized state.
	 * 
	 * @param directedNode
	 */
	public void setDirectedNode(Node directedNode) {
		if(!inited){
			this.directedNode = directedNode;
		} else {
			loggy.warn("Action not allowed while parasite is in initialized state.");
		}
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void update(long timeDelta) {
		if(inited  &&  !vertList.isEmpty()){
			timeDelta = timeDelta + leftoverTime;
			leftoverTime = timeDelta % timePerBlink;
			int steps = (int)(timeDelta / timePerBlink);
			
			if(reversed){
				steps *= -1;
			}
			
			currentIndex += steps + vertList.size();
			
			currentIndex = currentIndex % vertList.size();
			
			loggy.debug("Updating: " +steps+" steps. With leftover time: " + leftoverTime+". Index: " + currentIndex);
			
			setLocalTranslation(vertList.get(currentIndex));
			if(reversed){
				lookAt((faceList.get(currentIndex).negate().add(getLocalTranslation())), Vector3f.UNIT_Y);
			} else {
				lookAt(faceList.get(currentIndex).add(getLocalTranslation()), Vector3f.UNIT_Y);
			}
		}		
	}	
	
	public static void main(String args[]){
		DirectionalBlinker blinky = new DirectionalBlinker();
		blinky.update(125);
		blinky.update(1000);
		blinky.update(525);
		blinky.update(300);
	}
	
	public long getTimePerBlink() {
		return timePerBlink;
	}

	public void setTimePerBlink(long timePerBlink) {
		this.timePerBlink = timePerBlink;
	}

	public boolean isInited() {
		return inited;
	}

	public List<Vector3f> getVertList() {
		if(!inited){
			return vertList;
		} else {
			loggy.warn("Action not allowed while parasite is in initialized state.");
		}
		return null;
	}

	public List<Vector3f> getFaceList() {
		if(!inited){
			return faceList;
		} else {
			loggy.warn("Action not allowed while parasite is in initialized state.");
		}
		return null;
	}

	public Node getRepresentingNode() {
		return representingNode;
	}
	
	public void setRepresentingNode(Node representingNode) {
		loggy.warn("Action not allowed.");
	}

	public void setVertList(List<Vector3f> vertList) {
		loggy.warn("Action not allowed.");
	}

	public void setFaceList(List<Vector3f> faceList) {
		loggy.warn("Action not allowed.");
	}

	public boolean isReversed() {
		return reversed;
	}

	public void setReversed(boolean reversed) {
		this.reversed = reversed;
	}
	
	//Begin delegated methods;

	public void addController(Controller controller) {
		representingNode.addController(controller);
	}

	public int attachChild(Spatial child) {
		return representingNode.attachChild(child);
	}

	public int attachChildAt(Spatial child, int index) {
		return representingNode.attachChildAt(child, index);
	}

	public void batchChange(Geometry geometry, int index1, int index2) {
		representingNode.batchChange(geometry, index1, index2);
	}

	public void calculateCollisions(Spatial scene, CollisionResults results) {
		representingNode.calculateCollisions(scene, results);
	}

	public void calculatePick(Ray ray, PickResults results) {
		representingNode.calculatePick(ray, results);
	}

	public void clearControllers() {
		representingNode.clearControllers();
	}

	public void clearRenderState(int renderStateType) {
		representingNode.clearRenderState(renderStateType);
	}

	public void detachAllChildren() {
		representingNode.detachAllChildren();
	}

	public int detachChild(Spatial child) {
		return representingNode.detachChild(child);
	}

	public Spatial detachChildAt(int index) {
		return representingNode.detachChildAt(index);
	}

	public int detachChildNamed(String childName) {
		return representingNode.detachChildNamed(childName);
	}

	public void draw(Renderer r) {
		representingNode.draw(r);
	}

	public boolean equals(Object obj) {
		return representingNode.equals(obj);
	}

	public void findCollisions(Spatial scene, CollisionResults results) {
		representingNode.findCollisions(scene, results);
	}

	public void findPick(Ray toTest, PickResults results) {
		representingNode.findPick(toTest, results);
	}

	public Spatial getChild(int i) {
		return representingNode.getChild(i);
	}

	public Spatial getChild(String name) {
		return representingNode.getChild(name);
	}

	public int getChildIndex(Spatial sp) {
		return representingNode.getChildIndex(sp);
	}

	public ArrayList<Spatial> getChildren() {
		return representingNode.getChildren();
	}

	public Class getClassTag() {
		return representingNode.getClassTag();
	}

	public Controller getController(int i) {
		return representingNode.getController(i);
	}

	public int getControllerCount() {
		return representingNode.getControllerCount();
	}

	public ArrayList<Controller> getControllers() {
		return representingNode.getControllers();
	}

	public int getCullMode() {
		return representingNode.getCullMode();
	}

	public int getLastFrustumIntersection() {
		return representingNode.getLastFrustumIntersection();
	}

	public int getLightCombineMode() {
		return representingNode.getLightCombineMode();
	}

	public int getLocalCullMode() {
		return representingNode.getLocalCullMode();
	}

	public int getLocalLightCombineMode() {
		return representingNode.getLocalLightCombineMode();
	}

	public int getLocalNormalsMode() {
		return representingNode.getLocalNormalsMode();
	}

	public int getLocalRenderQueueMode() {
		return representingNode.getLocalRenderQueueMode();
	}

	public Quaternion getLocalRotation() {
		return representingNode.getLocalRotation();
	}

	public Vector3f getLocalScale() {
		return representingNode.getLocalScale();
	}

	public int getLocalTextureCombineMode() {
		return representingNode.getLocalTextureCombineMode();
	}

	public Vector3f getLocalTranslation() {
		return representingNode.getLocalTranslation();
	}

	public int getLocks() {
		return representingNode.getLocks();
	}

	public String getName() {
		return representingNode.getName();
	}

	public int getNormalsMode() {
		return representingNode.getNormalsMode();
	}

	public Node getParent() {
		return representingNode.getParent();
	}

	public int getQuantity() {
		return representingNode.getQuantity();
	}

	public int getRenderQueueMode() {
		return representingNode.getRenderQueueMode();
	}

	public RenderState getRenderState(int type) {
		return representingNode.getRenderState(type);
	}

	public int getTextureCombineMode() {
		return representingNode.getTextureCombineMode();
	}

	public int getTriangleCount() {
		return representingNode.getTriangleCount();
	}

	public int getType() {
		return representingNode.getType();
	}

	public Savable getUserData(String key) {
		return representingNode.getUserData(key);
	}

	public int getVertexCount() {
		return representingNode.getVertexCount();
	}

	public BoundingVolume getWorldBound() {
		return representingNode.getWorldBound();
	}

	public Quaternion getWorldRotation() {
		return representingNode.getWorldRotation();
	}

	public Vector3f getWorldScale() {
		return representingNode.getWorldScale();
	}

	public Vector3f getWorldTranslation() {
		return representingNode.getWorldTranslation();
	}

	public int getZOrder() {
		return representingNode.getZOrder();
	}

	public boolean hasChild(Spatial spat) {
		return representingNode.hasChild(spat);
	}

	public boolean hasCollision(Spatial scene, boolean checkTriangles) {
		return representingNode.hasCollision(scene, checkTriangles);
	}

	public int hashCode() {
		return representingNode.hashCode();
	}

	public boolean isCollidable() {
		return representingNode.isCollidable();
	}

	public Vector3f localToWorld(Vector3f in, Vector3f store) {
		return representingNode.localToWorld(in, store);
	}

	public void lock() {
		representingNode.lock();
	}

	public void lock(Renderer r) {
		representingNode.lock(r);
	}

	public void lockBounds() {
		representingNode.lockBounds();
	}

	public void lockBranch() {
		representingNode.lockBranch();
	}

	public void lockMeshes() {
		representingNode.lockMeshes();
	}

	public void lockMeshes(Renderer r) {
		representingNode.lockMeshes(r);
	}

	public void lockShadows() {
		representingNode.lockShadows();
	}

	public void lockTransforms() {
		representingNode.lockTransforms();
	}

	public void lookAt(Vector3f position, Vector3f upVector) {
		representingNode.lookAt(position, upVector);
	}

	public void onDraw(Renderer r) {
		representingNode.onDraw(r);
	}

	public void propagateBoundToRoot() {
		representingNode.propagateBoundToRoot();
	}

	public void propagateStatesFromRoot(Stack[] states) {
		representingNode.propagateStatesFromRoot(states);
	}

	public void read(JMEImporter e) throws IOException {
		representingNode.read(e);
	}

	public boolean removeController(Controller controller) {
		return representingNode.removeController(controller);
	}

	public Controller removeController(int index) {
		return representingNode.removeController(index);
	}

	public boolean removeFromParent() {
		return representingNode.removeFromParent();
	}

	public Savable removeUserData(String key) {
		return representingNode.removeUserData(key);
	}

	public void rotateUpTo(Vector3f newUp) {
		representingNode.rotateUpTo(newUp);
	}

	public void setCullMode(int mode) {
		representingNode.setCullMode(mode);
	}

	public void setIsCollidable(boolean isCollidable) {
		representingNode.setIsCollidable(isCollidable);
	}

	public void setLastFrustumIntersection(int intersects) {
		representingNode.setLastFrustumIntersection(intersects);
	}

	public void setLightCombineMode(int lightCombineMode) {
		representingNode.setLightCombineMode(lightCombineMode);
	}

	public void setLocalRotation(Matrix3f rotation) {
		representingNode.setLocalRotation(rotation);
	}

	public void setLocalRotation(Quaternion quaternion) {
		representingNode.setLocalRotation(quaternion);
	}

	public void setLocalScale(float localScale) {
		representingNode.setLocalScale(localScale);
	}

	public void setLocalScale(Vector3f localScale) {
		representingNode.setLocalScale(localScale);
	}

	public void setLocalTranslation(float x, float y, float z) {
		representingNode.setLocalTranslation(x, y, z);
	}

	public void setLocalTranslation(Vector3f localTranslation) {
		representingNode.setLocalTranslation(localTranslation);
	}

	public void setLocks(int locks, Renderer r) {
		representingNode.setLocks(locks, r);
	}

	public void setLocks(int locks) {
		representingNode.setLocks(locks);
	}

	public void setModelBound(BoundingVolume modelBound) {
		representingNode.setModelBound(modelBound);
	}

	public void setName(String name) {
		representingNode.setName(name);
	}

	public void setNormalsMode(int mode) {
		representingNode.setNormalsMode(mode);
	}

	public void setRenderQueueMode(int renderQueueMode) {
		representingNode.setRenderQueueMode(renderQueueMode);
	}

	public RenderState setRenderState(RenderState rs) {
		return representingNode.setRenderState(rs);
	}

	public void setTextureCombineMode(int textureCombineMode) {
		representingNode.setTextureCombineMode(textureCombineMode);
	}

	public void setUserData(String key, Savable data) {
		representingNode.setUserData(key, data);
	}

	public void setZOrder(int order, boolean setOnChildren) {
		representingNode.setZOrder(order, setOnChildren);
	}

	public void setZOrder(int order) {
		representingNode.setZOrder(order);
	}

	public void swapChildren(int index1, int index2) {
		representingNode.swapChildren(index1, index2);
	}

	public String toString() {
		return representingNode.toString();
	}

	public void unlock() {
		representingNode.unlock();
	}

	public void unlock(Renderer r) {
		representingNode.unlock(r);
	}

	public void unlockBounds() {
		representingNode.unlockBounds();
	}

	public void unlockBranch() {
		representingNode.unlockBranch();
	}

	public void unlockMeshes() {
		representingNode.unlockMeshes();
	}

	public void unlockMeshes(Renderer r) {
		representingNode.unlockMeshes(r);
	}

	public void unlockShadows() {
		representingNode.unlockShadows();
	}

	public void unlockTransforms() {
		representingNode.unlockTransforms();
	}

	public void updateGeometricState(float time, boolean initiator) {
		representingNode.updateGeometricState(time, initiator);
	}

	public void updateModelBound() {
		representingNode.updateModelBound();
	}

	public void updateRenderState() {
		representingNode.updateRenderState();
	}

	public void updateWorldBound() {
		representingNode.updateWorldBound();
	}

	public void updateWorldData(float time) {
		representingNode.updateWorldData(time);
	}

	public void updateWorldVectors() {
		representingNode.updateWorldVectors();
	}

	public Vector3f worldToLocal(Vector3f in, Vector3f store) {
		return representingNode.worldToLocal(in, store);
	}

	public void write(JMEExporter e) throws IOException {
		representingNode.write(e);
	}		
}
