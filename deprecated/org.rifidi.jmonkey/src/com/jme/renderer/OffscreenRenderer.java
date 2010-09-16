/*
 * Copyright (c) 2003-2007 jMonkeyEngine
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'jMonkeyEngine' nor the names of its contributors 
 *   may be used to endorse or promote products derived from this software 
 *   without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
 
package com.jme.renderer;
 
import java.nio.IntBuffer;
import java.util.ArrayList;
 
import com.jme.scene.Spatial;
 
/**
 * OffscreenRenderer defines an interface that handles rendering a
 * scene to a buffer and copy it to an IntBuffer, which can then be used to create an image
 * (for instance an AWT BufferedImage, or an SWT ImageData). Creation of this object is
 * typically handled via a call to a DisplaySystem subclass.
 *
 * Example Usage: <br>
 * NOTE: This example uses the DisplaySystem class to obtain the
 * OffscreenRenderer.
 *
 *  DisplaySystem.getDisplaySystem().createOffscreenRenderer(...)
 * 
 *
 * @see com.jme.system.DisplaySystem
 * @see com.jme.renderer.TextureRenderer
 * @author Olivier Sambourg
 * @author Joshua Slack
 * @version $Id: TextureRenderer.java,v 1.18 2007/02/05 16:23:44 nca Exp $
 */
public interface OffscreenRenderer {
 
    
    /**
     * 
     * isSupported obtains the capability of the graphics card.
     * If the graphics card does not have pbuffer or FBO support (depending on the
     * class implementing this interface), false is returned, 
     * otherwise, true is returned. OffscreenRenderer will not process any
     * scene elements if pbuffer or FBO are not supported.
     *
     * @return if this graphics card supports the method used for offscreen rendering.
     */
    public boolean isSupported();
 
    /**
     * getCamera retrieves the camera this renderer is using.
     *
     * @return the camera this renderer is using.
     */
    public Camera getCamera();
 
    /**
     * setCamera sets the camera this renderer should use.
     *
     * @param camera
     *            the camera this renderer should use.
     */
    public void setCamera(Camera camera);
 
    /**
     * render renders a scene. As it recieves a base class of
     * Spatial the renderer hands off management of the scene to
     * spatial for it to determine when a Geometry leaf is
     * reached. The result of the rendering is then copied into an IntBuffer.
     * 
     * @param spat
     *            the scene to render.
     */
    public void render(Spatial spat);
 
    /**
     * render renders a scene. As it recieves a base class of
     * Spatial the renderer hands off management of the scene to
     * spatial for it to determine when a Geometry leaf is
     * reached. The result of the rendering is then copied into an IntBuffer.
     * 
     * @param spat
     *          the scene to render.
     * @param doClear
     * 			clear the buffers before rendering, or not.
     */
    public void render(Spatial spat, boolean doClear);
 
    
    /**
     * getImageData returns an IntBuffer containing all the pixel data
     * from the rendered scene. It can then be used to create an image.
     * 
     * @return the rendered scene pixel data.
     */
    public IntBuffer getImageData();
    
    /**
     * render renders a scene. As it recieves a base class of
     * Spatial the renderer hands off management of the scene to
     * spatial for it to determine when a Geometry leaf is
     * reached. The result of the rendering is then copied into an IntBuffer.
     * 
     * @param spats
     *            an array of Spatials to render.
     */
    public void render(ArrayList<? extends Spatial> spats);
 
    /**
     * render renders a scene. As it recieves a base class of
     * Spatial the renderer hands off management of the scene to
     * spatial for it to determine when a Geometry leaf is
     * reached. The result of the rendering is then copied into an IntBuffer.
     * 
     * @param spats
     *         	an array of Spatials to render.
     * @param doClear
     * 			clear the buffers before rendering, or not
     */
    public void render(ArrayList<? extends Spatial> spats, boolean doClear);
 
    /**
     * setBackgroundColor sets the color of window. This color
     * will be shown for any pixel that is not set via typical rendering
     * operations.
     *
     * @param c
     *            the color to set the background to.
     */
    public void setBackgroundColor(ColorRGBA c);
 
    /**
     * getBackgroundColor retrieves the color used for the window
     * background.
     *
     * @return the background color that is currently set to the background.
     */
    public ColorRGBA getBackgroundColor();
    
    /**
     * Any wrapping up and cleaning up of Offscreen information is performed here.
     */
    public void cleanup();
 
    public int getWidth();
    public int getHeight();
}

