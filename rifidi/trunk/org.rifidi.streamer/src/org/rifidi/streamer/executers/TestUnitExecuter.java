package org.rifidi.streamer.executers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rifidi.streamer.exceptions.NotInitializedException;
import org.rifidi.streamer.executers.listener.TestUnitStateListener;
import org.rifidi.streamer.registry.InputObjectRegistry;
import org.rifidi.streamer.xml.actions.Action;
import org.rifidi.streamer.xml.actions.BatchAction;
import org.rifidi.streamer.xml.actions.WaitAction;
import org.rifidi.streamer.xml.batch.Batch;
import org.rifidi.streamer.xml.testSuite.TestUnit;

public class TestUnitExecuter implements Runnable {

	private Log logger = LogFactory.getLog(TestUnitExecuter.class);

	private TestUnitStateListener app;
	private Thread thread;
	private boolean running = false;

	private TestUnit testUnit;
	private HashMap<Integer, ScenarioExecuter> scenarios = new HashMap<Integer, ScenarioExecuter>();

	private InputObjectRegistry registry;

	private boolean scenariosGenerated = false;
	private int runningScenarios = 0;

	private boolean infinite = false;

	private List<TestUnitStateListener> testUnitStateListeners = new ArrayList<TestUnitStateListener>();

	public TestUnitExecuter(TestUnit testUnit, TestUnitStateListener app,
			InputObjectRegistry registry) {
		this.testUnit = testUnit;
		this.app = app;
		this.registry = registry;
	}

	public void start() throws NotInitializedException {
		if (scenariosGenerated) {
			running = true;
			thread = new Thread(this, "TestUnit " + testUnit.getID());
			thread.start();
		} else {
			logger.error("Scenarios are not created yet. (createScenarios())");
		}
	}

	public void stop() {
		running = false;
		if (thread.isAlive()) {
			thread.interrupt();
		}
		for (ScenarioExecuter scenario : scenarios.values()) {
			if (scenario.isRunning()) {
				scenario.stop();
			}
		}
	}

	private void dispose() {
		running = false;
		for (ScenarioExecuter scenario : scenarios.values()) {
			if (scenario.isRunning()) {
				scenario.stop();
			}
		}
		app.testUnitFinishedEvent();
	}

	public void createScenarios() {
		for (Action action : testUnit.getActions()) {
			if (action instanceof BatchAction) {
				int scenarioID = ((BatchAction) action).getScenarioID();
				if (!scenarios.containsKey(scenarioID)) {
					logger.info("Creating Scenario " + scenarioID);
					ScenarioExecuter scenarioExecuter = new ScenarioExecuter(
							registry.getScenario(scenarioID), registry, this);
					scenarios.put(scenarioID, scenarioExecuter);
				}
			}
		}
		scenariosGenerated = true;
	}

	@Override
	public void run() {

		// Starting the Scenarios
		logger.info("Starting up the Scenarios in the TestSuite");
		try {
			for (ScenarioExecuter scenario : scenarios.values()) {
				scenario.start();
				runningScenarios++;
			}
		} catch (NotInitializedException e) {
			dispose();
		}

		// Iterating the Test
		try {
			logger.info("TestUnit waits " + testUnit.getPreWait()
					+ " ms before processing");
			Thread.sleep(testUnit.getPreWait());
			int iterations = testUnit.getIterations();
			if (iterations == -1)
				infinite = true;
			while ((iterations-- > 0 || infinite) && running) {
				// notfiy Listeners of iteration status
				nextIteration(testUnit.getIterations() - iterations);
				logger.debug("remaining iterations: " + iterations);

				for (Action action : testUnit.getActions()) {
					processAction(action);
				}
			}
			logger.info("TestUnit waits " + testUnit.getPostWait()
					+ " ms before exiting");
			Thread.sleep(testUnit.getPostWait());
		} catch (InterruptedException e) {
			dispose();
			return;
		}

		// All iterations are done
		logger.debug("All iterations finished");
		running = false;
		try {
			while (runningScenarios > 0) {
				for (ScenarioExecuter scenario : scenarios.values()) {
					if (scenario.isRunning() && scenario.isWaiting()
							&& scenario.getBatchesInScenario() == 0) {
						logger.debug("*** Scenario " + scenario.getID()
								+ " will be interrupted");
						scenario.interrupt();
					}
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
		}
		app.testUnitFinishedEvent();
	}

	private void processAction(Action action) throws InterruptedException {
		if (action instanceof WaitAction) {
			WaitAction waitAction = (WaitAction) action;
			long waitTime = waitAction.getWaitTime();
			logger.info("Wait for " + waitTime
					+ "ms before processing next Batches");
			Thread.sleep(waitTime);
		}
		if (action instanceof BatchAction) {
			BatchAction batchAction = (BatchAction) action;
			logger.info("Adding Batch " + batchAction.getBatchID()
					+ " to Scenario " + batchAction.getScenarioID());
			ScenarioExecuter scenarioExecuter = scenarios.get(batchAction
					.getScenarioID());
			Batch batch = registry.getBatch(batchAction.getBatchID());
			scenarioExecuter.addBatch(batch);
		}
	}

	public void scenarioEnded() {
		synchronized (thread) {
			runningScenarios--;
			logger.debug("Remaining Scenarios " + runningScenarios);
			if (runningScenarios <= 0) {
				logger.debug("Notifing Listners that TestUnit finished");
				// app.testUnitFinishedEvent();
				dispose();
			}
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void addIterationListener(TestUnitStateListener listener) {
		testUnitStateListeners.add(listener);
	}

	public void removeIterationListener(TestUnitStateListener listener) {
		testUnitStateListeners.remove(listener);
	}

	private void nextIteration(int iteration) {
		for (TestUnitStateListener listener : testUnitStateListeners) {
			listener.nextIteration(iteration);
		}
	}

	public ArrayList<ScenarioExecuter> getScenarioExecuters() {
		return new ArrayList<ScenarioExecuter>(scenarios.values());
	}
}
