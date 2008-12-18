package org.rifidi.streamer.executers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.streamer.exceptions.NotInitializedException;
import org.rifidi.streamer.executers.listener.ScenarioStateListener;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.scenario.PathItem;
import org.rifidi.streamer.xml.scenario.Scenario;

/**
 * @author Andreas Huebner - andreas@pramari.com
 * 
 */
public class ScenarioExecuter implements Runnable {

	private Log logger = LogFactory.getLog(ScenarioExecuter.class);

	private TestUnitExecuter testUnit;
	private Scenario scenario;
	private ArrayList<PathItemExecuter> path = new ArrayList<PathItemExecuter>();

	private Thread thread;
	private boolean running = false;
	private boolean waiting = false;

	private LinkedBlockingQueue<Batch> batchQueue = new LinkedBlockingQueue<Batch>();

	private int batchesInScenario = 0;

	private ArrayList<ScenarioStateListener> scenarioStateChanceListeners = new ArrayList<ScenarioStateListener>();
	private String state;

	public ScenarioExecuter(Scenario scenario, InputObjectRegistry registry,
			TestUnitExecuter testUnit) {
		this.scenario = scenario;
		this.testUnit = testUnit;
		if (scenario == null) {
			logger.error("Couldn't create Scenario");
		} else {
			buildPath(registry);
		}
	}

	private void buildPath(InputObjectRegistry registry) {
		List<PathItem> pathItems = scenario.getPathItems();
		logger.info("Scenario " + scenario.getID() + " - building " + pathItems.size() + " PathItems");
		for (int i = 0; i < pathItems.size(); i++) {
			logger.debug("Scenario " + scenario.getID()
					+ " building path item " + i);
			PathItemExecuter pathItemExecuter = new PathItemExecuter(i,
					pathItems.get(i), registry, this);
			path.add(pathItemExecuter);
		}
		logger.debug("Scenario " + scenario.getID() + " linking PathItems");
		for (int i = 0; i < path.size() - 1; i++) {
			path.get(i).setNextPathItem(path.get(i + 1));
		}
		logger.debug("Scenario " + scenario.getID() + "- path created");
	}

	public void addBatch(Batch batch) {
		if (running == false) {
			logger.debug("Scenario " + scenario.getID() + " is not started");
		}
		synchronized (thread) {
			batchesInScenario++;
			batchQueue.add(batch);
			stateChangedEvent("remaining " + batchesInScenario);
		}
	}

	public void start() throws NotInitializedException {
		if(scenario == null)
		{
			throw new NotInitializedException("Scenario not initialized");
		}
		thread = new Thread(this, "Scenario - " + scenario.getID());
		running = true;
		thread.start();
		stateChangedEvent("OK");
	}

	public void stop() {
		running = false;
		if (thread.isAlive())
			thread.interrupt();
	}

	private void dispose() {
		running = false;
		stateChangedEvent("stopped");
		for (PathItemExecuter pathItem : path) {
			pathItem.stop();
		}
		logger.info("Stopped Scenario " + scenario.getID());
		testUnit.scenarioEnded();
	}

	@Override
	public void run() {
		// Start all PathItems
		try {
			for (PathItemExecuter pathItemExecuter : path) {
				pathItemExecuter.start();
			}
		} catch (NotInitializedException e) {
			stateChangedEvent("error");
			running = false;
			e.printStackTrace();
		}
		while (running) {
			try {
				Batch batch = null;

				waiting = true;
				batch = batchQueue.take();
				waiting = false;

				logger.debug("Scenario " + scenario.getID() + " recived Batch "
						+ batch.getID());
				path.get(0).addBatch(batch);
			} catch (InterruptedException e) {
				if ((testUnit.isRunning() == false) && (batchesInScenario == 0)
						&& batchQueue.isEmpty()) {
					logger.debug("-- END -- Scenario " + scenario.getID()
							+ " received interrupt (Queue: "
							+ batchQueue.size() + "/ Batches in Scenario:"
							+ batchesInScenario + ")");
					running = false;
				} else {
					logger.debug("Scenario " + scenario.getID()
							+ " received interrupt (Queue: "
							+ batchQueue.size() + "/ Batches in Scenario:"
							+ batchesInScenario + ")");
				}
			}
		}
		dispose();
	}

	/**
	 * 
	 */
	public void batchReachedEnd() {
		synchronized (thread) {
			batchesInScenario--;
			if (batchesInScenario == 0) {
				if (!testUnit.isRunning()) {
					logger.debug("Try to stop Scenario " + scenario.getID()
							+ " and all containing PathItems.");
					dispose();
				} else {
					stateChangedEvent("waiting");
				}
			} else {
				stateChangedEvent("remaining " + batchesInScenario);
			}
		}
	}

	public void scenarioEnded() {

	}

	/**
	 * @return the batchesInScenario
	 */
	public int getBatchesInScenario() {
		return batchesInScenario;
	}

	/**
	 * @param listener
	 */
	public void addStateChangeListener(ScenarioStateListener listener) {
		logger.debug("Register ScenarioStateListener");
		scenarioStateChanceListeners.add(listener);
	}

	/**
	 * @param listener
	 */
	public void removeStateChangeListener(ScenarioStateListener listener) {
		logger.debug("Unregister ScenarioStateListener");
		scenarioStateChanceListeners.remove(listener);
	}

	/**
	 * @param state
	 */
	private void stateChangedEvent(String state) {
		this.state = state;
		for (ScenarioStateListener listener : scenarioStateChanceListeners) {
			listener.stateChangeEvent(this);
		}
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @return the running
	 */
	public boolean isRunning() {
		return running;
	}

	public boolean isWaiting() {
		return waiting;
	}

	public void interrupt() {
		thread.interrupt();
	}

	/**
	 * @return
	 */
	public int getID() {
		return scenario.getID();
	}

	public void pathItemEnded(PathItemExecuter pathItemExecuter) {
		// TODO Auto-generated method stub
	}
}
