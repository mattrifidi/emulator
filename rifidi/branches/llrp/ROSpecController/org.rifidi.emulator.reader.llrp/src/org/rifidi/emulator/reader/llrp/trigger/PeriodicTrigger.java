package org.rifidi.emulator.reader.llrp.trigger;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.utilities.Timer;

/**
 * This Trigger is for the RoSpec Start Event & KeepAlives. It does not support
 * UTC times yet.
 * 
 * "If UTC time is not specified, the first start time is determined as (time of
 * message receipt + offset), else, the first start time is determined as (UTC
 * time + offset). Subsequent start times = first start time + k * period
 * (where, k > 0)." From the LLRP Specification.
 * 
 * It works by observing a timer. The timer, when it has expiered, notifies this
 * class which then updates its specState. The ROSpecContorller is listening for
 * this trigger and will start the rospec when this notifies it of a change.
 * 
 * @author kyle
 * 
 */
public class PeriodicTrigger implements TimerTrigger, Observer {

	/**
	 * The logger for this class.
	 */
	@SuppressWarnings("unused")
	private static Log logger = LogFactory.getLog(PeriodicTrigger.class);

	/**
	 * 
	 */
	private int period;
	
	private long offset;

	private Timer recurrentTimer;

	private Timer initial_start;

	private static String INITIAL_START_TIMER = "initialStartTimer";

	private static String START_TRIGGER_TIMER = "recurrent_trigger_timer";

	private TriggerObservable specState;

	private ArrayList<Object> extraInfo;

	private boolean stopTimer;

	private long totalSuspendTime = 0;
	
	private long lastSuspendTime = 0;
	
	private long lastFireTime = 0;
	
	private long startTime;
	
	private boolean suspended = false;

	/**
	 * Constructor for periodic start trigger if it is being used by a
	 * keepalive.
	 * 
	 * @param firstStartTime
	 *            ms until first start trigger should be fired.
	 * @param period
	 *            ms periods at which subsequent triggers should be fired. If
	 *            set to 0, then only one trigger will be fired
	 */
	public PeriodicTrigger(long offset, int period) {
		this.period = period;
		extraInfo = new ArrayList<Object>();
		extraInfo.add(this.getClass());
		stopTimer = false;
		this.offset = offset;
	}

	/**
	 * Constructor for periodic start trigger if it is being used by a RoSpec.
	 * 
	 * @param firstStartTime
	 *            ms until first start trigger should be fired.
	 * @param period
	 *            ms periods at which subsequent triggers should be fired. If
	 *            set to 0, then only one trigger will be fired
	 * @param roSpecNum
	 *            The number of this rospec
	 */
	public PeriodicTrigger(long offset, int period, int roSpecNum) {
		this(offset, period);
		extraInfo.add(roSpecNum);
	}

	public void startTimer() {
		logger.debug("Starting Offset timer...");
		initial_start = new Timer(offset);
		initial_start.addObserver(this);
		Thread t = new Thread(initial_start, INITIAL_START_TIMER);
		startTime = System.currentTimeMillis();
		t.start();
		if(suspended){
			initial_start.suspend();
		}
	}

	private void startRecurrentTimer() {
		if (recurrentTimer != null) {
			recurrentTimer = null;
		}
		recurrentTimer = new Timer(period);
		recurrentTimer.addObserver(this);
		Thread t = new Thread(recurrentTimer, START_TRIGGER_TIMER);
		t.start();
		if(suspended){
			recurrentTimer.suspend();
		}

	}

	public void stopTimer() {
		this.stopTimer = true;
	}

	public void setTriggerObservable(TriggerObservable specState) {
		this.specState = specState;

	}

	public void update(Observable arg0, Object arg1) {
		if (!stopTimer) {
			if (arg0 == this.initial_start) {
				long realoffsetTime = System.currentTimeMillis() - startTime;
				logger.debug("Offset timer up.  Real offset time was: "
						+ realoffsetTime + " Execution offset time was "
						+ (realoffsetTime - totalSuspendTime));
			} else if (arg0 == this.recurrentTimer) {
				long realRecurrentTime = System.currentTimeMillis() - startTime;
				logger.debug("Periodic timer up.  Real period time was: "
						+ realRecurrentTime + " Execution period time was "
						+ (realRecurrentTime - totalSuspendTime));
			}
			specState.fireStartTrigger(this.getClass());
			if (period > 0) {
				startRecurrentTimer();

				startTime = System.currentTimeMillis();
				totalSuspendTime = 0;
			}
		}
	}

	public void cleanUp() {
		if (this.recurrentTimer != null){
			this.recurrentTimer.deleteObserver(this);
			recurrentTimer.stop();
		}
		if (this.initial_start != null){
			this.initial_start.deleteObserver(this);
			initial_start.stop();
		}


	}

	public void resume() {
		logger.debug("resumeing Periodic Trigger");
		
		suspended = false;
		
		if (initial_start != null) {
			initial_start.resume();
		}
		if (recurrentTimer != null) {
			recurrentTimer.resume();
		}
		totalSuspendTime += System.currentTimeMillis() - lastSuspendTime;

	}

	public void suspend() {
		logger.debug("suspending Periodic Trigger");
		
		suspended = true;
		
		if (this.initial_start != null) {
			initial_start.suspend();
		}
		if (this.recurrentTimer != null) {
			recurrentTimer.suspend();
		}
		lastSuspendTime = System.currentTimeMillis();
	}

}
