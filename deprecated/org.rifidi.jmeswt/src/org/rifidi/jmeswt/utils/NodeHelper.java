package org.rifidi.jmeswt.utils;

import java.util.ArrayList;
import java.util.List;

import com.jme.scene.Node;
import com.jme.scene.SharedMesh;
import com.jme.scene.Spatial;
import com.jme.scene.state.RenderState;
import com.jme.util.CloneImportExport;

/**
 * A helper class to provide utility functions for dealing with some node
 * operations
 * 
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class NodeHelper {

	/**
	 * Default node copier. copies the node and all its children
	 * 
	 * @param n
	 *            the node to copy
	 * @return a copy of the node
	 */
	public static Node nodeCopy(Node n) {
		return (Node) nodeCopy(n, true);
	}

	/**
	 * Copy the given node into a new node Note: this doesn't work on physics
	 * nodes, or nodes including physics collision geometry at all
	 * 
	 * @param n
	 *            the node to copy
	 * @param withChildren
	 *            whether or not to copy this node's children
	 * @return the copied node
	 */
	public static Node nodeCopy(Node n, boolean withChildren) {
		List<Spatial> children = null;
		if (n.getChildren() != null)
			children = new ArrayList<Spatial>(n.getChildren());

		// if children aren't important, detatch them
		if (!withChildren)
			n.detachAllChildren();

		// copy the node
		Node toReturn = (Node) copySpatial(n);

		// reattach children if they were detatched
		if (!withChildren && n.getChildren() != null)
			for (Spatial s : children)
				n.attachChild(s);

		return toReturn;
	}

	/**
	 * Copy the given spatial and return the copy
	 * 
	 * @param from
	 *            the spatial to copy
	 * @return the copy of the spatial
	 */
	public static Spatial copySpatial(Spatial from) {
		Spatial r = null;
		if (from != null) {
			try {
//				JMEExporter exp = new BinaryExporter();
//				ByteArrayOutputStream bos = new ByteArrayOutputStream();
//				exp.save(from, bos);
//				byte[] bytes = bos.toByteArray();
//				ByteArraySpatialGenerator<Spatial> generator = new ByteArraySpatialGenerator<Spatial>(
//				bytes);
//				ObjectPool<Spatial> pool = new ObjectPool<Spatial>(generator, 0);
//				r = pool.get();

//				// create piped streamage
//				PipedInputStream pis = new PipedInputStream();
//				PipedOutputStream pos = new PipedOutputStream(pis);
//
//				// export to piped stream
//				BinaryExporter e = new BinaryExporter();
//				e.save(from, pos);
//				pos.close();
//
//				// import from piped stream
//				BinaryImporter i = new BinaryImporter();
//				r = (Spatial) i.load(pis);

				CloneImportExport cie = new CloneImportExport();
				cie.saveClone(from);
				r = (Spatial) cie.loadClone();

//			} catch (IOException e) {
//				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		r.updateGeometricState(0, true);
		r.updateRenderState();
		return r;
	}

	/**
	 * Print the hierarchy of the given node for debugging and readability
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
	 * Print the hierarchy of the given node for debugging and readability
	 * 
	 * @param n
	 *            The root node to start at
	 * @param curdepth
	 *            The current depth in the scenegraph recursion
	 * @param depth
	 *            How deep to recurse into the scenegraph
	 * @return A string storing the hierarchy information
	 */
	public static String printNodeHierarchy(Spatial n, int curdepth, int depth) {
		if (n == null)
			return "";
		String r = "";
		int i;

		r += gentabs(curdepth) + "} " + n.getName();
		r += "\t" + chopClass(n.getClass().getName());
		r += "\t" + renderStateDescriptorString(n);
		if (n instanceof Node)
			if (((Node) n).getChildren() != null)
				r += "\t(" + ((Node) n).getChildren().size() + ")";
			else
				r += "\t(0)";
		r += "\n";
		if (curdepth < depth) {
			if (n instanceof Node && ((Node) n).getChildren() != null) {
				Node node = (Node) n;
				for (i = 0; i < node.getChildren().size(); i++)
					r += printNodeHierarchy(node.getChild(i), curdepth + 1,
							depth);
			}
		}

		return r;
	}

	/**
	 * Return a string indicating which renderstates are set for a given spatial
	 * 
	 * @param s
	 *            the spatial to construct the descriptor string for
	 * @return a string providing a visual representation of the render states
	 *         for the spatial
	 */
	public static String renderStateDescriptorString(Spatial s) {
		String r = "";
		for (int i = 0; i < RenderState.RS_MAX_STATE; i++)
			if (s.getRenderState(i) != null)
				r += i % 10;
			else
				r += "-";
		return r;
	}

	/**
	 * utility function for extracting only class name
	 * 
	 * @param c
	 *            The full class string
	 * @return The name of the class type
	 */
	public static String chopClass(String c) {
		int last = c.lastIndexOf(".");
		String r = c.substring(last + 1);
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

	/**
	 * Determines whether or not node n has a shared mesh as a child and returns
	 * the result
	 * 
	 * @param n
	 *            the node to check for shared mesh children
	 * @return true if the node has a shared mesh child, or false otherwise
	 */
	public static boolean hasSharedMesh(Node n) {
		for (Spatial s : n.getChildren()){
			if (s instanceof SharedMesh){
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the first instance of a shared mesh found as a child of n
	 * 
	 * @param n
	 *            the node to retrieve the shared mesh from
	 * @return the shared mesh
	 */
	public static SharedMesh getSharedMesh(Node n) {
		for (Spatial s : n.getChildren())
			if (s instanceof SharedMesh)
				return (SharedMesh) s;
		return null;
	}

	/**
	 * Returns all children of the given node that are instances of class c
	 * @param n the node whose children to check
	 * @param c the class of children to look for
	 * @return an list of the children of the requested type
	 */
	public static List<Spatial> getChildrenOfType( Node n, Class c ) {
		List<Spatial> list = new ArrayList<Spatial>();
		for ( Spatial s : n.getChildren() )
			if ( c.isInstance(s) )
				list.add(s);
		return list;
	}

	/**
	 * Applies the given render state to the given shared mesh's batches
	 * 
	 * @param sm
	 *            the shared mesh to apply the renderstate to
	 * @param rs
	 *            the renderstate to apply
	 */
	public static void applyBatchRenderStates(SharedMesh sm, RenderState rs) {
		for (int i = 0; i < sm.getBatchCount(); i++)
			sm.getBatch(i).setRenderState(rs);
	}

	/**
	 * Applies the given renderstates to the given shared mesh's batches
	 * 
	 * @param sm
	 *            the shared mesh to apply the renderstates to
	 * @param rs
	 *            the list of renderstates to apply
	 */
	public static void applyBatchRenderStates(SharedMesh sm,
			ArrayList<RenderState> rs) {
		for (int i = 0; i < sm.getBatchCount(); i++)
			sm.getBatch(i).setRenderState(rs.get(i));
	}

	/**
	 * Return a list of the materialstates on each of the given sharedmesh's
	 * batches
	 * 
	 * @param sm
	 *            the sharedmesh to scan for materialstates
	 * @return the list of materialstates
	 */
	public static ArrayList<RenderState> getRenderStates(SharedMesh sm) {
		ArrayList<RenderState> states = new ArrayList<RenderState>();
		for (int i = 0; i < sm.getBatchCount(); i++)
			states.add(sm.getBatch(i).getRenderState(RenderState.RS_MATERIAL));
		return states;
	}
}
