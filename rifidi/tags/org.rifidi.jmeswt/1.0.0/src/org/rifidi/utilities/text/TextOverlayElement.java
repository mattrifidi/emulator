/**
 * 
 */
package org.rifidi.utilities.text;

import com.jme.app.SimpleGame;
import com.jme.image.Image;
import com.jme.image.Texture;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Text;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;

/**
 * @author dan
 */
public class TextOverlayElement {
	private static String DEFAULT_FONT = "com/jme/app/defaultfont.tga";
//	private static final Integer MAX_BUFFER_SIZE = 500;
//	private StringBuffer buffer = new StringBuffer(MAX_BUFFER_SIZE);
	private static float fadespeed = 0.01f;
	private static int id = 0;
	private Text textGeom;
//	private TextOverlay manager;
//	private OverlayMessage message;
	private String text;
	private Node textNode;
	private float ttl = -1;
	private boolean fading = false;
//	private String id;

	/**
	 * Initialize the text overlay object
	 */
	public TextOverlayElement( String message, float ttl ) {
		this.text = message;
		this.ttl = ttl;

		createOverlayNode();

	}

	public void createOverlayNode() {
		TextureManager.clearCache();

		// create texture state for the overlay element's font
		TextureState texState = DisplaySystem.getDisplaySystem().getRenderer().createTextureState();
		texState.setTexture( TextureManager.loadTexture(
				SimpleGame.class.getClassLoader().getResource(DEFAULT_FONT),
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
		textGeom = Text.createDefaultTextLabel( id + "-Label" );
		textGeom.setTextureCombineMode( TextureState.REPLACE );
		textGeom.setCullMode( SceneElement.CULL_NEVER );
		textGeom.setRenderState(texState);
		textGeom.print(text);

		// Initialize the node that will render the text element
		textNode = new Node( "TextNode_"+id );
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

//		if ( !fading ) {
//			if ( ttl > 0 ) {
//				ttl -= dt;
//				if ( ttl <= 0 ) {
//					fade();
//					fading = true;
//				}
//			}
//		} else		fade();
	}

	public void fade() {
		ColorRGBA color = textGeom.getTextColor();
		if ( color.a - fadespeed > 0 ) {
			color.a -= fadespeed;
//			text.setTextColor(color);
		} else {
			color.a = 0;
//			if ( manager != null )
//				manager.removeOverlay(id);
//			System.out.println("STUB: Manager Remove!!!!");
			ttl = 0;
		}

		textGeom.updateRenderState();
	}

	/**
	 * @param ttl the ttl to set
	 */
	public void setTTL(float ttl) {
		this.ttl = ttl;
		if ( fading ) {
			fading = false;
			textGeom.clearRenderState(RenderState.RS_ALPHA);
			textGeom.getTextColor().a = 1.0f;
		}
	}

	public float getTTL() {
		return ttl;
	}

	/**
	 * Render the text display to the given renderer
	 * @param renderer the renderer to draw the overlay to
	 */
	public void render( Renderer renderer ) {
		renderer.draw(textNode);
	}

	public void setText( String text ) {
		textGeom.print(text);
		this.text = text;
	}

	public void setTextColor( ColorRGBA color ) {
		textGeom.setTextColor(color.clone());
	}

	public String getText() { return text; }
	public Text getTextGeom() { return textGeom; }
	public Node getNode() { return textNode; }
}
