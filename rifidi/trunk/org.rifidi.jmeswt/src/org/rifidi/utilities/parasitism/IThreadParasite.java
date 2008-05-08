package org.rifidi.utilities.parasitism;

/**
 * 
 * @see DirectionalBlinker
 * @author Jeremy Choens - "Ghost" - jeremy@pramari.com
 */
public interface IThreadParasite {

	/**
	 * Should only be called reflexively by the IThreadParasiteHost on
	 * registering.
	 * 
	 * @param host
	 */
	public void setHost(IThreadParasiteHost host);
	
	/**
	 * Useful for grouping parasites and checking to see if a parasite is currently hosted.
	 * 
	 * @return
	 */
	public IThreadParasiteHost getHost();

	/**
	 * Should only be called reflexively by the IThreadParasiteHost on
	 * unregistering.
	 * 
	 */
	public void clearHost();

	/**
	 * Update function for the parasite. Be sure to track left over time(or
	 * calculate delta yourself), as excess time is parasite-dependent.
	 * 
	 * @param timeDelta
	 */
	public void update(long timeDelta);

	public void init();

	public void deInit();
}
