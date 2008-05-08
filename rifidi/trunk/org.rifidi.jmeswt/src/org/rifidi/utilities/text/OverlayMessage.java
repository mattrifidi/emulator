package org.rifidi.utilities.text;

import org.rifidi.utilities.math.RandomGUID;

/**
 * @author dan
 */
public class OverlayMessage {
	private static float defaulttimeout = -1;
	private float timeout;
	private String text;
	private String id;

	public OverlayMessage( String message, String id ) {
		this(message,id,defaulttimeout);
	}

	public OverlayMessage( String message, float timeout ) {
		this(message,new RandomGUID().toString(),timeout);
	}

	public OverlayMessage( String message, String id, float timeout ) {
		this.timeout = timeout;
		this.text = message;
		this.id = id;
	}

	/**
	 * @return the defaulttimeout
	 */
	public static float getDefaulttimeout() {
		return defaulttimeout;
	}

	/**
	 * @param defaulttimeout the defaulttimeout to set
	 */
	public static void setDefaulttimeout(float defaulttimeout) {
		OverlayMessage.defaulttimeout = defaulttimeout;
	}

	/**
	 * @return the timeout
	 */
	public float getTimeout() {
		return timeout;
	}

	/**
	 * @param timeout the timeout to set
	 */
	public void setTimeout(float timeout) {
		this.timeout = timeout;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
}
