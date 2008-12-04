package org.rifidi.designer.library.retail;

import com.jme.math.Quaternion;
import com.jme.math.Vector3f;

/**
 * Class for storing position information for a 3d object.
 * 
 * @author jochen
 * 
 */
public class Position {

	public Vector3f translation;
	public Quaternion rotation;

	/**
	 * @param translation
	 * @param rotation
	 */
	public Position(Vector3f translation, Quaternion rotation) {
		super();
		this.translation = translation;
		this.rotation = rotation;
	}

}