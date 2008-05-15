package org.rifidi.jmonkey;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.opengl.GLCanvas;

import com.jme.renderer.Camera;

/**
 * 
 */

/**
 * @author jochen
 *
 */
public class Canvas3d {
	public List<Camera> cameras=new ArrayList<Camera>();
	public GLCanvas canvas;
	public Canvas3d(GLCanvas canvas){
		this.canvas=canvas;
	}
}
