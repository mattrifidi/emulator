/**
 * 
 */
package org.rifidi.utilities.image;

import org.eclipse.swt.graphics.ImageData;

/**
 * @author Dan West - 'Phoenix' - dan@pramari.com
 */
public class ImageDataTools {
	/**
	 * Draws a horizontal line in the provided image data (ignores any invalid points)
	 * @param imageData the image data to render the line onto
	 * @param y	the y coordinate of the line
	 * @param x1 the starting x coordinate of the line
	 * @param x2 the ending x coordinate of the line
	 * @param h the height of the line
	 * @param color the color of the line
	 */
	public static void horizontalLine( ImageData imageData, int y, int x1, int x2, int h, int color ) {
		if ( x1 > x2 ) {
			Integer t = x2;
			x2 = x1; x1 = t;
		}

		for ( int i = (int) -Math.floor(h/2f); i < Math.ceil(h/2f); i++ )
			for ( int j = x1; j <= x2; j++ )
				colorPixel(imageData, j, y+i, color);
	}

	/**
	 * Draws a vertical line in the provided image data (ignores any invalid pixels
	 * @param imageData the image data to render the line onto
	 * @param x the x coordinate of the line
	 * @param y1 the starting y coordinate of the line
	 * @param y2 the ending y coordinate of the line
	 * @param w the width of the line
	 * @param color the color of the line
	 */
	public static void verticalLine( ImageData imageData, int x, int y1, int y2, int w, int color ) {
		if ( y1 > y2 ) {
			Integer t = y2;
			y2 = y1; y1 = t;
		}

		for ( int i = (int) -Math.floor(w/2f); i < Math.ceil(w/2f); i++ )
			for ( int j = y1; j <= y2; j++ )
				colorPixel(imageData, x+i, j, color);
	}

	/**
	 * Sets the specified pixel in the imagedata to the given color (or doesn't if (x,y) isn't a valid pixel)
	 * @param imageData the imagedata to change
	 * @param x the x coordinate of the pixel to color
	 * @param y the y coordinate of the pixel to color
	 * @param color the color to set the pixel to
	 */
	public static void colorPixel( ImageData imageData, int x, int y, int color ) {
//		System.out.println("("+x+","+y+") -- "+imageData.width+"x"+imageData.height);
		if ( x >= 0 && y >= 0 && x < imageData.width && y < imageData.height )
			imageData.setPixel(x, y, color);
	}
}
