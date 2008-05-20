package org.rifidi.utilities.parasitism;

/**
 * 
 * 
 * @see GenericParasiteHost
 * @author Jeremy Choens - "Ghost" - jeremy@pramari.com
 */
public interface IThreadParasiteHost {

	/**
	 * Register a parasite and reflexively parent it. Unregister it first if
	 * necessary.
	 * 
	 * @param parasite
	 * @param autostart -
	 *            if true, call init() on new parasite.
	 */
	public void registerParasite(IThreadParasite parasite, boolean autostart);

	/**
	 * Unregister a parasite and reflexively unparent it.
	 * 
	 * @param parasite
	 * @param autostop -
	 *            if true, call deInit() on the parasite. (this should be true
	 *            under most circumstances)
	 */
	public void removeParasite(IThreadParasite parasite, boolean autostop);

	/**
	 * Wrap the update(long time) functions of all parasites into one callable
	 * and send it off. Call from within the render or update thread as applies
	 * to the implementation before callables are ran.
	 * 
	 */
	public void parasiticUpdate(long time);

	/**
	 * Iterate through all hosted objects and set their enablements by calling
	 * init() or deInit().
	 * 
	 * @param enabled
	 */
	public void setEnablement(boolean enabled);

	/**
	 * Sets the state of the Host whether or not to ignore update calls,
	 * effectively freezing all hosted objects.
	 * 
	 * @param pause
	 */
	public void pauseUnpause(boolean pause);

}
