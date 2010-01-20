/**
 * 
 */
package org.rifidi.prototyper.mapeditor.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.ui.PlatformUI;

/**
 * @author Kyle Neumeier - kyle@pramari.com
 * 
 */
public class FloorplanElement extends AbstractMapModelElement {
	static final long serialVersionUID = 1;

	private transient ImageData data;
	private transient Image image;
	private transient byte[] imageBytes;
	/** Resize factor */
	private Float pixelsPerFoot;

	public FloorplanElement(ImageData data, Float pixelsPerFoot) {
		this.data = data;
		this.pixelsPerFoot = pixelsPerFoot;
		ImageLoader imageLoader = new ImageLoader();
		imageLoader.data = new ImageData[] { data };
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		imageLoader.save(stream, SWT.IMAGE_PNG);
		imageBytes = stream.toByteArray();
	}

	public Image getFloorplanImage() {
		if (image == null) {
			image = new Image(PlatformUI.getWorkbench().getDisplay(), data);
		}
		return image;
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.writeFloat(pixelsPerFoot);
		out.writeInt(imageBytes.length);
		out.write(imageBytes);
	}

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

	public void dispose() {
		super.dispose();
		image.dispose();
	}
	
	public Float getScale(){
		return new Float(pixelsPerFoot);
	}
}
