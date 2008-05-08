package org.rifidi.utilities.collada;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.jme.light.Light;
import com.jme.light.LightNode;
import com.jme.renderer.Camera;
import com.jme.scene.CameraNode;
import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.state.LightState;
import com.jmex.model.collada.ColladaImporter;

/**
 * Wrapper class for loading collada scenes using the jme ColladaImporter
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class ColladaScene {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory.getLog(ColladaScene.class);

	/**
	 * Storage for the lights used by the scene
	 */
	private Map<String,Node> lights = null;

	/**
	 * Storage for the cameras used by the scene
	 */
	private Map<String,Node> cameras = null;

	/**
	 * Storage for the objects in the scene
	 */
	private Map<String,Node> objects = null;

	/**
	 * The root node of the model
	 */
	Node model = null;

	public ColladaScene( Node model ) {
		this.model = model;
		parseScene();
	}

	public ColladaScene( String id, URL scenefile ) {
		load( id, scenefile );
	}

	/**
	 * Load a collada scene from the given directory and use the given id to
	 * identify it
	 * 
	 * @param id
	 *            The id to use to identify this scene
	 * @param modeldir
	 *            The directory to search for resource files
	 * @param modelname
	 *            The name of the model file to load
	 * @throws  
	 */
	public void load(String id, URL url) {
		int last = url.getPath().lastIndexOf('/');
		String modelname = url.getPath().substring(last+1);
		String modeldir = url.getPath().substring(0,last+1);

		try {
			InputStream modelstream;
			modelstream = url.openStream();
			logger.debug("loading collada: "+id+" "+url.getPath()+" "+modelstream);
			ColladaImporter.load(modelstream, id);
			model = ColladaImporter.getModel();
			if (model == null) {
				logger.error("ColladaImporter error while loading model from "
						+ url.toString());
				return;
			}
		} catch (FileNotFoundException e) {
			logger.error("Unable to locate model file: "+modeldir+modelname);
			model = null;	return;
		} catch (MalformedURLException e) {
			logger.error("Malformed url: "+url);
			model = null;	return;
		} catch (IOException e) {
			logger.error("I/O Exception: "+url);
			model = null;	return;
		}

		logger.debug("loading process is complete, model loaded from: "+ url.getPath());
		ColladaImporter.cleanUp();
		parseScene();
	}

	private void parseScene() {
		assert ( model != null );

		// initialize storage
		lights  = new HashMap<String,Node>();
		cameras = new HashMap<String,Node>();
		objects = new HashMap<String,Node>();

		// Iterate through scenes
		for ( Spatial scene : model.getChildren() ) {
			// Iterate through objects
			for ( Spatial obj : ((Node)scene).getChildren() ) {
				Node n = ((Node)obj);
				if ( n.getQuantity() == 1 ) {
					Spatial child = n.getChild(0);
					if ( child instanceof LightNode )
						lights.put(n.getName(),n);
					else if ( child instanceof CameraNode ) 
						cameras.put(n.getName(), n);
					else if ( child instanceof SharedMesh )
						objects.put(n.getName(), n);
					else
						logger.debug("Unknown spatial detected in scene: "+scene.getName());
				}
			}
		}
	}

	/**
	 * Return the list of camera names
	 * @return The list of camera names
	 */
	public ArrayList<String> getCameraNames() {
		return new ArrayList<String>(cameras.keySet());
	}

	/**
	 * Return the requested camera info node (the parent node of the cameranode)
	 * @param camname The name of the camera info node to retrieve
	 * @return The requested camera info node.
	 */
	public Node getCameraInfoNode( String camname ) {
		return cameras.get(camname);
	}

	/**
	 * Get the requested camera node
	 * @param camname The name of the node to retrieve
	 * @return The requested cameraNode
	 */
	public CameraNode getCameraNode( String camname ) {
		return (CameraNode) getCameraInfoNode(camname).getChild(0);
	}

	/**
	 * Get the requested camera from the scene
	 * @param camname The name of the camera to return
	 * @return The requested camera
	 */
	public Camera getCamera( String camname ) {
		return getCameraNode(camname).getCamera();
	}

	/**
	 * Return the list of object names in the scene
	 * @return The requested object
	 */
	public ArrayList<String> getObjectNames() {
		return new ArrayList<String>( objects.keySet() );
	}

	/**
	 * Return the requested object node
	 * @param objname The object to retrieve
	 * @return The requested object Node
	 */
	public Node getObjectNode( String objname ) {
		return objects.get(objname);
	}

	/**
	 * Add the lights used by the scene to the LightState manager
	 * 
	 * @param ls
	 *            The LightState that will be using the lights
	 */
	public void addLights(LightState ls) {
		for ( String lname : getLightNames() ) {
			logger.debug("adding light \'" + lname
					+ "\' to the lightstate");
			ls.attach( getLight(lname) );
		}
	}

	/**
	 * @return the main scene
	 */
	public Node getModel() {
		return (Node)model.getChild(0);
	}

	/**
	 * Return the list of light names
	 * @return The list of light names
	 */
	public ArrayList<String> getLightNames() {
		return new ArrayList<String>(lights.keySet());
	}

	/**
	 * Return the light info node
	 * @param lightname The name of the light info node to return
	 * @return The requested light info node
	 */
	public Node getLightInfoNode( String lightname ) {
		return lights.get(lightname);
	}

	/**
	 * Get the requested light node
	 * @param lightname The name of the light node to return
	 * @return The requested light node
	 */
	public LightNode getLightNode( String lightname ) {
		return (LightNode) getLightInfoNode(lightname).getChild(0);
	}

	/**
	 * Get the requested light from the scene
	 * @param lightname The name of the light to retrieve
	 * @return The requested light
	 */
	public Light getLight( String lightname ) {
		return getLightNode(lightname).getLight();
	}

	/**
	 * Print the hierarchy of the scenegraph for debugging and readability
	 * 
	 * @param n
	 *            The root node to start at
	 * @param depth
	 *            How deep to recurse into the scenegraph
	 * @return A string storing the hierarchy information
	 */
	public static String printNodeHierarchy(Node n, int depth) {
		return printNodeHierarchy(n, 0, depth);
	}

	/**
	 * Print the hierarchy of the scenegraph for debugging and readability
	 * 
	 * @param n
	 *            The root node to start at
	 * @param curdepth
	 *            The current depth in the scenegraph recursion
	 * @param depth
	 *            How deep to recurse into the scenegraph
	 * @return A string storing the hierarchy information
	 */
	public static String printNodeHierarchy(Node n, int curdepth, int depth) {
		if (curdepth > depth || n == null)
			return "";
		String r = "";
		int i;

		r += gentabs(curdepth) + "} " + n.getName();
		r += "\t" + chopClass(n.getClass().getName());
//		r += "\t" + n.get
		if (n.getChildren() != null)
			r += " (" + n.getChildren().size() + ")";
		else
			r += " [0]";
		r += "\n";
		if (n.getChildren() != null) {
			for (i = 0; i < n.getChildren().size(); i++) {
				if (n.getChild(i) instanceof Node)
					r += printNodeHierarchy((Node) n.getChild(i), curdepth + 1,
							depth);
				else if (curdepth + 1 <= depth)
					r += gentabs(curdepth + 1) + "} " + n.getChild(i).getName()
							+ "\t" + chopClass(n.getChild(i).getClass().getName()) + "\n";
			}
		}

		return r;
	}

	/**
	 * utility function for extracting only class name
	 * 
	 * @param c
	 *            The full class string
	 * @return The name of the class type
	 */
	public static String chopClass( String c ) {
		int last = c.lastIndexOf(".");
		String r = c.substring(last+1);
		return r;
	}

	/**
	 * utility function for intenting a given number of tabs
	 * 
	 * @param numtabs
	 *            The number of tabs to indent
	 * @return A string containing the requested number of tabs
	 */
	public static String gentabs(int numtabs) {
		String r = "";
		for (int i = 0; i < numtabs; i++)
			r += "\t";
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String r = printNodeHierarchy(model, 3);

		// generate camera info
		r += cameras.size() + " cameras:\n";
		for ( String name : cameras.keySet() ) {
			Node cam = cameras.get(name);
			r += "\t" + cam.getName();
			r += "\t" + cam.getLocalTranslation();
			r += "\t" + cam.getLocalRotation();
			r += "\n";
		}

		// generate light info
		r += lights.size() + " lights:\n";
		for ( String name : lights.keySet() ) {
			Node light = lights.get(name);
			r += "\t" + light.getName();
			r += "\t" + light.getLocalTranslation();
			r += "\t" + light.getLocalRotation();
			r += "\n";
		}

		// generate object info
		r += objects.size() + " objects:\n";
		for ( String name : objects.keySet() ) {
			Node obj = objects.get(name);
			r += "\t" + obj.getName();
			r += "\t" + obj.getLocalTranslation();
			r += "\t" + obj.getLocalRotation();
			r += "\n";
		}

		return r;
	}
}
