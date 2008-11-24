/**
 * 
 */
package org.rifidi.emulator.reader.alien.uptime;

/**
 * 
 * 
 * @author Matthew Dean - matt@pramari.com
 */
public class AlienUptime {
	
	/**
	 * 
	 */
	public static final long NANO_TO_SECONDS = 1000000000;
	
	/**
	 * 
	 */
	public static final long NANO_TO_MILLI = 1000000;
	
	/**
	 * 
	 */
	private static AlienUptime instance = new AlienUptime();
	
	/**
	 * 
	 */
	private Long uptimeStart;
	
	/**
	 * 
	 */
	private AlienUptime() {
		uptimeStart = System.currentTimeMillis();
	}

	/**
	 * @return the instance
	 */
	public static AlienUptime getInstance() {
		return instance;
	}
	
	/**
	 * 
	 */
	public void resetUptimeStart() {
		uptimeStart = System.nanoTime();
	}
	
	/**
	 * Get uptime in seconds.  
	 */
	public long getUptimeInSeconds() {
		long uptimeStartSeconds = uptimeStart/NANO_TO_SECONDS;
		long uptimeActualSeconds = System.nanoTime()/NANO_TO_SECONDS;
		
		return uptimeActualSeconds / uptimeStartSeconds;
	}
	
	
}
