/*
 *  FadingTextNode.java
 *
 *  Project:		RiFidi Designer - A Virtualization tool for 3D RFID environments
 *  http://www.rifidi.org
 *  http://rifidi.sourceforge.net
 *  Copyright:	    Pramari LLC and the Rifidi Project
 *  License:		Lesser GNU Public License (LGPL)
 *  http://www.opensource.org/licenses/lgpl-license.html
 */
package org.rifidi.designer.services.core.messaging;

import com.jme.app.SimpleGame;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * 
 * 
 * @author Jochen Mader - jochen@pramari.com - May 27, 2008
 * @author Dan West
 */
public class FadingTextNode {
	private String text;
	private float ttl;
	private Text textGeom;	
	private Node textNode;
	private static float fadespeed = 0.01f;
	/**
	 * @param text
	 * @param ttl
	 */
	public FadingTextNode(String text, float ttl) {
		this.text = text;
		this.ttl = ttl;
		TextureManager.clearCache();

		// create texture state for the overlay element's font
		TextureState texState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		texState.setTexture( TextureManager.loadTexture(
				SimpleGame.class.getClassLoader().getResource("com/jme/app/defaultfont.tga"),
				Texture.MM_LINEAR_LINEAR, Texture.FM_LINEAR,
				Image.GUESS_FORMAT_NO_S3TC, 1.0f, true) );
		texState.setEnabled(true);

		// create alphastate for the text overlay element
		AlphaState as = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
		as.setDstFunction(AlphaState.DB_ONE_MINUS_SRC_ALPHA);
		as.setSrcFunction(AlphaState.SB_SRC_ALPHA);
		as.setBlendEnabled(true);
		as.setEnabled(true);

		// Initialize the text element
		textGeom = Text.createDefaultTextLabel( text + "-Label" );
		textGeom.setTextureCombineMode( TextureState.REPLACE );
		textGeom.setCullMode( SceneElement.CULL_NEVER );
		textGeom.setRenderState(texState);
		textGeom.print(text);

		// Initialize the node that will render the text element
		textNode = new Node( "TextNode_"+text );
		textNode.setRenderState( textGeom.getRenderState( RenderState.RS_ALPHA ) );
		textNode.setRenderState( textGeom.getRenderState( RenderState.RS_TEXTURE ) );
		textNode.setCullMode( SceneElement.CULL_NEVER );
		textNode.attachChild( textGeom );

		// make sure text node is up to date
		textNode.updateGeometricState(0, true);
		textNode.updateRenderState();
		textNode.updateWorldData(0);
	}
	/**
	 * Reduce the ttl count and begin fade if the time has come
	 */
	public void update( float dt ) {
		if ( ttl > 0 ) {
			if ( ttl - dt < 0 ) {
				fade();
			} else if ( ttl != 0 ) {
				ttl -= dt;
				if ( ttl == 0 )
					ttl += dt*0.01f;
			}
		}
	}

	public void render( Renderer renderer ) {
		renderer.draw(textNode);
	}
	
	public void fade() {
		ColorRGBA color = textGeom.getTextColor();
		if ( color.a - fadespeed > 0 ) {
			color.a -= fadespeed;
		} else {
			color.a = 0;
			ttl = 0;
		}
		textGeom.getLocalTranslation().addLocal(0,textGeom.getHeight(),0);
		textNode.updateGeometricState(0, true);
		textGeom.updateRenderState();
	}
}
