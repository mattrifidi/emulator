/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.PlatformUI;

/**
 * This class represents a floorplan. A Floorplan is on the bottom layer of a
 * prototype map view.
 * 
 * TODO: Floorplans should have a scale associated with them that other elements
 * can use to automatically scale to the proper size
 * 
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class FloorplanElement extends AbstractMapModelElement {
	/** The serial version ID for the floorplan */
	static final long serialVersionUID = 1;
	/** The ImageData of the floorplan map */
	private transient ImageData data;
	/** The image of the floorplan map */
	private transient Image image;
	/** The bytes of the floorplan map */
	private transient byte[] imageBytes;
	/** Resize factor */
	private Float pixelsPerFoot;

	/**
	 * Constructor
	 * 
	 * @param data
	 *            The image data
	 * @param pixelsPerFoot
	 *            The scale of the image
	 */
	public FloorplanElement(ImageData data, Float pixelsPerFoot) {
		this.data = data;
		this.pixelsPerFoot = pixelsPerFoot;
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { data };
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageLoader.save(stream, SWT.IMAGE_PNG);
		imageBytes = stream.toByteArray();
	}

	/**
	 * The floorplan image
	 * 
	 * @return
	 */
	public Image getFloorplanImage() {
		if (image == null) {
			image = new Image(PlatformUI.getWorkbench().getDisplay(), data);
		}
		return image;
	}

	/**
	 * This method serializes the floorplan model object
	 * 
	 * @param out
	 * @throws IOException
	 */
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeFloat(pixelsPerFoot);
		out.writeInt(imageBytes.length);
		out.write(imageBytes);
	}

	/**
	 * This method reconstructs the floorplan model object from a byte array
	 * 
	 * @param in
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readObject(ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.pixelsPerFoot = in.readFloat();
		int length = in.readInt();
		byte[] buffer = new byte[length];
		in.readFully(buffer);
		this.imageBytes = buffer;
		ImageLoader imageLoader = new ImageLoader();
		ByteArrayInputStream stream = new ByteArrayInputStream(buffer);
		ImageData[] data = imageLoader.load(stream);
		this.data = data[0];
	}

	/**
	 * Call to destroy this object
	 */
	@Override
	public void dispose() {
		super.dispose();
		image.dispose();
	}

	/**
	 * Get the scale of the floorplan
	 * 
	 * @return
	 */
	public Float getScale() {
		return new Float(pixelsPerFoot);
	}
}
